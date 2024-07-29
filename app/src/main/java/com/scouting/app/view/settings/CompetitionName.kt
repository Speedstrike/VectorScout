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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.scouting.app.R
import com.scouting.app.components.BasicInputField
import com.scouting.app.components.DialogScaffold
import com.scouting.app.components.SmallButton
import com.scouting.app.misc.CompetitionManager
import com.scouting.app.theme.PrimaryOrangeDark
import com.scouting.app.viewmodel.SettingsViewModel

@Composable
fun CompetitionNameDialog(viewModel: SettingsViewModel, competitionManager: CompetitionManager) {
    if (!viewModel.showingCompetitionNameDialog) {
        return
    }
    DialogScaffold(icon = painterResource(id = R.drawable.ic_edit_pen),
        contentDescription = stringResource(R.string.ic_edit_pen_content_desc),
        title = stringResource(id = R.string.settings_enter_competition_name),
        onDismissRequest = {
            viewModel.apply {
                showingCompetitionNameDialog = false
                restoreMatchType()
            }
        }
    ) {
        Column {
            BasicInputField(
                icon = painterResource(id = R.drawable.ic_edit_pen),
                contentDescription = stringResource(id = R.string.ic_edit_pen_content_desc),
                hint = stringResource(id = R.string.settings_enter_competition_name),
                textFieldValue = competitionManager.competitionName,
                onValueChange = { value ->
                    viewModel.apply {
                        competitionManager.competitionName = value
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(25.dp)
            )
            SmallButton(
                text = stringResource(id = R.string.home_page_device_edit_dialog_save_button),
                icon = painterResource(id = R.drawable.ic_checkmark_outline),
                contentDescription = stringResource(id = R.string.ic_checkmark_outline_content_desc),
                onClick = {
                    viewModel.apply {
                        if (competitionManager.competitionName.text.isNotBlank() && !competitionManager.competitionList.contains(competitionManager.competitionName.text)) {
                            viewModel.showingCompetitionNameDialog = false
                            viewModel.showingQualMAXDialog = true
                        }
                    }
                },
                color = PrimaryOrangeDark,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 25.dp)
            )
            QualMAXDialog(viewModel, competitionManager)
        }
    }
}