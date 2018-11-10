package wacode.yamada.yuki.nempaymentapp.view.paging

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import wacode.yamada.yuki.nempaymentapp.R


abstract class TransactionPagingListener(private val linearLayoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {
    abstract fun onLoadMore()

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val allowLoading = recyclerView.getTag(R.id.allow_loading) as Boolean

        if (allowLoading && recyclerView.childCount != 1 && linearLayoutManager.findLastVisibleItemPosition() - 1 == linearLayoutManager.itemCount - 2) {
            onLoadMore()
            recyclerView.setTag(R.id.allow_loading, false)
        }
    }
}