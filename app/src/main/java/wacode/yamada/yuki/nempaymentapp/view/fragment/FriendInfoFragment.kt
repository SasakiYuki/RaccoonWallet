package wacode.yamada.yuki.nempaymentapp.view.fragment

import android.os.Bundle
import kotlinx.android.synthetic.main.fragment_create_friend_address.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.extentions.getColor
import wacode.yamada.yuki.nempaymentapp.room.address_book.FriendInfo


class FriendInfoFragment : BaseFragment() {
    override fun layoutRes() = R.layout.fragment_create_friend_address

    fun setupFriendInfo(friendInfo: FriendInfo) {
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
        fun newInstance(): FriendInfoFragment {
            val fragment = FriendInfoFragment()
            val args = Bundle()
            args.putInt(ARG_CONTENTS_NAME_ID, R.string.address_book_friend_info_title)
            fragment.arguments = args
            return fragment
        }
    }
}