/*
 * Copyright (c) 2024 Aaryan Karlapalem
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.scouting.app.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val PrimaryOrangeDark = Color(0xFFF1953E)
val SecondaryPurpleDark = Color(0xFFA6A7FF)
val ErrorRedDark = Color(0xFFFFA2A2)
val AffirmativeGreenDark = Color(0xFFB4E2B9)
val SunYellowDark = Color(0xFFFFF59E)
val QuatBlueDark = Color(0xFF90C8FA)
val BackgroundGrayDark = Color(0xFF3B3B3B)

val NeutralGrayDark = Color(0XFFD2D2D2)

private val DarkColorPalette = darkColorScheme(
    primary = PrimaryOrangeDark,
    secondary = AffirmativeGreenDark,
    tertiary = SecondaryPurpleDark,
    error = ErrorRedDark,
    background = BackgroundGrayDark,
    surface = BackgroundGrayDark,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = PrimaryOrangeDark
)

@Composable
fun ScoutingTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorPalette,
        typography = typography(),
        shapes = Shapes,
        content = content
    )
}