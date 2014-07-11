package com.toppatch.mv;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.toppatch.mv.helper.AdminHelper;

public class ReportServer {

	private static final String TAG = "ReportServer";
	/**
	 * 
	 * @param level 0= Verbose , 1=Debug , 2= Info, 3 =Warning, 4=Error
	 * @param message
	 */
	public static void report(int level, String message){
		//Save the message in a temporary buffer and send it to a server.
		switch(level){
		case 4:
			Log.e(TAG, message);
			break;
		case 3:
			Log.w(TAG, message);
			break;
		case 2:
			Log.i(TAG, message);
			break;
		case 1:
			Log.d(TAG, message);
			break;
		default:
			Log.v(TAG, message);
		}
	}
	
	public static void e(String message){
		report(4, message);
	}
	
	public static void w(String message){
		
		report(3, message);
	}
	
	public static void i(String message){
		report(2, message);
	}
	
	public static void d(String message){
		report(1, message);
	}
	
	public static void v(String message){
		report(0, message);
	}


public class SendResultThread extends Thread{
	JSONObject jsonObject=new JSONObject();
	public SendResultThread(Context context,String uuid,String callback,JSONObject args) {
		Log.d(TAG, "Sending "+args.toString());
		try {
			Log.i(TAG, "sendig uuid:"+uuid);
			jsonObject.put("command_uuid",uuid);
			jsonObject.put("gcm_id",AdminHelper.getGCMId(context));
			jsonObject.put("result",args);
			TelephonyManager mngr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE); 
			jsonObject.put(Constants.IMEI, mngr.getDeviceId());
			Log.i(TAG, "sending device info:"+jsonObject.toString());
			start();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i(TAG, "error sending JSON info:"+jsonObject.toString());
		}
		
		
	}
	@Override
	public void run() {
		try {
		HttpClient client = new DefaultHttpClient();
		HttpPut put = new HttpPut(Config.BASE_URL+"/samsung/save");
		StringEntity entity;
	    entity = new StringEntity(jsonObject.toString());
		entity.setContentType("application/json;charset=UTF-8");//text/plain;charset=UTF-8
		entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
		put.setEntity(entity); 
		client.execute(put);
		} catch(Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i(TAG, "error sending to server"+jsonObject.toString());
		}
	}
	}

}
