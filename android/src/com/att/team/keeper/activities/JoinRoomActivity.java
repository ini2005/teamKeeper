package com.att.team.keeper.activities;

import java.util.Locale;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.att.team.keeper.R;
import com.att.team.keeper.fragments.PickRoomFragment;
import com.att.team.keeper.fragments.UserDetailsFragment;
import com.att.team.keeper.fragments.PickRoomFragment.IPickRoomFragment;
import com.att.team.keeper.fragments.UserDetailsFragment.IUserDetailsFragment;

public class JoinRoomActivity extends Activity implements ActionBar.TabListener, IPickRoomFragment, IUserDetailsFragment {
	
	private SectionsPagerAdapter mSectionsPagerAdapter;
	
	private ViewPager mViewPager;
	
	private PickRoomFragment mPickRoomFragment;
	
	private static final String TAG = JoinRoomActivity.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.join_group_layout);
		
		// Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.joinGroup_viewPager);
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
        
//        actionBar.newTab().setText(mSectionsPagerAdapter.getPageTitle(0)).setTabListener(this);
//        actionBar.newTab().setText(mSectionsPagerAdapter.getPageTitle(1)).setTabListener(this);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
	
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onDetailsEntered(String phoneNumber, String firstName,
			String lastName) {
		int roomNumber = mPickRoomFragment.getRoomNumber();
		String log = new StringBuilder().append("Joining room number ").append(roomNumber).append(". Phone number: ").
				append(phoneNumber).append(", First name: ").append(firstName).append(", Last name: ").append(lastName).
				toString();
		Log.d(TAG, log);
	}

	@Override
	public void onRoomPicked() {
		// TODO Auto-generated method stub
		mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
	}
	
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
        	switch (position) {
        	case 0:
        		mPickRoomFragment = new PickRoomFragment(); 
        		return mPickRoomFragment;
        	case 1:
        		return new UserDetailsFragment();
        	}
        	return null;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.pick_room).toUpperCase(l);
                case 1:
                    return getString(R.string.enter_details).toUpperCase(l);
            }
            return null;
        }
    }

}
