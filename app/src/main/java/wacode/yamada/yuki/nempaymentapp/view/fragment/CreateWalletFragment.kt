package wacode.yamada.yuki.nempaymentapp.view.fragment

import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.view.View
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_create_wallet.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.view.activity.CreateWalletDisplayType
import wacode.yamada.yuki.nempaymentapp.view.activity.OnCreateWalletPageChangeListener


class CreateWalletFragment : BaseFragment() {
    private var listener: OnCreateWalletPageChangeListener? = null

    override fun layoutRes() = R.layout.fragment_create_wallet

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOKButtonClickListener()
        setupEditText()
        setupTermsOfServiceTextView()
    }

    private fun setupTermsOfServiceTextView() {
        termsOfServiceTextView.setOnClickListener {
            val url = "https://raccoonwallet.com/tos_pp/"
            val builder = CustomTabsIntent.Builder()
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(activity, Uri.parse(url))
        }
    }

    private fun getWalletName() = editText.text.toString()

    private fun setupOKButtonClickListener() {
        button.setOnClickListener {
            listener?.onNameConfirm(getWalletName())
            listener?.onClickNextButton(CreateWalletDisplayType.CREATE)
        }
    }

    override fun onResume() {
        super.onResume()
        editText.setText("")
    }

    private fun setupEditText() {
        isNotTextEmptyObservable()
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ item ->
                    button.alpha = if (item) 1.0f else 0.5f
                    button.isEnabled = item
                })
    }

    private fun isNotTextEmptyObservable() = RxTextView.textChanges(editText)
            .map { it ->
                it.isNotEmpty()
            }

    companion object {
        fun newInstance(listener: OnCreateWalletPageChangeListener): CreateWalletFragment {
            val fragment = CreateWalletFragment()
            val bundle = Bundle()
            bundle.putInt(BaseFragment.ARG_CONTENTS_NAME_ID, R.string.create_wallet_activity_title)
            fragment.arguments = bundle
            fragment.listener = listener
            return fragment
        }
    }
}