package com.toppatch.mv.samsung.components;

import org.json.JSONObject;

import com.toppatch.mv.Constants;
import com.toppatch.mv.helper.NotificationHelper;

import android.content.Context;
import android.util.Log;

public class NotificationComponent extends Component {

	private static final String TAG = "NotificationComponent";

	public NotificationComponent(Context context) {
		super(context);
	}

	@Override
	public void execute(JSONObject data) {
		if(data!=null){
			Log.d(TAG, "Executing "+data.toString());
			JSONObject notification = data.optJSONObject(Constants.NOTIFICATION_DATA);
			if(notification!=null){
				String message=notification.optString(Constants.NOTIFICATION_MESSAGE,null);
				String title = notification.optString(Constants.NOTIFICATION_TITLE,null);
				String url = notification.optString(Constants.NOTIFICATION_URL,null);
				if(message!=null){
					NotificationHelper.sendNotification(mContext, title,message,url);
				}
			}
		}
	}
}
