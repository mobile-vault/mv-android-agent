package com.toppatch.mv.helper;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.ViewDebug.FlagToString;

import com.toppatch.mv.Constants;
import com.toppatch.mv.R;
import com.toppatch.mv.gcm.GCMActivity;
import com.toppatch.mv.ui.activities.MainActivity;
import com.toppatch.mv.ui.activities.MessageActivity;

public class NotificationHelper {

	public static final int NOTIFICATION_ID = 1;
	private static final String TAG = "NotificationHelper";

	// Put the message into a notification and post it.
	// This is just one simple example of what you might choose to do with
	// a GCM message.
	public static void sendNotification(Context context,String title,String message,String url) {
		if(title==null) title = "MobileVault";
		if(message==null) message = "";

		NotificationManager mNotificationManager;
		mNotificationManager = (NotificationManager)
				context.getSystemService(Context.NOTIFICATION_SERVICE);
		PendingIntent contentIntent = null;
		//		if(url!=null){
		Intent openUrlIntent = new Intent(context,MessageActivity.class);
		Log.d(TAG, "title = "+title);
		openUrlIntent.putExtra(Constants.NOTIFICATION_TITLE, title);
		Log.d(TAG, "message ="+message);
		openUrlIntent.putExtra(Constants.NOTIFICATION_MESSAGE, message);
		contentIntent = PendingIntent.getActivity(context, 0,
				openUrlIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		//		}

		NotificationCompat.Builder mBuilder =
				new NotificationCompat.Builder(context)
		.setSmallIcon(R.drawable.ic_launcher)
		.setContentTitle(title)
		.setStyle(new NotificationCompat.BigTextStyle()
		.bigText(message))
		.setAutoCancel(true)
		.setContentText(message);


		if(contentIntent!=null)	mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	}
}
