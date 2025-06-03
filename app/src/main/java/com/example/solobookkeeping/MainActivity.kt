package com.example.solobookkeeping

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.MoneyOff
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Savings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.solobookkeeping.ui.screens.AccountScreen
import com.example.solobookkeeping.ui.screens.BookkeepingScreen
import com.example.solobookkeeping.ui.screens.DebtScreen
import com.example.solobookkeeping.ui.screens.StatisticsScreen
import com.example.solobookkeeping.ui.theme.SoloBookkeepingTheme

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
    val label: String,
    val icon: ImageVector,
    val route: String
) {
    object Bookkeeping :
        BottomNavItem("Bookkeeping", Icons.Outlined.Savings, "bookkeeping")

    object Debt : BottomNavItem("Debt", Icons.Outlined.MoneyOff, "debt")
    object Statistics : BottomNavItem("Statistics", Icons.Outlined.BarChart, "statistics")
    object Account : BottomNavItem("Account", Icons.Outlined.Person, "account")
}

@Composable
fun MainScreen() {
    val nacController = rememberNavController()
    val items = listOf(
        BottomNavItem.Bookkeeping,
        BottomNavItem.Debt,
        BottomNavItem.Statistics,
        BottomNavItem.Account
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val currentRoute =
                    nacController.currentBackStackEntryAsState().value?.destination?.route
                items.forEach { item ->
                    NavigationBarItem(
                        selected = currentRoute == item.route,
                        onClick = {
                            nacController.navigate(item.route) {
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                    )
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        NavHost(
            navController = nacController,
            startDestination = BottomNavItem.Bookkeeping.route,
            Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Bookkeeping.route) { BookkeepingScreen() }
            composable(BottomNavItem.Debt.route) { DebtScreen() }
            composable(BottomNavItem.Statistics.route) { StatisticsScreen() }
            composable(BottomNavItem.Account.route) { AccountScreen() }
        }
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    SoloBookkeepingTheme {
        MainScreen()
    }

}