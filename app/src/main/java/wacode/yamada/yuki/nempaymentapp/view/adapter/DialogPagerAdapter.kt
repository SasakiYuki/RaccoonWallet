package wacode.yamada.yuki.nempaymentapp.view.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class DialogPagerAdapter(val fragments: List<Fragment>, fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getCount(): Int = fragments.size

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }
}