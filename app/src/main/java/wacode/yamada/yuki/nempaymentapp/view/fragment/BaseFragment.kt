package wacode.yamada.yuki.nempaymentapp.view.fragment

import android.os.Bundle
import android.support.annotation.NonNull
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import wacode.yamada.yuki.nempaymentapp.view.activity.BaseFragmentActivity
import wacode.yamada.yuki.nempaymentapp.view.dialog.LoadingDialogFragment


abstract class BaseFragment : Fragment() {
    private var loadingDialog: LoadingDialogFragment? = null

    abstract fun layoutRes(): Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layoutRes(), container, false)
    }

    protected fun replaceFragment(fragment: BaseFragment, animated: Boolean) {
        if (activity is BaseFragmentActivity) {
            (activity as BaseFragmentActivity).replaceFragment(fragment, animated)
        }
    }

    protected fun showProgress() {
        val isHidden = loadingDialog?.isHidden.let { it } ?: true

        if (isHidden) {
            loadingDialog = LoadingDialogFragment()
            loadingDialog!!.show(activity?.supportFragmentManager, LoadingDialogFragment::class.java.simpleName)
        }
    }

    protected fun hideProgress() {
        val isHidden = loadingDialog?.isHidden.let { it } ?: true

        if (!isHidden) {
            loadingDialog!!.dismiss()
            loadingDialog = null
        }
    }

    protected fun finish() {
        activity?.finish()
    }

    @NonNull
    fun getTitle() = arguments?.getInt(ARG_CONTENTS_NAME_ID, DEFAULT_VALUE_VISIBLE_TOOLBAR) ?: 0

    companion object {
        const val ARG_CONTENTS_NAME_ID = "contents_name_id"
        const val DEFAULT_VALUE_VISIBLE_TOOLBAR = Int.MIN_VALUE
    }
}