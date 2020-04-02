package com.tcc.luis.poster.empresa.ui.minha_empresa.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class TabsAdapter extends FragmentPagerAdapter {
    private List<Fragment> listFragments = new ArrayList<>();
    private List<String> listFragmentsTitle =  new ArrayList<>();

    public TabsAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public void add(Fragment frag, String title){
        this.listFragments.add(frag);
        this.listFragmentsTitle.add(title);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return listFragments.get(position);
    }


    @Override
    public CharSequence getPageTitle(int position){
        return listFragmentsTitle.get(position);
    }

    @Override
    public int getCount() {
        return listFragments.size();
    }
}
