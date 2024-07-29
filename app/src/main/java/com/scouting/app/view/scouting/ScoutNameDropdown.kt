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
package com.scouting.app.view.scouting

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.*
import androidx.compose.ui.unit.dp
import com.scouting.app.R
import com.scouting.app.misc.VectorScouts

@Composable
fun ScoutNameDropdown(
    onValueChange: (String?) -> Unit,
    selectedValue: String?,
) {
    val scouts = ArrayList<String>()
    for (i in 0 until VectorScouts.entries.size - 1) {
        scouts.add(VectorScouts.entries[i].toString().replace("_", " "))
    }

    var expanded by remember { mutableStateOf(false) }

    Box {
        Column {
            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .clickable(onClick = { expanded = true })
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled),
                        shape = MaterialTheme.shapes.small
                    )
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = selectedValue ?: stringResource(id = R.string.finish_match_name_confirmation_hint),
                    style = MaterialTheme.typography.body1,
                    color = if (selectedValue == null) Color.Gray else Color.Black,
                    modifier = Modifier.align(Alignment.Center)
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_user_avatar),
                    contentDescription = null,
                    tint = if (selectedValue == null) Color.Gray else Color.Black,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                scouts.forEach { scout ->
                    DropdownMenuItem(onClick = {
                        onValueChange(scout)
                        expanded = false
                    }) {
                        Text(scout)
                    }
                }
            }
        }
    }
}

