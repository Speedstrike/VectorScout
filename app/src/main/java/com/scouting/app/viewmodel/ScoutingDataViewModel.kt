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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.scouting.app.misc.CompetitionManager
import com.scouting.app.theme.PrimaryOrangeDark
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException


class ScoutingDataViewModel : ViewModel() {
    var competitionSelected by mutableStateOf("NO_COMPETITION_SELECTED")

    var showingCompetitionSelectDialog by mutableStateOf(false)
    var showingNoCompetitionsDialog by mutableStateOf(false)

    private val pitMotorsUsed = mutableMapOf(
        "Kraken x360" to 0F,
        "NEO Motor" to 0F,
        "Talon FX 500" to 0F,
    )
    private val pitOrganization = mutableMapOf(
        "1" to 0F,
        "2" to 0F,
        "3" to 0F,
        "4" to 0F,
        "5" to 0F
    )

    private val robotLeavesStartingZone = mutableMapOf(
        "Leaves starting zone" to 0F,
        "Does not leave starting zone" to 0F
    )
    private val autoNotesScored = mutableMapOf(
        "Amp" to 0F,
        "Speaker" to 0F,
        "Amplified speaker" to 0F
    )

    private val teleNotesScored = mutableMapOf(
        "Amp" to 0F,
        "Speaker" to 0F,
        "Amplified speaker" to 0F
    )
    private val teleNotePickup = mutableMapOf(
        "Source" to 0F,
        "Floor" to 0F
    )

    private val endgameStageStartTime = mutableMapOf(
        ArrayList<Int>() to 0
    )
    private val endgameBoolean = mutableMapOf(
        "microphoneSpotlight" to 0F,
        "noteInTrap" to 0F,
        "diedDuringMatch" to 0F
    )
    private val endgameRobotPosition = mutableMapOf(
        "Hanging on chain" to 0F,
        "Parked in stage area" to 0F,
        "Off stage" to 0F
    )

