package wacode.yamada.yuki.nempaymentapp.view.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.fragment_friend_wallet.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfo
import wacode.yamada.yuki.nempaymentapp.view.controller.WalletInfoClickListener
import wacode.yamada.yuki.nempaymentapp.view.controller.WalletInfoListController

class CreateFriendWalletFragment : BaseFragment() {
    var walletList = ArrayList<WalletInfo>()
    private lateinit var controller: WalletInfoListController

    override fun layoutRes() = R.layout.fragment_friend_wallet

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(context)
        controller = WalletInfoListController(object : WalletInfoClickListener {
            override fun onRowClick(walletInfo: WalletInfo) {

            }
        })

        recyclerView.adapter = controller.adapter
    }

    fun onListItemChanged(walletInfo: WalletInfo) {
        walletList.add(walletInfo)
        controller.setData(walletList)

        recyclerView.visibility = View.VISIBLE
        emptyRootView.visibility = View.GONE
    }

    companion object {
        const val PAGE_POSITION = 1

        fun newInstance(): CreateFriendWalletFragment {
            val fragment = CreateFriendWalletFragment()
            val args = Bundle()
            args.putInt(ARG_CONTENTS_NAME_ID, R.string.create_friend_wallet_title)
            fragment.arguments = args
            return fragment
        }
    }
}