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

import android.os.Environment
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.scouting.app.misc.AllianceType
import com.scouting.app.misc.AllianceType.RED
import com.scouting.app.misc.CompetitionManager
import com.scouting.app.misc.MatchStage.AUTO
import com.scouting.app.misc.MatchType
import com.scouting.app.misc.ScoutingType
import com.scouting.app.misc.ScoutingType.MATCH
import com.scouting.app.misc.ScoutingType.PIT
import com.scouting.app.misc.TemplateTypes
import com.scouting.app.model.TemplateFormatMatch
import com.scouting.app.model.TemplateFormatPit
import com.scouting.app.model.TemplateItem
import com.scouting.app.utilities.quoteForCSV
import com.tencent.mmkv.MMKV
import java.io.File
import java.io.IOException
import java.util.Scanner

class ScoutingViewModel() : ViewModel() {

    var autoListItems = mutableStateListOf<TemplateItem>()
    var teleListItems = mutableStateListOf<TemplateItem>()
    var endgameListItems = mutableStateListOf<TemplateItem>()
    var pitListItems = mutableStateListOf<TemplateItem>()
    private var saveKeyOrderList by mutableStateOf<List<String>?>(null)

    var scoutingType by mutableStateOf(PIT)
    var currentMatchStage by mutableStateOf(AUTO)
    var currentAllianceMonitoring by mutableStateOf(RED)
    var currentTeamNumberMonitoring by mutableStateOf(TextFieldValue())
    var currentMatchMonitoring by mutableStateOf(TextFieldValue())

    var scoutName by mutableStateOf("")

    private val preferences = MMKV.defaultMMKV()

