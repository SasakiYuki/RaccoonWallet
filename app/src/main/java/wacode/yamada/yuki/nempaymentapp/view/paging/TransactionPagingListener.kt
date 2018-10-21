package wacode.yamada.yuki.nempaymentapp.view.paging

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import wacode.yamada.yuki.nempaymentapp.view.adapter.TransactionAdapter


abstract class TransactionPagingListener(private val linearLayoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {
    abstract fun onLoadMore()

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val loading = recyclerView.tag as Boolean

        if (!loading && recyclerView.childCount != 1 && linearLayoutManager.findLastVisibleItemPosition() + TransactionAdapter.HEADER_SIZE == linearLayoutManager.itemCount) {
            onLoadMore()
            recyclerView.tag = true
        }
    }
}