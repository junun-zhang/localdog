package com.example.ireader.ui.util

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 屏幕类型枚举
 */
enum class ScreenType {
    SMALL,      // 小屏幕 (手机)
    MEDIUM,     // 中等屏幕 (平板)
    LARGE       // 大屏幕 (大平板/折叠屏)
}

/**
 * 获取当前屏幕类型
 */
@Composable
fun getScreenType(): ScreenType {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp
    
    return when {
        screenWidthDp < 600 -> ScreenType.SMALL
        screenWidthDp < 840 -> ScreenType.MEDIUM
        else -> ScreenType.LARGE
    }
}

/**
 * 根据屏幕类型获取网格列数
 */
@Composable
fun getGridColumnsCount(): Int {
    return when (getScreenType()) {
        ScreenType.SMALL -> 2
        ScreenType.MEDIUM -> 3
        ScreenType.LARGE -> 4
    }
}

/**
 * 根据屏幕类型获取卡片宽度
 */
@Composable
fun getCardWidth(): Dp {
    return when (getScreenType()) {
        ScreenType.SMALL -> 160.dp
        ScreenType.MEDIUM -> 180.dp
        ScreenType.LARGE -> 200.dp
    }
}

/**
 * 根据屏幕类型获取卡片高度
 */
@Composable
fun getCardHeight(): Dp {
    return when (getScreenType()) {
        ScreenType.SMALL -> 220.dp
        ScreenType.MEDIUM -> 240.dp
        ScreenType.LARGE -> 260.dp
    }
}

/**
 * 根据屏幕类型获取字体大小乘数
 */
@Composable
fun getFontSizeMultiplier(): Float {
    return when (getScreenType()) {
        ScreenType.SMALL -> 1f
        ScreenType.MEDIUM -> 1.1f
        ScreenType.LARGE -> 1.2f
    }
}

/**
 * 根据屏幕类型获取内边距
 */
@Composable
fun getPadding(): Dp {
    return when (getScreenType()) {
        ScreenType.SMALL -> 16.dp
        ScreenType.MEDIUM -> 20.dp
        ScreenType.LARGE -> 24.dp
    }
}

/**
 * 根据屏幕类型获取间距
 */
@Composable
fun getSpacing(): Dp {
    return when (getScreenType()) {
        ScreenType.SMALL -> 12.dp
        ScreenType.MEDIUM -> 16.dp
        ScreenType.LARGE -> 20.dp
    }
}