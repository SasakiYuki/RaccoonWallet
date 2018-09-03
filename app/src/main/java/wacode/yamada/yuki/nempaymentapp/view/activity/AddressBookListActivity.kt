package wacode.yamada.yuki.nempaymentapp.view.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_address_book_list.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.di.ViewModelFactory
import wacode.yamada.yuki.nempaymentapp.rest.item.FriendInfoItem
import wacode.yamada.yuki.nempaymentapp.view.controller.AddressBookListController
import wacode.yamada.yuki.nempaymentapp.viewmodel.AddressBookListViewModel
import javax.inject.Inject


class AddressBookListActivity : BaseActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: AddressBookListViewModel
    private lateinit var controller: AddressBookListController
    private val friendInfoList = ArrayList<FriendInfoItem>()

    override fun setLayout() = R.layout.activity_address_book_list

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AddressBookListViewModel::class.java)
        setupViewModelObserve()

        setupViews()
    }

    private fun setupViews() {
        addressRecyclerView.layoutManager = LinearLayoutManager(this)
        controller = AddressBookListController()
        addressRecyclerView.adapter = controller.adapter
    }

    private fun setupViewModelObserve() {
        viewModel.run {
            loadingStatus.observe(this@AddressBookListActivity, Observer {
                it ?: return@Observer

                if (it) showProgress() else hideProgress()
            })
        }
    }

    companion object {
        fun createIntent(context: Context): Intent {
            val intent = Intent(context, AddressBookListActivity::class.java)
            return intent
        }
    }
}
