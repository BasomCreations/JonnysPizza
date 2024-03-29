package com.example.jonnyspizza.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.jonnyspizza.Cart;
import com.example.jonnyspizza.DrinkFragment;
import com.example.jonnyspizza.PizzaFragment;
import com.example.jonnyspizza.R;
import com.example.jonnyspizza.SubFragment;
import com.example.jonnyspizza.WingsFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3, R.string.tab_text_4};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        /**
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return PlaceholderFragment.newInstance(position + 1); */
        Fragment fragment = PizzaFragment.newInstance();
        switch (position){
            case 0:
                fragment = PizzaFragment.newInstance();
                break;
            case 1:
                fragment = SubFragment.newInstance();
                break;
            case 2:
                fragment = WingsFragment.newInstance();
                break;
            case 3:
                fragment = DrinkFragment.newInstance();
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 4 total pages.
        return 4;
    }
}