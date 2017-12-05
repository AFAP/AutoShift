package com.afap.autoshift;

import android.content.Context;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.afap.autoshift.fragment.DepthFragment;
import com.afap.autoshift.model.PlatformInfo;

import org.json.JSONException;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
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
        File cFile = new File(Environment.getExternalStorageDirectory(), "shift_config.js");
        String jsonStr;

        if (cFile.exists()) {
            jsonStr = getStringFromFile(cFile);
        } else {
            jsonStr = getStringFromAssets(this, "config.js");
        }

        try {
            JSONArray array = new JSONArray(jsonStr);
            for (int i = 0; i < array.length(); i++) {
                PlatformInfo platformInfo = PlatformInfo.parseInfo(array.getJSONObject(i));

                mPlatforms.add(platformInfo);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void init() {
        mTabLayout = findViewById(R.id.tablayout);
        mViewPager = findViewById(R.id.viewpager);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
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
                mFragments.add(DepthFragment.newInstance(mPlatforms.get(i), mPlatforms.get(j)));
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


    public String getStringFromAssets(Context context, String path) {
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(path));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line;
            String result = "";
            while ((line = bufReader.readLine()) != null)
                result += line;
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getStringFromFile(File file) {
        try {
            InputStreamReader inputReader = new FileReader(file)  ;
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line;
            String result = "";
            while ((line = bufReader.readLine()) != null)
                result += line;
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
