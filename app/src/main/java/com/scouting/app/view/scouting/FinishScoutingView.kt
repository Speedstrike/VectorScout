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

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.scouting.app.R
import com.scouting.app.components.LargeHeaderBar
import com.scouting.app.components.SmallButton
import com.scouting.app.components.SpacedRow
import com.scouting.app.misc.CompetitionManager
import com.scouting.app.misc.MatchType
import com.scouting.app.misc.NavDestination
import com.scouting.app.misc.ScoutingType
import com.scouting.app.misc.VectorScouts
import com.scouting.app.theme.*
import com.scouting.app.utilities.composableContext
import com.scouting.app.utilities.returnTo
import com.scouting.app.viewmodel.ScoutingViewModel

@Composable
fun FinishScoutingView(navController: NavController, viewModel: ScoutingViewModel, competitionManager: CompetitionManager) {
    val context = composableContext

    ScoutingTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column {
                LargeHeaderBar(
                    title = stringResource(id = R.string.finish_match_header_title),
                    navController = navController
                )
                SpacedRow(modifier = Modifier.padding(top = 50.dp)) {
                    Text(
                        text = stringResource(id = R.string.finish_match_name_select),
                        style = MaterialTheme.typography.headlineSmall
                    )
                    ScoutNameDropdown(
                        onValueChange = {
                            if (it != null) {
                                viewModel.scoutName = it
                            }
                        },
                        selectedValue = viewModel.scoutName
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp, vertical = 50.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    SmallButton(
                        text = stringResource(id = R.string.finish_match_done_button_text),
                        icon = painterResource(id = R.drawable.ic_save_file),
                        contentDescription = stringResource(id = R.string.ic_save_file_content_desc),
                        onClick = {
                            if (viewModel.scoutName == "") {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.finish_match_scout_name_blank_toast_text),
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                viewModel.writeScoutingDataToFile(competitionManager, viewModel.scoutingType)
                                if(competitionManager.competitionModeEnabled && viewModel.scoutingType == ScoutingType.MATCH) {
                                    when(competitionManager.competitionMatchType) {
                                        MatchType.QUALIFICATION -> {
                                            competitionManager.competitionQualMatchCount++
                                            if(competitionManager.competitionQualMatchCount == competitionManager.competitionQualMatchMAX.text.toInt()) {
                                                competitionManager.competitionMatchType = MatchType.QUARTERFINAL
                                            }
                                        }

                                        MatchType.QUARTERFINAL -> {
                                            competitionManager.competitionQuarterMatchCount++
                                            if(competitionManager.competitionQuarterMatchCount == 4) {
                                                competitionManager.competitionMatchType = MatchType.SEMIFINAL
                                            }
                                        }

                                        MatchType.SEMIFINAL -> {
                                            competitionManager.competitionSemiMatchCount++
                                            if(competitionManager.competitionSemiMatchCount == 2) {
                                                competitionManager.competitionMatchType = MatchType.FINAL
                                            }
                                        }

                                        MatchType.FINAL -> {
                                            competitionManager.competitionMatchType = MatchType.COMPLETE
                                        }

                                        MatchType.COMPLETE -> TODO() // Do nothing here as match type cannot be COMPLETE at this time of execution
                                    }
                                } else {
                                    navController.returnTo(NavDestination.HomePage)
                                }
                                navController.returnTo(NavDestination.HomePage)
                            }
                        },
                        color = PrimaryOrangeDark,
                        colorBorder = NeutralGrayDark
                    )
                }
            }
        }
    }
}