package com.youxiang8727.kmmcomposablemodule.ui.common

import androidx.annotation.IntRange
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import kotlinx.coroutines.launch

@Composable
fun <T> AutoAdjustColumn(
    data: List<T>,
    initialData: T,
    onDataChangeAction: (T) -> Unit = {},
    @IntRange(from = 1)
    visibleContentSize: Int,
    modifier: Modifier = Modifier,
    itemContent: @Composable (T) -> Unit
) {
    val density = LocalDensity.current
    val paddingHeightPercentage = (visibleContentSize - 1) / visibleContentSize.toFloat() / 2f

    var scrollToDefaultData by remember {
        mutableStateOf(false)
    }

    val lazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            lazyListState.scrollToItem(
                index = data.indexOf(initialData)
            )

            scrollToDefaultData = true
        }
    }

    LaunchedEffect(lazyListState.isScrollInProgress) {
        if (scrollToDefaultData.not()) return@LaunchedEffect

        val padding = lazyListState.layoutInfo.viewportSize.height * paddingHeightPercentage
        val center = lazyListState.layoutInfo.viewportSize.height / 2f

        val closestItemIndex = lazyListState
            .layoutInfo
            .visibleItemsInfo
            .firstOrNull {
                val top = padding + it.offset
                val bottom = top + it.size
                center in top..bottom
            }?.index ?: 0

        scope.launch {
            lazyListState.animateScrollToItem(
                index = closestItemIndex
            )
            onDataChangeAction(data[closestItemIndex])
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = lazyListState,
        contentPadding = PaddingValues(
            vertical = with(density) {
                (lazyListState.layoutInfo.viewportSize.height * paddingHeightPercentage).toDp()
            }
        )
    ) {
        items(data) { d ->
            itemContent(d)
        }
    }
}