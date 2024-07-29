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
package com.scouting.app.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.scouting.app.R
import com.scouting.app.components.LargeButton
import com.scouting.app.components.MediumButton
import com.scouting.app.components.SettingsPreference
import com.scouting.app.components.SmallButton
import com.scouting.app.misc.CompetitionManager
import com.scouting.app.misc.MatchType
import com.scouting.app.misc.NavDestination
import com.scouting.app.theme.AffirmativeGreenDark
import com.scouting.app.theme.ErrorRedDark
import com.scouting.app.theme.NeutralGrayDark
import com.scouting.app.theme.PrimaryOrangeDark
import com.scouting.app.view.data.CompetitionSelectDialog
import com.scouting.app.view.data.NoCompetitionsDialog
import com.scouting.app.view.settings.CompetitionCompleteDialog
import com.scouting.app.view.settings.CompetitionNameDialog
import com.scouting.app.view.settings.DevicePositionDialog
import com.scouting.app.view.settings.DisableCompetitionModeDialog
import com.scouting.app.view.settings.QualMAXDialog
import com.scouting.app.viewmodel.ScoutingDataViewModel
import com.scouting.app.viewmodel.SettingsViewModel
import com.tencent.mmkv.MMKV
import java.lang.Error

@Composable
fun HomePageView(
    navController: NavController,
    viewModel: SettingsViewModel,
    viewModel2: ScoutingDataViewModel,
    competitionManager: CompetitionManager
) {
    viewModel.competitionModeButtonColor = if (competitionManager.competitionModeEnabled) {
        AffirmativeGreenDark
    } else {
        ErrorRedDark
    }

    LaunchedEffect(true) {
        viewModel.loadSavedPreferences()
    }
    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 35.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp, start = 5.dp, end = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = when (competitionManager.competitionModeEnabled) {
                        true -> competitionManager.competitionName.text
                        false -> "DEMO"
                    },
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )
                SmallButton(
                    text = stringResource(id = R.string.home_page_button_competition_mode),
                    onClick = {
                        if(competitionManager.competitionModeEnabled) {
                            viewModel.showingDisableCompetitionModeDialog = true
                        } else {
                            viewModel.showingCompetitionNameDialog = true
                        }
                    },
                    color = viewModel.competitionModeButtonColor,
                )
            }
            Spacer(modifier = Modifier.fillMaxHeight(fraction = 0.1F))
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        style = MaterialTheme.typography.displayLarge,
                        fontSize = 80.sp,
                        color = PrimaryOrangeDark
                    )
                    Text(
                        text = stringResource(id = R.string.home_page_subtitle_text),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                Column {
                    LargeButton(
                        text = if (competitionManager.competitionModeEnabled) {
                            when (competitionManager.competitionMatchType) {
                                MatchType.QUALIFICATION -> "Scout match ${competitionManager.competitionQualMatchCount + 1}"
                                MatchType.QUARTERFINAL -> "Scout quarterfinal match ${competitionManager.competitionQuarterMatchCount + 1}"
                                MatchType.SEMIFINAL -> "Scout semifinal match ${competitionManager.competitionSemiMatchCount + 1}"
                                MatchType.FINAL -> "Scout final match"
                                MatchType.COMPLETE -> "Scouting complete!"
                            }
                        } else {
                            stringResource(id = R.string.home_page_button_start_text)
                        },
                        icon = painterResource(id = R.drawable.ic_play_button),
                        contentDescription = stringResource(id = R.string.ic_play_button_content_desc),
                        onClick = {
                            if (competitionManager.competitionMatchType == MatchType.COMPLETE) {
                                viewModel.showingCompetitionCompleteDialog = true
                            } else {
                                navController.navigate(NavDestination.StartMatchScouting)
                            }
                        },
                        color = PrimaryOrangeDark,
                        modifier = Modifier.padding(bottom = 20.dp),
                        colorBorder = NeutralGrayDark
                    )
                    LargeButton(
                        text = stringResource(id = R.string.home_page_button_pit_text),
                        icon = painterResource(id = R.drawable.ic_result),
                        contentDescription = stringResource(id = R.string.ic_result_content_desc),
                        onClick = {
                            if (competitionManager.competitionModeEnabled && competitionManager.competitionMatchType == MatchType.COMPLETE) {
                                viewModel.showingCompetitionCompleteDialog = true
                            } else {
                                navController.navigate(NavDestination.StartPitScouting)
                            }
                        },
                        color = NeutralGrayDark,
                        modifier = Modifier.padding(bottom = 20.dp),
                        colorBorder = PrimaryOrangeDark
                    )
                    LargeButton(
                        text = stringResource(R.string.home_page_button_view_data),
                        icon = painterResource(id = R.drawable.ic_chart_combo),
                        contentDescription = stringResource(id = R.string.ic_chart_combo_content_desc),
                        onClick = {
                            viewModel2.setCompetitionList(competitionManager)
                            if (competitionManager.competitionList.size == 0) {
                                viewModel2.showingNoCompetitionsDialog = true
                            } else {
                                viewModel2.showingCompetitionSelectDialog = true
                            }
                        },
                        color = PrimaryOrangeDark,
                        modifier = Modifier.padding(bottom = 20.dp),
                        colorBorder = NeutralGrayDark
                    )
                }
            }
            CompetitionNameDialog(viewModel, competitionManager)
            QualMAXDialog(viewModel, competitionManager)
            DisableCompetitionModeDialog(viewModel, competitionManager)
            CompetitionCompleteDialog(viewModel)
            CompetitionSelectDialog(navController, viewModel2, competitionManager)
            NoCompetitionsDialog(viewModel2)
        }
    }
}


