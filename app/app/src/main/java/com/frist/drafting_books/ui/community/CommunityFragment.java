package com.frist.drafting_books.ui.community;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;


import com.frist.drafting_books.R;
import com.frist.drafting_books.ui.home.HomeViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class CommunityFragment extends Fragment {
    private ViewPager2 mviewPager2;//页面
    private TabLayout mTabLayout;//头部
    private ArrayList<String> titles;//头部标题


private  View root;
    List<Fragment> mfragments;
    private HomeViewModel homeViewModel;
    private static final String TAG = "HomeFragment";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
       root = inflater.inflate(R.layout.fragment_community, container, false);

//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
//

        ;

        //mviewpage 操作
        init();

        return root;
    }

//初始化标题栏与下方滑动页面
    private void init() {
        mfragments = new ArrayList<>();

        mfragments.add(BlankFragment.newInstance(BlankFragment.ALL));//这里传入两个参数
        mfragments.add(BlankFragment.newInstance(BlankFragment.FRIEND));
        titles = new ArrayList<>();
        titles.add("全国书房");
        titles.add("好友书房");
        mviewPager2=root.findViewById(R.id.viewpager2);
        mTabLayout=root.findViewById(R.id.tab_layout);
        //调试代码
//        if(mviewPager2==null){
//            Log.d(TAG, "mviewPager:null ");
//        }
        MyFragmentPagerAdapter myAdapter=new MyFragmentPagerAdapter(getActivity().getSupportFragmentManager(),getLifecycle(),mfragments);

        //Log.d(TAG, Integer.toString(mfragments.size()));

        mviewPager2.setAdapter(myAdapter);

        new TabLayoutMediator(mTabLayout, mviewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(titles.get(position));
            }
        }).attach();

        mviewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Log.d("amy",position+"");
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }
}