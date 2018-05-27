package wacode.yamada.yuki.nempaymentapp.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import wacode.yamada.yuki.nempaymentapp.view.fragment.HomeFragment;
import wacode.yamada.yuki.nempaymentapp.view.fragment.QrScanFragment;
import wacode.yamada.yuki.nempaymentapp.view.fragment.top.QRLabFragment;
import wacode.yamada.yuki.nempaymentapp.view.fragment.top.ReceiveFragment;
import wacode.yamada.yuki.nempaymentapp.view.fragment.top.SendTopFragment;

// TODO kotlin化 リファクタ
public class ExampleFragmentPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> list = new ArrayList<>();

    public ExampleFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        list.add(QRLabFragment.Companion.newInstance());
        list.add(ReceiveFragment.Companion.newInstance(-1L, null));
        list.add(HomeFragment.Companion.newInstance());
        list.add(SendTopFragment.Companion.newInstance());
        list.add(QrScanFragment.Companion.newInstance(false, false));
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "ページ" + String.valueOf(position);
    }
}

