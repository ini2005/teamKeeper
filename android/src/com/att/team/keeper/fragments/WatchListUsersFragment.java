package com.att.team.keeper.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.att.team.keeper.R;
import com.att.team.keeper.activities.InformMemberLostActivity;
import com.att.team.keeper.activities.InformMemberLostActivity.InformMemberLostExtras;
import com.att.team.keeper.dtos.MemberDto;
import com.att.team.keeper.services.BluetoothService;
import com.att.team.keeper.services.BluetoothService.IResponseListener;

public class WatchListUsersFragment extends Fragment implements IResponseListener {
	
	private UsersAdapter mAdapter;
	
	private ListView mUsersList;
	
	private LayoutInflater mInflater;
	
	private static final String TAG = WatchListUsersFragment.class.getSimpleName();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mInflater = inflater;
		View view = inflater.inflate(R.layout.watch_users_layout, null);
		
		List<MemberDto> members = BluetoothService.INSTANCE.getLatestMembers();
		if (members == null) {
			members = new ArrayList<MemberDto>();
			Log.d(TAG, "Received members list with null");
		}
		mAdapter = new UsersAdapter(members);

		mUsersList = (ListView) view.findViewById(R.id.watch_userList);
		mUsersList.setAdapter(mAdapter);
		
		BluetoothService.INSTANCE.setResponseListener(this);

		return view;
	}
	
	@Override
	public void onResponse(List<MemberDto> list) {
		mAdapter = new UsersAdapter(list);
		mUsersList.setAdapter(mAdapter);
		
		Intent intent = new Intent(this.getActivity(), InformMemberLostActivity.class);
		intent.putExtra(InformMemberLostExtras.NAMES_EXTRA, "TESTTTT!!!!!!!");
		intent.putExtra(InformMemberLostExtras.NUMBER_OF_LOSTS_EXTRA, 1);
		
		getActivity().startActivity(intent);
		Log.d(TAG, "Received members list with size " + list.size());
	}
	
	private class UsersAdapter extends BaseAdapter {
		
		private List<MemberDto> mMembers;
		
		public UsersAdapter(List<MemberDto> members) {
			mMembers = members;
		}

		@Override
		public int getCount() {
			return mMembers.size();
		}

		@Override
		public Object getItem(int position) {
			return mMembers.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = mInflater.inflate(R.layout.watch_users_list_item, null);
			ImageView image = (ImageView)view.findViewById(R.id.watchItem_image);
			TextView firstName = (TextView)view.findViewById(R.id.watchItem_firstName);
			TextView lastName = (TextView)view.findViewById(R.id.watchItem_lastName);
			TextView phoneNumber = (TextView)view.findViewById(R.id.watchItem_phoneNumber);
			
			MemberDto member = mMembers.get(position);
			String imageUrl = member.getImageUrl();
			if (imageUrl != null) {				
				image.setImageURI(Uri.parse(imageUrl));
			}
			
//			if (member.getPanic() == "Panic") {				
//				firstName.setTextColor(getActivity().getResources().getColor(android.R.color.holo_red_light));
//				lastName.setTextColor(getActivity().getResources().getColor(android.R.color.holo_red_light));
//				phoneNumber.setTextColor(getActivity().getResources().getColor(android.R.color.holo_red_light));
//			}
			
			firstName.setText(member.getFirstName());
			lastName.setText(member.getLastName());
			phoneNumber.setText(member.getMobileNumber());
			
			return view;
		}
		
	}

}
