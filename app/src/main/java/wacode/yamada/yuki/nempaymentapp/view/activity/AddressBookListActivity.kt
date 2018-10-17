package wacode.yamada.yuki.nempaymentapp.view.activity

import android.app.Activity
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
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_address_book_list.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.di.ViewModelFactory
import wacode.yamada.yuki.nempaymentapp.extentions.getColorFromResource
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
    private var friendInfoList = ArrayList<FriendInfoItem>()
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
        setupButtons()
    }


    override fun onResume() {
        super.onResume()
        friendInfoList.clear()
        viewModel.findFriendInfo()
    }

    override fun onBackPressed() {
        if (deleteMaterialButton.visibility == View.VISIBLE) {
            val list = createDeleteMode(false)
            controller.setData(list)

            deleteMaterialButton.visibility = View.GONE
            defaultMaterialButton.visibility = View.VISIBLE
        } else {
            super.onBackPressed()
        }
    }

    private fun setupAddressBookList() {
        val dividerItemDecoration = DividerItemDecoration(addressRecyclerView.context, LinearLayoutManager(this).orientation)
        controller = AddressBookListController(object : AddressBookListController.OnAddressBookClickListener {
            override fun onClickItem(friendId: Long) {
                startActivityForResult(AddressBookActivity.createIntent(this@AddressBookListActivity, friendId), AddressBookActivity.REQUEST_CODE_ADDRESS_BOOK)
            }

            override fun onItemChecked(friendId: Long, isChecked: Boolean) {
                friendInfoList = Observable.fromIterable(friendInfoList)
                        .map {
                            return@map if (it.friendInfo.id == friendId) {
                                FriendInfoItem(friendInfo = it.friendInfo, isChecked = isChecked, deleteMode = it.deleteMode)
                            } else {
                                it
                            }
                        }
                        .toList()
                        .blockingGet() as ArrayList<FriendInfoItem>
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
                deleteMaterialButton.visibility = View.GONE
                defaultMaterialButton.visibility = View.VISIBLE

                friendInfoList.add(it)
                controller.setData(friendInfoList)
            })
        }
    }

    private fun setupButtons() {
        defaultMaterialButton.setOnClickListener(object : RaccoonDoubleMaterialButton.OnDoubleButtonClickListener {
            override fun onLeftClick() {
                friendInfoList = createDeleteMode(true)

                controller.setData(friendInfoList)

                deleteMaterialButton.visibility = View.VISIBLE
                defaultMaterialButton.visibility = View.GONE
            }

            override fun onRightClick() {
                startActivity(CreateAddressBookActivity.createIntent(this@AddressBookListActivity))
            }
        })

        deleteMaterialButton.setClickListener(View.OnClickListener {
            if (friendInfoList.isNotEmpty()) {
                viewModel.removeAndGetAllFriendInfo(friendInfoList)
                friendInfoList.clear()
            }
        })
    }

    private fun createDeleteMode(deleteMode: Boolean): ArrayList<FriendInfoItem> {
        return Observable.fromIterable(friendInfoList)
                .map { FriendInfoItem(friendInfo = it.friendInfo, deleteMode = deleteMode) }
                .toList()
                .blockingGet() as ArrayList<FriendInfoItem>
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                AddressBookActivity.REQUEST_CODE_ADDRESS_BOOK ->
                        finish()
            }
        }
    }

    companion object {
        fun createIntent(context: Context): Intent {
            val intent = Intent(context, AddressBookListActivity::class.java)
            return intent
        }
    }
}
