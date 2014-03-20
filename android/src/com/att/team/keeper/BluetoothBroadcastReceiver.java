package com.att.team.keeper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class BluetoothBroadcastReceiver extends BroadcastReceiver {

	static final String TAG = "BluetoothBroadcastReceiver";

	private Map<BluetoothDevice, Integer> mDeviceList = new HashMap<BluetoothDevice, Integer>();

	private IBluetoothBroadcastReceiverListener mBluetoothBroadcastReceiverListener;

	private BluetoothAdapter mAdapter;

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (BluetoothDevice.ACTION_FOUND.equals(action)) {
			BluetoothDevice device = intent
					.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,
					Short.MIN_VALUE);
			mDeviceList.put(device, rssi);
		} else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
			Log.d(TAG, "\nDiscovery Started...");
		} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
			Log.d(TAG, "\nDiscovery Finished");

			Set<BluetoothDevice> bluetoothDevices = mDeviceList.keySet();

			for (BluetoothDevice device : bluetoothDevices) {

				Log.d(TAG, "\nGetting Services for " + device.getName() + ", "
						+ device + ", rssi " + mDeviceList.get(device) + "dBm");
			}

			mBluetoothBroadcastReceiverListener
					.onDiscoveryFinished(mDeviceList);

			mDeviceList.clear();
			mAdapter.startDiscovery();

		}

	}

	private static boolean mIsRegister = false;

	/**
	 * Registers this dynamic broadcast receiver to start handling intents.<br/>
	 * 
	 * @param applicationContext
	 *            An application context.
	 */
	public synchronized void registerDynamically(Context applicationContext,
			IBluetoothBroadcastReceiverListener listener) {
		if (mIsRegister == true) {
			return;
		}

		mBluetoothBroadcastReceiverListener = listener;

		// Register the BroadcastReceiver
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		filter.addAction(BluetoothDevice.ACTION_UUID);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

		applicationContext.getApplicationContext().registerReceiver(this,
				filter);

		// Getting the Bluetooth adapter
		mAdapter = BluetoothAdapter.getDefaultAdapter();
		Log.d(TAG, "Adapter: " + mAdapter);
		
		CheckBTState(applicationContext);

		mIsRegister = true;

	}

	/**
	 * Unregisters this dynamic broadcast receiver to stop handling intents.<br/>
	 * 
	 * @param applicationContext
	 *            An application context.
	 */
	public synchronized void unregisterDynamically(Context applicationContext) {

		applicationContext.getApplicationContext().unregisterReceiver(this);

		mIsRegister = false;
	}

	private void CheckBTState(Context applicationContext) {
		// Check for Bluetooth support and then check to make sure it is turned
		// on
		// If it isn't request to turn it on
		// List paired devices
		// Emulator doesn't support Bluetooth and will return null
		if (mAdapter == null) {
			Log.d(TAG, "Bluetooth NOT supported. Aborting.");
			return;
		} else {
			if (mAdapter.isEnabled()) {
				Log.d(TAG, "Bluetooth is enabled...");

				// Starting the device discovery
				mAdapter.startDiscovery();
			} else {
				Intent enableBtIntent = new Intent(
						mAdapter.ACTION_REQUEST_ENABLE);
				enableBtIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				applicationContext.startActivity(enableBtIntent);
			}
		}
	}

	public interface IBluetoothBroadcastReceiverListener {

		public void onDiscoveryFinished(Map<BluetoothDevice, Integer> devices);

	}

}
