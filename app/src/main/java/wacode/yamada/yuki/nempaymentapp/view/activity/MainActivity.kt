package wacode.yamada.yuki.nempaymentapp.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.deeplinkdispatch.DeepLink
import com.google.gson.Gson
import com.journeyapps.barcodescanner.BarcodeResult
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.model.DrawerEntity
import wacode.yamada.yuki.nempaymentapp.model.DrawerItemType
import wacode.yamada.yuki.nempaymentapp.model.MyProfileEntity
import wacode.yamada.yuki.nempaymentapp.model.PaymentQREntity
import wacode.yamada.yuki.nempaymentapp.repository.MyProfileRepository.Companion.KEY_PREF_MY_PROFILE
import wacode.yamada.yuki.nempaymentapp.types.MainBottomNavigationType
import wacode.yamada.yuki.nempaymentapp.utils.*
import wacode.yamada.yuki.nempaymentapp.view.activity.callback.QrScanCallback
import wacode.yamada.yuki.nempaymentapp.view.activity.callback.SplashCallback
import wacode.yamada.yuki.nempaymentapp.view.activity.drawer.AboutActivity
import wacode.yamada.yuki.nempaymentapp.view.activity.drawer.MosaicListActivity
import wacode.yamada.yuki.nempaymentapp.view.activity.drawer.RaccoonDonateActivity
import wacode.yamada.yuki.nempaymentapp.view.activity.profile.MyAddressProfileActivity
import wacode.yamada.yuki.nempaymentapp.view.adapter.TopFragmentPagerAdapter
import wacode.yamada.yuki.nempaymentapp.view.controller.DrawerListController
import wacode.yamada.yuki.nempaymentapp.view.dialog.RaccoonConfirmViewModel
import wacode.yamada.yuki.nempaymentapp.view.fragment.SplashFragment
import wacode.yamada.yuki.nempaymentapp.view.fragment.top.SendTopFragment
import javax.inject.Inject


@DeepLink("https://raccoonwallet.com/payment?amount={amount}&addr={addr}&msg={msg}&name={name}")
class MainActivity : BaseActivity(), SplashCallback, QrScanCallback, DrawerListController.OnDrawerClickListener, HasSupportFragmentInjector {
    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>
    private val compositeDisposable = CompositeDisposable()
    private lateinit var controller: DrawerListController

    private val shouldShowSplash by lazy {
        intent.getBooleanExtra(ARG_SHOULD_SHOW_SPLASH, true)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentDispatchingAndroidInjector

    override fun setLayout() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        showSplash()
        setupRxBus()
    }

    private fun parseIntent() {
        val paymentQrEntity = PaymentQREntity.convert(intent)
        paymentQrEntity?.let {
            changeSendTopFragment(it)
        }
    }

