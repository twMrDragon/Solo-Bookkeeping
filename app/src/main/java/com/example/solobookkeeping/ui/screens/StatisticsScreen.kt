package com.example.solobookkeeping.ui.screens

import YearMonthPickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.solobookkeeping.model.Bookkeeping
import com.example.solobookkeeping.model.Category
import com.example.solobookkeeping.model.ExpenseCategory
import com.example.solobookkeeping.ui.components.RingChart
import com.example.solobookkeeping.viewmodel.StatisticsViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    modifier: Modifier = Modifier,
    statisticsViewModel: StatisticsViewModel,
    onCardClick: (Category) -> Unit
) {

    var showStartDialog by remember { mutableStateOf(false) }
    var showEndDialog by remember { mutableStateOf(false) }

    val startYear by statisticsViewModel.startYear.collectAsState()
    val startMonth by statisticsViewModel.startMonth.collectAsState()

    val endYear by statisticsViewModel.endYear.collectAsState()
    val endMonth by statisticsViewModel.endMonth.collectAsState()

    val groupedEntries by statisticsViewModel.groupedEntries.collectAsState()
    val categoryRatios by statisticsViewModel.categoryRatios.collectAsState()

    val total = groupedEntries.values.sumOf {
        it.sumOf { it.amount }
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
//            Spacer(modifier = Modifier.width(20.dp))

            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    showStartDialog = true
                },
                shape = MaterialTheme.shapes.small,
            ) {
                Text(
                    text = "起始時間 : \n ${startYear} 年 ${startMonth} 月",
                    fontSize = 16.sp, // 字體變大
                    textAlign = TextAlign.Center, // 文字置中（需要配合 Modifier.fillMaxWidth()）
                    modifier = Modifier.fillMaxWidth()
                )
            }
//            Spacer(modifier = Modifier.width(25.dp))

            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    showEndDialog = true
                },
                shape = MaterialTheme.shapes.small,
            ) {
                Text(
                    text = "結束時間 : \n ${endYear} 年 ${endMonth} 月",
                    fontSize = 16.sp, // 字體變大
                    textAlign = TextAlign.Center, // 文字置中（需要配合 Modifier.fillMaxWidth()）
                    modifier = Modifier.fillMaxWidth()
                )
            }

//            Spacer(modifier = Modifier.width(20.dp))
            YearMonthPickerDialog(
                show = showStartDialog,
                year = startYear,
                month = startMonth,
                onDismiss = { showStartDialog = false },
                onConfirm = { year, month ->
                    showStartDialog = false
                    statisticsViewModel.loadEntriesInRange(year, month, endYear, endMonth)
                }
            )
            YearMonthPickerDialog(
                show = showEndDialog,
                year = endYear,
                month = endMonth,
                onDismiss = { showEndDialog = false },
                onConfirm = { year, month ->
                    showEndDialog = false
                    statisticsViewModel.loadEntriesInRange(startYear, startMonth, year, month)
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
            text = "$%.2f".format(total),
            fontSize = 24.sp, // 字體變大
            textAlign = TextAlign.Center, // 文字置中（需要配合 Modifier.fillMaxWidth()）
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))


//        val GG = groupedEntries.entries.toList()
        //var SBK: List<Bookkeeping> = GG.get(1).value

        categoryRatios.forEach { (category, ratio) ->
            val entries = groupedEntries[category] ?: emptyList()
            val totalAmount = entries.sumOf { it.amount }.toFloat()

            ForJ(
                modifier.clickable {
                    onCardClick(category)
                },
                category = category,
                ratio = ratio,
                total = totalAmount
            )
        }
    }

}


@Composable
fun ForJ(
    modifier: Modifier = Modifier,
    category: Category,
    ratio: Float,
    total: Float
) {


//    val NowP = 0.8f
//    var Myprocess by remember { mutableStateOf(NowP / 1.0f) }

    Row(
        modifier = modifier
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = category.icon,
            contentDescription = null,
            modifier = Modifier
                .padding(end = 8.dp)
                .background(category.color, CircleShape)
                .padding(8.dp),
            tint = Color.White,
        )


        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        ) {
            Text(
                text = category.title,
            )

            //Spacer(modifier = Modifier.width(5.dp))

            LinearProgressIndicator(
                progress = { ratio },
                color = category.color,
                trackColor = Color.LightGray,
                modifier = Modifier
                    .height(10.dp)
                    .width(200.dp)
                    .clip(MaterialTheme.shapes.large)

            )
        }


        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "%.0f%%".format(ratio * 100),
            )
            Text(
                text = "%.2f".format(total),
            )
        }
    }
}




