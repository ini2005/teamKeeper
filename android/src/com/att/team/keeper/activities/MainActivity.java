package com.att.team.keeper.activities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;

import com.att.team.keeper.R;
import com.att.team.keeper.services.BluetoothService;

public class MainActivity extends Activity {

	private static final int REQUEST_ENABLE_BT = 1;

	private Map<BluetoothDevice, Integer> btDeviceList = new HashMap<BluetoothDevice, Integer>();

	private TextView mMyBluetoothNameTextView;
	private TextView mMyBluetoothMacTextView;
	private TextView out;
	private BluetoothAdapter btAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_mian);

		mMyBluetoothNameTextView = (TextView) findViewById(R.id.bluetooth_name_textView);
		mMyBluetoothMacTextView = (TextView) findViewById(R.id.bluetooth_mac_textView);

		super.onCreate(savedInstanceState);

		out = (TextView) findViewById(R.id.out);
		// out.setMovementMethod(new ScrollingMovementMethod());

		// Register the BroadcastReceiver
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		filter.addAction(BluetoothDevice.ACTION_UUID);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		registerReceiver(ActionFoundReceiver, filter); // Don't forget to
														// unregister during
														// onDestroy

		// Getting the Bluetooth adapter
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		out.append("\nAdapter: " + btAdapter);


		Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 3600);
		startActivity(discoverableIntent);
		
		CheckBTState();

	}

	private void CheckBTState() {
		// Check for Bluetooth support and then check to make sure it is turned
		// on
		// If it isn't request to turn it on
		// List paired devices
		// Emulator doesn't support Bluetooth and will return null 
		if (btAdapter == null) {
			out.append("\nBluetooth NOT supported. Aborting.");
			return;
		} else {
			if (btAdapter.isEnabled()) {
				out.append("\nBluetooth is enabled...");

				// Starting the device discovery
				btAdapter.startDiscovery();
			} else {
				Intent enableBtIntent = new Intent(
						btAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			}
		}
	}

	@Override
	protected void onResume() {

		mMyBluetoothNameTextView.setText(BluetoothService.INSTANCE
				.getLocalBluetoothName());
		mMyBluetoothMacTextView.setText(BluetoothService.INSTANCE
				.getBluetoothMacAddress());
		
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (btAdapter != null) {
			btAdapter.cancelDiscovery();
		}
		unregisterReceiver(ActionFoundReceiver);
	}

	private final BroadcastReceiver ActionFoundReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,
						Short.MIN_VALUE);
				btDeviceList.put(device, rssi);
			} else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
				out.append("\nDiscovery Started...");
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
					.equals(action)) {
				out.append("\nDiscovery Finished");

				Set<BluetoothDevice> bluetoothDevices = btDeviceList.keySet();

				for (BluetoothDevice device : bluetoothDevices) {

					out.append("\nGetting Services for " + device.getName()
							+ ", " + device + ", rssi "
							+ btDeviceList.get(device) + "dBm");
				}

			}
		}
	};

}
