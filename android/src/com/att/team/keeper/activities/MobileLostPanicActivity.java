package com.att.team.keeper.activities;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.att.team.keeper.R;
import com.att.team.keeper.TeamKeeperApplication;

public class MobileLostPanicActivity extends Activity {

	private MediaPlayer mMediaPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mobile_lost_panic_activity);

		mMediaPlayer = MediaPlayer.create(this, R.raw.alert);
		mMediaPlayer.start();

		getActionBar().hide();
		
		TeamKeeperApplication.isPanicAlertOn = true;

	}

	@Override
	protected void onDestroy() {
		TeamKeeperApplication.isPanicAlertOn = false;
		super.onDestroy();
	}
	
	public void onClick_stopAlert(View v) {
		mMediaPlayer.stop();
		finish();

	}

	public void onClick_map(View v) {
		double latitude = 40.714728;
		double longitude = -73.998672;
		String label = "ABC Label";
		String uriBegin = "geo:" + latitude + "," + longitude;
		String query = latitude + "," + longitude + "(" + label + ")";
		String encodedQuery = Uri.encode(query);
		String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
		Uri uri = Uri.parse(uriString);
		Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
		startActivity(intent);

	}

	public void onClick_phone(View v) {
		String number = "tel:" + "+972523551288";
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number)); 
        startActivity(callIntent);
	}

}
