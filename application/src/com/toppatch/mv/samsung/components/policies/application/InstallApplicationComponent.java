package com.toppatch.mv.samsung.components.policies.application;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.util.Log;

import com.toppatch.mv.Constants;
import com.toppatch.mv.samsung.components.Component;

public class InstallApplicationComponent extends Component{

	private static final String TAG = "InstallApplicationComponent";
	private Context mContext;
	public InstallApplicationComponent(Context context) {
		super(context);
		mContext = context;
	}

	@Override
	public void execute(JSONObject data){ 
	/*	 String testAppInternalMemoryPath = Environment.getExternalStorageDirectory().getPath()+"/Download/Peace TV.apk";
		 Log.d(TAG, "Installing an application package has been successful!"+testAppInternalMemoryPath);
		 EnterpriseDeviceManager edm = (EnterpriseDeviceManager) mContext.getSystemService
		      (EnterpriseDeviceManager.ENTERPRISE_POLICY_SERVICE);
		 ApplicationPolicy appPolicy = edm.getApplicationPolicy();
		 try {
		     boolean result = appPolicy.installApplication(testAppInternalMemoryPath,
		                      false);
		     if (true == result) {
		         Log.d(TAG, "Installing an application package has been successful!");
		     } else {
		         Log.w(TAG, "Installing an application package has failed.");
		     }

		 } catch (SecurityException e) {
		     Log.w(TAG, "SecurityException: " + e);
		 }*/
		if(data!=null){
			String packages= data.optString(Constants.APP_ANDROID_INSTALL,null);
			if(packages!=null){
				try{
					
					JSONArray apps = new JSONArray(packages);
					Log.d(TAG, "Executing "+data.toString());
					if(apps!=null){
						for(int i=0;i<apps.length();i++){
							try {
								if(!isPackageInstalled(apps.getJSONObject(i).getString("id"), mContext)){
									
									try {
										Intent intent = new Intent();
										intent.setAction(Intent.ACTION_VIEW);
										intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
										intent.setData(Uri.parse("market://details?id=" + apps.getJSONObject(i).getString("id")));
										mContext.startActivity(intent);
									} catch (android.content.ActivityNotFoundException anfe) {
										Intent intent = new Intent();
										intent.setAction(Intent.ACTION_VIEW);
										intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
										intent.setData(Uri.parse("http://play.google.com/store/apps/details?id=" + apps.getJSONObject(i).getString("id")));
										mContext.startActivity(intent);
									}
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}
				}catch(JSONException e){
					e.printStackTrace();
				}
			}
		}
	}

	private boolean isPackageInstalled(String packagename, Context context) {
		PackageManager pm = context.getPackageManager();
		try {
			pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
			return true;
		} catch (NameNotFoundException e) {
			return false;
		}
	}


}
