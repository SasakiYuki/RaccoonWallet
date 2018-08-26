package wacode.yamada.yuki.nempaymentapp.view.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.view.View
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_friend_info.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.di.ViewModelFactory
import wacode.yamada.yuki.nempaymentapp.extentions.getColor
import wacode.yamada.yuki.nempaymentapp.room.address_book.FriendInfo
import wacode.yamada.yuki.nempaymentapp.view.activity.OnFriendDataChangeCallback
import wacode.yamada.yuki.nempaymentapp.viewmodel.FriendInfoViewModel
import javax.inject.Inject


class FriendInfoFragment : BaseFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var friendInfoViewModel: FriendInfoViewModel

    private val friendId by lazy {
        arguments?.getLong(PARAMS_FRIEND_ID)
    }

    override fun layoutRes() = R.layout.fragment_friend_info

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)

        friendInfoViewModel = ViewModelProviders.of(this, viewModelFactory).get(FriendInfoViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        friendInfoViewModel.getFriendInfo(friendId!!)
        setupViewModelObserve()
    }

    private fun setupViewModelObserve() {
        friendInfoViewModel.run {
            loadingStatus.observe(this@FriendInfoFragment, Observer {
                it ?: return@Observer
                if (it) showProgress() else hideProgress()
            })

            friendInfoLiveData.observe(this@FriendInfoFragment, Observer {
                it ?: return@Observer
                setupViews(it)
                (activity as OnFriendDataChangeCallback).onFriendInfoChanged(it)
            })
        }
    }

    private fun setupViews(friendInfo: FriendInfo) {
        lastNameEditText.setText(friendInfo.lastName)
        lastNameEditText.isEnabled = false
        lastNameEditText.setTextColor(getColor(context!!, R.color.textBlack))

        lastNameKanaEditText.setText(friendInfo.lastNameKana)
        lastNameKanaEditText.isEnabled = false
        lastNameKanaEditText.setTextColor(getColor(context!!, R.color.textBlack))

        firstNameEditText.setText(friendInfo.firstName)
        firstNameEditText.isEnabled = false
        firstNameEditText.setTextColor(getColor(context!!, R.color.textBlack))

        firstNameKanaEditText.setText(friendInfo.firstNameKana)
        firstNameKanaEditText.isEnabled = false
        firstNameKanaEditText.setTextColor(getColor(context!!, R.color.textBlack))

        phoneNumberEditText.setText(friendInfo.phoneNumber)
        phoneNumberEditText.isEnabled = false
        phoneNumberEditText.setTextColor(getColor(context!!, R.color.textBlack))

        mailAddressEditText.setText(friendInfo.mailAddress)
        mailAddressEditText.isEnabled = false
        mailAddressEditText.setTextColor(getColor(context!!, R.color.textBlack))
    }

    companion object {
        const val PAGE_POSITION = 1
        private const val PARAMS_FRIEND_ID = "params_friend_id"

        fun newInstance(friendId: Long): FriendInfoFragment {
            val fragment = FriendInfoFragment()
            val args = Bundle()
            args.putInt(ARG_CONTENTS_NAME_ID, R.string.address_book_friend_info_title)
            args.putLong(PARAMS_FRIEND_ID, friendId)
            fragment.arguments = args
            return fragment
        }
    }
}