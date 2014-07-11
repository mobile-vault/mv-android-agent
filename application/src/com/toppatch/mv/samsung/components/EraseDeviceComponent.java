package com.toppatch.mv.samsung.components;

import org.json.JSONObject;

import com.toppatch.mv.helper.WebHelper;

import android.app.enterprise.EnterpriseDeviceManager;
import android.app.enterprise.SecurityPolicy;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


public class EraseDeviceComponent extends Component {
   Context con;
   private static final String TAG = "EraseDeviceComponent";
   private int response=0;
   private SecurityPolicy securityPolicy ;
	public EraseDeviceComponent(Context context) {
		super(context);
		con=context;
	}

	/**
	 * Selectively wipes external memory, internal memory, or both
	 */
	@Override
	public void execute(JSONObject data) {
		Log.e(TAG, "Found the fucking class");
		edm = getEdm();
		securityPolicy= edm.getSecurityPolicy();
		new Thread(new Runnable() {
			@Override
			public void run() {
				response=WebHelper.putRequestVoilate(con);
				Log.i(TAG,"response:"+response);
			if(securityPolicy!=null && response==200){
			    securityPolicy.wipeDevice(SecurityPolicy.WIPE_INTERNAL_EXTERNAL_MEMORY);
    			}
			}
		}).start();
	}
}