package wacode.yamada.yuki.nempaymentapp.view.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ArrayAdapter
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_address_book_list.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.di.ViewModelFactory
import wacode.yamada.yuki.nempaymentapp.rest.item.FriendInfoItem
import wacode.yamada.yuki.nempaymentapp.view.controller.AddressBookListController
import wacode.yamada.yuki.nempaymentapp.view.custom_view.BackLayerSearchView
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
        viewModel.getAllFriendInfo()
    }

    private fun setupViews() {
        val dividerItemDecoration = DividerItemDecoration(addressRecyclerView.context, LinearLayoutManager(this).orientation)
        controller = AddressBookListController(object : AddressBookListController.OnAddressBookClickListener {
            override fun onClickItem(friendId: Long) {
                startActivity(AddressBookActivity.createIntent(this@AddressBookListActivity, friendId))
            }
        })

        addressRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@AddressBookListActivity)
            addItemDecoration(dividerItemDecoration)
            adapter = controller.adapter
        }

        backLayerSearchView.setOnItemClickListener(object : BackLayerSearchView.OnItemClickListener {
            override fun onItemClicked(word: String, type: BackLayerSearchView.SearchType) {
                friendInfoList.clear()

                addressRecyclerView.visibility = View.GONE
                searchEmptyMessage.visibility = View.VISIBLE

                viewModel.findPatterMatchFriendInfoByNameAndType(word, type)
            }

            override fun onFinishClick() {
                finish()
            }
        })

        val adapter = ArrayAdapter.createFromResource(this, R.array.address_book_sort_array, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sortSpinner.adapter = adapter
    }

    private fun setupViewModelObserve() {
        viewModel.run {
            loadingStatus.observe(this@AddressBookListActivity, Observer {
                it ?: return@Observer

                if (it) showProgress() else hideProgress()
            })

                friendInfoLiveData.observe(this@AddressBookListActivity, Observer {
                it ?: return@Observer

                addressRecyclerView.visibility = View.VISIBLE
                searchEmptyMessage.visibility = View.GONE

                friendInfoList.add(it)
                controller.setData(friendInfoList)
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
