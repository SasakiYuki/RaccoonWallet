package wacode.yamada.yuki.nempaymentapp.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_address_list.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.rest.item.FriendInfoItem
import wacode.yamada.yuki.nempaymentapp.view.controller.AddressBookRowController


class AddressListActivity : BaseActivity() {
    private lateinit var controller: AddressBookRowController
    private val friendInfoList = ArrayList<FriendInfoItem>()

    override fun setLayout() = R.layout.activity_address_list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        addressRecyclerView.layoutManager = LinearLayoutManager(this)
        controller = AddressBookRowController()
        addressRecyclerView.adapter = controller.adapter
    }

    companion object {
        fun createIntent(context: Context): Intent {
            val intent = Intent(context, AddressListActivity::class.java)
            return intent
        }
    }
}