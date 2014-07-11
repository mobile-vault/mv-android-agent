package com.toppatch.mv.samsung.components.policies.access;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.enterprise.EnterpriseDeviceManager;
import android.content.Context;
import android.util.Log;

import com.toppatch.mv.Constants;
import com.toppatch.mv.samsung.components.Component;

public class AllowRemoveAdminComponent extends Component {

	private static final String TAG = "ChangeAdminComponent";

	public AllowRemoveAdminComponent(Context context) {
		super(context);
	}

	@Override
	public void execute(JSONObject data) {
		if(data!=null){
			Log.d(TAG, "executing "+data.toString());
			try {
				boolean changeSettingsEnable = data.getBoolean(Constants.ACCESS_CHANGE_ADMIN_ENABLE);
				EnterpriseDeviceManager edm2 = getEdm();
				if(edm2!=null){
					edm2.setAdminRemovable(changeSettingsEnable);
				}
			} catch (JSONException e) {
				Log.d(TAG,"No "+Constants.ACCESS_CHANGE_ADMIN_ENABLE+" sent");
			}
		}

	}

}
