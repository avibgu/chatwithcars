package com.appspot.chatwithcars;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.gcm.GCMRegistrar;

public class MainActivity extends Activity {

	public static final String APP_URL = "http://chatwithcars.appspot.com";
	private static final String SENDER_ID = "208496254463";

	private WebView myWebView;
	private String regId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		myWebView = (WebView) findViewById(R.id.webview);

		myWebView.loadUrl(APP_URL);

		WebSettings webSettings = myWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);

		myWebView.addJavascriptInterface(new WebAppInterface(this), "Android");

		myWebView.setWebViewClient(new MyWebViewClient());
		myWebView.setWebViewClient(new WebViewClient());
		
		// GCM
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);	//TODO: can be removed when everything is working
		
		regId = GCMRegistrar.getRegistrationId(this);
		
//		if (regId.equals(""))	TODO
		  GCMRegistrar.register(this, SENDER_ID);

//		else
//		  ;	// Already registered
		
	}

	private class MyWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {

			if (Uri.parse(url).getHost().equals(APP_URL)) {
				// This is my web site, so do not override; let my WebView load
				// the page
				return false;
			}

			// Otherwise, the link is not for a page on my site, so launch
			// another Activity that handles URLs
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			startActivity(intent);

			return true;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		// Check if the key event was the Back button and if there's history
		if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
			myWebView.goBack();
			return true;
		}

		// If it wasn't the Back key or there's no web page history, bubble up
		// to the default
		// system behavior (probably exit the activity)
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
