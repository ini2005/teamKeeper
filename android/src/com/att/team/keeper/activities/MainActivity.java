package com.att.team.keeper.activities;

import java.util.Locale;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.att.team.keeper.R;
import com.att.team.keeper.fragments.DebugFragment;
import com.att.team.keeper.fragments.WatchListUsersFragment;
import com.att.team.keeper.fragments.WatchWebUsersFragment;
import com.att.team.keeper.services.BluetoothService;

public class MainActivity extends Activity implements ActionBar.TabListener {
	
	private SectionsPagerAdapter mSectionsPagerAdapter;
	
	private ViewPager mViewPager;
	
	private DebugFragment mDebugFragment;
	
	//private static final String TAG = MainActivity.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		
		// Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.main_viewPager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        
        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}

	@Override
	protected void onResume() {
		if(mDebugFragment == null){
			mDebugFragment = new DebugFragment();
		}
		BluetoothService.INSTANCE.startScanningForDevices(mDebugFragment);
		super.onResume();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_set_visable) {

			Intent discoverableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(
					BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 3600);
			discoverableIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			startActivity(discoverableIntent);

			return true;
		} else if(id == R.id.action_clear_log) {
			mDebugFragment.clearLog();
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		
	}
	
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
        	switch (position) {
        	case 0:
        		return new WatchWebUsersFragment();
        	case 1:
        		return new WatchListUsersFragment();
        	case 2:
        		if(mDebugFragment == null) {
        			
        			mDebugFragment = new DebugFragment(); 
        		}
        		return mDebugFragment;
        	}
        	return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.web).toUpperCase(l);
                case 1:
                    return getString(R.string.watch_users).toUpperCase(l);
                case 2:
                    return getString(R.string.debug).toUpperCase(l);
            }
            return null;
        }
    }

}