    private val matchFileText: String =
        "{\"autoTemplateItems\":[{\"id\":\"a0de41c5-caf9-46d5-bb63-7a87b53f99b6\",\"saveKey\":\"aef719ea\",\"saveKey2\":\"null\",\"saveKey3\":\"null\",\"text\":\"Robot starting position\",\"text2\":\"Amp side\",\"text3\":\"Middle\",\"text4\":\"Source side\",\"type\":\"TRI_BUTTON\"},{\"id\":\"9fdedce4-8841-4050-8db0-4dc502960eb8\",\"saveKey\":\"990f7162\",\"saveKey2\":\"null\",\"saveKey3\":\"null\",\"text\":\"Robot leaves starting zone?\",\"text2\":\"null\",\"text3\":\"null\",\"text4\":\"null\",\"type\":\"CHECK_BOX\"},{\"id\":\"52bf4b0a-e1f0-41ff-88f3-19f36bd08e70\",\"saveKey\":\"324eee00\",\"saveKey2\":\"7a3ffb5e\",\"saveKey3\":\"4a805704\",\"text\":\"Amp notes\",\"text2\":\"Speaker notes\",\"text3\":\"Amplified speaker notes\",\"text4\":\"null\",\"type\":\"TRI_SCORING\"},{\"id\":\"a03e7f1c-8c8c-46d6-897f-93eeb90ee4fe\",\"saveKey\":\"d422dff0\",\"saveKey2\":\"null\",\"saveKey3\":\"null\",\"text\":\"Amplified speaker count\",\"text2\":\"null\",\"text3\":\"null\",\"text4\":\"null\",\"type\":\"SCORE_BAR\"},{\"id\":\"11ff75c3-f37c-46bc-806f-5d7b918dc0fd\",\"saveKey\":\"dd14a903\",\"saveKey2\":\"null\",\"saveKey3\":\"null\",\"text\":\"Overall auto rating\",\"text2\":\"null\",\"text3\":\"null\",\"text4\":\"null\",\"type\":\"RATING_BAR\"},{\"id\":\"4370043c-1821-48ba-af67-1594eb837306\",\"saveKey\":\"f74864ef\",\"saveKey2\":\"null\",\"saveKey3\":\"null\",\"text\":\"Notes\",\"text2\":\"null\",\"text3\":\"null\",\"text4\":\"null\",\"type\":\"TEXT_FIELD\"}],\"endgameTemplateItems\":[{\"id\":\"9f29728e-6987-4834-afdf-e6ced9322e7d\",\"saveKey\":\"9e653b4b\",\"saveKey2\":\"null\",\"saveKey3\":\"null\",\"text\":\"On stage time start\",\"text2\":\"null\",\"text3\":\"null\",\"text4\":\"null\",\"type\":\"SCORE_BAR\"},{\"id\":\"dd0dfa8a-01a7-48ca-b537-e5085d46d787\",\"saveKey\":\"edea320c\",\"saveKey2\":\"null\",\"saveKey3\":\"null\",\"text\":\"Microphone spotlight?\",\"text2\":\"null\",\"text3\":\"null\",\"text4\":\"null\",\"type\":\"CHECK_BOX\"},{\"id\":\"e787e0fd-6628-4e17-9a28-2040a793f515\",\"saveKey\":\"919739b0\",\"saveKey2\":\"null\",\"saveKey3\":\"null\",\"text\":\"Note in trap?\",\"text2\":\"null\",\"text3\":\"null\",\"text4\":\"null\",\"type\":\"CHECK_BOX\"},{\"id\":\"3cc9a69f-2464-4156-b5b2-f9c751a2b267\",\"saveKey\":\"a259b057\",\"saveKey2\":\"null\",\"saveKey3\":\"null\",\"text\":\"Died during match?\",\"text2\":\"null\",\"text3\":\"null\",\"text4\":\"null\",\"type\":\"CHECK_BOX\"},{\"id\":\"9e73eab6-e3af-40a1-9533-5db8103406f9\",\"saveKey\":\"ccdf46fa\",\"saveKey2\":\"null\",\"saveKey3\":\"null\",\"text\":\"Robot ending position\",\"text2\":\"Hanging on chain\",\"text3\":\"Parked in stage area\",\"text4\":\"Off stage\",\"type\":\"TRI_BUTTON\"},{\"id\":\"c1c35729-1ee6-4a16-abc5-3a08ad02d64d\",\"saveKey\":\"9de825be\",\"saveKey2\":\"null\",\"saveKey3\":\"null\",\"text\":\"Overall endgame rating\",\"text2\":\"null\",\"text3\":\"null\",\"text4\":\"null\",\"type\":\"RATING_BAR\"},{\"id\":\"0bdec5d2-a44a-44b7-a6f5-e00c23c3a55b\",\"saveKey\":\"cec85fc8\",\"saveKey2\":\"null\",\"saveKey3\":\"null\",\"text\":\"Notes\",\"text2\":\"null\",\"text3\":\"null\",\"text4\":\"null\",\"type\":\"TEXT_FIELD\"}],\"isMatchTemplate\":true,\"saveOrderByKey\":[\"aef719ea\",\"990f7162\",\"324eee00\",\"7a3ffb5e\",\"4a805704\",\"d422dff0\",\"dd14a903\",\"f74864ef\",\"fca7ca37\",\"fdc2c11a\",\"aff2cc8b\",\"23932f0f\",\"211aced1\",\"ae6893fc\",\"9342108d\",\"5a431137\",\"9e653b4b\",\"edea320c\",\"919739b0\",\"a259b057\",\"ccdf46fa\",\"9de825be\",\"cec85fc8\"],\"teleTemplateItems\":[{\"id\":\"baa17c08-ad7d-45b7-ae69-55e522f48ff9\",\"saveKey\":\"fca7ca37\",\"saveKey2\":\"fdc2c11a\",\"saveKey3\":\"aff2cc8b\",\"text\":\"Amp notes\",\"text2\":\"Speaker notes\",\"text3\":\"Amplified speaker notes\",\"text4\":\"null\",\"type\":\"TRI_SCORING\"},{\"id\":\"477006c4-fdd7-4f38-9c7a-cbcecaf23560\",\"saveKey\":\"23932f0f\",\"saveKey2\":\"null\",\"saveKey3\":\"null\",\"text\":\"Amplified speaker count\",\"text2\":\"null\",\"text3\":\"null\",\"text4\":\"null\",\"type\":\"SCORE_BAR\"},{\"id\":\"51147f75-fab2-4bf0-bb47-c3940472d00c\",\"saveKey\":\"211aced1\",\"saveKey2\":\"null\",\"saveKey3\":\"null\",\"text\":\"Picked up notes from source?\",\"text2\":\"null\",\"text3\":\"null\",\"text4\":\"null\",\"type\":\"CHECK_BOX\"},{\"id\":\"70d89484-2ff2-49d7-a2f7-292e2909e8ef\",\"saveKey\":\"ae6893fc\",\"saveKey2\":\"null\",\"saveKey3\":\"null\",\"text\":\"Picked up notes from floor?\",\"text2\":\"null\",\"text3\":\"null\",\"text4\":\"null\",\"type\":\"CHECK_BOX\"},{\"id\":\"7dd2fae8-dbf4-4656-aa0f-fdaa4ed87479\",\"saveKey\":\"9342108d\",\"saveKey2\":\"null\",\"saveKey3\":\"null\",\"text\":\"Overall tele-op rating\",\"text2\":\"null\",\"text3\":\"null\",\"text4\":\"null\",\"type\":\"RATING_BAR\"},{\"id\":\"4bf873c4-4a69-4c2f-92f5-7ae1da0f64a7\",\"saveKey\":\"5a431137\",\"saveKey2\":\"null\",\"saveKey3\":\"null\",\"text\":\"Notes\",\"text2\":\"null\",\"text3\":\"null\",\"text4\":\"null\",\"type\":\"TEXT_FIELD\"}],\"title\":\"Test2024.json\"}"
    private val pitFileText: String =
        "{\"isMatchTemplate\":false,\"saveOrderByKey\":[\"6bdcaf04\",\"072fc444\",\"9ab60c18\"],\"templateItems\":[{\"id\":\"bad15d6c-fe54-40cf-afc4-282ddcbf1f28\",\"saveKey\":\"6bdcaf04\",\"saveKey2\":\"null\",\"saveKey3\":\"null\",\"text\":\"Motors used\",\"text2\":\"Kraken x360\",\"text3\":\"NEO Motor\",\"text4\":\"TalonFX Falcon 500\",\"type\":\"TRI_BUTTON\"},{\"id\":\"da1c5377-cf56-4575-aea2-c82daa2fb325\",\"saveKey\":\"072fc444\",\"saveKey2\":\"null\",\"saveKey3\":\"null\",\"text\":\"Pit organization\",\"text2\":\"null\",\"text3\":\"null\",\"text4\":\"null\",\"type\":\"RATING_BAR\"},{\"id\":\"3201dc7f-17e2-4aff-a48b-2b900a35864e\",\"saveKey\":\"9ab60c18\",\"saveKey2\":\"null\",\"saveKey3\":\"null\",\"text\":\"Notes\",\"text2\":\"null\",\"text3\":\"null\",\"text4\":\"null\",\"type\":\"TEXT_FIELD\"}],\"title\":\"Test 1015.json\"}"

