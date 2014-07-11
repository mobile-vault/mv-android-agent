package com.toppatch.mv.ui.activities;

import android.app.Activity;
import android.app.enterprise.ApplicationPolicy;
import android.app.enterprise.EnterpriseDeviceManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.toppatch.mv.R;

public class WelcomeActivity extends Activity{
private String TAG="Welcome Activity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		
	}
}