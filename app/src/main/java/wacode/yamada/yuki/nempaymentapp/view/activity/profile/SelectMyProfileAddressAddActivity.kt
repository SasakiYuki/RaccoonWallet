package wacode.yamada.yuki.nempaymentapp.view.activity.profile

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import dagger.android.AndroidInjection
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_select_my_profile_address_add.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.di.ViewModelFactory
import wacode.yamada.yuki.nempaymentapp.room.wallet.Wallet
import wacode.yamada.yuki.nempaymentapp.view.activity.BaseActivity
import wacode.yamada.yuki.nempaymentapp.view.controller.WalletAddEntity
import wacode.yamada.yuki.nempaymentapp.view.controller.WalletAddListController
import wacode.yamada.yuki.nempaymentapp.viewmodel.SelectMyProfileAddressAddViewModel
import javax.inject.Inject

class SelectMyProfileAddressAddActivity : BaseActivity() {
    override fun setLayout() = R.layout.activity_select_my_profile_address_add

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: SelectMyProfileAddressAddViewModel
    private lateinit var controller: WalletAddListController

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setupToolbar()
        setupViewModel()
        setupViews()
    }

    private fun setupToolbar() {
        setToolbarTitle(R.string.select_my_profile_address_add_activity_title)
        setToolBarBackButton()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SelectMyProfileAddressAddViewModel::class.java)
        viewModel.apply {
            walletListLiveData.observe(this@SelectMyProfileAddressAddActivity, Observer {
                it ?: return@Observer
                controller.setData(convertWalletToWalletAddEntity(it))
                findAllMyAddress()
            })
            myAddressLiveData.observe(this@SelectMyProfileAddressAddActivity, Observer {
                it ?: return@Observer
                select(it.id)
            })
            walletInfoLiveData.observe(this@SelectMyProfileAddressAddActivity, Observer {
                it ?: return@Observer
                filterWallet(it)
                controller.setData(walletAddList)
            })
            findAllWallet()
        }
    }

    private fun convertWalletToWalletAddEntity(list: List<Wallet>): ArrayList<WalletAddEntity> {
        return Observable.fromIterable(list)
                .map {
                    WalletAddEntity(it.name, it.address, true)
                }
                .toList()
                .blockingGet() as ArrayList<WalletAddEntity>
    }

    private fun setupViews() {
        recycler.layoutManager = LinearLayoutManager(this)
        controller = WalletAddListController(object : WalletAddListController.WalletAddListClickListener {
            override fun onClickRow() {

            }
        })
        recycler.adapter = controller.adapter
    }

    companion object {
        const val REQUEST_CODE = 208
        fun createIntent(context: Context): Intent {
            val intent = Intent(context, SelectMyProfileAddressAddActivity::class.java)
            return intent
        }
    }
}
