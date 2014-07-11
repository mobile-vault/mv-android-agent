package com.toppatch.mv.samsung.components.policies.browser;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.enterprise.BrowserPolicy;
import android.app.enterprise.EnterpriseDeviceManager;
import android.content.Context;
import android.util.Log;

import com.toppatch.mv.Constants;
import com.toppatch.mv.ReportServer;
import com.toppatch.mv.samsung.components.Component;

public class PopupComponent extends Component {

	private static final String TAG = "PopupComponent";

	public PopupComponent(Context context) {
		super(context);
	}

	@Override
	public void execute(JSONObject data) {
		if(data!=null){
			Log.d(TAG, "Executing "+data.toString());
			try {
				boolean enable = data.getBoolean(Constants.BROWSER_POPUP_ENABLE);
				EnterpriseDeviceManager edm = getEdm();
				if(edm!=null){
					try{
						BrowserPolicy browserPolicy = edm.getBrowserPolicy();
						browserPolicy.setPopupsSetting(enable);
					}catch(Exception e){
						ReportServer.e(TAG+data.toString()+e.toString());
					}
				}
			} catch (JSONException e) {
				//If there is no Constants.ANDROID_BROWSER_ENABLE in the json
				e.printStackTrace();
			}
		}
	}

}
