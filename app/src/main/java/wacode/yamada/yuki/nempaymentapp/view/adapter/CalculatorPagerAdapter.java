package wacode.yamada.yuki.nempaymentapp.view.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.nakama.arraypageradapter.ArrayFragmentStatePagerAdapter;

import java.util.ArrayList;

import wacode.yamada.yuki.nempaymentapp.rest.item.MosaicItem;
import wacode.yamada.yuki.nempaymentapp.view.fragment.send.CalculatorTitleFragment;

public class CalculatorPagerAdapter extends ArrayFragmentStatePagerAdapter<MosaicItem> {

    private ArrayList<Fragment> fragments = new ArrayList<>();

    public CalculatorPagerAdapter(FragmentManager fm, ArrayList<MosaicItem> mosaics) {
        super(fm, mosaics);
    }

    @Override
    public Fragment getFragment(MosaicItem item, int position) {
        if (fragments.size() < position+1) {
            CalculatorTitleFragment fragment =CalculatorTitleFragment.Companion.newInstance(item);
            fragments.add(position, fragment);
        }
        return fragments.get(position);
    }

    public Fragment getCurrentFragment(int position) {
        return fragments.get(position);
    }

    public void add(MosaicItem item,int position) {
        fragments.add(position,CalculatorTitleFragment.Companion.newInstance(item));
    }

    public ArrayList<Fragment> getFragments() {
        return fragments;
    }
}