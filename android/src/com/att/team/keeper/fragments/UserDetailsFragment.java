package com.att.team.keeper.fragments;

import com.att.team.keeper.R;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class UserDetailsFragment extends Fragment {

	private IUserDetailsFragment mListener;

	public interface IUserDetailsFragment {
		void onDetailsEntered(String phoneNumber, String firstName,
				String lastName);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.user_details_layout, null);

		((Button) view.findViewById(R.id.userDetails_joinButton))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String phoneNumber = ((EditText)view.findViewById(R.id.userDetails_phoneNumber)).getText().toString();
						String firstName = ((EditText)view.findViewById(R.id.userDetails_firstName)).getText().toString();
						String lastName = ((EditText)view.findViewById(R.id.userDetails_lastName)).getText().toString();
						mListener.onDetailsEntered(phoneNumber, firstName, lastName);
					}
				});
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mListener = (IUserDetailsFragment) activity;
	}

}
