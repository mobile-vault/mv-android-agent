package com.toppatch.mv.samsung.components;

import org.json.JSONObject;

import android.app.enterprise.EnterpriseDeviceManager;
import android.content.Context;
import android.util.Log;

public abstract class Component {

	private static final String TAG = "Component";
	Context mContext;
	EnterpriseDeviceManager edm;

	/**
	 * Getting the system service instance makes it faster...
	 * @param context
	 */
	public Component(Context context) {
		//TODO check the fucking permissions here and throw and exception in case the user dont have enough rights...
		mContext = context;
		try{
			edm= (EnterpriseDeviceManager) context.getSystemService(EnterpriseDeviceManager.ENTERPRISE_POLICY_SERVICE);
		}catch(Exception e){
			Log.e(TAG, "Device doesnt have an enterprise device manager. ");
		}
	}

	public abstract void execute(JSONObject data);

	protected EnterpriseDeviceManager getEdm(){
		return edm;
	}

}
