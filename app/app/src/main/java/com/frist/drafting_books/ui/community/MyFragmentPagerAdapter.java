package com.frist.drafting_books.ui.community;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyFragmentPagerAdapter extends FragmentStateAdapter {

    List<Fragment> fragmentsList=new ArrayList<>();

    public MyFragmentPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle,List<Fragment> fragments) {
        super(fragmentManager, lifecycle);
        fragmentsList=fragments;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentsList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentsList.size();
    }
}
