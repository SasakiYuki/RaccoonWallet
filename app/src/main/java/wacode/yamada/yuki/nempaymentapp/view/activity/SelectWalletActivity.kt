package wacode.yamada.yuki.nempaymentapp.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_select_wallet.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import wacode.yamada.yuki.nempaymentapp.NemPaymentApplication
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.model.SimpleWalletEntity
import wacode.yamada.yuki.nempaymentapp.room.wallet.Wallet
import wacode.yamada.yuki.nempaymentapp.utils.NemCommons
import wacode.yamada.yuki.nempaymentapp.utils.RxBus
import wacode.yamada.yuki.nempaymentapp.utils.RxBusEvent
import wacode.yamada.yuki.nempaymentapp.utils.WalletManager
import wacode.yamada.yuki.nempaymentapp.view.controller.WalletListController

class SelectWalletActivity : BaseActivity(), WalletListController.OnWalletListClickListener {
    private lateinit var controller: WalletListController
    private val compositeDisposable = CompositeDisposable()

    override fun setLayout() = R.layout.activity_select_wallet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        controller = WalletListController(this)

        setToolbarTitle(R.string.select_wallet_activity_title)
        setupRecyclerView()
        getWalletList()
    }

    override fun onResume() {
        super.onResume()
        overridePendingTransition(R.anim.anim_slide_in_down, R.anim.anim_slide_out_down)
        getWalletList()
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(R.anim.anim_slide_in_up, R.anim.anim_slide_out_up)
    }

    private fun getWalletList() {
        showProgress()
        CoroutineScope(Dispatchers.Main).launch {
            async(Dispatchers.IO) {
                val wallets = NemPaymentApplication.database.walletDao().findAll()
                fetchWalletInfo(wallets)
            }.await()
        }
    }

    private fun fetchWalletInfo(wallets: List<Wallet>) {
        if (wallets.isEmpty()) {
            renderList()
            return
        }

        for (wallet in wallets) {
            if (!wallet.isMultisig) {
                compositeDisposable.addAll(NemCommons.getAccountInfo(wallet.address)
                        .subscribe({
                            if (it.meta.cosignatories.isNotEmpty()) {
                                updateWallet(wallet)
                            } else {
                                renderList()
                            }
                        }, { e ->
                            e.printStackTrace()
                            hideProgress()
                            renderList()
                        }))
            } else {
                renderList()
            }
        }
    }

    private fun updateWallet(wallet: Wallet) {
        CoroutineScope(Dispatchers.Main).launch {
            val newWallet = Wallet(
                    id = wallet.id,
                    salt = wallet.salt,
                    encryptedSecretKey = wallet.encryptedSecretKey,
                    publicKey = wallet.publicKey,
                    address = wallet.address,
                    name = wallet.name,
                    isMultisig = true
            )
            async(Dispatchers.IO) {
                NemPaymentApplication.database.walletDao().update(newWallet)
            }.await()
            renderList()
        }
    }

    private fun setupRecyclerView() {
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = controller.adapter
    }

    private fun renderList() {
        CoroutineScope(Dispatchers.Main).launch {
            val wallets = async(Dispatchers.IO) {
                return@async NemPaymentApplication.database.walletDao().findAll()
            }.await()
            val selectedWalletId = WalletManager.getSelectedWalletId(this@SelectWalletActivity.applicationContext)

            val array = Observable.fromIterable(wallets)
                    .map { it ->
                        if (it.id == selectedWalletId) {
                            SimpleWalletEntity(id = it.id, walletName = it.name, isSelected = true, isMultisig = it.isMultisig)
                        } else {
                            SimpleWalletEntity(id = it.id, walletName = it.name, isSelected = false, isMultisig = it.isMultisig)
                        }
                    }
                    .toList()
                    .blockingGet() as ArrayList<SimpleWalletEntity>

            controller.setData(array)
            hideProgress()
        }
    }

    override fun onClickCreateButton() {
        startActivity(ChooseCreateOrScanWalletActivity.createIntent(this, false))
    }

    override fun onClickSettingButton(id: Long, isMultisig: Boolean) {
        onClickRow(id)
        startActivity(WalletSettingActivity.getCallingIntent(this@SelectWalletActivity.baseContext, id, isMultisig))
    }

    override fun onClickRow(id: Long) {
        WalletManager.saveSelectWallet(applicationContext, id)
        renderList()
        RxBus.send(RxBusEvent.SELECT)
    }

    companion object {
        fun createIntent(context: Context) = Intent(context, SelectWalletActivity::class.java)
    }
}
