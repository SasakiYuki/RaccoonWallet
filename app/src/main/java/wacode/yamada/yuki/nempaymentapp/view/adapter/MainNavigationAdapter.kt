package wacode.yamada.yuki.nempaymentapp.view.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import wacode.yamada.yuki.nempaymentapp.BR
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.types.MainNavigationType
import wacode.yamada.yuki.nempaymentapp.model.MainNavigationEntity
import wacode.yamada.yuki.nempaymentapp.viewmodel.MainNavigationRowEventHandler
import wacode.yamada.yuki.nempaymentapp.viewmodel.MainNavigationRowLongEventHandlers
import wacode.yamada.yuki.nempaymentapp.viewmodel.MainNavigationRowViewModel

class MainNavigationAdapter(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), MainNavigationRowEventHandler, MainNavigationRowLongEventHandlers {
    private val list: ArrayList<MainNavigationEntity> = ArrayList()

    var onClickHandlers: ((MainNavigationEntity) -> Unit)? = null
    var onLongClickHandlers: ((MainNavigationEntity) -> Unit)? = null

    init {
        for (item in MainNavigationType.values()) {
            list.add(MainNavigationEntity(context.getString(item.textResource), context.getDrawable(item.drawableResource), item == MainNavigationType.MENU, type = item))
        }
    }

    internal class MainNavigationRowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: ViewDataBinding? = DataBindingUtil.bind<ViewDataBinding>(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val viewModel = MainNavigationRowViewModel(list[position])

        val binding = (holder as MainNavigationRowViewHolder).binding
        binding?.let {
            binding.setVariable(BR.viewModel, viewModel)
            binding.setVariable(BR.handlers, this)
            binding.setVariable(BR.longHandlers, this)
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.row_main_navigation, parent, false)
        return MainNavigationRowViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onClick(view: View, viewModel: MainNavigationRowViewModel) {
        onClickHandlers?.let {
            it(viewModel.mainNavigation)
        }
    }

    override fun onLongClick(view: View, viewModel: MainNavigationRowViewModel): Boolean {
        onLongClickHandlers?.let {
            it(viewModel.mainNavigation)
        }
        return true
    }
}
