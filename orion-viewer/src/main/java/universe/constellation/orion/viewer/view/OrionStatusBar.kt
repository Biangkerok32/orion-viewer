package universe.constellation.orion.viewer.view

import android.graphics.Bitmap
import android.graphics.ColorMatrixColorFilter
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import universe.constellation.orion.viewer.*
import java.util.concurrent.CountDownLatch

/**
 * Created by mike on 10/11/15.
 */
class OrionStatusBarHelper(val view: ViewGroup) : OrionBookListener, OrionImageListener {
    val panel = view.findViewById(R.id.orion_status_bar) as ViewGroup
    val title = view.findViewById(R.id.title) as TextView
    val offset = view.findViewById(R.id.offset) as TextView
    val page = view.findViewById(R.id.page) as TextView
    val totalPages = view.findViewById(R.id.totalPages) as TextView
    var info: LayoutPosition? = null

    override fun onNewBook(title: String?, pageCount: Int) {
        this.title.text = title
        this.totalPages.text = "/$pageCount"
        this.page.text = "?"
        this.offset.text = "[?, ?]"
    }

    override fun onNewImage(bitmap: Bitmap?, info: LayoutPosition?, latch: CountDownLatch?) {
        info?.let {
            offset.text = "[${pad(info.x.offset)}:${pad(info.y.offset)}]"
            page.text = "${info.pageNumber + 1}"
        }
    }

    private fun pad(value: Int): String {
        val pValue = Math.abs(value)
        if (pValue < 10) {
            return "  " + value
        } else if (pValue < 100) {
            return " " + value
        } else {
            return "" + value
        }
    }

    fun setShowOffset(showOffset: Boolean) {
        offset.visibility = if (showOffset) View.VISIBLE else View.GONE
    }

    fun setShowStatusBar(showStatusBar: Boolean) {
        panel.visibility = if (showStatusBar) View.VISIBLE else View.GONE
    }

    fun setColorMatrix(colorMatrix: FloatArray?) {
        val colorFilter = if (colorMatrix == null) null else ColorMatrixColorFilter(colorMatrix)
        (0..panel.childCount-1).forEach {
            val child = panel.getChildAt(it)
            when(child) {
                is TextView -> child.paint.colorFilter = colorFilter
            }
        }
    }

}