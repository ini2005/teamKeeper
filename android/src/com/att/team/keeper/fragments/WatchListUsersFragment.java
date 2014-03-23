package com.att.team.keeper.fragments;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.att.team.keeper.R;
import com.att.team.keeper.TeamKeeperApplication;
import com.att.team.keeper.activities.InformMemberLostActivity;
import com.att.team.keeper.activities.InformMemberLostActivity.InformMemberLostExtras;
import com.att.team.keeper.activities.MobileLostPanicActivity;
import com.att.team.keeper.dtos.LastSeenByEntry;
import com.att.team.keeper.dtos.MemberDto;
import com.att.team.keeper.services.BluetoothService;
import com.att.team.keeper.services.BluetoothService.IResponseListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class WatchListUsersFragment extends Fragment implements
		IResponseListener {

	protected ImageLoader imageLoader = ImageLoader.getInstance();
	AnimateFirstDisplayListener animateFirstListener = new AnimateFirstDisplayListener();
	DisplayImageOptions options;

	private UsersAdapter mAdapter;

	private ListView mUsersList;

	private LayoutInflater mInflater;

	private static final String TAG = WatchListUsersFragment.class
			.getSimpleName();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true)
				.cacheOnDisc(true).considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(20)).build();

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

		if (this.getActivity() == null) {
			return;
		}

		if (TeamKeeperApplication.isPanicAlertOn == true) {
			return;

		}

		for (MemberDto memberDto : list) {
			if (TextUtils.isEmpty(memberDto.getPanic()) == false) {

				if (BluetoothService.INSTANCE.getBluetoothMacAddress()
						.equalsIgnoreCase(memberDto.getBluetoothMac()) == true) {
					Log.d(TAG, "i am lost :(");
					Intent intent = new Intent(this.getActivity(),
							MobileLostPanicActivity.class);
					intent.putExtra(
							InformMemberLostExtras.NAMES_EXTRA,
							memberDto.getFirstName() + " "
									+ memberDto.getLastName());
					intent.putExtra(
							InformMemberLostExtras.NUMBER_OF_LOSTS_EXTRA, 1);

					List<LastSeenByEntry> lastSeenByEntries = memberDto
							.getLastSeenBy();

					String lastSeenByString = "";
					if (lastSeenByEntries != null) {
						for (LastSeenByEntry lastSeenByEntry : lastSeenByEntries) {
							lastSeenByString += lastSeenByEntry.getName() + " "
									+ lastSeenByEntry.getLastName() + ", ";
						}
					}
					
					intent.putExtra(InformMemberLostExtras.LAST_SEEN_BY_EXTRA,
							lastSeenByString);

					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					getActivity().startActivity(intent);
				} else {
					Log.d(TAG, "team member lost :(");
					Intent intent = new Intent(this.getActivity(),
							InformMemberLostActivity.class);
					intent.putExtra(
							InformMemberLostExtras.NAMES_EXTRA,
							memberDto.getFirstName() + " "
									+ memberDto.getLastName());
					intent.putExtra(
							InformMemberLostExtras.NUMBER_OF_LOSTS_EXTRA, 1);

					List<LastSeenByEntry> lastSeenByEntries = memberDto
							.getLastSeenBy();
					String lastSeenByString = "";
					for (LastSeenByEntry lastSeenByEntry : lastSeenByEntries) {
						lastSeenByString += lastSeenByEntry.getName() + " "
								+ lastSeenByEntry.getLastName() + ", ";
					}
					intent.putExtra(InformMemberLostExtras.LAST_SEEN_BY_EXTRA,
							lastSeenByString);

					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					getActivity().startActivity(intent);

				}

				break;

			}
		}

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
			ImageView image = (ImageView) view
					.findViewById(R.id.watchItem_image);
			TextView firstName = (TextView) view
					.findViewById(R.id.watchItem_firstName);
			TextView lastName = (TextView) view
					.findViewById(R.id.watchItem_lastName);
			TextView phoneNumber = (TextView) view
					.findViewById(R.id.watchItem_phoneNumber);

			MemberDto member = mMembers.get(position);
			String imageUrl = member.getImageUrl();
			if (imageUrl != null) {
				// show The Image
				imageLoader.displayImage(imageUrl, image, options,
						animateFirstListener);

			}

			if (member.getPanic() != null && member.getPanic().length() > 0) {
				firstName.setTextColor(getActivity().getResources().getColor(
						android.R.color.holo_red_light));
				lastName.setTextColor(getActivity().getResources().getColor(
						android.R.color.holo_red_light));
				phoneNumber.setTextColor(getActivity().getResources().getColor(
						android.R.color.holo_red_light));
			}

			firstName.setText(member.getFirstName());
			lastName.setText(member.getLastName());
			phoneNumber.setText(member.getMobileNumber());

			return view;
		}

	}

	private static class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
		}
	}

}
