package com.toppatch.mv.generic.components;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.toppatch.mv.Constants;
import com.toppatch.mv.helper.NotificationHelper;

public class NotificationComponent extends Component {

	private static final String TAG = "NotificationComponent";

	public NotificationComponent(Context context) {
		super(context);
	}

	@Override
	public void execute(JSONObject data) {
		Log.d(TAG, "Execute: " + data);
		if(data != null) {
			JSONObject notification = data.optJSONObject(Constants.NOTIFICATION_DATA);
			if(notification != null) {
				String message = notification.optString(Constants.NOTIFICATION_MESSAGE, null);
				String title = notification.optString(Constants.NOTIFICATION_TITLE, null);
				String url = notification.optString(Constants.NOTIFICATION_URL, null);
				if(message != null){
					NotificationHelper.sendNotification(mContext, title, message, url);
				}
			}
		}
	}
}
