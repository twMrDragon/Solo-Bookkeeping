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

import kotlin.math.min
import com.example.solobookkeeping.ui.components.RingChartSegment
import com.example.solobookkeeping.ui.components.RingChart



@Composable
fun StatisticsScreen(modifier: Modifier = Modifier) {
    val NowP=0.8f
    var Myprocess by remember { mutableStateOf(NowP/1.0f) }



    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Text(
            "Statistics Screen"
        )
        Spacer(modifier = Modifier.height(30.dp))

        RingChart(
            segments = listOf(
                RingChartSegment(0.0f,0.2f, Color(0xFF5B9279)),
                RingChartSegment(0.2f,0.4f, Color(0xFFB36A28)),
                RingChartSegment(0.4f,1.0f, Color(0xFFDDDDDD)),
            ),
            modifier = Modifier.size(200.dp),
            strokeWidth = 40f,
            useDash = false

        )
        Spacer(modifier = Modifier.height(30.dp))
        LinearProgressIndicator(
            progress = {Myprocess},
            modifier = Modifier
                .width(120.dp)
                .height(4.dp)

        )
    }

}





@Preview(showBackground = true)
@Composable
private fun StatisticsScreenPreview() {
    SoloBookkeepingTheme {
        StatisticsScreen()
    }
}