    private fun setupNemIcon() {
        nemIcon.setOnClickListener({
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) drawerLayout.closeDrawers()
            else drawerLayout.openDrawer(GravityCompat.START)
        })
    }

    private fun setupRxBus() {
        RxBus
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { it ->
                    when (it) {
                        RxBusEvent.SELECT, RxBusEvent.REMOVE -> {
                            setupNavigationRecyclerView()
                        }
                    }
                }
    }

    private fun setupNavigationRecyclerView() {
        CoroutineScope(Dispatchers.Main).launch {
            val myProfileString = SharedPreferenceUtils[this@MainActivity, KEY_PREF_MY_PROFILE, Gson().toJson(MyProfileEntity())]
            val myProfile = Gson().fromJson(myProfileString, MyProfileEntity::class.java)
            controller = DrawerListController(this@MainActivity,
                    if (myProfile.name.isEmpty()) getString(R.string.my_address_profile_activity_title_initial_guest) else myProfile.name,
                    myProfile.screenPath,
                    myProfile.iconPath)
            navigationRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
            navigationRecyclerView.adapter = controller.adapter
            val drawerIconTypes = DrawerItemType.values()
            val list = ArrayList<DrawerEntity>()
            drawerIconTypes.mapTo(list) { DrawerEntity(ContextCompat.getDrawable(this@MainActivity, it.imageResource)!!, getString(it.titleResource), it.drawerType) }
            controller.setData(list)
        }
    }

    override fun onHeaderClick() {
        startActivityForResult(MyAddressProfileActivity.createIntent(this), MyAddressProfileActivity.REQUEST_CODE_MY_ADDRESS_PROFILE_ACTIVITY)
    }

    override fun onRowClick(drawerEntity: DrawerEntity) {
        when (drawerEntity.title) {
            getString(R.string.main_navigation_home) -> closeDrawerAndMoveHome()
            getString(R.string.main_navigation_about) -> startActivity(AboutActivity.createIntent(this))
            getString(R.string.main_navigation_mosaic_gallery) -> startActivityForResult(MosaicListActivity.createIntent(this), MosaicListActivity.RESULT_MOSAIC_LIST)
            getString(R.string.main_navigation_donate) -> startActivity(RaccoonDonateActivity.createIntent(this))
            getString(R.string.main_navigation_help) -> showHelpWeb()
            getString(R.string.main_navigation_setting) -> startActivity(SettingActivity.getCallingIntent(this))
            getString(R.string.main_navigation_address_book) -> startActivity(AddressBookListActivity.createIntent(this))
        }
    }

    private fun closeDrawerAndMoveHome() {
        drawerLayout.closeDrawers()
        viewpager.currentItem = HOME_POSITION
    }

    private fun setupViewPager() {
        val adapter = TopFragmentPagerAdapter(supportFragmentManager)
        viewpager.adapter = adapter
        tabLayout.setupWithViewPager(viewpager)
        viewpager.currentItem = HOME_POSITION
        viewpager.offscreenPageLimit = 5
    }

    private fun setupBottomTabLayout() {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        setupTabAt(inflater, 0)
        setupTabAt(inflater, 1)
        setupTabAt(inflater, 2)
        setupTabAt(inflater, 3)
        setupTabAt(inflater, 4)
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val color = R.color.textGray
                tab?.let {
                    getTabTextView(tab)?.let {
                        setTextColor(it, color)
                    }
                    getTabImageView(tab)?.let {
                        setDrawableTint(it, color)
                    }
                }
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                val color = R.color.nemGreen
                tab?.let {
                    getTabTextView(tab)?.let {
                        setTextColor(it, color)
                    }
                    getTabImageView(tab)?.let {
                        setDrawableTint(it, color)
                    }
                }
            }

        })
    }

    private fun getTabImageView(tab: TabLayout.Tab): ImageView? {
        tab.customView?.let {
            return it.findViewById(R.id.tabLayoutImageView)
        }
        return null
    }

    private fun getTabTextView(tab: TabLayout.Tab): TextView? {
        tab.customView?.let {
            return it.findViewById(R.id.tabLayoutTextView)
        }
        return null
    }

    private fun setupTabAt(layoutInflater: LayoutInflater, position: Int) {
        val tab = tabLayout.getTabAt(position)
        val items = MainBottomNavigationType.values()
        val text = getString(items[position].textResource)

        val tab1View = layoutInflater.inflate(R.layout.view_main_tab, null)
        val textView = tab1View.findViewById<TextView>(R.id.tabLayoutTextView)
        val imageView = tab1View.findViewById<ImageView>(R.id.tabLayoutImageView)
        textView.text = text
        if (position == HOME_POSITION) {
            val color = R.color.nemGreen
            setTextColor(textView, color)
            setDrawableTint(imageView, color)
        }
        imageView.setImageDrawable(ContextCompat.getDrawable(this, items[position].drawableResource))
        tab!!.customView = tab1View
    }

    private fun setDrawableTint(imageView: ImageView, color: Int) {
        imageView.setColorFilter(ContextCompat.getColor(this@MainActivity, color), android.graphics.PorterDuff.Mode.SRC_IN)
    }

    private fun setTextColor(textView: TextView, color: Int) {
        textView.setTextColor(ContextCompat.getColor(this, color))
    }

    private fun showSplash() {
        if (shouldShowSplash) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            val fragment = SplashFragment.newInstance()
            fragmentTransaction.replace(R.id.fragment_container, fragment, fragment::class.java.simpleName)
            fragmentTransaction.commit()
        } else {
            hideSplash()
        }
    }

    override fun hideSplash() {
        if (isAlreadyRaccoonWallet()) {
            saveRegisterDate()
            startActivity(FirstTutorialActivity.createIntent(this))
            finish()
        } else {
            setupNavigationRecyclerView()
            setupViewPager()
            setupBottomTabLayout()
            setupNemIcon()
            if (!ReviewAppealUtils.isAlreadyShownReviewDialog(this) && RegisterDateUtils.isThreeDaysLater(this)) {
                val viewModel = RaccoonConfirmViewModel()
                ReviewAppealUtils.saveAlreadyShownReviewDialog(this)
                ReviewAppealUtils.createReviewDialog(this, supportFragmentManager, viewModel)
            }
        }
        parseIntent()
    }

    private fun showHelpWeb() {
        val url = "https://raccoonwallet.com/help/"
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(url))
    }

    private fun changeSendTopFragment(paymentQREntity: PaymentQREntity) {
        viewpager.currentItem = SendTopFragment.VIEW_PAGER_POSITION
        val fragment = (viewpager.adapter as TopFragmentPagerAdapter).getItem(viewpager.currentItem)
        (fragment as SendTopFragment).putQRScanItems(paymentQREntity)
    }

    private fun changeSendTopFragment(address: String) {
        viewpager.currentItem = SendTopFragment.VIEW_PAGER_POSITION
        val fragment = (viewpager.adapter as TopFragmentPagerAdapter).getItem(viewpager.currentItem)
        (fragment as SendTopFragment).putAddressEditText(address)
    }

    override fun onQrScanResult(result: BarcodeResult?) {
        result?.let {
            if (it.text.contains("addr")) {
                val entity: PaymentQREntity = Gson().fromJson(it.text, PaymentQREntity::class.java)
                changeSendTopFragment(entity)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MosaicListActivity.RESULT_MOSAIC_LIST) {
            if (resultCode == RESULT_OK) {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        } else if (requestCode == MyAddressProfileActivity.REQUEST_CODE_MY_ADDRESS_PROFILE_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                data?.let {
                    onMyAddressProfileResult(it)
                }
            }
        }
    }

    private fun onMyAddressProfileResult(intent: Intent) {
        closeDrawerAndMoveHome()
        val address = intent.getStringExtra(MyAddressProfileActivity.RESULT_PAYMENT_ADDRESS)
        changeSendTopFragment(address)
    }

    override fun onPause() {
        super.onPause()
        compositeDisposable.clear()
    }

    private fun isAlreadyRaccoonWallet() = SharedPreferenceUtils[this, SP_IS_FIRST_RACCOON, true]

    private fun saveRegisterDate() = RegisterDateUtils.saveRegisterDate(this)

    companion object {
        const val SP_IS_FIRST_RACCOON = "sp_is_first_raccoon"
        private const val HOME_POSITION = 2
        private const val ARG_SHOULD_SHOW_SPLASH = "args_show_splash"

        fun createIntent(context: Context) = Intent(context, MainActivity::class.java)

        fun createIntent(context: Context, showSplash: Boolean): Intent {
            val intent = createIntent(context)
            intent.putExtra(ARG_SHOULD_SHOW_SPLASH, showSplash)
            return intent
        }
    }
}
