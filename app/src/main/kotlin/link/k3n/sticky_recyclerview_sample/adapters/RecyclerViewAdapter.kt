package link.k3n.sticky_recyclerview_sample.adapters

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import link.k3n.sticky_recyclerview_sample.R
import link.k3n.sticky_recyclerview_sample.databinding.ListItemContentBinding
import link.k3n.sticky_recyclerview_sample.databinding.ListItemHeaderBinding
import link.k3n.sticky_recyclerview_sample.viewmodels.MainViewModel
import link.k3n.sticky_recyclerview_sample.views.custom.StickyHeaderItemDecoration


/**
 * RecyclerView Adapter
 */
class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(), StickyHeaderItemDecoration.StickyHeaderInterface {

    private var mItems = ArrayList<MainViewModel.BaseListItem>()

    fun setItems(items: ArrayList<MainViewModel.BaseListItem>) {
        mItems = items
        notifyDataSetChanged()
    }

    //region RecyclerView.Adapter

    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (mItems[position]) {
            is MainViewModel.HeaderListItem -> R.layout.list_item_header
            else -> R.layout.list_item_content
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            R.layout.list_item_header -> HeaderViewHolder.create(parent)
            else -> ContentViewHolder.create(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when (holder) {
            is HeaderViewHolder -> holder.update(mItems[position])
            is ContentViewHolder -> holder.update(mItems[position])
        }
    }

    //endregion

    //region ViewHolder

    private interface ViewHolderInterface {
        fun update(item: MainViewModel.BaseListItem)
    }

    private class HeaderViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView), ViewHolderInterface {
        var mBinding: ListItemHeaderBinding = DataBindingUtil.getBinding(itemView)

        constructor(binding: ListItemHeaderBinding) : this(binding.root) {
            mBinding = binding
        }

        companion object {
            fun create(parent: ViewGroup?) : HeaderViewHolder = HeaderViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent?.context), R.layout.list_item_header, parent, false) as ListItemHeaderBinding)
        }

        override fun update(item: MainViewModel.BaseListItem) {
            if (item is MainViewModel.HeaderListItem) {
                mBinding.headerTextView.text = item.mHeaderTitle
                mBinding.executePendingBindings()
            }
        }
    }

    private class ContentViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView), ViewHolderInterface {
        var mBinding: ListItemContentBinding = DataBindingUtil.getBinding(itemView)

        constructor(binding: ListItemContentBinding) : this(binding.root) {
            mBinding = binding
        }

        companion object {
            fun create(parent: ViewGroup?) : ContentViewHolder = ContentViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent?.context), R.layout.list_item_content, parent, false) as ListItemContentBinding)
        }

        override fun update(item: MainViewModel.BaseListItem) {
            if (item is MainViewModel.ContentListItem) {
                mBinding.contentTextView.text = item.mContent
                mBinding.executePendingBindings()
            }
        }
    }

    //endregion

    //region StickyHeaderItemDecoration.StickyHeaderInterface

    override fun getHeaderPositionForItem(itemPosition: Int): Int {
        var position = itemPosition
        var headerPosition = -1
        while (position >= 0) {
            if (isHeader(position)) {
                headerPosition = position
                break
            }
            --position
        }
        return headerPosition
    }

    override fun getHeaderLayout(headerPosition: Int): Int = R.layout.list_item_header

    override fun bindHeaderData(binding: ListItemHeaderBinding, header: View, headerPosition: Int) {
        val item = mItems[headerPosition]
        if (item is MainViewModel.HeaderListItem) {
            binding.headerTextView.text = item.mHeaderTitle
        }
    }

    override fun isHeader(itemPosition: Int): Boolean = mItems[itemPosition] is MainViewModel.HeaderListItem

    //endregion
}