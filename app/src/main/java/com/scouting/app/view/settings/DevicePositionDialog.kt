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
package com.scouting.app.view.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.scouting.app.R
import com.scouting.app.components.DialogScaffold
import com.scouting.app.components.RatingBar
import com.scouting.app.components.SmallButton
import com.scouting.app.components.SpacedRow
import com.scouting.app.misc.AllianceType
import com.scouting.app.theme.ErrorRedDark
import com.scouting.app.theme.QuatBlueDark
import com.scouting.app.utilities.composableContext
import com.scouting.app.viewmodel.SettingsViewModel

@Composable
fun DevicePositionDialog(viewModel: SettingsViewModel) {
    val context = composableContext
    if (viewModel.showingDevicePositionDialog) {
        DialogScaffold(
            icon = painterResource(id = R.drawable.ic_machine_learning),
            contentDescription = stringResource(id = R.string.ic_machine_learning_content_desc),
            title = stringResource(id = R.string.settings_tablet_configuration_title),
            onDismissRequest = {
                viewModel.apply {
                    showingDevicePositionDialog = false
                    applyDevicePositionChange()
                }
            }
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                SpacedRow(
                    modifier = Modifier.padding(end = 10.dp, top = 40.dp, bottom = 40.dp),
                    horizontalPadding = 20.dp
                ) {
                    val alliancePositionValues =
                        context.resources.getStringArray(R.array.alliance_position_values)
                    RatingBar(
                        values = 2,
                        customTextValues = alliancePositionValues.asList(),
                        onValueChange = {
                            viewModel.deviceAlliancePosition = AllianceType.valueOf(alliancePositionValues[it - 1])
                        },
                        allianceSelectionColor = true,
                        startingSelectedIndex = alliancePositionValues.indexOf(viewModel.deviceAlliancePosition.name)
                    )
                    RatingBar(
                        values = 3,
                        onValueChange = {
                            viewModel.deviceRobotPosition = it
                        },
                        customColor = when (viewModel.deviceAlliancePosition) {
                            AllianceType.RED -> ErrorRedDark
                            else -> QuatBlueDark
                        },
                        startingSelectedIndex = viewModel.deviceRobotPosition - 1
                    )
                }
                SmallButton(
                    text = stringResource(id = R.string.home_page_device_edit_dialog_save_button),
                    icon = painterResource(id = R.drawable.ic_checkmark_outline),
                    contentDescription = stringResource(id = R.string.ic_checkmark_outline_content_desc),
                    onClick = {
                        viewModel.apply {
                            showingDevicePositionDialog = false
                            applyDevicePositionChange()
                        }
                    },
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 25.dp)
                )
            }
        }
    }
}