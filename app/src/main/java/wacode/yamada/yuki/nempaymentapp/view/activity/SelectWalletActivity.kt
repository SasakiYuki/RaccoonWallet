package wacode.yamada.yuki.nempaymentapp.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_select_wallet.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import wacode.yamada.yuki.nempaymentapp.NemPaymentApplication
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.event.WalletBackBarEvent
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
    private var intentAnimation = IntentAnimation.SLIDE_IN

    override fun setLayout() = R.layout.activity_select_wallet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        controller = WalletListController(this)

        setToolbarTitle(R.string.select_wallet_activity_title)
        setupRecyclerView()
        getWalletList()
        setupRxBus()
    }

    private fun setupRxBus() {
        compositeDisposable.add(
                RxBus.receive(WalletBackBarEvent::class.java)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            intentAnimation = IntentAnimation.SLIDE_IN
                        }
        )
    }

    override fun onResume() {
        super.onResume()
        when (intentAnimation) {
            IntentAnimation.SLIDE_IN ->
                overridePendingTransition(R.anim.anim_slide_in_down, R.anim.anim_slide_out_down)
            IntentAnimation.SLIDE_RIGHT ->
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right)
        }
        getWalletList()
    }

    override fun onPause() {
        super.onPause()
        when (intentAnimation) {
            IntentAnimation.SLIDE_IN ->
                overridePendingTransition(R.anim.anim_slide_in_up, R.anim.anim_slide_out_up)
            IntentAnimation.SLIDE_RIGHT ->
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    private fun getWalletList() {
        showProgress()
        async(UI) {
            val wallets = bg { NemPaymentApplication.database.walletDao().findAll() }
                    .await()
            fetchWalletInfo(wallets)
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
        async(UI) {
            val newWallet = Wallet(
                    id = wallet.id,
                    salt = wallet.salt,
                    encryptedSecretKey = wallet.encryptedSecretKey,
                    publicKey = wallet.publicKey,
                    address = wallet.address,
                    name = wallet.name,
                    isMultisig = true
            )
            bg {
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
        async(UI) {
            val wallets = bg { NemPaymentApplication.database.walletDao().findAll() }.await()
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
        intentAnimation = IntentAnimation.SLIDE_IN
        startActivity(ChooseCreateOrScanWalletActivity.createIntent(this, false))
    }

    override fun onClickSettingButton(id: Long, isMultisig: Boolean) {
        onClickRow(id)
        intentAnimation = IntentAnimation.SLIDE_RIGHT
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

    enum class IntentAnimation {
        SLIDE_IN,
        SLIDE_RIGHT
    }
}
