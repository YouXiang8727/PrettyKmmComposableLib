package com.youxiang8727.kmmcomposablemodule.ui.timepickerdialog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Dialog
import com.youxiang8727.kmmcomposablemodule.ui.common.AutoAdjustColumn
import kotlinx.datetime.LocalTime

@Composable
fun TimePickerDialog(
    properties: TimePickerDialogProperties = TimePickerDialogProperties(),
    topBar: @Composable (RowScope.() -> Unit),
    bottomBar: @Composable (RowScope.() -> Unit),
) {
    var time by remember {
        mutableStateOf(properties.initialTime)
    }

    LaunchedEffect(time) {
        properties.onSelectedTimeChange(time)
    }

    Dialog(
        onDismissRequest = {
            properties.onDismissRequest()
        },
        properties = properties.dialogProperties
    ) {
        Card {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    topBar()
                }

                Row(
                    modifier = Modifier.fillMaxWidth()
                        .aspectRatio(properties.contentAspectRatio)
                        .drawWithContent {
                            drawContent()
                            drawRect(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        properties.maskColor,
                                        Color.Transparent,
                                        properties.maskColor
                                    ),
                                    startY = 0f,
                                    endY = size.height
                                )
                            )

                            val halfItemHeight =
                                size.height / properties.visibleContentSize.toFloat() / 2f
                            val center = size.height / 2f
                            drawLine(
                                color = properties.selectedLineColor,
                                strokeWidth = properties.selectedLineWidth,
                                start = Offset(0f, center - halfItemHeight),
                                end = Offset(size.width, center - halfItemHeight)
                            )

                            drawLine(
                                color = properties.selectedLineColor,
                                strokeWidth = properties.selectedLineWidth,
                                start = Offset(0f, center + halfItemHeight),
                                end = Offset(size.width, center + halfItemHeight)
                            )
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AutoAdjustColumn(
                        data = properties.hours,
                        initialData = properties.hour,
                        onDataChangeAction = { hour ->
                            time = LocalTime(hour, time.minute)
                        },
                        visibleContentSize = properties.visibleContentSize,
                        modifier = Modifier.weight(1f)
                            .fillMaxWidth()
                            .aspectRatio(properties.autoAdjustColumnAspectRatio)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth()
                                .aspectRatio(properties.itemAspectRatio),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = it.toString().padStart(2, '0')
                            )
                        }
                    }

                    Text(
                        text = ":"
                    )

                    AutoAdjustColumn(
                        data = properties.minutes,
                        initialData = properties.minute,
                        onDataChangeAction = { minute ->
                            time = LocalTime(time.hour, minute)
                        },
                        visibleContentSize = properties.visibleContentSize,
                        modifier = Modifier.weight(1f)
                            .fillMaxWidth()
                            .aspectRatio(properties.autoAdjustColumnAspectRatio)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth()
                                .aspectRatio(properties.itemAspectRatio),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = it.toString().padStart(2, '0')
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    bottomBar()
                }
            }
        }
    }
}