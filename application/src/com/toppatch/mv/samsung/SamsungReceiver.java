package com.toppatch.mv.samsung;

import com.toppatch.mv.Memory;
import com.toppatch.mv.helper.AdminHelper;
import com.toppatch.mv.ui.activities.MainActivity;

import android.app.enterprise.license.EnterpriseLicenseManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

public class SamsungReceiver extends BroadcastReceiver{

	private static final String TAG = "SamsungReceiver";
	private  int count =0;

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, "Samsung E SDK licence Receiver");
		try{
			String status= intent.getExtras().getString(EnterpriseLicenseManager.EXTRA_LICENSE_STATUS);
			Log.i("see","reciever:"+status);
			if(status.equals("success")){
				AdminHelper.setSamsungESDKEnabled(context,true);
				//For security, reset the license key
				Memory.resetLicenceKey();
				Intent intentone = new Intent(context.getApplicationContext(), MainActivity.class);
				intentone.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intentone);
				//context.unregisterReceiver(this);
			}else{
				count++;
				Toast.makeText(context, "You need to accept the licence to continue", Toast.LENGTH_LONG).show();
				if(count<=1) {
					Registration.register(context);
				}else{
					Intent intentone = new Intent(context.getApplicationContext(), MainActivity.class);
					intentone.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					intentone.putExtra("fromReciever",true);
					context.startActivity(intentone);
				}
			    
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}