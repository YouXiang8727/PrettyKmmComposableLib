package com.youxiang8727.kmmcomposablemodule.ui.timepickerdialog

import androidx.annotation.IntRange
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.DialogProperties
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class TimePickerDialogProperties(
    val dialogProperties: DialogProperties = DialogProperties(
        dismissOnClickOutside = false,
        dismissOnBackPress = false,
        usePlatformDefaultWidth = false
    ),
    val onDismissRequest: () -> Unit = {},
    val initialTime: LocalTime = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).time,
    val onSelectedTimeChange: (LocalTime) -> Unit = {},
    @IntRange(from = 3, to = 7)
    val visibleContentSize: Int = 5,
    val contentAspectRatio: Float = 1.5f,
    val maskColor: Color = Color.White,
    val selectedLineColor: Color = Color.LightGray,
    val selectedLineWidth: Float = 1f
) {
    val hours: List<Int> = (0..23).toList()

    val minutes: List<Int> = (0..59).toList()

    val hour = initialTime.hour
    val minute = initialTime.minute

    val autoAdjustColumnAspectRatio = contentAspectRatio / 2
    val itemAspectRatio: Float = autoAdjustColumnAspectRatio * visibleContentSize
}
