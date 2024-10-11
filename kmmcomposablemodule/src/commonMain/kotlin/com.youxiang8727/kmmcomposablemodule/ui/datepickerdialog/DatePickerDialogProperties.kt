package com.youxiang8727.kmmcomposablemodule.ui.datepickerdialog

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.DialogProperties
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class DatePickerDialogProperties(
    val dialogProperties: DialogProperties = DialogProperties(
        dismissOnClickOutside = false,
        dismissOnBackPress = false,
        usePlatformDefaultWidth = false
    ),
    val onDismissRequest: () -> Unit = {},
    val initialDate: LocalDate = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).date,
    val onSelectDateChange: (LocalDate) -> Unit = {},
    val onShowDateChange: (LocalDate) -> Unit = {},
    val textColor: Color = Color.LightGray,
    val selectedDateTextColor: Color = Color.Black,
    val selectedDateIndicatorColor: Color = Color.Red.copy(.5f)
)
