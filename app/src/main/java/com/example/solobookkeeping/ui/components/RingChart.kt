package com.example.solobookkeeping.ui.components

import android.graphics.DashPathEffect
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import kotlin.math.min
import android.graphics.Paint as AndroidPaint
import android.graphics.Paint.Cap as PaintCap
import android.graphics.Paint.Style as PaintStyle
import android.graphics.RectF as AndroidRectF

data class RingChartSegment(val start: Float, val end: Float, val color: Color)

@Composable
fun RingChart(
    segments: List<RingChartSegment>,
    modifier: Modifier = Modifier,
    strokeWidth: Float = 60f,
    useDash: Boolean = false
) {
    androidx.compose.foundation.Canvas(modifier = modifier) {
        val diameter = min(size.width, size.height)
        val fix = strokeWidth / 2f
        val topLeft =
            Offset((size.width - diameter) / 2f + fix, (size.height - diameter) / 2f + fix)
        val arcSize = Size(diameter - strokeWidth, diameter - strokeWidth)

        segments.forEach { segment ->
            val sweep = (segment.end - segment.start) * 360f
            val startAngle = segment.start * 360f - 90f

            drawIntoCanvas { canvas ->
                val paint = AndroidPaint().apply {
                    color = segment.color.toArgb()
                    isAntiAlias = true
                    style = PaintStyle.STROKE
                    strokeCap = PaintCap.BUTT
                    this.strokeWidth = strokeWidth
                    if (useDash) {
                        pathEffect = DashPathEffect(floatArrayOf(30f, 15f), 0f)
                    }
                }

                val rect = AndroidRectF(
                    topLeft.x,
                    topLeft.y,
                    topLeft.x + arcSize.width,
                    topLeft.y + arcSize.height
                )

                canvas.nativeCanvas.drawArc(
                    rect,
                    startAngle,
                    sweep,
                    false,
                    paint
                )
            }
        }
    }
}
