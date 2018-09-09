package wacode.yamada.yuki.nempaymentapp.view.custom_view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.ColorRes
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.view_back_layer_search.view.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.extentions.getColorFromResource
import wacode.yamada.yuki.nempaymentapp.extentions.getColorStateListFromResource


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

    private fun setChipColors(chip: Chip, @ColorRes colorRes: Int) {
        chip.chipStrokeColor = context.getColorStateListFromResource(colorRes)
        chip.setTextColor(context.getColorFromResource(colorRes))
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        searchButton.setOnClickListener { listener.onItemClicked(searchEditText.text.toString(), getSearchType()) }

        chipGroup.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.allChip -> {
                    setChipColors(allChip, R.color.nemGreen)
                    setChipColors(localChip, R.color.textGray)
                    setChipColors(twitterChip, R.color.textGray)
                    listener.onItemClicked(searchEditText.text.toString(), getSearchType())
                }
                R.id.localChip -> {
                    setChipColors(allChip, R.color.textGray)
                    setChipColors(localChip, R.color.nemGreen)
                    setChipColors(twitterChip, R.color.textGray)
                    listener.onItemClicked(searchEditText.text.toString(), getSearchType())
                }
                R.id.twitterChip -> {
                    setChipColors(allChip, R.color.textGray)
                    setChipColors(localChip, R.color.textGray)
                    setChipColors(twitterChip, R.color.nemGreen)
                    listener.onItemClicked(searchEditText.text.toString(), getSearchType())
                }
            }
        }



        backButton.setOnClickListener { listener.onFinishClick() }
    }

    private fun getSearchType(): SearchType {
        return when {
            allChip.isChecked -> SearchType.ALL
            localChip.isChecked -> SearchType.LOCAL
            else -> SearchType.TWITTER
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(word: String, type: SearchType)

        fun onFinishClick()
    }

    enum class SearchType {
        ALL,
        LOCAL,
        TWITTER
    }
}
