package com.provider.admin.cpepsi_provider.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.provider.admin.cpepsi_provider.Fragment.Accept_fragment;
import com.provider.admin.cpepsi_provider.Fragment.All_fragment;
import com.provider.admin.cpepsi_provider.Fragment.Complete_fragment;
import com.provider.admin.cpepsi_provider.Fragment.Pending_fragment;
import com.provider.admin.cpepsi_provider.Fragment.decline_fragment;

public class PagerView extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;

    //Constructor to the class
    public PagerView(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                All_fragment tab1 = new All_fragment();
                return tab1;
            case 1:
                Complete_fragment tab2 = new Complete_fragment();
                return tab2;
            case 2:
                Accept_fragment tab3 = new Accept_fragment();
                return tab3;
            case 3:
                decline_fragment tab4 = new decline_fragment();
                return tab4;
            case 4:
                Pending_fragment tab5 = new Pending_fragment();
                return tab5;
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }
}

