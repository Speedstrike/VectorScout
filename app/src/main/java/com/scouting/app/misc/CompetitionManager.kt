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
package com.scouting.app.misc

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue

class CompetitionManager {
    var competitionModeEnabled by mutableStateOf(false)
    var competitionMatchType by mutableStateOf(MatchType.QUALIFICATION)

    var competitionName by mutableStateOf(TextFieldValue())
    var competitionQualMatchMAX by mutableStateOf(TextFieldValue())

    var competitionMatchData = ArrayList<Map<String, Any>>()
    var competitionPitData = ArrayList<Map<String, Any>>()
    var competitionSaveFile: StringBuilder = StringBuilder()
    var competitionList: ArrayList<String> = ArrayList()

    var competitionQualMatchCount by mutableIntStateOf(0)
    var competitionQuarterMatchCount by mutableIntStateOf(0)
    var competitionSemiMatchCount by mutableIntStateOf(0)

    fun clearCompetitionData() {
        competitionModeEnabled = false
        competitionMatchType = MatchType.QUALIFICATION

        competitionMatchData.clear()
        competitionPitData.clear()
        competitionSaveFile.clear()

        competitionQualMatchCount = 0
        competitionQuarterMatchCount = 0
        competitionSemiMatchCount = 0
    }
}



