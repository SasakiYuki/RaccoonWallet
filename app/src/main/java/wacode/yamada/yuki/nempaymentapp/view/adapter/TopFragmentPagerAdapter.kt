package wacode.yamada.yuki.nempaymentapp.view.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import wacode.yamada.yuki.nempaymentapp.view.fragment.HomeFragment
import wacode.yamada.yuki.nempaymentapp.view.fragment.QrScanFragment
import wacode.yamada.yuki.nempaymentapp.view.fragment.top.QRLabFragment
import wacode.yamada.yuki.nempaymentapp.view.fragment.top.ReceiveFragment
import wacode.yamada.yuki.nempaymentapp.view.fragment.top.SendTopFragment
import java.util.*

class TopFragmentPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private val list = ArrayList<Fragment>()

    init {
        list.add(QRLabFragment.newInstance())
        list.add(ReceiveFragment.newInstance(-1L, null))
        list.add(HomeFragment.newInstance())
        list.add(SendTopFragment.newInstance())
        list.add(QrScanFragment.newInstance(false, false))
    }

    override fun getItem(position: Int): Fragment {
        return list[position]
    }

    override fun getCount(): Int {
        return 5
    }
}