    fun loadTemplateItems() {
        val serializedTemplate = Gson().fromJson(
            if (scoutingType == MATCH) matchFileText else pitFileText,
            if (scoutingType == PIT) {
                TemplateFormatPit::class.java
            } else {
                TemplateFormatMatch::class.java
            }
        )
        if (scoutingType == MATCH) {
            serializedTemplate as TemplateFormatMatch
            autoListItems.apply {
                clear()
                addAll(serializedTemplate.autoTemplateItems)
            }
            teleListItems.apply {
                clear()
                addAll(serializedTemplate.teleTemplateItems)
            }
            endgameListItems.apply {
                clear()
                addAll(serializedTemplate.endgameTemplateItems)
            }
            saveKeyOrderList = serializedTemplate.saveOrderByKey
        } else {
            serializedTemplate as TemplateFormatPit
            pitListItems.apply {
                clear()
                addAll(serializedTemplate.templateItems)
            }
            saveKeyOrderList = serializedTemplate.saveOrderByKey
        }
    }

    fun writeScoutingDataToFile(competitionManager: CompetitionManager, dataType: ScoutingType) {
        val itemList = if (scoutingType == MATCH) {
            autoListItems.toList() + teleListItems.toList() + endgameListItems.toList()
        } else {
            pitListItems
        }

        // Append ordered, user-inputted match data
        competitionManager.competitionSaveFile.append(saveKeyOrderList!!.joinToString(",") { key ->
            itemList.findItemValueWithKey(key).toString().quoteForCSV()
        })

        Log.e("XX", competitionManager.competitionSaveFile.toString())

        when(dataType) {
            MATCH -> competitionManager.competitionMatchData.add(parseMatchData(competitionManager))
            PIT -> competitionManager.competitionPitData.add(parsePitData(competitionManager))
        }
        if (!competitionManager.competitionModeEnabled || competitionManager.competitionMatchType == MatchType.FINAL) {
            saveCompetitionData(competitionManager)
        }
        competitionManager.competitionSaveFile.clear()
    }

