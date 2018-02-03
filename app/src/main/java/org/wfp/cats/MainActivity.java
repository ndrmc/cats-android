package org.wfp.cats;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.wfp.cats.fragment.DispatchFragment;
import org.wfp.cats.fragment.ReceivingFragment;
import org.wfp.cats.fragment.SettingFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final int RECEIVING_FRAGMENT_POSITION = 0;
    private static final int DISPATCH_FRAGMENT_POSITION = 1;
    private static final int SETTINGS_FRAGMENT_POSITION = 2;
    private static final int PAGE_COUNT = 3;

    private SectionsPagerAdapter mSectionsPagerAdapter;

    @BindView(R.id.container)
    ViewPager mViewPager;

    @BindView(R.id.sliding_tabs)
    TabLayout tabLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private FragmentManager mFragmentManager;

    private Fragment mReceivingFragment;

    private Fragment mDispatchFragment;

    private Fragment mSettingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        mFragmentManager = getSupportFragmentManager();

        mViewPager.setAdapter(new SectionsPagerAdapter(mFragmentManager));

        tabLayout.setupWithViewPager(mViewPager);


        if(savedInstanceState == null) {

            mReceivingFragment = ReceivingFragment.getInstance();

            mDispatchFragment = DispatchFragment.getInstance();

            mSettingsFragment = SettingFragment.getInstance();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case RECEIVING_FRAGMENT_POSITION:
                    return mReceivingFragment;
                case DISPATCH_FRAGMENT_POSITION:
                    return mDispatchFragment;
                case SETTINGS_FRAGMENT_POSITION:
                    return mSettingsFragment;
            }

            return new ReceivingFragment();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case RECEIVING_FRAGMENT_POSITION:
                    return getString(R.string.receiving);
                case DISPATCH_FRAGMENT_POSITION:
                    return getString(R.string.dispatch);
                case SETTINGS_FRAGMENT_POSITION:
                    return getString(R.string.settings);
            }
            return getString(R.string.receiving);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(mReceivingFragment != null) {
            mReceivingFragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
