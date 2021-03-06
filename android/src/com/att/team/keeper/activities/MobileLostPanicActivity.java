package com.att.team.keeper.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.TextView;

import com.att.team.keeper.R;
import com.att.team.keeper.TeamKeeperApplication;
import com.att.team.keeper.activities.InformMemberLostActivity.InformMemberLostExtras;

public class MobileLostPanicActivity extends Activity {

	private MediaPlayer mMediaPlayer;

	private Vibrator mVibrator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mobile_lost_panic_activity);

		Bundle extras = getIntent().getExtras();
		String lastSeenBy = extras
				.getString(InformMemberLostExtras.LAST_SEEN_BY_EXTRA);

		((TextView) findViewById(R.id.informLostMember_lastSeenBy))
				.setText(lastSeenBy);

		mMediaPlayer = MediaPlayer.create(this, R.raw.alert);
		mMediaPlayer.start();

		mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		if (mVibrator.hasVibrator()) {
			long[] pattern = new long[] { 0, 500, 500 };
			mVibrator.vibrate(pattern, 0);
		}

		getActionBar().hide();

		TeamKeeperApplication.isPanicAlertOn = true;

	}

	public void onClick_stopAlert(View v) {
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

	@Override
	protected void onDestroy() {
		TeamKeeperApplication.isPanicAlertOn = false;
		super.onDestroy();

		mMediaPlayer.stop();
		if (mVibrator.hasVibrator()) {
			mVibrator.cancel();
		}
	}

}
