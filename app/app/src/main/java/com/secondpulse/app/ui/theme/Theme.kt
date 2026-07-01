package com.secondpulse.app.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object SpColors {
    val Background = Color(0xFFF6EEDC); val Surface = Color(0xFFFBF6EA); val SurfaceAlt = Color(0xFFF2E8D5)
    val BorderSoft = Color(0xFFCFC2AA); val BorderStrong = Color(0xFFB9A98D); val TextPrimary = Color(0xFF132F5E)
    val TextSecondary = Color(0xFF4C4439); val TextMuted = Color(0xFF766C5D); val Accent = Color(0xFF123A73)
    val AccentActive = Color(0xFF0F356A); val OnAccent = Color(0xFFFFF9EE); val IconInactive = Color(0xFF756A57)
    val Focus = Color(0xFF245A9B)
}

private val scheme = lightColorScheme(
    primary = SpColors.Accent, onPrimary = SpColors.OnAccent, background = SpColors.Background,
    onBackground = SpColors.TextSecondary, surface = SpColors.Surface, onSurface = SpColors.TextSecondary,
    outline = SpColors.BorderSoft
)

private val serif = FontFamily.Serif
private val typography = Typography(
    displaySmall = androidx.compose.ui.text.TextStyle(fontFamily = serif, fontSize = 30.sp, lineHeight = 36.sp, fontWeight = FontWeight.SemiBold),
    headlineMedium = androidx.compose.ui.text.TextStyle(fontFamily = serif, fontSize = 28.sp, lineHeight = 34.sp, fontWeight = FontWeight.SemiBold),
    titleLarge = androidx.compose.ui.text.TextStyle(fontFamily = serif, fontSize = 20.sp, lineHeight = 25.sp, fontWeight = FontWeight.SemiBold),
    bodyLarge = androidx.compose.ui.text.TextStyle(fontFamily = serif, fontSize = 18.sp, lineHeight = 28.sp),
    bodyMedium = androidx.compose.ui.text.TextStyle(fontFamily = serif, fontSize = 16.sp, lineHeight = 23.sp),
    bodySmall = androidx.compose.ui.text.TextStyle(fontFamily = serif, fontSize = 14.sp, lineHeight = 19.sp),
    labelMedium = androidx.compose.ui.text.TextStyle(fontFamily = serif, fontSize = 13.sp, lineHeight = 16.sp, fontWeight = FontWeight.Medium),
)

@Composable fun SecondPulseTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = scheme, typography = typography, content = content)
}
