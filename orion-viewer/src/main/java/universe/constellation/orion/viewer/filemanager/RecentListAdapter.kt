/*
 * Orion Viewer - pdf, djvu, xps and cbz file viewer for android devices
 *
 * Copyright (C) 2011-2017 Michael Bogdanov & Co
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

package universe.constellation.orion.viewer.filemanager

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import universe.constellation.orion.viewer.R
import universe.constellation.orion.viewer.prefs.GlobalOptions

class RecentListAdapter(context: Context, entries: List<GlobalOptions.RecentEntry>) : ArrayAdapter<GlobalOptions.RecentEntry>(context, R.layout.file_entry, R.id.fileName, entries) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val newConvertView = super.getView(position, convertView, parent)
        getItem(position)?.let {
            val name = it.lastPathElement

            val icon = getIconByNameExtension(name)
            val iconView = newConvertView!!.findViewById<ImageView>(R.id.fileImage)!!
            iconView.setImageResource(icon)

            val fileName = newConvertView.findViewById<TextView>(R.id.fileName)!!
            fileName.text = name
        }

        return newConvertView
    }
}

fun getIconByNameExtension(name: String): Int {
    val extName = name.fileExtension.toLowerCase()
    return when (extName) {
        "pdf" -> R.drawable.pdf
        "djvu", "djv" -> R.drawable.djvu
        "cbz", "tif", "tiff" -> R.drawable.cbz
        "xps", "oxps" -> R.drawable.xps
        "xml" -> R.drawable.xml
        else -> R.drawable.djvu
    }
}

val String.fileExtension
    get() = substringAfterLast('.', "")