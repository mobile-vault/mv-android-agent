package com.toppatch.mv.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.toppatch.mv.Config;
import com.toppatch.mv.Constants;


public class WebHelper {
  private static String TAG="WebHelper";
	public static String sendGetRequest(String url){
		try {
			HttpGet request = new HttpGet();
			request.setURI(new URI(url));
			return sendRequest(request);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String sendPostRequest(String url,ArrayList<NameValuePair> data){
		try{
			HttpPost request = new HttpPost();
			request.setURI(new URI(url));
			request.setEntity(new UrlEncodedFormEntity(data));
			return sendRequest(request);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static String sendRequest(HttpRequestBase request){
		HttpResponse response=null;
		try{
			HttpClient client=new DefaultHttpClient();
			response=client.execute(request);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(response!=null){
				InputStream content;
				try {
					String currentLine=null;
					StringBuffer buff = new StringBuffer();
					content = response.getEntity().getContent();
					BufferedReader reader = new BufferedReader(new InputStreamReader(content));
					while((currentLine=reader.readLine())!=null){
						buff.append(currentLine);
					}					
					return buff.toString();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else{
				return null;
			}
		}
		return null;
	}
	public static int putRequestVoilate(Context con){
		
					JSONObject jsonObject=new JSONObject();
					
					try {
						
						jsonObject.put("command_uuid",Constants.UNINSTALL_UUID);
						jsonObject.put("gcm_id",AdminHelper.getGCMId(con));
						TelephonyManager mngr = (TelephonyManager)con.getSystemService(Context.TELEPHONY_SERVICE); 
						jsonObject.put(Constants.IMEI, mngr.getDeviceId());
						HttpClient client = new DefaultHttpClient();
						HttpPut put = new HttpPut(Config.BASE_URL+"/samsung/save");
						StringEntity entity;
					    entity = new StringEntity(jsonObject.toString());
						entity.setContentType("application/json;charset=UTF-8");//text/plain;charset=UTF-8
						entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
						put.setEntity(entity); 
						HttpResponse response=client.execute(put);
					    return response.getStatusLine().getStatusCode();
						} catch(Exception e) {
							e.printStackTrace();
							Log.i(TAG, "error sending to server"+jsonObject.toString());
						}
					return -1;
				
		
			//report.new SendResultThread(context,Constants.UNINSTALL_UUID,null,null);
			
	}
}
