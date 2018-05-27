package wacode.yamada.yuki.nempaymentapp.view.fragment

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_tutorial_description.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.view.activity.OnPrivateKeyStorePageChangeListener
import wacode.yamada.yuki.nempaymentapp.view.activity.PrivateKeyStoreSupportDisplayType

class TutorialDescriptionFragment : BaseFragment() {
    override fun layoutRes() = R.layout.fragment_tutorial_description
    private var listenerPrivateKeyStore: OnPrivateKeyStorePageChangeListener? = null

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButtonClickListener()
    }

    private fun setupButtonClickListener() {
        button.setOnClickListener {
            listenerPrivateKeyStore?.onClickNextButton(PrivateKeyStoreSupportDisplayType.TUTORIAL)
        }
    }

    companion object {
        fun newInstance(listenerPrivateKeyStore: OnPrivateKeyStorePageChangeListener): TutorialDescriptionFragment {
            val fragment = TutorialDescriptionFragment()
            fragment.listenerPrivateKeyStore = listenerPrivateKeyStore
            val bundle = Bundle()
            bundle.putInt(ARG_CONTENTS_NAME_ID, R.string.tutorial_description_fragment_title)
            fragment.arguments = bundle
            return fragment
        }
    }
}