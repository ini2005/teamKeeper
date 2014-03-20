package com.att.team.keeper.fragments;

import com.att.team.keeper.R;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class PickRoomFragment extends Fragment {

	private IPickRoomFragment mListener;
	
	private static final String TAG = PickRoomFragment.class.getSimpleName();

	public interface IPickRoomFragment {
		void onRoomPicked();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.pick_room_layout, null);
		((Button)view.findViewById(R.id.pickRoom_continueButton)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mListener.onRoomPicked();
			}
		});
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mListener = (IPickRoomFragment) activity;
	}
	
	public int getRoomNumber() {
		String roomNumber = ((EditText)getView().findViewById(R.id.pickRoom_roomNumber)).getText().toString();
		try {
			return Integer.parseInt(roomNumber);
		} catch (NumberFormatException e) {
			Log.e(TAG, "Failed to parse room number, returning 0 instead");
			return 0;
		}
	}

}
