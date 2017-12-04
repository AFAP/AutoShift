package com.afap.autoshift;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.afap.autoshift.fragment.DepthFragment;
import com.afap.autoshift.model.PlatformInfo;

import java.util.ArrayList;
import java.util.List;

public class IndexActivity extends AppCompatActivity {

    private List<PlatformInfo> mPlatforms;

    private List<String> titles = new ArrayList<>();


    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private MyViewPagerAdapter mPagerAdapter;
    private List<Fragment> mFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        initData();

        init();
    }

    private void initData() {
        mPlatforms = new ArrayList<>();

        PlatformInfo platformInfo1 = new PlatformInfo("Bittrex");
        PlatformInfo platformInfo2 = new PlatformInfo("Poloniex");
        PlatformInfo platformInfo3 = new PlatformInfo("Bitfinex");

        mPlatforms.add(platformInfo1);
        mPlatforms.add(platformInfo2);
        mPlatforms.add(platformInfo3);
    }


    private void init() {
        mTabLayout = findViewById(R.id.tablayout);
        mViewPager = findViewById(R.id.viewpager);
        //设置TabLayout标签的显示方式
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        //设置TabLayout点击事件
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        // 遍历匹配不同平台的组合,C(n)2
        for (int i = 0; i < mPlatforms.size() - 1; i++) {
            for (int j = i + 1; j < mPlatforms.size(); j++) {
                String title = mPlatforms.get(i).platformName + "\n<-->\n" + mPlatforms.get(j).platformName;
                mTabLayout.addTab(mTabLayout.newTab());
                titles.add(title);
                mFragments.add(DepthFragment.newInstance(title));
            }
        }
        mPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(), titles, mFragments);
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }


    class MyViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;
        private List<String> titles;

        public MyViewPagerAdapter(FragmentManager fm, List<String> titles, List<Fragment> fragments) {
            super(fm);
            this.titles = titles;
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int arg0) {
            return fragments.get(arg0);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}
