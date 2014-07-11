package com.toppatch.mv.samsung;

import com.toppatch.mv.Memory;
import com.toppatch.mv.helper.AdminHelper;
import com.toppatch.mv.ui.activities.LoginActivity2;

import android.app.enterprise.DeviceInventory;
import android.app.enterprise.EnterpriseDeviceManager;
import android.app.enterprise.license.EnterpriseLicenseManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender.SendIntentException;
import android.sax.StartElementListener;
import android.util.Log;

public class Registration {
	private static final String TAG = "com.toppatch.mv.samsung.Registration";
	
	public static boolean register(Context context){
		String key = Memory.getLicenceKey();
		EnterpriseLicenseManager mLicenceManager = EnterpriseLicenseManager.getInstance(context);
		Log.i("see", key);
		if(key==null)
		{
		 return false;
		}
		mLicenceManager.activateLicense(key);
		return true;
	}
	
	public static boolean arePermissionsGranted(Context context){
		EnterpriseDeviceManager edm = new EnterpriseDeviceManager(context);
		DeviceInventory deviceInventory = edm.getDeviceInventory();
		String deviceName = deviceInventory.getDeviceName();
		if (deviceName!=null){
			Log.i(TAG, "Device name is "+deviceName);
		}
		return true;
	}
	
	
}