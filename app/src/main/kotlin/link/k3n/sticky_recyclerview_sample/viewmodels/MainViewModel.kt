package link.k3n.sticky_recyclerview_sample.viewmodels

/**
 * MainViewModel
 */
class MainViewModel {

    /**
     * HeaderListItemを取得する
     *
     * @param title タイトル文字列
     * @return BaseListItem
     */
    fun getHeaderListItem(title: String) : BaseListItem {
        return HeaderListItem("header_$title")
    }

    /**
     * ContentListItemを取得する
     *
     * @param content コンテンツ文字列
     * @return BaseListItem
     */
    fun getContentListItem(content: String) : BaseListItem {
        return ContentListItem("content_$content")
    }

    //region ListItem class

    /**
     * RecyclerView用 BaseListItem
     */
    open class BaseListItem

    /**
     * RecyclerView用 HeaderListItem
     *
     * @param title タイトル文字列
     */
    internal class HeaderListItem(title: String) : BaseListItem() {
        var mHeaderTitle = title
    }

    /**
     * RecyclerView用 ContentListItem
     *
     * @param content コンテンツ文字列
     */
    internal class ContentListItem(content: String) : BaseListItem() {
        var mContent = content
    }

    //endregion
}