package wacode.yamada.yuki.nempaymentapp.view.custom_view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_back_layer_search.view.*
import wacode.yamada.yuki.nempaymentapp.R


class BackLayerSearchView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : LinearLayout(context, attrs, defStyleAttr) {
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?) : this(context, null, 0)

    init {
        val view = View.inflate(context, R.layout.view_back_layer_search, this)
        context?.obtainStyledAttributes(attrs, R.styleable.BackLayerSearchView, defStyleAttr, 0)?.let {
            try {
                view.titleText.text = it.getString(R.styleable.BackLayerSearchView_title)

                view.menuButton.setOnClickListener {
                    view.menuButton.visibility = View.GONE
                    view.closeButton.visibility = View.VISIBLE
                    view.searchRootView.visibility = View.VISIBLE
                }

                view.closeButton.setOnClickListener {
                    view.menuButton.visibility = View.VISIBLE
                    view.closeButton.visibility = View.GONE
                    view.searchRootView.visibility = View.GONE
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                it.recycle()
            }
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        searchButton.setOnClickListener { listener.onSearchClick(searchEditText.text.toString()) }
        backButton.setOnClickListener { listener.onFinishClick() }
    }

    interface OnItemClickListener {
        fun onSearchClick(word: String)

        fun onFinishClick()
    }
}
