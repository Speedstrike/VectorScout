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
package com.scouting.app

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.scouting.app.misc.CompetitionManager
import com.scouting.app.misc.NavDestination
import com.scouting.app.theme.ScoutingTheme
import com.scouting.app.utilities.handleDocumentUri
import com.scouting.app.utilities.openDocumentLauncher
import com.scouting.app.view.HomePageView
import com.scouting.app.view.data.ScoutingDataView
import com.scouting.app.view.scouting.FinishScoutingView
import com.scouting.app.view.scouting.ScoutingView
import com.scouting.app.view.scouting.StartMatchView
import com.scouting.app.view.scouting.StartPitScoutingView
import com.scouting.app.viewmodel.ScoutingDataViewModel
import com.scouting.app.viewmodel.ScoutingViewModel
import com.scouting.app.viewmodel.SettingsViewModel
import com.tencent.mmkv.MMKV


class MainActivity : ComponentActivity() {
    private val requestWritePermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            // Permission is granted, proceed with file creation
            Toast.makeText(this, "Write permission granted", Toast.LENGTH_SHORT).show()
        } else {
            // Permission is denied, handle accordingly
            Toast.makeText(this, "Write permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private lateinit var appContainer: AppContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MMKV.initialize(this)
        requestWritePermission()

        appContainer = AppContainer()

        val scoutingViewModel = appContainer.scoutingViewModel
        val settingsViewModel = appContainer.settingsViewModel
        val scoutingDataViewModel = appContainer.scoutingDataViewModel
        val competitionManager = appContainer.competitionManager

        setContent {
            ScoutingTheme {
                NavigationHost(scoutingViewModel, settingsViewModel, scoutingDataViewModel, competitionManager)
            }
        }

        openDocumentLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
            uri?.let {
                handleDocumentUri(this, it)
            }
        }
    }

    @Composable
    fun NavigationHost(
        scoutingViewModel: ScoutingViewModel,
        settingsViewModel: SettingsViewModel,
        scoutingDataViewModel: ScoutingDataViewModel,
        competitionManager: CompetitionManager
    ) {
        val navigationController = rememberNavController()

        NavHost(
            navController = navigationController,
            startDestination = NavDestination.HomePage,
            modifier = Modifier.fillMaxSize()
        ) {
            composable(NavDestination.HomePage) {
                HomePageView(
                    navigationController,
                    settingsViewModel,
                    scoutingDataViewModel,
                    competitionManager
                )
            }

            composable(NavDestination.StartMatchScouting) {
                StartMatchView(navigationController, scoutingViewModel, competitionManager)
            }

            composable(NavDestination.StartPitScouting) {
                StartPitScoutingView(navigationController, scoutingViewModel)
            }

            composable(
                route = "${NavDestination.Scouting}/{type}",
                arguments = listOf(navArgument("type") { type = NavType.BoolType })
            ) {
                ScoutingView(
                    navController = navigationController,
                    viewModel = scoutingViewModel,
                    scoutingMatch = it.arguments?.getBoolean("type") ?: false,
                    competitionManager
                )
            }

            composable(NavDestination.FinishScouting) {
                FinishScoutingView(navigationController, scoutingViewModel, competitionManager)
            }

            composable(NavDestination.ScoutingData) {
                ScoutingDataView(navigationController, scoutingDataViewModel)
            }
        }
    }

    private fun requestWritePermission() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            when (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                PackageManager.PERMISSION_GRANTED -> {
                    // Permission is already granted, proceed with file creation
                    Toast.makeText(this, "Write permission already granted", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // Request the permission
                    requestWritePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }
        }
    }
}