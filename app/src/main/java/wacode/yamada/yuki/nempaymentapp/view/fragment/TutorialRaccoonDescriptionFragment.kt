package wacode.yamada.yuki.nempaymentapp.view.fragment

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_tutorial_raccoon_description.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.view.activity.OnPrivateKeyStorePageChangeListener
import wacode.yamada.yuki.nempaymentapp.view.activity.PrivateKeyStoreSupportDisplayType

class TutorialRaccoonDescriptionFragment : BaseFragment() {
    private var listenerPrivateKeyStore: OnPrivateKeyStorePageChangeListener? = null
    override fun layoutRes() = R.layout.fragment_tutorial_raccoon_description

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButton()
    }

    private fun setupButton() {
        button.setOnClickListener {
            listenerPrivateKeyStore?.onClickNextButton(PrivateKeyStoreSupportDisplayType.RACCOON_DESCRIPTION)
        }
    }

    companion object {
        fun newInstance(listenerPrivateKeyStore: OnPrivateKeyStorePageChangeListener): TutorialRaccoonDescriptionFragment {
            val fragment = TutorialRaccoonDescriptionFragment()
            fragment.listenerPrivateKeyStore = listenerPrivateKeyStore
            val bundle = Bundle()
            bundle.putInt(ARG_CONTENTS_NAME_ID, R.string.tutorial_login_description_fragment_title)
            fragment.arguments = bundle
            return fragment
        }
    }
}