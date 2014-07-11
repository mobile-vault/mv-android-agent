package com.toppatch.mv.gcm;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.toppatch.mv.Config;

public class GCMActivity extends Activity {

	private static final String TAG = "GCMActvity";
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	String SENDER_ID = Config.GCM_SENDER_ID;
	static final int RESULT_ENABLE = 1;

	TextView mDisplay;
	GoogleCloudMessaging gcm;
	AtomicInteger msgId = new AtomicInteger();
	SharedPreferences prefs;
	Context context=GCMActivity.this;
	String regid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "GCMActivity");
		super.onCreate(savedInstanceState);
		if(checkPlayServices()){
			gcm = GoogleCloudMessaging.getInstance(this);
			regid = getRegistrationId(context);
			if (regid==null) {
				registerInBackground();
			}
		}else {
			Log.i(TAG, "No valid Google Play Services APK found.");
		}
		Log.i("see", "No v:"+regid);
	}

	@Override
	protected void onResume() {
		super.onResume();
		checkPlayServices();
	}

	/**
	 * Check the device to make sure it has the Google Play Services APK. If
	 * it doesn't, display a dialog that allows users to download the APK from
	 * the Google Play Store or enable it in the device's system settings.
	 */ 
	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.i(TAG, "This device is not supported.");
				finish();
			}
			return false;
		}
		return true;
	}


	/**
	 * Gets the current registration ID for application on GCM service.
	 * <p>
	 * If result is empty, the app needs to register.
	 *
	 * @return registration ID, or empty string if there is no existing
	 *         registration ID.
	 */
	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getGCMPreferences(context);
		String registrationId = prefs.getString(PROPERTY_REG_ID, null);
		if (registrationId==null) {
			Log.i(TAG, "Registration not found.");
			return null;
		}
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i(TAG, "App version changed.");
			return null;
		}
		Log.e(TAG,"Registration id= "+registrationId);
		return registrationId;
	}

	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	private SharedPreferences getGCMPreferences(Context context) {
		// This sample app persists the registration ID in shared preferences, but
		// how you store the regID in your app is up to you.
		return getSharedPreferences(GCMActivity.class.getSimpleName(),
				Context.MODE_PRIVATE);
	}

	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}


	/**
	 * Registers the application with GCM servers asynchronously.
	 * <p>
	 * Stores the registration ID and app versionCode in the application's
	 * shared preferences.
	 */
	private void registerInBackground() {
		new AsyncTask<Void,Void,String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(context);
					}
					regid = gcm.register(SENDER_ID);
					msg = "Device registered, registration ID=" + regid;

					// You should send the registration ID to your server over HTTP,
					// so it can use GCM/HTTP or CCS to send messages to your app.
					// The request to your server should be authenticated if your app
					// is using accounts.
					sendRegistrationIdToBackend(regid);

					// Persist the regID - no need to register again.
					storeRegistrationId(context, regid);

					//Store the gcm id on server and see the response....
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
					// If there is an error, don't just keep trying to register.
					// Require the user to click a button again, or perform
					// exponential back-off.
				} catch (Exception e) {
					e.printStackTrace();
				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				if(msg!=null){
					Log.e(TAG, msg);
					setResult(RESULT_OK);
				}else{
					Log.e(TAG,"Could not register");
					setResult(RESULT_CANCELED);
				}
				finish();
			}
		}.execute(null, null, null);
	}
	/**
	 * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP
	 * or CCS to send messages to your app. Not needed for this demo since the
	 * device sends upstream messages to a server that echoes back the message
	 * using the 'from' address in the message.
	 */
	private void sendRegistrationIdToBackend(String id) {
		//Send the registration id together with email and passcode to server to update... After this point, the email passcode combination expires
		//ArrayList<NameValuePair> params= new ArrayList<NameValuePair>();
		//params.add(new BasicNameValuePair("email", email));
		//params.add(new BasicNameValuePair("passcode", passcode));
		//params.add(new BasicNameValuePair("gcm", id));		
		//String response = WebHelper.sendPostRequest(Config.GCM_REG_URL, params);
		//try {
		//	JSONObject job = new JSONObject(response);
		//	boolean pass = job.optBoolean("pass");
		//	if(pass){
		//		Log.e(TAG,"Successful registration");
//				Toast.makeText(getApplicationContext(), "Successful registration", Toast.LENGTH_LONG).show();
		//	}else{
		//		Log.e(TAG,"registration failed");
//				Toast.makeText(getApplicationContext(), "Failed to register on server. Try again in a while...", Toast.LENGTH_LONG).show();
		//	}
		//} catch (JSONException e) {
		//	e.printStackTrace();
		//}
	}

	/**
	 * Stores the registration ID and app versionCode in the application's
	 * {@code SharedPreferences}.
	 *
	 * @param context application's context.
	 * @param regId registration ID
	 */
	private void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getGCMPreferences(context);
		int appVersion = getAppVersion(context);
		Log.i(TAG, "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, regId);
		editor.putInt(PROPERTY_APP_VERSION, appVersion);
		editor.commit();
	}
}
