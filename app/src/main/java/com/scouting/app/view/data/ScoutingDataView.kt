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

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.scouting.app.R
import com.scouting.app.components.MediumHeaderBar
import com.scouting.app.components.TabLayout
import com.scouting.app.misc.NavDestination
import com.scouting.app.theme.PrimaryOrangeDark
import com.scouting.app.viewmodel.ScoutingDataViewModel
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun ScoutingDataView(
    navController: NavController,
    viewModel: ScoutingDataViewModel,
) {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 4 })

    Surface {
        Column {
            ScoutingDataHeader(
                navController,
                pagerState
            )
            HorizontalPager(
                state = pagerState
            ) { page ->
                when (page) {
                    0 -> viewModel.displayScoutingData("AUTO")

                    1 -> viewModel.displayScoutingData("TELEOP")

                    2 -> viewModel.displayScoutingData("ENDGAME")

                    3 -> viewModel.displayScoutingData("PIT")
                }
            }

        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScoutingDataHeader(
    navController: NavController,
    pagerState: PagerState
) {
    val async = rememberCoroutineScope()

    MediumHeaderBar(
        title = stringResource(id = R.string.scouting_data_header_title),
        color = PrimaryOrangeDark,
        navController = navController,
        iconLeft = painterResource(id = R.drawable.ic_play_button),
        contentDescription = stringResource(id = R.string.ic_play_button_content_desc),
        onIconLeftClick = {
            navController.navigate(NavDestination.StartMatchScouting)
        }
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        TabLayout(
            items = listOf(
                stringResource(id = R.string.scouting_data_auto_header),
                stringResource(id = R.string.scouting_data_tele_header),
                stringResource(id = R.string.scouting_data_endgame_header),
                stringResource(id = R.string.scouting_data_pit_header)
            ),
            selection = remember {
                derivedStateOf { pagerState.currentPage }
            },
            onSelectionChange = {
                async.launch { pagerState.animateScrollToPage(it) }
            },
            modifier = Modifier.padding(top = 10.dp),
        )
    }
}