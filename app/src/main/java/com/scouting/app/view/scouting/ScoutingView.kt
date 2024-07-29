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

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.scouting.app.R
import com.scouting.app.components.BasicInputField
import com.scouting.app.components.LabeledCounter
import com.scouting.app.components.LabeledRatingBar
import com.scouting.app.components.LabeledTriCounter
import com.scouting.app.components.SmallButton
import com.scouting.app.components.SpacedRow
import com.scouting.app.components.TriButtonBlock
import com.scouting.app.misc.AllianceType
import com.scouting.app.misc.CompetitionManager
import com.scouting.app.misc.MatchStage
import com.scouting.app.misc.MatchType
import com.scouting.app.misc.NavDestination
import com.scouting.app.misc.ScoutingType
import com.scouting.app.misc.TemplateTypes
import com.scouting.app.model.TemplateItem
import com.scouting.app.theme.AffirmativeGreenDark
import com.scouting.app.theme.PrimaryOrangeDark
import com.scouting.app.theme.ScoutingTheme
import com.scouting.app.theme.SecondaryPurpleDark
import com.scouting.app.theme.SunYellowDark
import com.scouting.app.viewmodel.ScoutingViewModel

@Composable
fun ScoutingView(navController: NavController, viewModel: ScoutingViewModel, scoutingMatch: Boolean, competitionManager: CompetitionManager) {
    ScoutingTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column {
                if (scoutingMatch) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(30.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = String.format(
                                    when(competitionManager.competitionModeEnabled) {
                                        true -> when(competitionManager.competitionMatchType) {
                                            MatchType.QUALIFICATION -> "Match ${competitionManager.competitionQualMatchCount + 1}"
                                            MatchType.QUARTERFINAL -> stringResource(R.string.in_match_header_match_type_quarterfinal)
                                            MatchType.SEMIFINAL -> stringResource(R.string.in_match_header_match_type_semifinal)
                                            MatchType.FINAL -> stringResource(R.string.in_match_header_match_type_final)
                                            MatchType.COMPLETE -> "XXX"
                                        }
                                        false -> "Match ${viewModel.currentMatchMonitoring.text}"
                                    }
                                ),
                                color = PrimaryOrangeDark,
                                style = MaterialTheme.typography.displaySmall
                            )
                            Text(
                                text = String.format(
                                    stringResource(id = R.string.in_match_header_team_number_format),
                                    viewModel.currentTeamNumberMonitoring.text
                                ),
                                style = MaterialTheme.typography.headlineSmall
                            )
                            Text(
                                text = when (viewModel.currentAllianceMonitoring) {
                                    AllianceType.BLUE -> stringResource(id = R.string.in_match_header_alliance_blue)
                                    AllianceType.RED -> stringResource(id = R.string.in_match_header_alliance_red)
                                    AllianceType.NONE -> "ERROR!"
                                },
                                style = MaterialTheme.typography.headlineSmall,
                                color = when (viewModel.currentAllianceMonitoring) {
                                    AllianceType.BLUE -> Color.Blue
                                    AllianceType.RED -> Color.Red
                                    AllianceType.NONE -> Color.Transparent
                                }
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Card(
                                shape = MaterialTheme.shapes.medium,
                                elevation = CardDefaults.cardElevation(0.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = when(viewModel.currentMatchStage) {
                                        MatchStage.AUTO -> SecondaryPurpleDark
                                        MatchStage.TELEOP-> AffirmativeGreenDark
                                        MatchStage.ENDGAME-> SunYellowDark
                                    }
                                )
                            ) {
                                AnimatedContent(targetState = viewModel.currentMatchStage.name,
                                    label = ""
                                ) {
                                    Text(
                                        text = it,
                                        style = MaterialTheme.typography.headlineMedium + TextStyle(fontWeight = FontWeight.SemiBold),
                                        modifier = Modifier.padding(
                                            horizontal = 15.dp,
                                            vertical = 10.dp
                                        ),
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            }
                            Row {
                                if (viewModel.scoutingType == ScoutingType.MATCH) {
                                    SmallButton(
                                        text = "",
                                        icon = painterResource(id = R.drawable.ic_arrow_back),
                                        contentDescription = stringResource(id = R.string.ic_arrow_back_content_desc),
                                        onClick = {
                                            viewModel.let {
                                                when(it.currentMatchStage) {
                                                    MatchStage.AUTO -> navController.popBackStack()
                                                    MatchStage.TELEOP -> it.currentMatchStage = MatchStage.AUTO
                                                    MatchStage.ENDGAME -> it.currentMatchStage = MatchStage.TELEOP
                                                }
                                            }
                                        },
                                        color = MaterialTheme.colorScheme.onBackground,
                                        modifier = Modifier.padding(top = 15.dp, end = 15.dp),
                                        outlineStyle = true
                                    )
                                }
                                SmallButton(
                                    text = if (viewModel.scoutingType == ScoutingType.MATCH) {
                                        if (viewModel.currentMatchStage == MatchStage.ENDGAME) {
                                            stringResource(id = R.string.in_match_scouting_end_button_text_short)
                                        } else {
                                            ""
                                        }
                                    }
                                    else {
                                        stringResource(id = R.string.in_match_stage_finish_scout_text)
                                    },
                                    icon = viewModel.let {
                                        if (it.scoutingType == ScoutingType.PIT || it.currentMatchStage == MatchStage.AUTO || it.currentMatchStage == MatchStage.TELEOP) {
                                            painterResource(id = R.drawable.ic_arrow_forward)
                                        } else {
                                            null
                                        }
                                    },
                                    contentDescription = stringResource(id = R.string.ic_arrow_forward_content_desc),
                                    onClick = {
                                        viewModel.let {
                                            if (it.currentMatchStage == MatchStage.ENDGAME) {
                                                navController.navigate(NavDestination.FinishScouting)
                                            } else {
                                                it.currentMatchStage = if (it.currentMatchStage == MatchStage.AUTO) MatchStage.TELEOP else MatchStage.ENDGAME
                                            }
                                        }
                                    },
                                    color = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier.padding(top = 15.dp),
                                    outlineStyle = true
                                )
                            }
                        }
                    }
                } else {
                    SpacedRow(modifier = Modifier.padding(vertical = 20.dp)) {
                        Column {
                            Text(
                                text = stringResource(id = R.string.in_pit_scouting_header_text),
                                color = PrimaryOrangeDark,
                                style = MaterialTheme.typography.headlineLarge
                            )
                            Text(
                                text = String.format(
                                    stringResource(id = R.string.in_pit_scouting_primary_subtitle_text),
                                    viewModel.currentTeamNumberMonitoring.text
                                ),
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier.padding(top = 5.dp)
                            )
                        }
                        SmallButton(
                            text = stringResource(id = R.string.in_pit_scouting_end_button_text),
                            icon = painterResource(id = R.drawable.ic_arrow_forward),
                            contentDescription = stringResource(id = R.string.ic_arrow_forward_content_desc),
                            onClick = {
                                navController.navigate(NavDestination.FinishScouting)
                            },
                            modifier = Modifier.padding(top = 15.dp),
                            outlineStyle = true
                        )
                    }
                }
                if (scoutingMatch) {
                    // Must have two different compositions of ScoutingTemplateLoadView
                    // because otherwise, compose will be "smart" and recycle the same
                    // views if at the same index the same view is going to exist (this
                    // is good behavior of LazyColumn) but in our case we want to recompose
                    // it so that the value of say, a counter is not persisted through
                    // match stage changes, confusing the user and messing up our data
                    AnimatedVisibility(visible = viewModel.currentMatchStage == MatchStage.AUTO) {
                        ScoutingTemplateLoadView(viewModel.autoListItems)
                    }
                    AnimatedVisibility(visible = viewModel.currentMatchStage == MatchStage.TELEOP) {
                        ScoutingTemplateLoadView(viewModel.teleListItems)
                    }
                    AnimatedVisibility(visible = viewModel.currentMatchStage == MatchStage.ENDGAME) {
                        ScoutingTemplateLoadView(viewModel.endgameListItems)
                    }
                } else {
                    ScoutingTemplateLoadView(viewModel.pitListItems)
                }
            }
        }
    }
}

