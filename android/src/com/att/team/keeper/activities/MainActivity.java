package com.att.team.keeper.activities;

import java.util.Map;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.att.team.keeper.BluetoothBroadcastReceiver.IBluetoothBroadcastReceiverListener;
import com.att.team.keeper.R;
import com.att.team.keeper.services.BluetoothService;

public class MainActivity extends Activity implements
		IBluetoothBroadcastReceiverListener {

	private TextView mMyBluetoothNameTextView;
	private TextView mMyBluetoothMacTextView;
	private TextView mLoggerTextView;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_mian);

		mMyBluetoothNameTextView = (TextView) findViewById(R.id.bluetooth_name_textView);
		mMyBluetoothMacTextView = (TextView) findViewById(R.id.bluetooth_mac_textView);
		mLoggerTextView = (TextView) findViewById(R.id.out);
		
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {

		mMyBluetoothNameTextView.setText(BluetoothService.INSTANCE
				.getLocalBluetoothName());
		mMyBluetoothMacTextView.setText(BluetoothService.INSTANCE
				.getBluetoothMacAddress());

		BluetoothService.INSTANCE.startScanningForDevices(this);

		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

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
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onDiscoveryFinished(Map<BluetoothDevice, Integer> devices) {
		
		 if(devices.keySet().size() == 0) {
			 return;
		 }
		
		for (BluetoothDevice bluetoothDevice : devices.keySet()) {
			mLoggerTextView.append("\nDeviceName: " );
			mLoggerTextView.append( bluetoothDevice.getName());
			mLoggerTextView.append("\nDeviceMac: " );
			mLoggerTextView.append( bluetoothDevice.getAddress());
			mLoggerTextView.append(" Device RSSI: " );
			mLoggerTextView.append( devices.get(bluetoothDevice).toString());
		}
		
		mLoggerTextView.append("\nDiscovery Finished...");
		
		mLoggerTextView.append("\n-----------------------------------");
		

	}

	@Override
	public void onDiscoveryStarted() {
		mLoggerTextView.append("\nDiscovery Started...");
		
	}

}
