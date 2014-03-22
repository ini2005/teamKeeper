package com.att.team.keeper.activities;

import com.att.team.keeper.R;
import com.att.team.keeper.TeamKeeperApplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.TextView;

public class InformMemberLostActivity extends Activity {

	private MediaPlayer mMediaPlayer;
	
	private Vibrator mVibrator;

	public interface InformMemberLostExtras {
		static final String NAMES_EXTRA = "NAMES_EXTRA";
		static final String NUMBER_OF_LOSTS_EXTRA = "NUMBER_OF_LOSTS";
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inform_member_lost_layout);

		Bundle extras = getIntent().getExtras();
		String names = extras.getString(InformMemberLostExtras.NAMES_EXTRA);
		int number = extras
				.getInt(InformMemberLostExtras.NUMBER_OF_LOSTS_EXTRA);

		String message = new StringBuilder()
				.append(names)
				.append(" ")
				.append(number == 1 ? getString(R.string.has_left_group)
						: getString(R.string.have_left_group)).toString();

		((TextView) findViewById(R.id.informLostMember_informText))
				.setText(message);

		mMediaPlayer = MediaPlayer.create(this, R.raw.alert);
		mMediaPlayer.start();
		
		mVibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		if (mVibrator.hasVibrator()) {
			long[] pattern = new long[]{0, 500, 500};
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

	public static Intent createIntentForActivity(Activity launcherActivity,
			String namesOfLosts, int numberOfLosts) {
		Intent intent = new Intent(launcherActivity,
				InformMemberLostActivity.class);
		Bundle extras = new Bundle();
		extras.putString(InformMemberLostExtras.NAMES_EXTRA, namesOfLosts);
		extras.putInt(InformMemberLostExtras.NUMBER_OF_LOSTS_EXTRA,
				numberOfLosts);
		intent.putExtras(extras);
		return intent;
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
