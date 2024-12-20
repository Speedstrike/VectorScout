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
package com.scouting.app.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.scouting.app.theme.NeutralGrayDark

@Composable
fun SheetHandle() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Card(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 15.dp, bottom = 15.dp)
                .width(100.dp)
                .height(4.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = NeutralGrayDark),
            elevation = CardDefaults.cardElevation(0.dp)
        ) { }
    }
}

@Composable
fun BorderedCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.onBackground),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        content.invoke()
    }
}

@Composable
fun DottedRoundBox(
    modifier: Modifier = Modifier,
    height: Dp,
    content: @Composable () -> Unit
) {
    MaterialTheme.colorScheme.let { themeColors ->
        Box(modifier = modifier) {
            Canvas(
                Modifier
                    .fillMaxWidth()
                    .height(height)
            ) {
                drawRoundRect(
                    color = themeColors.onBackground,
                    style = Stroke(
                        width = 2.5f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    ),
                    cornerRadius = CornerRadius(15.0F, 15.0F)
                )
            }
            content.invoke()
        }
    }
}

@Composable
fun TabLayout(
    items: List<String>,
    selection: State<Int>,
    onSelectionChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        items.forEachIndexed { index, text ->
            Column(
                modifier = Modifier
                    .weight(1f) // Distribute space equally
                    .padding(horizontal = 10.dp, vertical = 10.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .border(
                        width = if (selection.value == index) 2.dp else 0.dp,
                        shape = MaterialTheme.shapes.medium,
                        color = if (selection.value == index) MaterialTheme.colorScheme.onBackground else Color.Transparent
                    )
                    .clickable {
                        onSelectionChange(index)
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }
        }
    }
}

@Composable
fun SpacedRow(
    modifier: Modifier = Modifier,
    horizontalPadding: Dp = 30.dp,
    content: @Composable () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        content.invoke()
    }
}

@Composable
fun SettingsDivider(modifier: Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth(0.8F)
            .clip(MaterialTheme.shapes.medium)
            .height(3.dp)
            .background(MaterialTheme.colorScheme.onBackground.copy(0.15F))
    ) {}
}

