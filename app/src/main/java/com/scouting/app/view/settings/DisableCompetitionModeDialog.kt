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

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.scouting.app.R
import com.scouting.app.components.DialogScaffold
import com.scouting.app.components.SmallButton
import com.scouting.app.components.SpacedRow
import com.scouting.app.misc.CompetitionManager
import com.scouting.app.theme.PrimaryOrangeDark
import com.scouting.app.theme.ScoutingTheme
import com.scouting.app.viewmodel.SettingsViewModel

@Composable
fun DisableCompetitionModeDialog(viewModel: SettingsViewModel, competitionManager: CompetitionManager) {
    if (viewModel.showingDisableCompetitionModeDialog) {
        ScoutingTheme {
            DialogScaffold(
                icon = painterResource(id = R.drawable.ic_help),
                contentDescription = stringResource(id = R.string.ic_help_content_desc),
                title = stringResource(id = R.string.settings_disable_competition_mode_title),
                onDismissRequest = {
                    viewModel.showingDisableCompetitionModeDialog = false
                }
            ) {
                Text(
                    text = stringResource(id = R.string.settings_disable_competition_mode_subtitle),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(horizontal = 30.dp, vertical = 20.dp)
                )
                SpacedRow(modifier = Modifier.padding(top = 10.dp, bottom = 30.dp)) {
                    SmallButton(
                        text = stringResource(id = R.string.settings_disable_competition_mode_button_text),
                        icon = painterResource(id = R.drawable.ic_checkmark_outline),
                        contentDescription = stringResource(id = R.string.ic_checkmark_outline_content_desc),
                        onClick = {
                            viewModel.showingDisableCompetitionModeDialog = false
                            competitionManager.competitionModeEnabled = false
                        },
                        color = PrimaryOrangeDark
                    )
                }
            }
        }
    }
}