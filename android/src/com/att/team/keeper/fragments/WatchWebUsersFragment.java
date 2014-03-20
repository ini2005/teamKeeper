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

public class WatchWebUsersFragment extends Fragment {
	
	private static final String TAG = WatchWebUsersFragment.class.getSimpleName();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.watch_web_layout, null);

		WebView usersWebView = (WebView) view.findViewById(R.id.watch_usersWeb);
		usersWebView.setWebViewClient(new InlineWebViewClient());
		usersWebView.loadUrl("http://www.google.com");

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
