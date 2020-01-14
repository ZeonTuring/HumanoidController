package com.example.max_e1controller;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3};
    private final Context mContext;
    private BluetoothActivity activity;

    public SectionsPagerAdapter(Context context, FragmentManager fm, BluetoothActivity act) {
        super(fm);
        mContext = context;
        activity = act;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        //return PlaceholderFragment.newInstance(position + 1);
        switch (position)
        {
        case 0:
        PairedDevice pairedDevice = new PairedDevice(activity);
        return pairedDevice;
        case 1:
        BluetoothClassic bluetoothClassic = new BluetoothClassic(activity);
        return bluetoothClassic;
        case 2:
        BluetoothLE bluetoothLE = new BluetoothLE(activity);
        return bluetoothLE;
        default:
        return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 3;
    }
}