package com.att.team.keeper.activities;

import java.util.Locale;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;

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
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		
	}
	
	@Override
	public void onDetailsEntered(String phoneNumber, String firstName,
			String lastName) {
		int roomNumber = mPickRoomFragment.getRoomNumber();
		String log = new StringBuilder().append("Joining room number ").append(roomNumber).append(". Phone number: ").
				append(phoneNumber).append(", First name: ").append(firstName).append(", Last name: ").append(lastName).
				toString();
		Log.d(TAG, log);
		
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
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
