package com.toppatch.mv.samsung;


import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.toppatch.mv.gcm.GCMActivity;
import com.toppatch.mv.helper.AdminHelper;
import com.toppatch.mv.helper.WebHelper;
import com.toppatch.mv.ui.activities.MainActivity;

public class TopPatchAdmin extends DeviceAdminReceiver {
	private static final String TAG = "TopPatchAdmin";
	Context con;
	void showToast(Context context, CharSequence msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	private void sendToGCMRegistrationActivity(Context context){
		Intent gcm = new Intent(context,GCMActivity.class);
		SharedPreferences pref = context.getSharedPreferences("temp", Context.MODE_PRIVATE);
		gcm.putExtra("email", pref.getString("email", null));
		gcm.putExtra("passcode", pref.getString("passcode", null));
		gcm.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(gcm);
	}
	
	/**
	 * This link takes you to {@code MainActivity}
	 */
	@Override
	public void onEnabled(Context context, Intent intent) {
		Toast.makeText(context, "MobileVault and Your Company. Best Friends Forever.", Toast.LENGTH_LONG).show();
		
		//Now open the dialog to access the license. 
		//Registration.register(context);
		Intent main = new Intent(context.getApplicationContext(),MainActivity.class);
		main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(main);
		//sendToGCMRegistrationActivity(context);
	}

	public CharSequence onDisableRequested(Context context, Intent intent) {
		return "Are you serious? Last time someone disabled it he was sent on a 2 week trip to Hawaii. To play with snakes.";
	}
    
	@Override
	public void onDisabled(Context context, Intent intent) {
		//Send a violation to the server...
		showToast(context, "I'll tell your mom. You're a bad guy!.");
		AdminHelper.setUserLoggedOut(context);
		con=context;
		new Thread(new Runnable() {
			@Override
			public void run() {
				int response=WebHelper.putRequestVoilate(con);
			}
			
		}).start();
		AdminHelper.setSamsungESDKEnabled(context,false);
	}
	@Override
	public void onPasswordChanged(Context context, Intent intent) {
		showToast(context, "You changed the password. Now I will delete all you data.");
	}
	@Override
	public void onPasswordFailed(Context context, Intent intent) {
		showToast(context, "Mistakes are to be learnt from. Learn, and try agin.");
	}
	@Override
	public void onPasswordSucceeded(Context context, Intent intent) {
		showToast(context, "Welcome. This device is under NSA supervision");
	}
}
