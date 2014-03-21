package com.att.team.keeper.fragments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.att.team.keeper.R;
import com.att.team.keeper.services.BluetoothService;

public class WatchWebUsersFragment extends Fragment {
	
	private static final String TAG = WatchWebUsersFragment.class.getSimpleName();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.watch_web_layout, null);

		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
		String mac = sharedPreferences.getString(BluetoothService.KEY_MAC_ADDRESS, null);
		WebView usersWebView = (WebView) view.findViewById(R.id.watch_usersWeb);
		usersWebView.setWebViewClient(new InlineWebViewClient());
		usersWebView.loadUrl("http://10.0.0.3:8080/team/dashboard/index2.html?mac=" + mac);

		return view;
	}

	private class InlineWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}

}
