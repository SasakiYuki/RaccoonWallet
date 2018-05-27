package wacode.yamada.yuki.nempaymentapp.view.dialog

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import io.reactivex.subjects.PublishSubject
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.view.ViewPagerIndicator
import wacode.yamada.yuki.nempaymentapp.view.adapter.DialogPagerAdapter
import wacode.yamada.yuki.nempaymentapp.view.fragment.SimpleMessageFragment


class RaccoonPagerDialog : DialogFragment() {
    private lateinit var viewModel: RaccoonPagerViewModel
    private val list = ArrayList<Fragment>()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.dialog_simple_pager, null, false)
        view?.let {
            setupMessagePager(it)
            setupTitle(it)
            setupButtons(it)
        }
        return view
    }

    private fun setupMessagePager(view: View) {
        val adapter = DialogPagerAdapter(list, childFragmentManager)
        val viewPager = view.findViewById<ViewPager>(R.id.viewpager)
        viewPager.adapter = adapter
        val indicator = view.findViewById<ViewPagerIndicator>(R.id.pagerIndicator)
        indicator.setCount(adapter.count)
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                indicator.setCurrentPosition(position)
            }

            override fun onPageSelected(position: Int) {
            }
        })
    }

    private fun setupTitle(view: View) {
        view.findViewById<TextView>(R.id.titleTextView).text = getTitle()
    }

    private fun setupButtons(view: View) {
        val button = view.findViewById<Button>(R.id.button)
        button.text = getButtonText()
        button.setOnClickListener {
            viewModel.onClickBottomButton()
            dismiss()
        }
    }

    private fun getTitle() = arguments.getString(ARG_TITLE, "")
    private fun getButtonText() = arguments.getString(ARG_BUTTON_TEXT, "")

    companion object {
        private const val ARG_TITLE = "title"
        private const val ARG_BUTTON_TEXT = "button_text"
        fun createDialog(viewModel: RaccoonPagerViewModel, title: String, buttonText: String, messageFragmentList: ArrayList<SimpleMessageFragment>): RaccoonPagerDialog {
            val dialog = RaccoonPagerDialog()
            dialog.viewModel = viewModel
            dialog.list.addAll(messageFragmentList)
            val args = Bundle().apply {
                putString(ARG_TITLE, title)
                putString(ARG_BUTTON_TEXT, buttonText)
            }
            dialog.arguments = args
            return dialog
        }
    }
}

class RaccoonPagerViewModel {
    val clickEvent: PublishSubject<RaccoonPagerType> = PublishSubject.create()

    fun onClickBottomButton() {
        onClickButton(RaccoonPagerType.BOTTOM_BUTTON)
    }

    private fun onClickButton(type: RaccoonPagerType) {
        clickEvent.onNext(type)
    }
}

enum class RaccoonPagerType {
    BOTTOM_BUTTON,
}
