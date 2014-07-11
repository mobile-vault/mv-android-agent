package com.toppatch.mv.samsung.components.policies.application;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.enterprise.ApplicationPolicy;
import android.app.enterprise.EnterpriseDeviceManager;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.toppatch.mv.Constants;
import com.toppatch.mv.samsung.components.Component;

public class UninstallApplicationComponent extends Component {

	private static final String TAG = "UninstallApplicationComponent";
	private Context mContext;
	public UninstallApplicationComponent(Context context) {
		super(context);
		mContext=context;
	}

	@Override
	public void execute(JSONObject data) {
		if(data!=null){
			Log.d(TAG, "Executing "+data.toString());
			String uninstallList = data.optString(Constants.APP_ANDROID_UNINSTALL);
			try {
				JSONArray apps = new JSONArray(uninstallList);
				if(apps!=null){
					EnterpriseDeviceManager edm2 = getEdm();
					if(edm2!=null){
						ApplicationPolicy applicationPolicy = edm2.getApplicationPolicy();
						
						for(int i=0;i<apps.length();i++){
							JSONObject app = new JSONObject(apps.getString(i));
									
							applicationPolicy.setApplicationUninstallationEnabled(app.optString("id",null));
							if(applicationPolicy.uninstallApplication(app.optString("id",null), false)){
								Log.i(TAG, "Successful uninstall "+apps.getString(i));
							}else{
								Log.e(TAG, "Failed to uninstall "+apps.getString(i));
//								Toast.makeText(mContext, "Failed uninstall of "+apps.getString(i), Toast.LENGTH_LONG).show();
							}
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

}
