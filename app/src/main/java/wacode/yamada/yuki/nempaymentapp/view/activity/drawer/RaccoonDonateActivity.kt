package wacode.yamada.yuki.nempaymentapp.view.activity.drawer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import wacode.yamada.yuki.nempaymentapp.view.activity.BaseFragmentActivity
import wacode.yamada.yuki.nempaymentapp.view.fragment.donate.DonateDetailFragment
import wacode.yamada.yuki.nempaymentapp.view.fragment.donate.DonateTopFragment

class RaccoonDonateActivity : BaseFragmentActivity() {
    private val viewModel = RaccoonDonateViewModel()

    override fun setLayout() = SIMPLE_FRAGMENT_ONLY_LAYOUT

    override fun initialFragment() = DonateTopFragment.newInstance(viewModel)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setToolBarBackButton()
        setupReplaceEvent()
    }
    private fun setupReplaceEvent() {
        viewModel.replaceEvent
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    when (it) {
                        DonateDisplayType.DETAIL -> replaceDonateDetail()
                    }
                }
    }

    private fun replaceDonateDetail() {
        replaceFragment(DonateDetailFragment.newInstance(viewModel), true)
    }

    companion object {
        fun createIntent(context: Context): Intent {
            val intent = Intent(context, RaccoonDonateActivity::class.java)
            return intent
        }
    }

    class RaccoonDonateViewModel {
        var donateType: DonateType = DonateType.ANDROID
        val replaceEvent: PublishSubject<DonateDisplayType> = PublishSubject.create()

        fun replace(donateDisplayType: DonateDisplayType) {
            replaceEvent.onNext(donateDisplayType)
        }
    }

    enum class DonateDisplayType {
        TOP,
        DETAIL,
        SEND
    }

    enum class DonateType {
        ANDROID,
        RHIME,
        IOS
    }
}

