package com.att.team.keeper.fragments;

import java.util.ArrayList;
import java.util.List;

import com.att.team.keeper.R;
import com.att.team.keeper.dtos.MemberDto;
import com.att.team.keeper.services.BluetoothService;
import com.att.team.keeper.services.BluetoothService.IResponseListener;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class WatchUsersFragment extends Fragment implements IResponseListener {
	
	private UsersAdapter mAdapter;
	
	private ListView mUsersList;
	
	private static final String TAG = WatchUsersFragment.class.getSimpleName();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.watch_layout, null);

		WebView usersWebView = (WebView) view.findViewById(R.id.watch_usersWeb);
		usersWebView.setWebViewClient(new InlineWebViewClient());
		usersWebView.loadUrl("http://www.google.com");
		
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

	private class InlineWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}
	
	@Override
	public void onResponse(List<MemberDto> list) {
		mAdapter = new UsersAdapter(list);
		mUsersList.setAdapter(mAdapter);
		
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
			ImageView image= (ImageView)convertView.findViewById(R.id.watchItem_image);
			TextView firstName = (TextView)convertView.findViewById(R.id.watchItem_firstName);
			TextView lastName = (TextView)convertView.findViewById(R.id.watchItem_lastName);
			TextView phoneNumber = (TextView)convertView.findViewById(R.id.watchItem_phoneNumber);
			
			MemberDto member = mMembers.get(position);
			image.setImageURI(Uri.parse(member.getImageUrl()));
			firstName.setText(member.getFirstName());
			lastName.setText(member.getLastName());
			phoneNumber.setText(member.getMobileNumber());
			return null;
		}
		
	}

}
