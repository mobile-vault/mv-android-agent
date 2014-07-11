package com.toppatch.mv.ui.activities;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.toppatch.mv.R;

public class SplashScreenActivity extends Activity {

	ImageView toppatchLogo;
	TextView toppatchText;
    String TAG="Splash";
    String authToken;
    String androidId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		Animation animLogo = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.rotate);
		Animation animText = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.fadeout);
		ImageView logo=(ImageView)findViewById(R.id.imageviewLogo);
		ImageView text=(ImageView)findViewById(R.id.imageViewText);
		logo.setAnimation(animLogo);
		text.setAnimation(animText);
		text.startAnimation(animText);
		 new Handler().postDelayed(new Runnable() {
	            @Override
	            public void run() {
	              Intent i = new Intent(SplashScreenActivity.this,MainActivity.class);
	              startActivity(i);
	              finish();
	              overridePendingTransition(R.anim.activity_fade_in,R.anim.activity_fade_out);
	             
	            }
	        },2000);
		

	}
	
	}
