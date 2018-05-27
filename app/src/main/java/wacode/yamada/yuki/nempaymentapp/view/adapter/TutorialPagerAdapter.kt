package wacode.yamada.yuki.nempaymentapp.view.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import wacode.yamada.yuki.nempaymentapp.view.fragment.FirstTutorialFragment

class TutorialPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {
        return FirstTutorialFragment.newInsntace(position)
    }

    override fun getCount(): Int {
        return 3
    }
}

