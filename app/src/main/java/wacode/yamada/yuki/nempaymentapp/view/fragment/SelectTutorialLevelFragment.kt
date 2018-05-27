package wacode.yamada.yuki.nempaymentapp.view.fragment

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_select_tutorial_level.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.view.activity.PrivateKeyStoreSupportTutorialType
import wacode.yamada.yuki.nempaymentapp.view.activity.SelectTutorialLevelListener

class SelectTutorialLevelFragment : BaseFragment() {
    private var listener: SelectTutorialLevelListener? = null

    override fun layoutRes() = R.layout.fragment_select_tutorial_level

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButtons()
    }

    private fun setupButtons() {
        newbieButton.setOnClickListener {
            listener?.onSelectTutorialType(PrivateKeyStoreSupportTutorialType.NEWBIE)
        }
        loginButton.setOnClickListener {
            listener?.onSelectTutorialType(PrivateKeyStoreSupportTutorialType.LOGIN)
        }
        raccoonButton.setOnClickListener {
            listener?.onSelectTutorialType(PrivateKeyStoreSupportTutorialType.RACCOON)
        }
    }

    companion object {
        fun newInstance(listener: SelectTutorialLevelListener): SelectTutorialLevelFragment {
            val fragment = SelectTutorialLevelFragment()
            val bundle = Bundle()
            bundle.putInt(ARG_CONTENTS_NAME_ID, R.string.select_tutorial_level_fragment_title)
            fragment.arguments = bundle
            fragment.listener = listener
            return fragment
        }
    }
}