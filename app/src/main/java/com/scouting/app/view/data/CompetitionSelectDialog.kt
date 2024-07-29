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
package com.scouting.app.view.data

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.scouting.app.R
import com.scouting.app.components.DialogScaffold
import com.scouting.app.components.LargeButton
import com.scouting.app.misc.CompetitionManager
import com.scouting.app.misc.NavDestination
import com.scouting.app.theme.NeutralGrayDark
import com.scouting.app.theme.PrimaryOrangeDark
import com.scouting.app.viewmodel.ScoutingDataViewModel

@Composable
fun CompetitionSelectDialog(
    navController: NavController,
    viewModel: ScoutingDataViewModel,
    competitionManager: CompetitionManager
) {
    if (!viewModel.showingCompetitionSelectDialog) {
        return
    }
    DialogScaffold(icon = painterResource(id = R.drawable.ic_edit_pen),
        contentDescription = stringResource(R.string.ic_edit_pen_content_desc),
        title = stringResource(id = R.string.scouting_data_select_competition_title),
        onDismissRequest = {
            viewModel.apply {
                showingCompetitionSelectDialog = false
            }
        }
    ) {
        Column(
            modifier = Modifier.padding(30.dp)
        ) {
            for (competition in competitionManager.competitionList) {
                Log.e("h0", competition)
                LargeButton(
                    text = competition,
                    onClick = {
                        viewModel.competitionSelected = competition
                        viewModel.showingCompetitionSelectDialog = false

                        navController.navigate(NavDestination.ScoutingData)

                        viewModel.parsePitData()
                        viewModel.parseMatchData()
                    },
                    color = NeutralGrayDark,
                    colorBorder = PrimaryOrangeDark,
                    modifier = Modifier.padding(bottom = 15.dp)
                )
            }
        }
    }
}