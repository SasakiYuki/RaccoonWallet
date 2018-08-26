package wacode.yamada.yuki.nempaymentapp.view.fragment

import android.os.Bundle
import kotlinx.android.synthetic.main.fragment_friend_info.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.room.address_book.FriendInfo

class CreateFriendInfoFragment : BaseFragment() {
    override fun layoutRes() = R.layout.fragment_friend_info

    fun getAndCheckFriendInfo(): FriendInfo? {
        return if (checkAllValidation()) {
            createPrams()
        } else {
            null
        }
    }

    private fun checkAllValidation(): Boolean {
        return when {
            lastNameEditText.text.isNullOrEmpty() -> {
                lastNameInputLayout.error = getString(R.string.create_friend_address_input_error)
                false
            }
            firstNameEditText.text.isNullOrEmpty() -> {
                firstNameInputLayout.error = getString(R.string.create_friend_address_input_error)
                false
            }
            else -> true
        }
    }

    private fun createPrams(): FriendInfo {
        return FriendInfo(
                id = Math.random().toLong(),
                lastName = lastNameEditText.text.toString(),
                lastNameKana = lastNameKanaEditText.text.toString(),
                firstName = firstNameEditText.text.toString(),
                firstNameKana = firstNameKanaEditText.text.toString(),
                phoneNumber = phoneNumberEditText.text.toString(),
                mailAddress = mailAddressEditText.text.toString()
        )
    }

    companion object {
        const val PAGE_POSITION = 1

        fun newInstance(): CreateFriendInfoFragment {
            val fragment = CreateFriendInfoFragment()
            val args = Bundle()
            args.putInt(ARG_CONTENTS_NAME_ID, R.string.create_friend_address_title)
            fragment.arguments = args
            return fragment
        }
    }
}