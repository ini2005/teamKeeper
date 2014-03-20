package com.att.team.keeper.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;

import com.att.team.keeper.BluetoothBroadcastReceiver;
import com.att.team.keeper.BluetoothBroadcastReceiver.IBluetoothBroadcastReceiverListener;
import com.att.team.keeper.dtos.DeviceRangeDto;
import com.att.team.keeper.dtos.MemberDto;
import com.att.team.keeper.dtos.RequestDto;
import com.att.team.keeper.dtos.ResponseDto;
import com.att.team.keeper.requests.BaseRequest.IRequestResult;
import com.att.team.keeper.requests.DeviceInRangeReqeust;
import com.att.team.keeper.requests.NetworkThread;
import com.example.android.wizardpager.wizard.model.CustomerInfoPage;

public enum BluetoothService implements IRequestResult<ResponseDto>,
		IBluetoothBroadcastReceiverListener {

	INSTANCE;

	static final String TAG = "BluetoothService";

	public final static String KEY_MAC_ADDRESS = "mac";
	private NetworkThread mNetworkThread;

	private Handler mHandler;

	private BluetoothBroadcastReceiver mBluetoothBroadcastReceiver;

	private Context mContext;
	
	private List<MemberDto> mMembersList;
	
	private IResponseListener mResponseListener = null;
	
	boolean mMacSet = false;
	
	
	public interface IResponseListener {
		void onResponse(List<MemberDto> list);
	}

	public void init(Context context) {
		mContext = context;
		mNetworkThread = new NetworkThread();
		mBluetoothBroadcastReceiver = new BluetoothBroadcastReceiver();
		mHandler = new Handler();
	}

	public void startScanningForDevices(
			IBluetoothBroadcastReceiverListener bluetoothBroadcastReceiverListener) {

		List<IBluetoothBroadcastReceiverListener> bluetoothBroadcastReceiverListeners = new ArrayList<BluetoothBroadcastReceiver.IBluetoothBroadcastReceiverListener>();
		bluetoothBroadcastReceiverListeners
				.add(bluetoothBroadcastReceiverListener);
		bluetoothBroadcastReceiverListeners.add(this);
		mBluetoothBroadcastReceiver.registerDynamically(mContext,
				bluetoothBroadcastReceiverListeners);
	}

	public void stopScanningForDevices() {
		mBluetoothBroadcastReceiver.unregisterDynamically(mContext);
	}

	/**
	 * get bluetooth local device name
	 * 
	 * @return device name String
	 */
	public String getLocalBluetoothName() {
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();

		// if device does not support Bluetooth
		if (mBluetoothAdapter == null) {
			Log.d(TAG, "device does not support bluetooth");
			return null;
		}

		return mBluetoothAdapter.getName();
	}

	/**
	 * get bluetooth adapter MAC address
	 * 
	 * @return MAC address String
	 */
	public String getBluetoothMacAddress() {
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();

		// if device does not support Bluetooth
		if (mBluetoothAdapter == null) {
			Log.d(TAG, "device does not support bluetooth");
			return null;
		}

		
		if(mMacSet == false){
			
			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());
			Editor editor = sharedPreferences.edit();
			editor.putString(KEY_MAC_ADDRESS, mBluetoothAdapter.getAddress());
			editor.commit();
			mMacSet = true;
		}
		return mBluetoothAdapter.getAddress();
	}

	@Override
	public void onSuccess(ResponseDto obj) {
		Log.d(TAG, "success");
		
		mMembersList = obj.getMembers();
		
		if (mResponseListener != null) {
			mResponseListener.onResponse(mMembersList);
		}
	}

	@Override
	public void onNetworkIsOffline() {

	}

	@Override
	public void onFailed(int responseCode, ResponseDto obj) {

	}

	@Override
	public Handler getCallbackHandler() {
		return mHandler;
	}

	@Override
	public void onDiscoveryFinished(Map<BluetoothDevice, Integer> devices) {

		DeviceInRangeReqeust deviceInRangeReqeust = new DeviceInRangeReqeust(
				mNetworkThread);

		RequestDto requestDto = new RequestDto();

		List<DeviceRangeDto> deviceInRangeListDto = new ArrayList<DeviceRangeDto>();

		for (BluetoothDevice device : devices.keySet()) {

			DeviceRangeDto deviceRange = new DeviceRangeDto();
			deviceRange.setBluetoothMac(device.getAddress());
			deviceRange.setBluetoothRssi(devices.get(device));
			deviceInRangeListDto.add(deviceRange);
		}

		requestDto.setDevicesInRange(deviceInRangeListDto);

		MemberDto memberDto = new MemberDto();
		memberDto.setBluetoothMac(getBluetoothMacAddress());
		memberDto.setBluetoothName(getLocalBluetoothName());

		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());
		
		
		memberDto.setFirstName(sharedPreferences.getString(CustomerInfoPage.NAME_DATA_KEY, null));
		memberDto.setLastName(sharedPreferences.getString(CustomerInfoPage.LAST_NAME_DATA_KEY, null));
		memberDto.setLastUpdateTime(System.currentTimeMillis());
		memberDto.setMobileNumber(sharedPreferences.getString(CustomerInfoPage.PHONE_DATA_KEY, null));

		requestDto.setMemberDto(memberDto);

		deviceInRangeReqeust.setRequestDto(requestDto);
		deviceInRangeReqeust.execute(this);

	}

	@Override
	public void onDiscoveryStarted() {
		

	}
	
	public void setResponseListener(IResponseListener listener) {
		mResponseListener = listener;
	}
	
	public List<MemberDto> getLatestMembers() {
		return mMembersList;
	}

}
