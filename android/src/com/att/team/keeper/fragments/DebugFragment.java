package com.att.team.keeper.fragments;

import java.util.Map;

import com.att.team.keeper.R;
import com.att.team.keeper.BluetoothBroadcastReceiver.IBluetoothBroadcastReceiverListener;
import com.att.team.keeper.services.BluetoothService;

import android.app.Fragment;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DebugFragment extends Fragment implements IBluetoothBroadcastReceiverListener {
	
	private TextView mMyBluetoothNameTextView;
	private TextView mMyBluetoothMacTextView;
	private TextView mLoggerTextView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.debug_layout, null);
		
		mMyBluetoothNameTextView = (TextView) view.findViewById(R.id.bluetooth_name_textView);
		mMyBluetoothMacTextView = (TextView) view.findViewById(R.id.bluetooth_mac_textView);
		mLoggerTextView = (TextView) view.findViewById(R.id.out);
		
		return view;
	}
	
	@Override
	public void onResume() {
		
		mMyBluetoothNameTextView.setText(BluetoothService.INSTANCE.getLocalBluetoothName());
		mMyBluetoothMacTextView.setText(BluetoothService.INSTANCE.getBluetoothMacAddress());

		

		super.onResume();
	}
	
	@Override
	public void onDiscoveryFinished(Map<BluetoothDevice, Integer> devices) {

		if (devices.keySet().size() == 0 || mLoggerTextView == null) {
			return;
		}

		for (BluetoothDevice bluetoothDevice : devices.keySet()) {
			mLoggerTextView.append("\nDeviceName: ");
			mLoggerTextView.append(bluetoothDevice.getName() == null ? " "
					: bluetoothDevice.getName());
			mLoggerTextView.append("\nDeviceMac: ");
			mLoggerTextView.append(bluetoothDevice.getAddress() == null ? " "
					: bluetoothDevice.getAddress());
			mLoggerTextView.append(" Device RSSI: ");
			mLoggerTextView.append(devices.get(bluetoothDevice).toString());
		}

		mLoggerTextView.append("\nDiscovery Finished...");

		mLoggerTextView.append("\n-----------------------------------");

	}

	@Override
	public void onDiscoveryStarted() {
		if (mLoggerTextView != null) {
			mLoggerTextView.append("\nDiscovery Started...");
		}
		
	}
	
	public void clearLog() {
		mLoggerTextView.setText("");
	}

}
