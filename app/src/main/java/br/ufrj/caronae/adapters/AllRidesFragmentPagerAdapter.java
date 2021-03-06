package br.ufrj.caronae.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import br.ufrj.caronae.frags.AllRidesListFrag;
import br.ufrj.caronae.models.modelsforjson.RideForJson;

public class AllRidesFragmentPagerAdapter extends FragmentPagerAdapter {
    final public static int PAGE_GOING = 0;
    final private static int PAGE_NOT_GOING = 1;
    private String tabTitles[];
    private Fragment frags[];

    public AllRidesFragmentPagerAdapter(FragmentManager fm, ArrayList<RideForJson> goingRides, ArrayList<RideForJson> notGoingRides, String[] tabTitles) {
        super(fm);

        this.tabTitles = tabTitles;

        Bundle bundle1 = new Bundle(), bundle2 = new Bundle();
        bundle1.putParcelableArrayList("rides", goingRides);
        bundle1.putInt("ID", PAGE_GOING);
        bundle2.putParcelableArrayList("rides", notGoingRides);
        bundle2.putInt("ID", PAGE_NOT_GOING);
        Fragment f1 = new AllRidesListFrag(), f2 = new AllRidesListFrag();
        f1.setArguments(bundle1);
        f2.setArguments(bundle2);
        frags = new Fragment[]{f1, f2};
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int position) {
        return frags[position];
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}