    private fun parseMatchData(competitionManager: CompetitionManager): Map<String, Any> {
        val sc = Scanner(competitionManager.competitionSaveFile.toString())
        sc.useDelimiter(",")

        return mapOf(
            "matchNumber" to when (competitionManager.competitionModeEnabled) {
                true -> when (competitionManager.competitionMatchType) {
                    MatchType.QUALIFICATION -> competitionManager.competitionQualMatchCount + 1
                    MatchType.QUARTERFINAL -> 1000 + competitionManager.competitionQuarterMatchCount
                    MatchType.SEMIFINAL -> 2000 + competitionManager.competitionSemiMatchCount
                    MatchType.FINAL -> 3000
                    MatchType.COMPLETE -> -1
                }

                false -> currentMatchMonitoring.text.toInt()
            },
            "teamNumber" to currentTeamNumberMonitoring.text.toInt(),
            "scoutName" to scoutName,

            "autonomous" to mapOf(
                "startingPosition" to when (sc.next().toInt()) {
                    0 -> "Amp side"
                    1 -> "Middle"
                    2 -> "Source side"
                    else -> "ERROR"
                },
                "robotLeavesStartingZone" to sc.nextBoolean(),
                "notesScoredInSpeaker" to sc.next().toInt(),
                "notesScoredInAmp" to sc.next().toInt(),
                "notesScoredInAmplifiedSpeaker" to sc.next().toInt(),
                "timesAmplified" to sc.next().toInt(),
                "autoRatingScore" to sc.next().toInt(),
                "scouterNotes" to sc.next()
            ),

            "tele-op" to mapOf(
                "notesScoredInSpeaker" to sc.next().toInt(),
                "notesScoredInAmp" to sc.next().toInt(),
                "notesScoredInAmplifiedSpeaker" to sc.next().toInt(),
                "timesAmplified" to sc.next().toInt(),
                "canPickUpNotesFromSource" to sc.nextBoolean(),
                "canPickUpNotesFromFloor" to sc.nextBoolean(),
                "teleOpRatingScore" to sc.next().toInt(),
                "scouterNotes" to sc.next()
            ),

            "endgame" to mapOf(
                "onStageTime" to sc.next().toInt(),
                "microphoneSpotlight" to sc.nextBoolean(),
                "noteInTrap" to sc.nextBoolean(),
                "diedDuringMatch" to sc.nextBoolean(),
                "robotEndingPosition" to when (sc.next().toInt()) {
                    0 -> "Hanging on chain"
                    1 -> "Parked in stage area"
                    2 -> "Off stage"
                    else -> "ERROR"
                },
                "endgameRatingScore" to sc.next().toInt(),
                "scouterNotes" to if (sc.hasNext()) sc.next() else ""
            )
        )
    }

