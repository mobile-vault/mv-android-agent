package com.toppatch.mv.samsung.components.policies.application;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.enterprise.Account;
import android.app.enterprise.ApplicationPolicy;
import android.app.enterprise.EmailAccountPolicy;
import android.app.enterprise.EnterpriseDeviceManager;
import android.app.enterprise.RestrictionPolicy;
import android.content.Context;
import android.util.Log;

import com.toppatch.mv.Constants;
import com.toppatch.mv.ReportServer;
import com.toppatch.mv.samsung.components.Component;

public class YoutubeComponent extends Component {

	private static final String TAG = "YoutubeComponent";

	public YoutubeComponent(Context context) {
		super(context);
	}

	@Override
	public void execute(JSONObject data) {
		if(data!=null){
			Log.d(TAG, "Executing "+data.toString());
			try {
				boolean enable = data.getBoolean(Constants.APP_YOUTUBE_ENABLE);
				EnterpriseDeviceManager edm = getEdm();
				if(edm!=null){
					try{
						ApplicationPolicy applicationPolicy = edm.getApplicationPolicy();
						if(!enable) applicationPolicy.disableYouTube();
						else applicationPolicy.enableYouTube();
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
