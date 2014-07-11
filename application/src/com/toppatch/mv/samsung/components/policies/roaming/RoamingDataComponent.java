package com.toppatch.mv.samsung.components.policies.roaming;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.enterprise.EnterpriseDeviceManager;
import android.app.enterprise.RestrictionPolicy;
import android.app.enterprise.RoamingPolicy;
import android.content.Context;
import android.util.Log;

import com.toppatch.mv.Constants;
import com.toppatch.mv.ReportServer;
import com.toppatch.mv.samsung.components.Component;

public class RoamingDataComponent extends Component {

	private static final String TAG = "RoamingDataComponent";

	public RoamingDataComponent(Context context) {
		super(context);
	}

	@Override
	public void execute(JSONObject data) {
		if(data!=null){
			Log.d(TAG, "Executing "+data.toString());
			try {
				boolean enable = data.getBoolean(Constants.ROAMING_DATA_ENABLE);
				EnterpriseDeviceManager edm = getEdm();
				if(edm!=null){
					try{
						RoamingPolicy roamingPolicy = edm.getRoamingPolicy();
						roamingPolicy.setRoamingData(enable);
					}catch(Exception e){
						ReportServer.e(TAG+data.toString()+e.toString());
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

}
