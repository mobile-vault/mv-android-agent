package com.toppatch.mv.samsung.components.policies.application;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.enterprise.ApplicationPolicy;
import android.app.enterprise.EnterpriseDeviceManager;
import android.content.Context;
import android.util.Log;

import com.toppatch.mv.Constants;
import com.toppatch.mv.samsung.components.Component;

public class BlackListApplicationComponent extends Component{

	private static final String TAG = "BlackListApplicationComponent";

	public BlackListApplicationComponent(Context context) {
		super(context);
	}

	@Override
	public void execute(JSONObject data) {
		if(data!=null){
			String blackListString = data.optString(Constants.APP_ANDROID_BLACK_LIST);
			try {
				JSONArray jar = new JSONArray(blackListString);
				EnterpriseDeviceManager edm2 = getEdm();
				if(jar!=null && edm2!=null){
					ApplicationPolicy applicationPolicy = edm2.getApplicationPolicy();
					for(int i=0;i<jar.length();i++){
						JSONObject job = new JSONObject(jar.getString(i));
						applicationPolicy.setApplicationInstallationDisabled(job.optString("id"));
						if(applicationPolicy.addAppPackageNameToBlackList(job.optString("id"))){
							Log.i(TAG, "Successfully added "+jar.getString(i)+" to blacklist");
						}else{
							Log.e(TAG,"Failed to add "+jar.getString(i)+" to blacklist");
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

}
