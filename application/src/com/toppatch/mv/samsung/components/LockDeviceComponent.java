package com.toppatch.mv.samsung.components;

import org.json.JSONObject;

import com.toppatch.mv.Constants;

import android.app.enterprise.EnterpriseDeviceManager;
import android.content.Context;
import android.util.Log;

public class LockDeviceComponent extends Component {

	
	private static final String TAG = "LockDeviceComponent";

	public LockDeviceComponent(Context context) {
		super(context);
	}

	@Override
	public void execute(JSONObject data) {
		//TODO
		Log.d(TAG, "Executing "+data);
		String passcode = data.optString(Constants.LOCK_DEVICE_NEW_PASSCODE,null);
		EnterpriseDeviceManager edm = getEdm();
		if(edm!=null && passcode!=null){
			try{
				edm.getSecurityPolicy().lockoutDevice(passcode, "Yo Man!!", null);
			}catch(SecurityException e){
				e.printStackTrace();
			}
		}
	}
}