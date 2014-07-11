package com.toppatch.mv.ui.activities;

import com.toppatch.mv.Constants;
import com.toppatch.mv.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

public class MessageActivity extends Activity {

	private static final String TAG = "MessageActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_message);
		
		setup();
	}

	private void setup(){
		if(getIntent()!=null){
			Bundle extras = getIntent().getExtras();
			if(extras!=null){
				String title = extras.getString(Constants.NOTIFICATION_TITLE);
				String message = extras.getString(Constants.NOTIFICATION_MESSAGE);
				Log.d(TAG, "title ="+title);
				Log.d(TAG, "message ="+message);
				((TextView)findViewById(R.id.notification_message)).setText(message);
				((TextView)findViewById(R.id.notification_title)).setText(title);
				setTitle(title);
			}else{
				finish();
			}
		}else{
			finish();
		}
	}
}