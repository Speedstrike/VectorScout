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
package com.scouting.app.model

import androidx.compose.runtime.MutableState
import com.scouting.app.misc.TemplateTypes

data class TemplateItem(
    // Require separate unique key from item index to allow drag and drop
    var id: String,
    var text: String,
    var text2: String? = null,
    var text3: String? = null,
    var text4: String? = null,
    var type: TemplateTypes,
    // The type of the item will always be non null so by using the type
    // we can determine which of these values we know will not be null
    var itemValueInt: MutableState<Int>? = null,
    var itemValue2Int: MutableState<Int>? = null,
    var itemValue3Int: MutableState<Int>? = null,
    var itemValueBoolean: MutableState<Boolean>? = null,
    var itemValueString: MutableState<String>? = null,
    var saveKey: String,
    var saveKey2: String? = null,
    var saveKey3: String? = null
)