    private fun parsePitData(competitionManager: CompetitionManager): Map<String, Any> {
        val sc = Scanner(competitionManager.competitionSaveFile.toString())
        sc.useDelimiter(",")

        return mapOf(
            "teamNumber" to currentTeamNumberMonitoring.text.toInt(),
            "scoutName" to scoutName,

            "data" to mapOf(
                "motorsUsed" to when(sc.next().toInt()) {
                    0 -> "Kraken x360"
                    1 -> "NEO Motor"
                    2 -> "Talon FX 500"
                    else -> "ERROR"
                },
                "pitOrganization" to sc.next().toInt(),
                "scouterNotes" to sc.next()
            )
        )
    }

    private fun saveCompetitionData(competitionManager: CompetitionManager) {
        // Create a new directory named "VectorScout" if it doesn't exist
        val outputDirectory = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
            "VectorScout"
        )
        if (!outputDirectory.exists()) {
            outputDirectory.mkdirs()
        }

        // Prepare the match file name with the competition name
        val matchFileName =
            if (competitionManager.competitionModeEnabled) "${competitionManager.competitionName.text}MatchData.json" else "DemoMatch${currentMatchMonitoring.text}.json"
        val matchFile = File(outputDirectory, matchFileName)

        try {
            // Write JSON data to the file
            matchFile.writeText(GsonBuilder().create().toJson(competitionManager.competitionMatchData))
        } catch (e: IOException) {
            e.printStackTrace()
        }

        // Prepare the pit file name with the competition name
        val pitFileName =
            if (competitionManager.competitionModeEnabled) "${competitionManager.competitionName.text}PitData.json" else "DemoPit${currentTeamNumberMonitoring.text}"
        val pitFile = File(outputDirectory, pitFileName)

        try {
            // Write JSON data to the file
            pitFile.writeText(GsonBuilder().create().toJson(competitionManager.competitionPitData))
        } catch (e: IOException) {
            e.printStackTrace()
        }

        if (competitionManager.competitionModeEnabled) {
            competitionManager.competitionList.add(competitionManager.competitionName.text)// Add the competition name to the list
        }
    }

    /**
     * Update the fields in StartMatchView. If a competition schedule is
     * enabled, match number and team number are set according to the set
     * device position, otherwise they are set to blank. Alliance color is
     * always set.
     */
    fun populateMatchData() {
        currentAllianceMonitoring =
            AllianceType.valueOf(preferences.decodeString("DEVICE_ALLIANCE_POSITION", "RED")!!)
        currentMatchMonitoring = TextFieldValue()
        currentTeamNumberMonitoring = TextFieldValue()
    }

    /**
     * Reset text fields in the StartPitScoutingView with the team name and the
     * team number according to the pit scouting schedule, or as blank if pit
     * scouting mode is disabled.
     */
    fun populatePitDataIfScheduled() {
        currentTeamNumberMonitoring = TextFieldValue()
    }

    /**
     * Fetch the corresponding item value using the save key
     */
    private fun List<TemplateItem>.findItemValueWithKey(key: String): Any? {
        var foundItem: Any? = null
        forEachIndexed { _, item ->
            try {
                when (key) {
                    item.saveKey -> {
                        foundItem = when (item.type) {
                            TemplateTypes.CHECK_BOX -> item.itemValueBoolean!!.value
                            TemplateTypes.TEXT_FIELD -> item.itemValueString!!.value
                            else /* SCORE_BAR, RATING_BAR, TRI_BUTTON OR TRI_SCORING */ -> item.itemValueInt!!.value
                        }
                        return@forEachIndexed
                    }
                    // We know that the only component that uses saveKey2 and saveKey3
                    // is the TRI_SCORING component, which is always an integer value
                    item.saveKey2 -> {
                        foundItem = item.itemValue2Int!!.value
                        return@forEachIndexed
                    }

                    item.saveKey3 -> {
                        foundItem = item.itemValue3Int!!.value
                        return@forEachIndexed
                    }
                }
            } catch (e: java.lang.NullPointerException) {
                foundItem = null
                return@forEachIndexed
            }
        }
        return foundItem
    }
}