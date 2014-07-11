package com.toppatch.mv.gcm;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.toppatch.mv.helper.NotificationHelper;
import com.toppatch.mv.samsung.Controller;

import android.R;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.enterprise.EnterpriseDeviceManager;
import android.app.enterprise.MiscPolicy;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class GcmIntentService extends IntentService {

	private static final String TAG = "GcmIntentService";
    NotificationCompat.Builder builder;
    
    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
    	Log.e(TAG, "onHandleIntent");
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);
        Log.e(TAG, "Got message from GCM Server");
        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                NotificationHelper.sendNotification(getApplicationContext(),"Error","Send error: " + extras.toString(),null);
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
            	NotificationHelper.sendNotification(getApplicationContext(),"Deleted","Deleted messages on server: " +extras.toString(),null);
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
            	Log.e(TAG, "Got data "+extras.toString());
            	Controller.execute(getApplicationContext(),extras);
            }
        }else{
        	Log.e(TAG, "No exxtras!!!! Da fuk is this GCM message for?");
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }
}