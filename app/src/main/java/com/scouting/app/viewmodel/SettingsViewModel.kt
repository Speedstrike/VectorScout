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
package com.scouting.app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.scouting.app.misc.AllianceType
import com.scouting.app.misc.AllianceType.RED
import com.scouting.app.theme.ErrorRedDark
import com.tencent.mmkv.MMKV

class SettingsViewModel : ViewModel() {
    private var matchType by mutableStateOf(TextFieldValue("NONE"))

    var deviceAlliancePosition by mutableStateOf(RED)
    var deviceRobotPosition by mutableIntStateOf(0)

    var showingDevicePositionDialog by mutableStateOf(false)
    var showingCompetitionNameDialog by mutableStateOf(false)
    var showingQualMAXDialog by mutableStateOf(false)
    var showingCompetitionCompleteDialog by mutableStateOf(false)
    var showingDisableCompetitionModeDialog by mutableStateOf(false)

    var competitionModeButtonColor = Color.Transparent

    private val preferences = MMKV.defaultMMKV()

    /**
     * Set all switches, input fields etc. to the values they were last
     * saved as in MMKV
     */
    fun loadSavedPreferences() {
        preferences.apply {
            deviceRobotPosition = decodeInt("DEVICE_ROBOT_POSITION", 1)
            deviceAlliancePosition = AllianceType.valueOf(decodeString("DEVICE_ALLIANCE_POSITION", "RED")!!)
            matchType = TextFieldValue(
                decodeString("MATCH_TYPE", matchType.text)!!
            )
        }
    }

    /**
     * Save the new device position to MMKV (RED/BLUE and position (1,2 or 3)
     */
    fun applyDevicePositionChange() {
        preferences.apply {
            encode("DEVICE_ALLIANCE_POSITION", deviceAlliancePosition.name)
            encode("DEVICE_ROBOT_POSITION", deviceRobotPosition)
        }
    }

    /**
     * Resets the matchType to what's on MMKV
     */
    fun restoreMatchType() {
        matchType = TextFieldValue(preferences.decodeString("MATCH_TYPE", "NONE")!!)
    }
}