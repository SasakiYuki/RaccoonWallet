package wacode.yamada.yuki.nempaymentapp.view.adapter

import android.content.Context
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import wacode.yamada.yuki.nempaymentapp.view.fragment.BaseFragment

class SimpleViewPagerAdapter(val context: Context, val fragments: List<BaseFragment>, fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int) = fragments[position]

    override fun getCount() = fragments.size

    override fun getPageTitle(position: Int): CharSequence {
        return if (context.getString(fragments[position].getTitleRes()).isNullOrEmpty()) {
            ""
        } else {
            context.getString(fragments[position].getTitleRes())
        }
    }
}