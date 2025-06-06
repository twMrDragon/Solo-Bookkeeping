package com.example.solobookkeeping

import YearMonthPickerDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.MoneyOff
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Savings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.solobookkeeping.ui.screens.AccountScreen
import com.example.solobookkeeping.ui.screens.BookkeepingScreen
import com.example.solobookkeeping.ui.screens.DebtScreen
import com.example.solobookkeeping.ui.screens.EditBookkeepingScreen
import com.example.solobookkeeping.ui.screens.StatisticsScreen
import com.example.solobookkeeping.ui.theme.SoloBookkeepingTheme
import com.example.solobookkeeping.viewmodel.BookkeepingViewModel
import java.time.YearMonth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SoloBookkeepingTheme {
                MainScreen()
            }
        }
    }
}

sealed class BottomNavItem(
    val label: String, val icon: ImageVector, val route: String
) {
    object Bookkeeping : BottomNavItem("Bookkeeping", Icons.Outlined.Savings, "bookkeeping")

    object Debt : BottomNavItem("Debt", Icons.Outlined.MoneyOff, "debt")
    object Statistics : BottomNavItem("Statistics", Icons.Outlined.BarChart, "statistics")
    object Account : BottomNavItem("Account", Icons.Outlined.Person, "account")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val bookkeepingViewModel: BookkeepingViewModel = viewModel()
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val showBottomBar = when {
        currentRoute == null -> true
        currentRoute.startsWith("edit_bookkeeping") -> false
        else -> true
    }
    val items = listOf(
        BottomNavItem.Bookkeeping,
        BottomNavItem.Debt,
        BottomNavItem.Statistics,
        BottomNavItem.Account
    )
    var showDialog by remember { mutableStateOf(false) }
    var selectedYear by remember { mutableStateOf(YearMonth.now().year) }
    var selectedMonth by remember { mutableStateOf(YearMonth.now().monthValue) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    when (currentRoute) {
                        BottomNavItem.Bookkeeping.route -> {
                            Row {
                                Button(
                                    modifier = Modifier.weight(1f), onClick = {
                                        showDialog = true
                                    }) {
                                    Text("${selectedYear} 年 ${selectedMonth} 月")
                                }
                            }
                        }

                        else -> {}
                    }
                },
                navigationIcon = {
                    if (!showBottomBar) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "back")
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Handle settings click */ }) {
                        Icon(Icons.AutoMirrored.Filled.HelpOutline, contentDescription = "help")
                    }
                },
            )
        }, bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    items.forEach { item ->
                        NavigationBarItem(
                            selected = currentRoute == item.route,
                            onClick = {
                                navController.navigate(item.route) {
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                        )
                    }
                }
            }
        }, modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Bookkeeping.route,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(BottomNavItem.Bookkeeping.route) {
                BookkeepingScreen(
                    modifier = Modifier.padding(
                        8.dp
                    ), viewModel = bookkeepingViewModel, onAddClick = {
                        navController.navigate("edit_bookkeeping")
                    })
            }
            composable(BottomNavItem.Debt.route) { DebtScreen() }
            composable(BottomNavItem.Statistics.route) { StatisticsScreen() }
            composable(BottomNavItem.Account.route) { AccountScreen() }
            composable("edit_bookkeeping") {
                EditBookkeepingScreen(
                    modifier = Modifier.padding(
                        8.dp
                    ),
//                    viewModel = viewModel(),
                    onCancel = {
                        navController.navigateUp()
                    },
                    onConfirm = { bookkeeping ->
                        bookkeepingViewModel.addBookkeeping(bookkeeping)
                        navController.navigateUp()
                    })
            }
        }
    }
    YearMonthPickerDialog(
        show = showDialog,
        onDismiss = { showDialog = false },
        onConfirm = { year, month ->
            selectedYear = year
            selectedMonth = month
            showDialog = false
            bookkeepingViewModel.loadEntriesByYearMonth(year, month)
        })
}

@Preview
@Composable
private fun MainScreenPreview() {
    SoloBookkeepingTheme {
        MainScreen()
    }

}