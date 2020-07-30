package wacode.yamada.yuki.nempaymentapp.view.adapter

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import wacode.yamada.yuki.nempaymentapp.BR
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.model.TransactionAppEntity
import wacode.yamada.yuki.nempaymentapp.types.TransactionType
import wacode.yamada.yuki.nempaymentapp.utils.DateComparator
import wacode.yamada.yuki.nempaymentapp.view.activity.SendMessageType
import wacode.yamada.yuki.nempaymentapp.viewmodel.TransactionRowEventHandler
import wacode.yamada.yuki.nempaymentapp.viewmodel.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TransactionAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(), TransactionRowEventHandler {
    private val transactionList: ArrayList<TransactionAppEntity> = ArrayList()
    private var onDateChangeListener: OnDateChangeListener? = null
    private var detachedOldPosition = -1
    private var attachOldPosition = -1
    private var showIndicator = false

    var onClickHandlers: ((TransactionAppEntity) -> Unit)? = null

    interface OnDateChangeListener {
        fun showDateLabel(date: String)

        fun showWalletBar()
    }

    internal class TransactionRowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: ViewDataBinding? = DataBindingUtil.bind(itemView)
    }

    internal class TransactionDateRowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val date = itemView.findViewById<TextView>(R.id.transactionDate)
    }

    internal class TransactionSpaceHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    internal class IndicatorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_DATE -> {
                TransactionDateRowViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_transaction_date, parent, false))
            }
            VIEW_TYPE_SPACE -> {
                TransactionSpaceHeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.header_transaction_space, parent, false))
            }
            VIEW_TYPE_INDICATOR -> {
                IndicatorViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_indicator, parent, false))
            }
            else -> {
                TransactionRowViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_transaction, parent, false))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_NORMAL -> {
                val viewModel = TransactionViewModel(transactionList[position - HEADER_SIZE])
                val binding = (holder as TransactionRowViewHolder).binding
                binding?.let {
                    binding.setVariable(BR.viewModel, viewModel)
                    binding.setVariable(BR.handlers, this)
                    binding.executePendingBindings()
                }
            }
            VIEW_TYPE_DATE -> {
                (holder as TransactionDateRowViewHolder).date.text = parseDate(transactionList[position - HEADER_SIZE].date)
            }
        }
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)

        if (holder.adapterPosition == 0) {
            onDateChangeListener!!.showWalletBar()
        }

        if (attachOldPosition == holder.adapterPosition + 1) {
            //１個づつ上がった場合
            if (holder is TransactionRowViewHolder) {
                val sdf = SimpleDateFormat("MM/dd, yyyy")
                val currentDate = sdf.parse(parseDate(transactionList[holder.adapterPosition].date))
                val nextDate = sdf.parse(parseDate(transactionList[holder.adapterPosition - 1].date))

                if (currentDate.compareTo(nextDate) == -1) {
                    onDateChangeListener!!.showDateLabel(sdf.format(nextDate))
                }
            }

        }
        attachOldPosition = holder.adapterPosition
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)

        if (transactionList.isEmpty()) return

        if (holder.adapterPosition == 0) {
            onDateChangeListener!!.showDateLabel(parseDate(transactionList[0].date))
        }

        if (detachedOldPosition == holder.adapterPosition - 1) {
            //順調に1個づつ下がっている場合
            if (holder is TransactionRowViewHolder) {
                if (itemCount > 1) {
                    val sdf = SimpleDateFormat("MM/dd, yyyy")
                    val currentDate = sdf.parse(parseDate(transactionList[holder.adapterPosition - 1].date))
                    val nextDate = sdf.parse(parseDate(transactionList[holder.adapterPosition].date))
                    if (currentDate.compareTo(nextDate) == 1) {
                        onDateChangeListener!!.showDateLabel(sdf.format(nextDate))
                    }
                }
            }
        }
        detachedOldPosition = holder.adapterPosition
    }


    override fun getItemViewType(position: Int): Int {
        return when {
            transactionList.size + HEADER_SIZE == position -> VIEW_TYPE_INDICATOR
            position == 0 -> VIEW_TYPE_SPACE
            checkDateViewType(position) -> VIEW_TYPE_DATE
            else -> VIEW_TYPE_NORMAL
        }
    }

    override fun getItemCount(): Int {
        return if (showIndicator) {
            transactionList.size + HEADER_SIZE + INDICATOR_SIZE
        } else {
            transactionList.size + HEADER_SIZE
        }
    }

    override fun onTransactionClick(view: View, viewModel: TransactionViewModel) {
        onClickHandlers?.let {
            it(viewModel.transactionAppEntity)
        }
    }

    fun addItem(transactionEntity: TransactionAppEntity) {
        if (!transactionList.contains(transactionEntity)) {
            transactionList.add(transactionEntity)
            addDate(transactionEntity.date)
            Collections.sort(transactionList, DateComparator())
            notifyDataSetChanged()
        }
    }

    fun clearItems() {
        transactionList.clear()
        attachOldPosition = -1
        detachedOldPosition = -1
    }

    fun setDateChangeListener(listener: OnDateChangeListener) {
        this.onDateChangeListener = listener
    }

    fun getLastTransactionId() = transactionList.last().transactionId

    private fun checkDateViewType(position: Int): Boolean {
        if (position == 1) return true

        if (position == transactionList.size + HEADER_SIZE) {
            return false
        }

        val model = transactionList[position - HEADER_SIZE]
        if (model.senderAddress.isEmpty() && model.recipientAddress.isEmpty()) {
            return true
        }

        return false
    }

    private fun parseDate(date: String?): String {
        val sdf = SimpleDateFormat("MM/dd,yyyy k:mm;ss")
        val date = sdf.parse(date)

        val sdf1 = SimpleDateFormat("MM/dd, yyyy")
        return sdf1.format(date)
    }

    private fun addDate(t1: String?) {
        val sdf = SimpleDateFormat("MM/dd,yyyy k:mm;ss")

        val targetDate = Calendar.getInstance()
        targetDate.time = sdf.parse(t1)
        targetDate.set(Calendar.HOUR_OF_DAY, 23)
        targetDate.set(Calendar.MINUTE, 59)
        targetDate.set(Calendar.SECOND, 59)

        val date1 = sdf.format(targetDate.time)

        val model = TransactionAppEntity(
                transactionType = TransactionType.OUTGOING,
                date = date1,
                messageType = SendMessageType.NONE
        )

        if (!transactionList.contains(model)) {
            transactionList.add(model)
        }
    }

    fun showIndicator(isShow: Boolean) {
        showIndicator = isShow

        if (!isShow) {
            notifyItemRemoved(transactionList.size + HEADER_SIZE)
        }
    }

    companion object {
        const val VIEW_TYPE_SPACE = 0
        const val VIEW_TYPE_NORMAL = 1
        const val VIEW_TYPE_DATE = 2
        const val VIEW_TYPE_INDICATOR = 3

        const val HEADER_SIZE = 1
        const val INDICATOR_SIZE = 1
    }
}