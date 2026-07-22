package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GlowCyan
import com.example.ui.theme.MintTertiary
import com.example.ui.theme.TealSecondary
import com.example.ui.theme.TurquoisePrimary

@Composable
fun CircularHydrationProgress(
    progress: Float, // 0.0f to 1.0f
    currentMl: Int,
    targetMl: Int,
    modifier: Modifier = Modifier,
    size: Dp = 250.dp,
    strokeWidth: Dp = 22.dp
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "progress_anim"
    )

    val percentage = (animatedProgress * 100).toInt()
    val isGoalReached = currentMl >= targetMl

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .testTag("circular_progress_container")
    ) {
        // Glowing background circle
        Box(
            modifier = Modifier
                .size(size - 30.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            GlowCyan,
                            Color.Transparent
                        )
                    )
                )
        )

        // Canvas progress rings
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .testTag("circular_progress_canvas")
        ) {
            val strokeWidthPx = strokeWidth.toPx()
            val arcSize = Size(size.toPx() - strokeWidthPx - 20.dp.toPx(), size.toPx() - strokeWidthPx - 20.dp.toPx())
            val topLeft = Offset(strokeWidthPx / 2 + 10.dp.toPx(), strokeWidthPx / 2 + 10.dp.toPx())

            // Track background
            drawArc(
                color = Color(0xFF2B2930),
                startAngle = 140f,
                sweepAngle = 260f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
            )

            // Active Progress arc
            if (animatedProgress > 0f) {
                val sweepAngle = 260f * animatedProgress
                drawArc(
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            TurquoisePrimary,
                            TealSecondary,
                            MintTertiary,
                            TurquoisePrimary
                        )
                    ),
                    startAngle = 140f,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
                )
            }
        }

        // Inner display layout
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$percentage%",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TurquoisePrimary
                )
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "$currentMl ml",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE6E1E5)
                )
            )

            Spacer(modifier = Modifier.height(2.dp))

            Surface(
                color = Color(0xFF004D40),
                shape = CircleShape,
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Text(
                    text = "Objectif: $targetMl ml",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = Color(0xFF80CBC4),
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }
        }
    }
}
