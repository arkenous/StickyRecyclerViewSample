package link.k3n.sticky_recyclerview_sample.views.custom

import android.databinding.DataBindingUtil
import android.graphics.Canvas
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import link.k3n.sticky_recyclerview_sample.databinding.ListItemHeaderBinding

/**
 * RecyclerView ItemDecoration custom class for RecyclerView Items sticky.
 * @author Daiki Terai
 * @see <a href="https://qiita.com/teradonburi/items/279fd0ed93871b4a413c">Original Qiita post</a>
 */
class StickyHeaderItemDecoration(listener: StickyHeaderInterface) : RecyclerView.ItemDecoration() {

    private val mListener = listener
    private lateinit var mCurrentHeader: View

    interface StickyHeaderInterface {
        fun getHeaderPositionForItem(itemPosition: Int): Int
        fun getHeaderLayout(headerPosition: Int): Int
        fun bindHeaderData(binding: ListItemHeaderBinding, header: View, headerPosition: Int)
        fun isHeader(itemPosition: Int): Boolean
    }

    override fun onDrawOver(c: Canvas?, parent: RecyclerView?, state: RecyclerView.State?) {
        super.onDrawOver(c, parent, state)

        parent ?: return
        c ?: return

        val topChild = parent.getChildAt(0) ?: return

        val topChildPosition = parent.getChildAdapterPosition(topChild)
        if (topChildPosition == RecyclerView.NO_POSITION) return

        val previousHeaderPosition = mListener.getHeaderPositionForItem(topChildPosition)
        if (previousHeaderPosition == -1) return

        // ヘッダビューが表示された
        mCurrentHeader = getHeaderViewFromItem(topChildPosition, parent)
        fixLayoutSize(parent, mCurrentHeader)
        val contactPoint = mCurrentHeader.bottom
        // 次のセルを取得し、次のセルがないならreturn
        val childInContact = getChildInContact(parent, contactPoint) ?: return

        // ヘッダの判定
        if (mListener.isHeader(parent.getChildAdapterPosition(childInContact))) {
            // 既存のStickyヘッダを押し上げる
            moveHeader(c, mCurrentHeader, childInContact)
            return
        }

        // Stickyヘッダの描画
        drawHeader(c, mCurrentHeader)
    }

    private fun getHeaderViewFromItem(itemPosition: Int, parent: RecyclerView): View {
        val headerPosition = mListener.getHeaderPositionForItem(itemPosition)
        val layoutResId = mListener.getHeaderLayout(headerPosition)
        // Stickyヘッダレイアウトをinflateする
        val binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), layoutResId, parent, false) as ListItemHeaderBinding
        val header = binding.root
        mListener.bindHeaderData(binding, header, headerPosition)
        return header
    }

    private fun drawHeader(c: Canvas, header: View) {
        c.save()
        c.translate(0f, 0f)
        header.draw(c)
        c.restore()
    }

    private fun moveHeader(c: Canvas, currentHeader: View, nextHeader: View) {
        c.save()
        c.translate(0f, (nextHeader.top - currentHeader.height).toFloat())
        currentHeader.draw(c)
        c.restore()
    }

    private fun getChildInContact(parent: RecyclerView, contactPoint: Int): View? {
        var childInContact: View? = null
        var counter = 0
        while (counter < parent.childCount) {
            val child = parent.getChildAt(counter)
            if (child.bottom > contactPoint && child.top <= contactPoint) {
                childInContact = child
                break;
            }
            ++counter
        }
        return childInContact
    }

    private fun fixLayoutSize(parent: ViewGroup, view: View) {
        // RecyclerViewのSpec
        val widthSpec = View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.UNSPECIFIED)

        // headerのSpec
        val childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec, parent.paddingStart + parent.paddingEnd, view.layoutParams.width)
        val childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec, parent.paddingTop + parent.paddingBottom, view.layoutParams.height)

        view.measure(childWidthSpec, childHeightSpec)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
    }
}