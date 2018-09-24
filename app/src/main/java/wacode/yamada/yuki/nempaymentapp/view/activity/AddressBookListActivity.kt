package wacode.yamada.yuki.nempaymentapp.view.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PopupMenu
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_address_book_list.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.di.ViewModelFactory
import wacode.yamada.yuki.nempaymentapp.extentions.getColorFromResource
import wacode.yamada.yuki.nempaymentapp.extentions.showToast
import wacode.yamada.yuki.nempaymentapp.rest.item.FriendInfoItem
import wacode.yamada.yuki.nempaymentapp.room.address_book.FriendInfoSortType
import wacode.yamada.yuki.nempaymentapp.view.controller.AddressBookListController
import wacode.yamada.yuki.nempaymentapp.view.custom_view.BackLayerSearchView
import wacode.yamada.yuki.nempaymentapp.view.custom_view.RaccoonDoubleMaterialButton
import wacode.yamada.yuki.nempaymentapp.viewmodel.AddressBookListViewModel
import javax.inject.Inject


class AddressBookListActivity : BaseActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: AddressBookListViewModel
    private lateinit var controller: AddressBookListController
    private val friendInfoList = ArrayList<FriendInfoItem>()
    private var sortType: FriendInfoSortType = FriendInfoSortType.NAME

    override fun setLayout() = R.layout.activity_address_book_list

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AddressBookListViewModel::class.java)
        setupViewModelObserve()

        setupAddressBookList()
        setupBackLayerSearchView()
        setupSortMenu()
        setupMaterialButton()
    }


    override fun onResume() {
        super.onResume()
        friendInfoList.clear()
        viewModel.findFriendInfo()
    }

    private fun setupAddressBookList() {
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
    }

    private fun setupBackLayerSearchView() {
        backLayerSearchView.setOnItemClickListener(object : BackLayerSearchView.OnItemStateChangeListener {
            override fun onItemChanged(word: String, type: BackLayerSearchView.SearchType) {
                friendInfoList.clear()

                addressRecyclerView.visibility = View.GONE
                searchEmptyMessage.visibility = View.VISIBLE

                viewModel.findFriendInfo(word, type, sortType)
            }

            override fun onFinishClick() {
                finish()
            }
        })
    }

    private fun setupSortMenu() {
        sortIcon.setOnClickListener {
            val popupMenu = PopupMenu(this, sortIcon)
            popupMenu.menuInflater.inflate(R.menu.menue_address_book_sort, popupMenu.menu)

            val menu = when (sortType) {
                FriendInfoSortType.WELL_SEND -> popupMenu.menu.getItem(1)
                else -> popupMenu.menu.getItem(0)
            }

            val spannableString = SpannableString(menu.title)
            spannableString.setSpan(ForegroundColorSpan(getColorFromResource(R.color.nemGreen)), 0, spannableString.length, 0)
            menu.title = spannableString

            popupMenu.show()

            popupMenu.setOnMenuItemClickListener {
                sortType = when (it.itemId) {
                    R.id.wll_send -> FriendInfoSortType.WELL_SEND
                    else -> FriendInfoSortType.NAME
                }
                true
            }
        }

    }

    private fun setupViewModelObserve() {
        viewModel.run {
            friendInfoLiveData.observe(this@AddressBookListActivity, Observer {
                it ?: return@Observer

                addressRecyclerView.visibility = View.VISIBLE
                searchEmptyMessage.visibility = View.GONE

                friendInfoList.add(it)
                controller.setData(friendInfoList)
            })
        }
    }

    private fun setupMaterialButton() {
        materialButton.setOnClickListener(object : RaccoonDoubleMaterialButton.OnDoubleButtonClickListener {
            override fun onLeftClick() {
                showToast("Coming Soon")
            }

            override fun onRightClick() {
                startActivity(CreateAddressBookActivity.createIntent(this@AddressBookListActivity))
            }
        })
    }

    companion object {
        fun createIntent(context: Context): Intent {
            val intent = Intent(context, AddressBookListActivity::class.java)
            return intent
        }
    }
}
