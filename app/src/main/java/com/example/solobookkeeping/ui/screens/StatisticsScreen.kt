package com.example.solobookkeeping.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.solobookkeeping.ui.theme.SoloBookkeepingTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathSegment
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.solobookkeeping.BottomNavItem

import kotlin.math.min
import com.example.solobookkeeping.ui.components.RingChartSegment
import com.example.solobookkeeping.ui.components.RingChart
import com.example.solobookkeeping.viewmodel.BookkeepingViewModel
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.solobookkeeping.core.util.monthIntToAbbreviation
import com.example.solobookkeeping.model.Bookkeeping
import com.example.solobookkeeping.model.Category
import com.example.solobookkeeping.model.ExpenseCategory
import com.example.solobookkeeping.ui.components.RingChart
import java.time.LocalDate




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    modifier: Modifier = Modifier
) {




    val bookkeepingViewModel: BookkeepingViewModel = viewModel()
    val navController = rememberNavController()



    var showDialog by remember { mutableStateOf(false) }
    val currentYear by bookkeepingViewModel.currentYear.collectAsState()
    val currentMonth by bookkeepingViewModel.currentMonth.collectAsState()


    val groupedEntries by bookkeepingViewModel.groupedEntries.collectAsState()
    val categoryRatios by bookkeepingViewModel.categoryRatios.collectAsState()



    Column(modifier = modifier
        .fillMaxSize()
        .padding(20.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Row {
            Spacer(modifier = Modifier.width(20.dp))

            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    showDialog = true
                },
                shape = MaterialTheme.shapes.small,
            ) {
                Text(
                    text = "起始時間 : \n ${currentYear} 年 ${currentMonth} 月",
                    fontSize = 16.sp, // 字體變大
                    textAlign = TextAlign.Center, // 文字置中（需要配合 Modifier.fillMaxWidth()）
                    modifier = Modifier.fillMaxWidth()
                )
            }




            Spacer(modifier = Modifier.width(25.dp))

            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    showDialog = true
                },
                shape = MaterialTheme.shapes.small,
            ) {
                Text(
                    text = "結束時間 : \n ${currentYear} 年 ${currentMonth} 月",
                    fontSize = 16.sp, // 字體變大
                    textAlign = TextAlign.Center, // 文字置中（需要配合 Modifier.fillMaxWidth()）
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.width(20.dp))
            YearMonthPickerDialog(
                show = showDialog,
                year = currentYear,
                month = currentMonth,
                onDismiss = { showDialog = false },
                onConfirm = { year, month ->
                    showDialog = false
                    bookkeepingViewModel.loadEntriesByYearMonth(year, month)
                }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        RingChart(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .aspectRatio(1f),
            strokeWidth = 100f,
            segments = calculateCategoryRing(categoryRatios),
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "total:123456$",
            fontSize = 24.sp, // 字體變大
            textAlign = TextAlign.Center, // 文字置中（需要配合 Modifier.fillMaxWidth()）
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))


        val GG=groupedEntries.entries.toList()
        //var SBK: List<Bookkeeping> = GG.get(1).value

        GG.forEach { RR ->

            RR.value.forEach { ha ->
                //if(SBK.contains(ha))
                ForJ(
                    entry = ha
                )

            }



        }

    }

}


@Composable
fun ForJ(
    modifier: Modifier = Modifier,
    entry: Bookkeeping,
    onEntryClick: (Bookkeeping) -> Unit = { /* Handle click */ }
) {
    val sign = when (entry.category) {
        is ExpenseCategory -> "-"
        else -> ""
    }


    val NowP=0.8f
    var Myprocess by remember { mutableStateOf(NowP/1.0f) }

    Row(
        modifier = modifier
            .clickable {
                onEntryClick(entry)
            }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = entry.category.icon,
            contentDescription = null,
            modifier = Modifier
                .padding(end = 8.dp)
                .background(entry.category.color, CircleShape)
                .padding(8.dp),
            tint = Color.White,
        )


        Column (
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        ){
            Text(
                text = entry.category.title,
            )

            //Spacer(modifier = Modifier.width(5.dp))

            LinearProgressIndicator(
                progress = {Myprocess},
                color = entry.category.color,
                trackColor = Color.LightGray,

                modifier = Modifier
                    .height(10.dp)
                    .width(200.dp)

            )
        }



        Text(
            text = "%s%.2f".format(sign, entry.amount),
        )
    }
}