    fun parsePitData() {
        try {
            // Construct the file path
            val documentsFolder = File("${System.getenv("EXTERNAL_STORAGE")}/Documents/VectorScout")
            val file = File(documentsFolder, competitionSelected + "PitData.json")

            // Check if the file exists
            if (!file.exists()) {
                throw IOException("File not found: $file")
            }

            // Read file content
            val reader = BufferedReader(FileReader(file))
            val sb = StringBuilder()
            var line: String?

            while (reader.readLine().also { line = it } != null) {
                sb.append(line)
            }
            val jsonString = sb.toString()

            // Parse JSON
            val jsonArray = JSONArray(jsonString)

            for (i in 0 until jsonArray.length()) {
                val matchObject: JSONObject = jsonArray.getJSONObject(i)

                if(matchObject.has("data")) {
                    val dataObject = matchObject.getJSONObject("data")
                    val motorUsed = dataObject.getString("motorsUsed")
                    val pitOrganizationValue = dataObject.getString("pitOrganization")

                    pitMotorsUsed[motorUsed] = pitMotorsUsed[motorUsed]!! + 1
                    pitOrganization[pitOrganizationValue] = pitOrganization[pitOrganizationValue]!! + 1
                }
            }

            reader.close()

        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    fun parseMatchData() {
        try {
            // Construct the file path
            val documentsFolder = File("${System.getenv("EXTERNAL_STORAGE")}/Documents/VectorScout")
            val file = File(documentsFolder, competitionSelected + "MatchData.json")

            // Check if the file exists
            if (!file.exists()) {
                throw IOException("File not found: $file")
            }

            // Read file content
            val reader = BufferedReader(FileReader(file))
            val sb = StringBuilder()
            var line: String?

            while (reader.readLine().also { line = it } != null) {
                sb.append(line)
            }
            val jsonString = sb.toString()

            // Parse JSON
            val jsonArray = JSONArray(jsonString)

            for (i in 0 until jsonArray.length()) {
                val matchObject = jsonArray.getJSONObject(i)

                // Check if auto block exists and extract notesScored variables
                if (matchObject.has("autonomous")) {
                    val autoObject = matchObject.getJSONObject("autonomous")
                    val leavesStartingZone = autoObject.getString("robotLeavesStartingZone").toBoolean()
                    val notesScoredInAmp = autoObject.getString("notesScoredInAmp").toInt()
                    val notesScoredInSpeaker = autoObject.getString("notesScoredInAmp").toInt()
                    val notesScoredInAmplifiedSpeaker = autoObject.getString("notesScoredInAmp").toInt()

                    if (autoObject.has("robotLeavesStartingZone")) {
                        when(leavesStartingZone) {
                            true -> robotLeavesStartingZone["Leaves starting zone"] = robotLeavesStartingZone["Leaves starting zone"]!! + 1
                            false -> robotLeavesStartingZone["Does not leave starting zone"] = robotLeavesStartingZone["Does not leave starting zone"]!! + 1
                        }
                    }
                    if (autoObject.has("notesScoredInAmp")) {
                        autoNotesScored["Amp"] = autoNotesScored["Amp"]!! + notesScoredInAmp
                    }
                    if (autoObject.has("notesScoredInSpeaker")) {
                        autoNotesScored["Speaker"] = autoNotesScored["Speaker"]!! + notesScoredInSpeaker
                    }
                    if (autoObject.has("notesScoredInAmplifiedSpeaker")) {
                        autoNotesScored["Amplified speaker"] = autoNotesScored["Amplified speaker"]!! + notesScoredInAmplifiedSpeaker
                    }
                }

                // Check if teleop block exists and extract notesScored variables
                if (matchObject.has("tele-op")) {
                    val teleObject = matchObject.getJSONObject("tele-op")
                    val notesScoredInAmp = teleObject.getString("notesScoredInAmp").toInt()
                    val notesScoredInSpeaker = teleObject.getString("notesScoredInAmp").toInt()
                    val notesScoredInAmplifiedSpeaker = teleObject.getString("notesScoredInAmp").toInt()
                    val pickupFromSource = teleObject.getString("canPickUpNotesFromSource").toBoolean()
                    val pickupFromFloor = teleObject.getString("canPickUpNotesFromFloor").toBoolean()

                    if (teleObject.has("notesScoredInAmp")) {
                        teleNotesScored["Amp"] = teleNotesScored["Amp"]!! + notesScoredInAmp
                    }
                    if (teleObject.has("notesScoredInSpeaker")) {
                        teleNotesScored["Speaker"] = teleNotesScored["Speaker"]!! + notesScoredInSpeaker
                    }
                    if (teleObject.has("notesScoredInAmplifiedSpeaker")) {
                        teleNotesScored["Amplified speaker"] = teleNotesScored["Amplified speaker"]!! + notesScoredInAmplifiedSpeaker
                    }
                    if (teleObject.has("canPickUpNotesFromSource") && pickupFromSource) {
                        teleNotePickup["Source"] = teleNotePickup["Source"]!! + 1
                    }
                    if (teleObject.has("canPickUpNotesFromFloor") && pickupFromFloor) {
                        teleNotePickup["Floor"] = teleNotePickup["Floor"]!! + 1
                    }
                }

                if (matchObject.has("endgame")) {
                    val endgameObject = matchObject.getJSONObject("endgame")
                    val onStageTime = endgameObject.getString("onStageTime").toInt()
                    val micSpotlight = endgameObject.getString("microphoneSpotlight").toBoolean()
                    val noteIntoTrap = endgameObject.getString("noteInTrap").toBoolean()
                    val diedInMatch = endgameObject.getString("diedDuringMatch").toBoolean()
                    val endingPosition = endgameObject.getString("robotEndingPosition")

                    if (endgameObject.has("onStageTime")) {
                        endgameStageStartTime.entries.firstOrNull()!!.key.add(onStageTime)
                    }
                    if (endgameObject.has("microphoneSpotlight") && micSpotlight) {
                        endgameBoolean["microphoneSpotlight"] = endgameBoolean["microphoneSpotlight"]!! + 1
                    }
                    if (endgameObject.has("noteInTrap") && noteIntoTrap) {
                        endgameBoolean["noteInTrap"] = endgameBoolean["noteInTrap"]!! + 1
                    }
                    if (endgameObject.has("diedDuringMatch") && diedInMatch) {
                        endgameBoolean["diedDuringMatch"] = endgameBoolean["diedDuringMatch"]!! + 1
                    }
                    if (endgameObject.has("robotEndingPosition")) {
                        endgameRobotPosition[endingPosition] = endgameRobotPosition[endingPosition]!! + 1
                    }
                }
            }
            reader.close()

        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    @Composable
    fun displayScoutingData(dataType: String) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 35.dp)
        ) {
            when (dataType) {
                "AUTO" -> {
                    Text(
                        text = "Starting zone",
                        style = MaterialTheme.typography.headlineMedium,
                        color = PrimaryOrangeDark
                    )
                    Text(
                        text = "Leaves starting zone - ${robotLeavesStartingZone["Leaves starting zone"]!!.toInt()}",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "Does not leave starting zone - ${robotLeavesStartingZone["Does not leave starting zone"]!!.toInt()}",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Text(
                        text = "",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Text(
                        text = "Notes scored",
                        style = MaterialTheme.typography.headlineMedium,
                        color = PrimaryOrangeDark
                    )
                    Text(
                        text = "Notes in amp - ${autoNotesScored["Amp"]!!.toInt()}",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "Notes in speaker - ${autoNotesScored["Speaker"]!!.toInt()}",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "Notes in amplified speaker - ${autoNotesScored["Amplified speaker"]!!.toInt()}",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

                "TELEOP" -> {
                    Text(
                        text = "Notes scored",
                        style = MaterialTheme.typography.headlineMedium,
                        color = PrimaryOrangeDark
                    )
                    Text(
                        text = "Notes in amp - ${teleNotesScored["Amp"]!!.toInt()}",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "Notes in speaker - ${teleNotesScored["Speaker"]!!.toInt()}",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "Notes in amplified speaker - ${teleNotesScored["Amplified speaker"]!!.toInt()}",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Text(
                        text = "",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Text(
                        text = "Notes pickup",
                        style = MaterialTheme.typography.headlineMedium,
                        color = PrimaryOrangeDark
                    )
                    Text(
                        text = "Pickup from source - ${teleNotePickup["Source"]!!.toInt()}",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "Pickup from floor - ${teleNotePickup["Floor"]!!.toInt()}",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Text(
                        text = "",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

                "ENDGAME" -> {
                    Text(
                        text = "On stage start time",
                        style = MaterialTheme.typography.headlineMedium,
                        color = PrimaryOrangeDark
                    )
                    Text(
                        text = "Average on stage start time - ${getEndgameOnStageStartTimeAverage()}s",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Text(
                        text = "",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Text(
                        text = "Endgame activities",
                        style = MaterialTheme.typography.headlineMedium,
                        color = PrimaryOrangeDark
                    )
                    Text(
                        text = "Microphone spotlight - ${endgameBoolean["microphoneSpotlight"]!!.toInt()} yes",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "Note in trap - ${endgameBoolean["noteInTrap"]!!.toInt()} yes",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "Died during match - ${endgameBoolean["diedDuringMatch"]!!.toInt()} yes",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Text(
                        text = "",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Text(
                        text = "Robot ending position",
                        style = MaterialTheme.typography.headlineMedium,
                        color = PrimaryOrangeDark
                    )
                    Text(
                        text = "Hanging on chain - ${endgameRobotPosition["Hanging on chain"]!!.toInt()} yes",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "Parked in stage area - ${endgameRobotPosition["Parked in stage area"]!!.toInt()} yes",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "Off stage - ${endgameRobotPosition["Off stage"]!!.toInt()} yes",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

                "PIT" -> {
                    Text(
                        text = "Motors used",
                        style = MaterialTheme.typography.headlineMedium,
                        color = PrimaryOrangeDark
                    )
                    Text(
                        text = "Kraken x360 - ${String.format("%.${2}f", pitMotorsUsed["Kraken x360"]!! / (pitMotorsUsed["Kraken x360"]!! + pitMotorsUsed["NEO Motor"]!! + pitMotorsUsed["Talon FX 500"]!!) * 100)}%",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "NEO Motor - ${String.format("%.${2}f", pitMotorsUsed["NEO Motor"]!! / (pitMotorsUsed["Kraken x360"]!! + pitMotorsUsed["NEO Motor"]!! + pitMotorsUsed["Talon FX 500"]!!) * 100)}%",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "Talon FX 500 - ${String.format("%.${2}f", pitMotorsUsed["Talon FX 500"]!! / (pitMotorsUsed["Kraken x360"]!! + pitMotorsUsed["NEO Motor"]!! + pitMotorsUsed["Talon FX 500"]!!) * 100)}%",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Text(
                        text = "",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Text(
                        text = "Pit organization",
                        style = MaterialTheme.typography.headlineMedium,
                        color = PrimaryOrangeDark
                    )
                    Text(
                        text = "Average pit organization - ${String.format("%.${2}f", (pitOrganization["1"]!! + pitOrganization["2"]!! * 2 + pitOrganization["3"]!! * 3 + pitOrganization["4"]!! * 4 + pitOrganization["5"]!! * 5) / (pitOrganization["1"]!! + pitOrganization["2"]!! + pitOrganization["3"]!! + pitOrganization["4"]!! + pitOrganization["5"]!!))}",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            }
        }
    }

    fun setCompetitionList(competitionManager: CompetitionManager) {
        val documentsDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
            "VectorScout"
        )

        if (documentsDir.exists() && documentsDir.isDirectory) {
            val files = documentsDir.listFiles()

            if (files != null) {
                // Map to track the presence of both files for each competition
                // The BooleanArray tracks [MatchData.json presence, PitData.json presence]
                val competitionFiles = mutableMapOf<String, BooleanArray>()

                for (file in files) {
                    if (file.isFile) {
                        val fileName = file.name
                        val competitionName: String

                        when {
                            fileName.endsWith("MatchData.json") -> {
                                competitionName = fileName.removeSuffix("MatchData.json")
                                val flags =
                                    competitionFiles.getOrPut(competitionName) { BooleanArray(2) }
                                flags[0] = true
                            }
                            fileName.endsWith("PitData.json") -> {
                                competitionName = fileName.removeSuffix("PitData.json")
                                val flags =
                                    competitionFiles.getOrPut(competitionName) { BooleanArray(2) }
                                flags[1] = true
                            }
                        }
                    }
                }
                competitionFiles.filter { it.value[0] && it.value[1] }.keys.forEach { competitionName ->
                    if(!competitionManager.competitionList.contains(competitionName)) {
                        competitionManager.competitionList.add(competitionName)
                    }
                }
            }
        }
    }

    private fun getEndgameOnStageStartTimeAverage(): Float {
        var sum = 0F
        for(i in endgameStageStartTime.entries.firstOrNull()!!.key) {
            sum += i
        }

        return sum / endgameStageStartTime.entries.firstOrNull()!!.key.size
    }
}
