package com.att.team.keeper.activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.att.team.keeper.R;
import com.att.team.keeper.services.BluetoothService;

public class MainActivity extends Activity {

	private TextView mMyBluetoothNameTextView;
	private TextView mMyBluetoothMacTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_mian);

		mMyBluetoothNameTextView = (TextView) findViewById(R.id.bluetooth_name_textView);
		mMyBluetoothMacTextView = (TextView) findViewById(R.id.bluetooth_mac_textView);

		Intent discoverableIntent = new Intent(
				BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		discoverableIntent.putExtra(
				BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 3600);
		discoverableIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		startActivity(discoverableIntent);

		super.onCreate(savedInstanceState);

	}

	@Override
	protected void onResume() {

		mMyBluetoothNameTextView.setText(BluetoothService.INSTANCE
				.getLocalBluetoothName());
		mMyBluetoothMacTextView.setText(BluetoothService.INSTANCE
				.getBluetoothMacAddress());

		BluetoothService.INSTANCE.startScanningForDevices();

		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}

}
