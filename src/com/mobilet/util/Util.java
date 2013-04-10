/**
 * 
 */
package com.mobilet.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.mobilet.buxferMobile.Main;

/**
 * @author edumontandon Classe com métodos transversais aos sistemas
 * 
 */
public class Util {

	public static String TAG = "com.mobilet.util.Util";

	public static boolean isOnline(Context context) {

		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo netInf = cm.getActiveNetworkInfo();
		
		if(netInf == null || !netInf.isConnectedOrConnecting()) return false;
		else return true;

	}

}
