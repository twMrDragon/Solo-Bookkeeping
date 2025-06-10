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
import androidx.compose.foundation.shape.CircleShape
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
import com.example.solobookkeeping.model.ExpenseCategory
import com.example.solobookkeeping.ui.components.RingChart
import com.example.solobookkeeping.viewmodel.StatisticsViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    modifier: Modifier = Modifier,
    statisticsViewModel: StatisticsViewModel
) {

    var showStartDialog by remember { mutableStateOf(false) }
    var showEndDialog by remember { mutableStateOf(false) }

    val startYear by statisticsViewModel.startYear.collectAsState()
    val startMonth by statisticsViewModel.startMonth.collectAsState()

    val endYear by statisticsViewModel.endYear.collectAsState()
    val endMonth by statisticsViewModel.endMonth.collectAsState()

    val groupedEntries by statisticsViewModel.groupedEntries.collectAsState()
    val categoryRatios by statisticsViewModel.categoryRatios.collectAsState()



    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
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

            Spacer(modifier = Modifier.width(20.dp))
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
            text = "total:123456$",
            fontSize = 24.sp, // 字體變大
            textAlign = TextAlign.Center, // 文字置中（需要配合 Modifier.fillMaxWidth()）
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))


        val GG = groupedEntries.entries.toList()
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


    val NowP = 0.8f
    var Myprocess by remember { mutableStateOf(NowP / 1.0f) }

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


        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        ) {
            Text(
                text = entry.category.title,
            )

            //Spacer(modifier = Modifier.width(5.dp))

            LinearProgressIndicator(
                progress = { Myprocess },
                color = entry.category.color,
                trackColor = Color.LightGray,
                modifier = Modifier
                    .height(10.dp)
                    .width(200.dp)
                    .clip(MaterialTheme.shapes.large)

            )
        }



        Text(
            text = "%s%.2f".format(sign, entry.amount),
        )
    }
}




