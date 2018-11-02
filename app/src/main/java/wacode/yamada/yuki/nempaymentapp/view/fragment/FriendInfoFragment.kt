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
                if (activity is OnFriendDataChangeCallback) {
                    (activity as OnFriendDataChangeCallback).onFriendInfoChanged(it)
                }
            })
        }
    }

    private fun setupViews(friendInfo: FriendInfo) {
        nameEditText.apply {
            setText(friendInfo.name)
            isEnabled = false
            setTextColor(getColor(context!!, R.color.textBlack))
        }

        nameRubyEdiText.apply {
            setText(friendInfo.nameRuby)
            isEnabled = false
            setTextColor(getColor(context!!, R.color.textBlack))
        }

        phoneNumberEditText.apply {
            setText(friendInfo.phoneNumber)
            isEnabled = false
            setTextColor(getColor(context!!, R.color.textBlack))
        }

        mailAddressEditText.apply {
            setText(friendInfo.mailAddress)
            isEnabled = false
            setTextColor(getColor(context!!, R.color.textBlack))
        }
    }

    fun changeEditFriendInfo() {
        nameEditText.isEnabled = true
        nameRubyEdiText.isEnabled = true
        phoneNumberEditText.isEnabled = true
        mailAddressEditText.isEnabled = true
    }

    fun changeDisplayFriendInfo() {
        nameEditText.isEnabled = false
        nameRubyEdiText.isEnabled = false
        phoneNumberEditText.isEnabled = false
        mailAddressEditText.isEnabled = false
    }

    fun getAndCheckFriendInfo(): FriendInfo? {
        return if (validateAllInfo()) {
            createPrams()
        } else {
            null
        }
    }

    private fun validateAllInfo(): Boolean {
        return when {
            nameEditText.text.isNullOrEmpty() -> {
                nameInputLayout.error = getString(R.string.create_friend_address_input_error)
                false
            }
            else -> true
        }
    }

    private fun createPrams(): FriendInfo {
        return FriendInfo(
                id = friendId!!,
                name = nameEditText.text.toString(),
                nameRuby = nameRubyEdiText.text.toString(),
                phoneNumber = phoneNumberEditText.text.toString(),
                mailAddress = mailAddressEditText.text.toString()
        )
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