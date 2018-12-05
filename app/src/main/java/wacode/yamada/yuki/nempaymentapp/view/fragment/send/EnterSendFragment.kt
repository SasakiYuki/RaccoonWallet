package wacode.yamada.yuki.nempaymentapp.view.fragment.send

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_enter_send.*
import kotlinx.android.synthetic.main.view_multi_calculator.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.di.ViewModelFactory
import wacode.yamada.yuki.nempaymentapp.rest.item.MosaicFullItem
import wacode.yamada.yuki.nempaymentapp.rest.item.MosaicItem
import wacode.yamada.yuki.nempaymentapp.rest.item.SendMosaicItem
import wacode.yamada.yuki.nempaymentapp.utils.WalletManager
import wacode.yamada.yuki.nempaymentapp.view.activity.SendType
import wacode.yamada.yuki.nempaymentapp.view.activity.SendViewModel
import wacode.yamada.yuki.nempaymentapp.view.adapter.CalculatorPagerAdapter
import wacode.yamada.yuki.nempaymentapp.view.controller.MosaicListController
import wacode.yamada.yuki.nempaymentapp.view.custom_view.OnClickMultiCalculator
import wacode.yamada.yuki.nempaymentapp.view.fragment.BaseFragment
import wacode.yamada.yuki.nempaymentapp.viewmodel.EnterMosaicListViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class EnterSendFragment : BaseFragment(), MosaicListController.OnMosaicListClickListener {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: SendViewModel
    private lateinit var enterMosaicListViewModel: EnterMosaicListViewModel
    override fun layoutRes() = R.layout.fragment_enter_send
    private lateinit var controller: MosaicListController
    private val compositeDisposable = CompositeDisposable()
    private val newMosaics = ArrayList<MosaicFullItem>()
    private val selectedMosaics = ArrayList<MosaicItem>()

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)

        enterMosaicListViewModel = ViewModelProviders.of(this, viewModelFactory).get(EnterMosaicListViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectedMosaics.add(MosaicItem.createNEMXEMItem())
        setupViews()
        setupCalculator()
        controller = MosaicListController(this, false, true)
        setupRecyclerView()
        setupViewModelObserve()
    }

    private fun setupViewModelObserve() {
        enterMosaicListViewModel.run {
            fullItemMosaic.observe(this@EnterSendFragment, Observer {
                it ?: return@Observer
                newMosaics.add(it)
                recycler.visibility = View.VISIBLE
                controller.setData(newMosaics)
            })
        }
    }

    private fun setupRecyclerView() {
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = controller.adapter

        context?.let {
            CoroutineScope(Dispatchers.Main).launch {
                val wallet = async(Dispatchers.IO) {
                    WalletManager.getSelectedWallet(it)
                }.await()
                enterMosaicListViewModel.getOwnedMosaicFullData(wallet!!.address)
            }
        }
    }

    private fun setupCalculator() {
        multiCalculatorView.setCalculatorListener(object : OnClickMultiCalculator {
            override fun onPutZERO(mosaicItem: MosaicItem) {
                if (multiCalculatorView.visibility == View.VISIBLE) {
                    if (mosaicItem.isNEMXEMItem()) {
                        controller.showHeader = false
                        controller.switchState = false
                    }
                    controller.setData(newMosaics)
                }
            }

            override fun onPutNotZERO(mosaicItem: MosaicItem) {
                if (!controller.showHeader) {
                    controller.showHeader = true
                    controller.setData(newMosaics)
                }
                if (!controller.switchState && mosaicItem.isNEMXEMItem()) {
                    controller.switchState = true
                    controller.setData(newMosaics)
                }
            }

            override fun onClickRight(hashMap: HashMap<String, String>) {
                val list = ArrayList<SendMosaicItem>()
                for (item in selectedMosaics) {
                    val amount = hashMap[item.getFullName()]
                    amount?.let {
                        list.add(SendMosaicItem(item, it.toDouble()))
                    }
                }
                viewModel.replaceFragment(SendType.SELECT_MODE, list)
            }
        })
    }

    private fun setupViews() {
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.enter_send_fragment_tab1)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.enter_send_fragment_tab2)))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    when (it.position) {
                        0 -> visibleCalculator()
                        1 -> visibleRecycler()
                    }
                }
            }
        })
    }

    private fun visibleCalculator() {
        recycler.visibility = View.GONE
        multiCalculatorView.visibility = View.VISIBLE
        multiCalculatorView.pagerAdapter = CalculatorPagerAdapter((context as AppCompatActivity).supportFragmentManager, selectedMosaics)
        multiCalculatorView.setupViewPager()
        multiCalculatorView.resetCurrentTexts()
        multiCalculatorView.setupAmount(0)
        timerObservable()
    }

    private fun timerObservable() = Observable.timer(DELAY, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread()).subscribe(onNext())

    private fun onNext(): Consumer<Long> {
        return Consumer {
            if (selectedMosaics.size > 0 && multiCalculatorView.getAmount(selectedMosaics[0].getFullName()) != null) {
                multiCalculatorView.updateDisplay(multiCalculatorView.getAmount(selectedMosaics[0].getFullName())!!)
            }
        }
    }

    private fun visibleRecycler() {
        multiCalculatorView.visibility = View.GONE
        multiCalculatorView.resetCalculatorPresenterImp()
        multiCalculatorView.resetCurrentTexts()
        recycler.visibility = View.VISIBLE
        multiCalculatorView.wrapViewPager.removeOnPageChangeListener(multiCalculatorView.wrapViewPagerPageChangeListener)
        multiCalculatorView.wrapViewPager.currentItem = 0
    }

    override fun onClickHeader(switchState: Boolean) {
        setupOnClickHeader(switchState)
        setupSelectedMosaicCount()
        addNemXEMItemIfEmpty()
        controller.setData(newMosaics)
    }

    private fun containsNemXemItem(): Boolean {
        selectedMosaics
                .filter { it.isNEMXEMItem() }
                .forEach { return true }
        return false
    }

    override fun onClickRow(item: MosaicItem) {
        setupSelectedMosaic(item)
        setupSelectedMosaicCount()
        resetListView(item)
    }

    private fun resetListView(checkedItem: MosaicItem) {
        val list = ArrayList<MosaicFullItem>()
        for (mosaic in newMosaics) {
            if (checkedItem.getFullName() == mosaic.getFullName()) {
                val newMosaic = MosaicFullItem(mosaic.divisibility, MosaicItem(mosaic.mosaicItem.mosaic, !checkedItem.checked))
                list.add(newMosaic)
            } else {
                list.add(mosaic)
            }
        }

        addNemXEMItemIfEmpty()
        controller.setData(list)
        newMosaics.clear()
        newMosaics.addAll(list)
    }

    private fun addNemXEMItemIfEmpty() {
        if (selectedMosaics.size < 1) {
            val mosaicItem = MosaicItem.createNEMXEMItem()
            selectedMosaics.add(mosaicItem)
            multiCalculatorView.pagerAdapter.add(mosaicItem)
            controller.showHeader = false
            controller.switchState = false
        }
    }

    private fun setupSelectedMosaicCount() {
        if (selectedMosaics.size > 0) {
            countText.text = selectedMosaics.size.toString()
            countText.visibility = View.VISIBLE
        } else {
            countText.visibility = View.GONE
        }
    }

    private fun setupOnClickHeader(switchState: Boolean) {
        val nemXemItem = MosaicItem.createNEMXEMItem()
        if (!switchState) {
            val localSelectedMosaics = ArrayList<MosaicItem>()
            selectedMosaics.filterTo(localSelectedMosaics) { it.getFullName() != nemXemItem.getFullName() }
            selectedMosaics.clear()
            selectedMosaics.addAll(localSelectedMosaics)
            multiCalculatorView.removeItem(nemXemItem)
            multiCalculatorView.pagerAdapter = CalculatorPagerAdapter((context as AppCompatActivity).supportFragmentManager, localSelectedMosaics)
            multiCalculatorView.setupViewPager()
            multiCalculatorView.resetCurrentTexts()
        } else {
            if (!containsNemXemItem()) {
                selectedMosaics.add(nemXemItem)
                multiCalculatorView.pagerAdapter.add(nemXemItem)
            }
        }
    }

    private fun setupSelectedMosaic(checkedItem: MosaicItem) {
        if (checkedItem.checked) {
            deleteSelectedMosaic(checkedItem)
        } else {
            selectedMosaics.add(checkedItem)
            multiCalculatorView.pagerAdapter.add(checkedItem)
        }

        if (selectedXemNemItem()) {
            controller.showHeader = true
            controller.switchState = true
        }

        multiCalculatorView.pagerIndicator.setCount(selectedMosaics.count())
    }

    private fun deleteSelectedMosaic(mosaicItem: MosaicItem) {
        val localSelectedMosaics = ArrayList<MosaicItem>()
        selectedMosaics.filterTo(localSelectedMosaics) { it.getFullName() != mosaicItem.getFullName() }
        selectedMosaics.clear()
        selectedMosaics.addAll(localSelectedMosaics)
        multiCalculatorView.removeItem(mosaicItem)
        multiCalculatorView.pagerAdapter = CalculatorPagerAdapter((context as AppCompatActivity).supportFragmentManager, localSelectedMosaics)
        multiCalculatorView.setupViewPager()
        multiCalculatorView.resetCurrentTexts()
    }

    private fun selectedXemNemItem(): Boolean {
        return selectedMosaics
                .firstOrNull()
                ?.isNEMXEMItem()
                ?: false
    }

    override fun onPause() {
        super.onPause()
        compositeDisposable.dispose()
    }

    companion object {
        private const val DELAY = 10L
        fun newInstance(viewModel: SendViewModel): EnterSendFragment {
            val fragment = EnterSendFragment()
            val args = Bundle().apply {
                putInt(ARG_CONTENTS_NAME_ID, R.string.enter_send_fragment_title)
            }
            fragment.viewModel = viewModel
            fragment.arguments = args
            return fragment
        }
    }
}

