package com.toppatch.mv.samsung;

import org.json.JSONObject;

import com.toppatch.mv.Constants;
import com.toppatch.mv.samsung.components.DeviceInformationComponent;
import com.toppatch.mv.samsung.components.EraseDeviceComponent;
import com.toppatch.mv.samsung.components.LockDeviceComponent;
import com.toppatch.mv.samsung.components.NotificationComponent;
import com.toppatch.mv.samsung.components.PowerOffComponent;
import com.toppatch.mv.samsung.components.RebootComponent;
import com.toppatch.mv.samsung.components.UnlockDeviceComponent;
import com.toppatch.mv.samsung.components.policies.PolicyControllerComponent;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

public class Controller {
	
	private static final String TAG = "Controller";

	/**
	 * It is the responsibility of the function to extract the data out of bundle and act accordingly. Since java is send by reference,
	 * we don'/t have to  
	 * @param key
	 * @param data
	 */
	public static void execute(Context context,Bundle data){
		
		String action = data.getString(Constants.CONTROLLER_ACTION,null);
		Log.e(TAG, "Got the message from GCM server "+data.toString());
		// Attributes can be null as well. Let individual components handle that.
		String attributes = data.getString(Constants.CONTROLLER_ATTRIBUTES,null);
		Log.d(TAG, "attributes "+attributes);
		JSONObject job=null;
		try{
			job = new JSONObject(attributes);
		}catch(Exception e){
			e.printStackTrace();
		}
		if(Constants.CONTROLLER_ACTION_CLEAR_PASSCODE.equals(action)){
			new UnlockDeviceComponent(context).execute(job);
		}else if(Constants.CONTROLLER_ACTION_DEVICE_LOCK.equals(action)){
			new LockDeviceComponent(context).execute(job);
		}else if(Constants.CONTROLLER_ACTION_ERASE_DEVICE.equals(action)){
			new EraseDeviceComponent(context).execute(job);
		}else if(Constants.CONTROLLER_ACTION_INSTALL_APPLICATION.equals(action)){
			//TODO
			Log.e(TAG, Constants.CONTROLLER_ACTION_INSTALL_APPLICATION+" Not implemented right now");
		}else if(Constants.CONTROLLER_ACTION_INSTALLED_APPLICATION_LIST.equals(action)){
			//TODO
			Log.e(TAG, Constants.CONTROLLER_ACTION_INSTALLED_APPLICATION_LIST+" Not implemented right now");
		}else if(Constants.CONTROLLER_ACTION_POLICY.equals(action)){
			new PolicyControllerComponent(context).execute(job);
		}else if(Constants.CONTROLLER_ACTION_REMOVE_APPLICATION.equals(action)){
			//TODO
			Log.e(TAG, Constants.CONTROLLER_ACTION_INSTALLED_APPLICATION_LIST+" Not implemented right now");
		}else if(Constants.CONTROLLER_ACTION_NOTIFICATION.equals(action)){
			new NotificationComponent(context).execute(job);
		}else if(Constants.CONTROLLER_ACTION_POWER_OFF.equals(action)){
			Log.i("see","s:"+job+"sct:"+action);
			new PowerOffComponent(context).execute(job);
		}else if(Constants.CONTROLLER_ACTION_REBOOT.equals(action)){
			Log.i("see","s:"+job+"sct:"+action);
			new RebootComponent(context).execute(job);
		}else if(Constants.CONTROLLER_ACTION_DEVICE_INFO.equals(action)){
			new DeviceInformationComponent(context).execute(job);
		}
	}
}