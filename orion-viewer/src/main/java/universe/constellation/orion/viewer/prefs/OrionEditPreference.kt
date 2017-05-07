/*
 * Orion Viewer - pdf, djvu, xps and cbz file viewer for android devices
 *
 * Copyright (C) 2011-2013  Michael Bogdanov & Co
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package universe.constellation.orion.viewer.prefs

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.widget.Toast
import universe.constellation.orion.viewer.R
import java.util.regex.Pattern

/**
 * User: mike
 * Date: 25.01.12
 * Time: 12:41
 */
class OrionEditPreference @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
        android.support.v7.preference.EditTextPreference(context, attrs, defStyle), android.support.v7.preference.Preference.OnPreferenceChangeListener, OrionPreference {

    private var minValue: Int? = null
    private var maxValue: Int? = null

    private var pattern: String? = null

    private var originalSummary: CharSequence? = null

    override var isCurrentBookOption: Boolean = false

    init {
        attrs?.let { init(it) }
    }

    override fun onPreferenceChange(preference: android.support.v7.preference.Preference, newValue: Any?): Boolean {
        if (minValue != null || maxValue != null) {
            if (newValue == null || "" == newValue) {
                Toast.makeText(context, "Value couldn't be empty!", Toast.LENGTH_SHORT).show()
                return false
            }
        }

        if (minValue != null && minValue!! > (newValue as String).toInt()) {
            Toast.makeText(context, "New value should be greater or equal than " + minValue!!, Toast.LENGTH_SHORT).show()
            return false
        }

        if (maxValue != null && maxValue!! < (newValue as String).toInt()) {
            Toast.makeText(context, "New value should be less or equal than " + maxValue!!, Toast.LENGTH_SHORT).show()
            return false
        }

        if (pattern != null && !Pattern.compile(pattern!!).matcher((newValue as String?)!!).matches()) {
            Toast.makeText(context, "Couldn't set value: wrong interval!", Toast.LENGTH_SHORT).show()
            return false
        }

        summary = originalSummary.toString() + ": " + newValue

        return true
    }


    private fun init(attrs: AttributeSet) {
        originalSummary = summary
        val a = context.obtainStyledAttributes(attrs, R.styleable.universe_constellation_orion_viewer_prefs_OrionEditPreference)
        pattern = a.getString(R.styleable.universe_constellation_orion_viewer_prefs_OrionEditPreference_pattern)
        minValue = getIntegerOrNull(a, R.styleable.universe_constellation_orion_viewer_prefs_OrionEditPreference_minValue)
        maxValue = getIntegerOrNull(a, R.styleable.universe_constellation_orion_viewer_prefs_OrionEditPreference_maxValue)
        isCurrentBookOption = a.getBoolean(R.styleable.universe_constellation_orion_viewer_prefs_OrionEditPreference_isBook, false)
        a.recycle()
        if (pattern != null || minValue != null || maxValue != null) {
            onPreferenceChangeListener = this
        }
    }

    private fun getIntegerOrNull(array: TypedArray, id: Int): Int? {
        val UNDEFINED = -10000
        val value = array.getInt(id, UNDEFINED)
        if (value == UNDEFINED) {
            return null
        } else {
            return value
        }
    }

    override fun onSetInitialValue(restoreValue: Boolean, defaultValue: Any?) {
        super.onSetInitialValue(if (isCurrentBookOption) true else restoreValue, defaultValue)
    }

    override fun persistString(value: String): Boolean {
        if (isCurrentBookOption) {
            return OrionPreferenceUtil.persistValue(this, value)
        } else {
            return super.persistString(value)
        }
    }

    override fun getPersistedInt(defaultReturnValue: Int): Int {
        if (isCurrentBookOption) {
            return OrionPreferenceUtil.getPersistedInt(this, defaultReturnValue)
        } else {
            return super.getPersistedInt(defaultReturnValue)
        }
    }


    override fun getPersistedString(defaultReturnValue: String?): String? {
        if (isCurrentBookOption) {
            return OrionPreferenceUtil.getPersistedString(this, defaultReturnValue)
        } else {
            return super.getPersistedString(defaultReturnValue)
        }
    }
}