@Composable
fun ScoutingTemplateLoadView(list: SnapshotStateList<TemplateItem>) {
    LaunchedEffect(true) {
        for (item in list) {
            when (item.type) {
                TemplateTypes.RATING_BAR -> {
                    if (item.itemValueInt == null) {
                        item.itemValueInt = mutableIntStateOf(1)
                    }
                }
                TemplateTypes.SCORE_BAR, TemplateTypes.TRI_BUTTON -> {
                    if (item.itemValueInt == null) {
                        item.itemValueInt = mutableIntStateOf(0)
                    }
                }
                TemplateTypes.CHECK_BOX -> {
                    if (item.itemValueBoolean == null) {
                        item.itemValueBoolean = mutableStateOf(false)
                    }
                }
                TemplateTypes.TEXT_FIELD -> {
                    if (item.itemValueString == null) {
                        item.itemValueString = mutableStateOf("")
                    }
                }
                TemplateTypes.TRI_SCORING -> {
                    if (item.itemValueInt == null) { // If one is null then the rest will be too
                        item.itemValueInt = mutableIntStateOf(0)
                        item.itemValue2Int = mutableIntStateOf(0)
                        item.itemValue3Int = mutableIntStateOf(0)
                    }
                }
                TemplateTypes.IMAGE, TemplateTypes.PLAIN_TEXT -> {}
            }
        }
    }
    LazyColumn(
        modifier = Modifier.padding(top = 20.dp),
        verticalArrangement = Arrangement.spacedBy(45.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        itemsIndexed(list) { _, item ->
            when (item.type) {
                TemplateTypes.SCORE_BAR -> {
                    if (item.itemValueInt == null) {
                        item.itemValueInt = remember { mutableIntStateOf(0) }
                    }
                    LabeledCounter(
                        text = item.text,
                        onValueChange = {
                            item.itemValueInt!!.value = it
                        },
                        incrementStep = 1,
                        modifier = Modifier.padding(horizontal = 30.dp),
                        startValue = item.itemValueInt!!.value
                    )
                }

                TemplateTypes.CHECK_BOX -> {
                    if (item.itemValueBoolean == null) {
                        item.itemValueBoolean = remember { mutableStateOf(false) }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                item.itemValueBoolean!!.let { it.value = !it.value }
                            }
                            .padding(start = 15.dp, end = 30.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = item.itemValueBoolean!!.value,
                            onCheckedChange = {
                                item.itemValueBoolean!!.value = it
                            }
                        )
                        Text(
                            text = item.text,
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(start = 15.dp)
                        )
                    }
                }

                TemplateTypes.PLAIN_TEXT -> {
                    Text(
                        text = item.text,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(horizontal = 30.dp)
                    )
                }

                TemplateTypes.TEXT_FIELD -> {
                    if (item.itemValueString == null) {
                        item.itemValueString = remember { mutableStateOf("") }
                    }
                    var tempItemState by remember {
                        mutableStateOf(TextFieldValue(item.itemValueString!!.value))
                    }
                    BasicInputField(
                        icon = painterResource(id = R.drawable.ic_text_format_center),
                        contentDescription = stringResource(id = R.string.ic_text_format_center_content_desc),
                        hint = item.text,
                        textFieldValue = tempItemState,
                        onValueChange = {
                            tempItemState = it
                            item.itemValueString!!.value = it.text
                        },
                        enabled = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp)
                    )
                }

                TemplateTypes.RATING_BAR -> {
                    if (item.itemValueInt == null) {
                        item.itemValueInt = remember { mutableIntStateOf(1) }
                    }
                    LabeledRatingBar(
                        text = item.text,
                        values = 5,
                        onValueChange = { item.itemValueInt!!.value = it },
                        modifier = Modifier.padding(horizontal = 30.dp),
                        startValue = item.itemValueInt!!.value - 1
                    )
                }

                TemplateTypes.TRI_SCORING -> {
                    if (item.itemValueInt == null) { // If one is null then the rest will be too
                        item.itemValueInt = remember { mutableIntStateOf(0) }
                        item.itemValue2Int = remember { mutableIntStateOf(0) }
                        item.itemValue3Int = remember { mutableIntStateOf(0) }
                    }
                    LabeledTriCounter(
                        text1 = item.text,
                        text2 = item.text2.toString(),
                        text3 = item.text3.toString(),
                        onValueChange1 = { item.itemValueInt!!.value = it },
                        onValueChange2 = { item.itemValue2Int!!.value = it },
                        onValueChange3 = { item.itemValue3Int!!.value = it },
                        startValueOne = item.itemValueInt!!.value,
                        startValueTwo = item.itemValue2Int!!.value,
                        startValueThree = item.itemValue3Int!!.value
                    )
                }

                TemplateTypes.TRI_BUTTON -> {
                    if (item.itemValueInt == null) {
                        item.itemValueInt = remember { mutableIntStateOf(0) }
                    }
                    TriButtonBlock(
                        headerText = item.text,
                        buttonLabelOne = item.text2.toString(),
                        buttonLabelTwo = item.text3.toString(),
                        buttonLabelThree = item.text4.toString(),
                        onValueChange = {
                            item.itemValueInt!!.value = it
                        },
                        initialSelection = item.itemValueInt!!.value,
                        modifier = Modifier.padding(
                            bottom = 10.dp,
                            start = 30.dp,
                            end = 30.dp,
                        )
                    )
                }

                else -> {
                    // Empty else block as there is nothing to do here
                }
            }
        }
    }
}