package wacode.yamada.yuki.nempaymentapp.view.adapter

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import wacode.yamada.yuki.nempaymentapp.BR
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.rest.model.MosaicIdEntity
import wacode.yamada.yuki.nempaymentapp.viewmodel.MosaicRowEventHandler
import wacode.yamada.yuki.nempaymentapp.viewmodel.MosaicRowLongEventHandler
import wacode.yamada.yuki.nempaymentapp.viewmodel.MosaicRowViewModel

class MosaicImageAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(), MosaicRowEventHandler, MosaicRowLongEventHandler {

    private val list: ArrayList<MosaicIdEntity> = ArrayList()

    var onClickHandlers: ((MosaicIdEntity) -> Unit)? = null
    var onLongClickHandlers: ((MosaicIdEntity) -> Unit)? = null

    internal class MosaicRowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: ViewDataBinding? = DataBindingUtil.bind<ViewDataBinding>(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val viewModel = MosaicRowViewModel(list[position])

        val binding = (holder as MosaicRowViewHolder).binding
        binding?.let {
            binding.setVariable(BR.viewModel, viewModel)
            binding.setVariable(BR.handlers, this)
            binding.setVariable(BR.longHandlers, this)
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.row_mosaic, parent, false)
        return MosaicRowViewHolder(view)
    }

    fun addItem(address: MosaicIdEntity) {
        if (!list.contains(address)) {
            list.add(address)
        }
    }

    override fun getItemCount() = list.size

    override fun onMosaicClick(view: View, viewModel: MosaicRowViewModel) {
        onClickHandlers?.let {
            it(viewModel.mosaicIdEntity)
        }
    }

    override fun onMosaicLongClick(view: View, viewModel: MosaicRowViewModel): Boolean {
        onLongClickHandlers?.let {
            it(viewModel.mosaicIdEntity)
        }
        return true
    }
}
