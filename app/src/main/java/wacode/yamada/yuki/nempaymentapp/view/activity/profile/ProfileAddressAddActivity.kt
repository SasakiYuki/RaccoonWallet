package wacode.yamada.yuki.nempaymentapp.view.activity.profile

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_profile_address_add.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.di.ViewModelFactory
import wacode.yamada.yuki.nempaymentapp.extentions.getColorFromResource
import wacode.yamada.yuki.nempaymentapp.extentions.remove
import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfo
import wacode.yamada.yuki.nempaymentapp.view.activity.BaseActivity
import wacode.yamada.yuki.nempaymentapp.viewmodel.ProfileAddressAddViewModel
import javax.inject.Inject

class ProfileAddressAddActivity : BaseActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: ProfileAddressAddViewModel

    private val type by lazy {
        intent.getSerializableExtra(INTENT_TYPE) as ProfileAddressAddType
    }

    override fun setLayout() = R.layout.activity_profile_address_add
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setupViewModel()
        setupViews()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ProfileAddressAddViewModel::class.java)
        viewModel.createLiveData.observe(this, Observer {
            it ?: return@Observer
            finishWithWalletInfo(it)
        })
        viewModel.errorLiveData.observe(this, Observer {
            it ?: return@Observer
            it.printStackTrace()
        })
    }

    private fun setupViews() {
        setToolBarBackButton()
        setTitle(R.string.profile_address_add_activity_title)
        materialButton.setOnClickListener {
            viewModel.isMaster = !viewModel.isMaster
            materialButton.apply {
                if (viewModel.isMaster) {
                    setTextColor(getWhite())
                    backgroundTintList = ColorStateList.valueOf(getOrange())
                    iconTint = ColorStateList.valueOf(getWhite())
                    rippleColor = ColorStateList.valueOf(getWhite())
                    strokeColor = ColorStateList.valueOf(getWhite())
                } else {
                    setTextColor(getOrange())
                    backgroundTintList = ColorStateList.valueOf(getWhite())
                    iconTint = ColorStateList.valueOf(getOrange())
                    rippleColor = ColorStateList.valueOf(getOrange())
                    strokeColor = ColorStateList.valueOf(getOrange())
                }
            }
        }
        createButton.setOnClickListener {
            createWalletInfoFromEditText()
        }
    }

    private fun getWhite() = getColorFromResource(android.R.color.white)

    private fun getOrange() = getColorFromResource(R.color.nemOrange)

    private fun createWalletInfoFromEditText() {
        val walletName = nameEditText.text.toString().remove("-")
        val address = addressEditText.text.toString()
        val isMaster = viewModel.isMaster
        WalletInfo(
                walletName = walletName,
                walletAddress = address,
                isMaster = isMaster
        ).let {
            when (type) {
                ProfileAddressAddType.MyProfile -> {
                    viewModel.create(it)
                }
                ProfileAddressAddType.FriendWallet -> {
                    viewModel.create(it)
                }
                else -> finishWithWalletInfo(it)
            }
        }
    }

    private fun finishWithWalletInfo(walletInfo: WalletInfo) {
        Intent().apply {
            intent.putExtra(INTENT_WALLET_INFO, walletInfo)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    companion object {
        private const val INTENT_TYPE = "intent_type"
        const val INTENT_WALLET_INFO = "intent_wallet_info"
        const val REQUEST_CODE = 1010
        fun createIntent(context: Context, type: ProfileAddressAddType = ProfileAddressAddType.MyProfile): Intent {
            return Intent(context, ProfileAddressAddActivity::class.java).apply {
                putExtra(INTENT_TYPE, type)
            }
        }
    }

    enum class ProfileAddressAddType {
        MyProfile,
        FriendWallet,
        Other
    }
}

