package com.toppatch.mv.ui.activities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class UpdateApp extends AsyncTask<String,Void,Void>{
private Context context;
public void setContext(Context contextf){
    context = contextf;
}

@Override
protected Void doInBackground(String... arg0) {
      try {
            URL url = new URL(arg0[0]);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("POST");
            c.setRequestProperty("User-Agent", "stagefright/1.1 (Linux;Android 2.3.6)");
            c.setDoOutput(true);
            c.connect();

            String PATH = Environment.getExternalStorageDirectory().getPath();
            File file = new File(PATH);
            file.mkdirs();
            File outputFile = new File(file, "update.apk");
            if(outputFile.exists()){
                outputFile.delete();
            }
            FileOutputStream fos = new FileOutputStream(outputFile);
            Log.e("UpdateAPP", "Update error! " + c.getResponseMessage());
            Log.e("UpdateAPP", "Update error! " + c.getResponseCode());
            InputStream is = c.getInputStream();

            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len1);
            }
            fos.close();
            is.close();

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(PATH+"update.apk")), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // without this flag android returned a intent error!
            context.startActivity(intent);


        } catch (Exception e) {
            Log.e("UpdateAPP", "Update error! " + e.getMessage());
            e.printStackTrace();
        }
    return null;
}}  