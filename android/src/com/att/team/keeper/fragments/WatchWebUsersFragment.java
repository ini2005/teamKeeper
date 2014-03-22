package com.att.team.keeper.fragments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.att.team.keeper.R;
import com.att.team.keeper.fragments.WatchWebUsersFragment.MyHandler;
import com.att.team.keeper.services.BluetoothService;

public class WatchWebUsersFragment extends Fragment {

	private static final String TAG = WatchWebUsersFragment.class
			.getSimpleName();

	private MyHandler mHandler;
	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.watch_web_layout, null);

		mHandler = new MyHandler();

		mHandler.sendEmptyMessage(0);

		return view;
	}

	public class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {

			SharedPreferences sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(getActivity()
							.getApplicationContext());
			String mac = sharedPreferences.getString(
					BluetoothService.KEY_MAC_ADDRESS, null);
			WebView usersWebView = (WebView) view
					.findViewById(R.id.watch_usersWeb);

			WebSettings webSettings = usersWebView.getSettings();
			webSettings.setJavaScriptEnabled(true);

			usersWebView.setWebViewClient(new InlineWebViewClient());
			String url = "http://75.55.104.202:8080/team/dashboard/index2.html?mac="
					+ mac;
			usersWebView.loadUrl(url);

			mHandler.sendEmptyMessageDelayed(0, 5000);

			Log.d(TAG, "url = " + url);

		}
	}

	@Override
	public void onDestroyView() {
		mHandler.removeCallbacksAndMessages(null);
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		mHandler.removeCallbacksAndMessages(null);
		super.onDestroy();
	}
	
	private class InlineWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}

}
