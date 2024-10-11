package com.youxiang8727.kmmcomposablemodule.ui.datepickerdialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus

@Composable
fun DatePickerDialog(
    properties: DatePickerDialogProperties = DatePickerDialogProperties(),
    topBar: @Composable (
        minusMonthAction: () -> Unit,
        plusMonthAction: () -> Unit
    ) -> Unit,
    bottomBar: @Composable (RowScope.() -> Unit),
) {
    var selectDate by remember {
        mutableStateOf(properties.initialDate)
    }

    var showDate by remember {
        mutableStateOf(properties.initialDate)
    }

    LaunchedEffect(selectDate) {
        properties.onSelectDateChange(selectDate)
    }

    LaunchedEffect(showDate) {
        properties.onShowDateChange(showDate)
    }

    val showDates by remember(showDate) {
        derivedStateOf {
            (1..31).mapNotNull {
                try {
                    LocalDate(showDate.year, showDate.month, it)
                } catch (e: Exception) {
                    null
                }
            }.toMutableList().apply {
                while (this.first().dayOfWeek != DayOfWeek.SUNDAY) {
                    this.add(0, this.first().minus(1, DateTimeUnit.DAY))
                }

                while (this.last().dayOfWeek != DayOfWeek.SATURDAY) {
                    this.add(this.last().plus(1, DateTimeUnit.DAY))
                }
            }.toList().groupBy {
                it.dayOfWeek
            }
        }
    }

    Dialog(
        onDismissRequest = {
            properties.onDismissRequest()
        },
        properties = properties.dialogProperties
    ) {
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                topBar(
                    {
                        showDate = showDate.minus(1, DateTimeUnit.MONTH)
                    },
                    {
                        showDate = showDate.plus(1, DateTimeUnit.MONTH)
                    }
                )

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    showDates.forEach { group ->
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            item {
                                Text(
                                    text = group.key.name.substring(0, 3)
                                )
                            }

                            items(group.value) { date ->
                                Text(
                                    modifier = Modifier.clickable {
                                        selectDate = date
                                        showDate = date
                                    }.drawBehind {
                                        if (date == selectDate) {
                                            drawCircle(
                                                color = properties.selectedDateIndicatorColor
                                            )
                                        }
                                    },
                                    text = date.dayOfMonth.toString(),
                                    color = if (date.month == showDate.month) properties.selectedDateTextColor else properties.textColor
                                )
                            }
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