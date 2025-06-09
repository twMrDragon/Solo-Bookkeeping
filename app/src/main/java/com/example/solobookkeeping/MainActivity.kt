package com.example.solobookkeeping

import YearMonthPickerDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.solobookkeeping.ui.components.DeleteDialog
import com.example.solobookkeeping.ui.screens.AccountScreen
import com.example.solobookkeeping.ui.screens.BookkeepingScreen
import com.example.solobookkeeping.ui.screens.DebtScreen
import com.example.solobookkeeping.ui.screens.EditBookkeepingScreen
import com.example.solobookkeeping.ui.screens.StatisticsScreen
import com.example.solobookkeeping.ui.theme.SoloBookkeepingTheme
import com.example.solobookkeeping.viewmodel.BookkeepingViewModel
import java.time.format.DateTimeFormatter

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
    var showYearMonthDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    val currentEntry by bookkeepingViewModel.currentEntry.collectAsState()
    val currentYear by bookkeepingViewModel.currentYear.collectAsState()
    val currentMonth by bookkeepingViewModel.currentMonth.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    when (currentRoute) {
                        BottomNavItem.Bookkeeping.route -> {
                            Row {
                                Button(
                                    modifier = Modifier.weight(1f),
                                    onClick = {
                                        showYearMonthDialog = true
                                    },
                                    shape = MaterialTheme.shapes.small,
                                ) {
                                    Text("${currentYear} 年 ${currentMonth} 月")
                                }
                            }
                        }
                        BottomNavItem.Statistics.route ->{
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // 左側空間，用來平衡 icon
                                Spacer(modifier = Modifier.width(25.dp))

                                // 文字置中，使用 weight() 撐開
                                Text(
                                    text = "期間總消費統計",
                                    modifier = Modifier
                                        .weight(1f),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.titleLarge
                                )
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
                        horizontal = 16.dp
                    ), viewModel = bookkeepingViewModel, onAddClick = {
                        bookkeepingViewModel.setCurrentEntry(null)
                        navController.navigate("edit_bookkeeping")
                    },
                    onEntryClick = { bookkeeping ->
                        bookkeepingViewModel.setCurrentEntry(bookkeeping)
                        navController.navigate("edit_bookkeeping")
                    },
                    onEntryLongClick = { bookkeeping ->
                        bookkeepingViewModel.setCurrentEntry(bookkeeping)
                        showDeleteDialog = true
                    })
            }
            composable(BottomNavItem.Debt.route) {
                DebtScreen(
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            composable(BottomNavItem.Statistics.route) { StatisticsScreen() }
            composable(BottomNavItem.Account.route) { AccountScreen() }
            composable("edit_bookkeeping") {
//                val modifyBookkeeping by bookkeepingViewModel.currentEntry.collectAsState()
                EditBookkeepingScreen(
                    modifier = Modifier.padding(
                        8.dp
                    ),
                    onCancel = {
                        navController.navigateUp()
                    },
                    onConfirm = { bookkeeping ->
                        if (currentEntry == null) {
                            bookkeepingViewModel.addBookkeeping(bookkeeping)
                        } else {
                            bookkeepingViewModel.updateBookkeeping(bookkeeping)
                        }
                        navController.navigateUp()
                    },
                    bookkeeping = currentEntry
                )
            }
        }
    }
    YearMonthPickerDialog(
        show = showYearMonthDialog,
        year = currentYear,
        month = currentMonth,
        onDismiss = { showYearMonthDialog = false },
        onConfirm = { year, month ->
            showYearMonthDialog = false
            bookkeepingViewModel.loadEntriesByYearMonth(year, month)
        })
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd") // 自訂格式
    DeleteDialog(
        show = showDeleteDialog,
        title = "刪除資料",
        text = "確定要刪除 ${currentEntry?.date?.format(formatter)} ${currentEntry?.title} 嗎?",
        onDismiss = { showDeleteDialog = false },
        onConfirm = {
            currentEntry?.let {
                bookkeepingViewModel.deleteBookkeeping(it)
            }
            showDeleteDialog = false
        }
    )
}

@Preview
@Composable
private fun MainScreenPreview() {
    SoloBookkeepingTheme {
        MainScreen()
    }

}