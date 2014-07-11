package com.toppatch.mv.helper;

import com.toppatch.mv.gcm.GCMActivity;
import com.toppatch.mv.samsung.TopPatchAdmin;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class AdminHelper {

	private static final String PREF_NAME = "toppatch";
	private static final String SAMSUNG_ESDK = "samsung_esdk";
	private static final String USER_LOGGED_IN = "user_logged_in";
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String TAG = "AdminHelper";

	public static boolean isAdminActive(Context context){
		DevicePolicyManager mDPM = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
		ComponentName mDeviceAdmin = new ComponentName(context, TopPatchAdmin.class);
		if(mDPM!=null && mDPM.isAdminActive(mDeviceAdmin)){
			return true;
		}else{
			return false;
		}
	}

	public static boolean isSamsungESDKEnabled(Context context){
		SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		Log.i("see", "sp:"+sharedPreferences.contains(SAMSUNG_ESDK)+"value:"+sharedPreferences.getBoolean(SAMSUNG_ESDK, false));
		return sharedPreferences.getBoolean(SAMSUNG_ESDK, false);
	}

	public static void setSamsungESDKEnabled(Context context,Boolean value){
		Editor edit = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
		edit.putBoolean(SAMSUNG_ESDK, value);
		edit.commit();
	}
	
	public static boolean isUserLoggedIn(Context context){
		SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		return sharedPreferences.getBoolean(USER_LOGGED_IN, false);
	}
	
	public static void setUserLoggedIn(Context context){
		Editor edit = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
		edit.putBoolean(USER_LOGGED_IN, true);
		edit.commit();
	}
	public static void setUserLoggedOut(Context context){
		Editor edit = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
		edit.putBoolean(USER_LOGGED_IN,false);
		edit.commit();
	}
	// SBIN0001067 RTGS
	// 
	
	public static boolean isGCMRegistered(Context context){
		SharedPreferences sharedPreferences = context.getSharedPreferences(GCMActivity.class.getSimpleName(),
				Context.MODE_PRIVATE);
		String registrationId=sharedPreferences.getString(PROPERTY_REG_ID, null);
		Log.i(TAG, "Registration id found."+registrationId);
		if (registrationId==null) {
			Log.i(TAG, "Registration not found.");
			return false;
		}else{
			return true;
		}
	}
	
	public static String getGCMId(Context context){
		SharedPreferences sharedPreferences = context.getSharedPreferences(GCMActivity.class.getSimpleName(),
				Context.MODE_PRIVATE);
		String registrationId=sharedPreferences.getString(PROPERTY_REG_ID, null);
		return registrationId;
	}
	
	
}
