package com.mobilet.buxferMobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;

public class Main extends Activity {

	WebView webview;
	public static final String TAG = "BuxferWebMobile.Main";
	public static final String PREFS_NAME = "BuxferWebMobile";
	public static final String PREFS_NAME_VERSION = "BuxferWebMobileVersion";

	private static final int ABOUT_BUXFER_ITEM = 1;
	private static final int ABOUT_BUXFER_WEBMOBILE_ITEM = 2;
	private static final int CHANGELOG_ITEM = 3;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// evaluate if we will show changelog
		try {
			// current version
			PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			int versionCode = packageInfo.versionCode;

			// version where changelog has been viewed
			SharedPreferences settings = getSharedPreferences(Main.PREFS_NAME, 0);
			int viewedChangelogVersion = settings.getInt(Main.PREFS_NAME_VERSION, 0);

			if (viewedChangelogVersion < versionCode) {
				Editor editor = settings.edit();
				editor.putInt(Main.PREFS_NAME_VERSION, versionCode);
				editor.commit();
				displayChangeLog();
			}
		} catch (NameNotFoundException e) {
			Log.w(TAG, "Unable to get version code. Will not show changelog", e);
		}

		webview = (WebView) findViewById(R.id.webview); // carrega a view
		webview.getSettings().setJavaScriptEnabled(true); // ativa o javascript

		webview.setWebViewClient(new MyWebViewClient()); // override URL
		webview.loadUrl(getString(R.string.url)); // carrega html

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater mInfl = getMenuInflater();
		mInfl.inflate(R.menu.menu, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.about_menuitem:
			
			break;

		case R.id.about_myapp_menuitem:
			break;

		case R.id.changelog_menuitem:
			displayChangeLog();
			break;

		default:
			Log.e(TAG, "Error selecting MenuItem");
			break;

		}
		return true;
	}

	private void displayChangeLog() {
		// load some kind of a view
		LayoutInflater li = LayoutInflater.from(this);
		View view = li.inflate(R.layout.changelog, null);

		new AlertDialog.Builder(this).setTitle("Changelog")
				.setIcon(android.R.drawable.ic_menu_info_details).setView(view)
				.setNegativeButton("Close", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						//
					}
				}).show();
	}

	/**
	 * Override to redirect the back button
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
			webview.goBack();
			Log.i(TAG, "backing to previous page");
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * Class to redirect the url to the webview
	 * 
	 * @author edumontandon
	 * 
	 */
	private class MyWebViewClient extends android.webkit.WebViewClient {

		public boolean shouldOverrideUrlLoading(WebView view, String url) {

			// verify if the user is on the site domain.
			// if not, use the default browser of the phone
			if (Uri.parse(url).getHost().equals("www.buxfer.com")) {

				view.loadUrl(url);
				Log.w(TAG, "Inside Buxfer domain");

				return false;
			} else {
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				startActivity(intent);

				Log.w(TAG, "Outside Buxfer domain");
				return true;
			}
		}

		/**
		 * Method to handle security problems, certificate in this case
		 */
		public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
			handler.proceed();
		}

	}
}
