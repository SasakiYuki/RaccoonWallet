package wacode.yamada.yuki.nempaymentapp.view.activity.profile

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_profile_address_add.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.di.ViewModelFactory
import wacode.yamada.yuki.nempaymentapp.extentions.getColorFromResource
import wacode.yamada.yuki.nempaymentapp.view.activity.BaseActivity
import wacode.yamada.yuki.nempaymentapp.viewmodel.ProfileAddressAddViewModel
import javax.inject.Inject

class ProfileAddressAddActivity : BaseActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: ProfileAddressAddViewModel
    override fun setLayout() = R.layout.activity_profile_address_add
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setupViews()
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
        }
    }
    private fun getWhite() = getColorFromResource(android.R.color.white)
    private fun getOrange() = getColorFromResource(R.color.nemOrange)
    private fun createWalletInfoFromEditText() {
        val walletName = nameEditText.text.toString()
        val address = addressEditText.text.toString()
    }
    companion object {
        fun createIntent(context: Context): Intent {
            val intent = Intent(context, ProfileAddressAddActivity::class.java)
            return intent
        }
    }
}