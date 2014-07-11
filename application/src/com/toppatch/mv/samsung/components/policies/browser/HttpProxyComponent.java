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

public class HttpProxyComponent extends Component {

	private static final String TAG = "HttpProxyComponent";

	public HttpProxyComponent(Context context) {
		super(context);
		
	}

	@Override
	public void execute(JSONObject data) {
		if(data!=null){
			Log.d(TAG, "Executing "+data.toString());
			try {
				String proxy = data.optString(Constants.BROWSER_HTTP_PROXY,null);
				EnterpriseDeviceManager edm = getEdm();
				if(edm!=null){
					try{
						BrowserPolicy browserPolicy = edm.getBrowserPolicy();
						browserPolicy.setHttpProxy(proxy);
					}catch(Exception e){
						ReportServer.e(TAG+data.toString()+e.toString());
					}
				}
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}
	}

}
