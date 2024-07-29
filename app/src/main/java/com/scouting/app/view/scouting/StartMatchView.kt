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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.scouting.app.R
import com.scouting.app.components.BasicInputField
import com.scouting.app.components.LargeButton
import com.scouting.app.components.LargeHeaderBar
import com.scouting.app.components.RatingBar
import com.scouting.app.components.SpacedRow
import com.scouting.app.misc.AllianceType
import com.scouting.app.misc.CompetitionManager
import com.scouting.app.misc.MatchStage
import com.scouting.app.misc.MatchType
import com.scouting.app.misc.NavDestination
import com.scouting.app.misc.ScoutingType
import com.scouting.app.theme.AffirmativeGreenDark
import com.scouting.app.theme.NeutralGrayDark
import com.scouting.app.theme.PrimaryOrangeDark
import com.scouting.app.theme.ScoutingTheme
import com.scouting.app.utilities.composableContext
import com.scouting.app.viewmodel.ScoutingViewModel

@Composable
fun StartMatchView(
    navController: NavController,
    viewModel: ScoutingViewModel,
    competitionManager: CompetitionManager
) {
    val context = composableContext
    LaunchedEffect(true) {
        viewModel.apply {
            populateMatchData()
        }
    }
    ScoutingTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column {
                LargeHeaderBar(
                    title = when(competitionManager.competitionModeEnabled) {
                        true -> when (competitionManager.competitionMatchType) {
                            MatchType.QUALIFICATION -> "Scout match ${competitionManager.competitionQualMatchCount + 1}"
                            MatchType.QUARTERFINAL -> stringResource(R.string.start_quarterfinal_header_title)
                            MatchType.SEMIFINAL -> stringResource(R.string.start_semifinal_header_title)
                            MatchType.FINAL -> stringResource(R.string.start_final_header_title)
                            MatchType.COMPLETE -> "ERROR: MATCH TYPE IS NONE"
                        }
                        false -> stringResource(R.string.start_match_header_title)
                    },
                    navController = navController
                )
                if (!competitionManager.competitionModeEnabled) {
                    SpacedRow(modifier = Modifier.padding(top = 50.dp)) {
                        Text(
                            text = stringResource(id = R.string.start_match_match_number_text),
                            style = MaterialTheme.typography.headlineSmall
                        )
                        BasicInputField(
                            hint = viewModel.currentMatchMonitoring.text,
                            textFieldValue = viewModel.currentMatchMonitoring,
                            onValueChange = { newText ->
                                viewModel.currentMatchMonitoring = newText
                            },
                            icon = painterResource(id = R.drawable.ic_time),
                            modifier = Modifier.width(115.dp),
                            numberKeyboard = true
                        )
                    }
                }
                SpacedRow(modifier = Modifier.padding(top = 50.dp)) {
                    Text(
                        text = stringResource(id = R.string.start_match_team_number_text),
                        style = MaterialTheme.typography.headlineSmall
                    )
                    BasicInputField(
                        hint = viewModel.currentTeamNumberMonitoring.text,
                        textFieldValue = viewModel.currentTeamNumberMonitoring,
                        onValueChange = { newText ->
                            viewModel.currentTeamNumberMonitoring = newText
                        },
                        icon = painterResource(id = R.drawable.ic_machine_learning),
                        modifier = Modifier.width(125.dp),
                        numberKeyboard = true
                    )
                }
                SpacedRow(modifier = Modifier.padding(top = 50.dp)) {
                    Text(
                        text = stringResource(id = R.string.start_match_alliance_selection_text),
                        style = MaterialTheme.typography.headlineSmall
                    )
                    RatingBar(
                        values = 2,
                        onValueChange = {
                            value -> when(value) {
                                1 -> viewModel.currentAllianceMonitoring = AllianceType.RED
                                2 -> viewModel.currentAllianceMonitoring = AllianceType.BLUE
                                else -> viewModel.currentAllianceMonitoring = AllianceType.NONE
                            }
                        },
                        customTextValues = listOf(
                            stringResource(id = R.string.start_match_alliance_label_red),
                            stringResource(id = R.string.start_match_alliance_label_blue)
                        ),
                        allianceSelectionColor = true,
                        enabled = true,
                        startingSelectedIndex = if (viewModel.currentAllianceMonitoring == AllianceType.RED) 0 else 1
                    )
                }
                LargeButton(
                    text = stringResource(id = R.string.start_match_begin_button_text),
                    icon = painterResource(id = R.drawable.ic_arrow_forward),
                    contentDescription = stringResource(id = R.string.ic_arrow_forward_content_desc),
                    onClick = {
                        viewModel.apply {
                            scoutingType = ScoutingType.MATCH
                        }
                        viewModel.loadTemplateItems()
                        if (
                            (viewModel.currentTeamNumberMonitoring.text.isBlank()) ||
                            (!competitionManager.competitionModeEnabled && viewModel.currentMatchMonitoring.text.isBlank())
                        ) {
                            Toast.makeText(
                                context,
                                context.getString(R.string.start_match_fill_out_fields_toast_text),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            viewModel.currentMatchStage = MatchStage.AUTO
                            navController.navigate("${NavDestination.Scouting}/" + true)
                        }
                    },
                    color = PrimaryOrangeDark,
                    modifier = Modifier.padding(horizontal = 30.dp, vertical = 50.dp),
                    colorBorder = NeutralGrayDark
                )
            }
        }
    }
}