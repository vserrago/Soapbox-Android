package com.example.soapbox;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

public class HttpBackgroundTask extends AsyncTask<String, String, JSONObject>{
	
	public static final String GET = "GET";
	public static final String POST = "POST";
	public static final String SUCCESS = "success";
	
	public static final String USERNAMEKEY = "[user][name]";
	public static final String EMAILKEY = "[user][email]";
	public static final String PASSWORDKEY = "[user][password]";
	public static final String PASSWORD_CKEY = "[user][password_confirmation]";
	private static final String INFO = "info";
	
	ProgressDialog mDialog;
	List<NameValuePair> postparams= new ArrayList<NameValuePair>();
	String URL=null;
	String method = null;
	Context context;
	String json = null;
	JSONObject jObj = null;
	
	boolean validUsername = true;
	
	public interface MyCallbackInterface {
        public void onRequestComplete(JSONObject result);
    }
	private MyCallbackInterface mCallback;
	
	public HttpBackgroundTask(String url, String method, ArrayList<NameValuePair> params, Context context, MyCallbackInterface mCallback) {
		this.context = context;
		URL=url;
		postparams=params;
		this.method = method;
		this.mCallback = mCallback;
	}
	@Override
	protected JSONObject doInBackground(String... params) {
		// TODO Auto-generated method stub
		// Making HTTP request

		try {
			// Making HTTP request 
			// check for request method
			HttpEntity httpEntity = null;
			if(method.equals(POST)){
				// request method is POST
				// defaultHttpClient
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(URL);
				httpPost.setEntity(new UrlEncodedFormEntity(postparams));

				HttpResponse httpResponse = httpClient.execute(httpPost);
				httpEntity = httpResponse.getEntity();
			//	is = httpEntity.getContent();

			}else if(method.equals(GET)){
				// request method is GET
				DefaultHttpClient httpClient = new DefaultHttpClient();
				String paramString = URLEncodedUtils.format(postparams, "utf-8");
				URL += "?" + paramString;
				HttpGet httpGet = new HttpGet(URL);

				HttpResponse httpResponse = httpClient.execute(httpGet);
				httpEntity = httpResponse.getEntity();
				//is = httpEntity.getContent();
			}      
			
			json = EntityUtils.toString(httpEntity);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// try parse the string to a JSON object
		try {
			//System.out.println(json);
			jObj = new JSONObject(json);
		} catch (JSONException e) {
			validUsername = false;
		}

		// return JSON String
		return jObj;

	}
    @Override
    protected void onPreExecute()
    {
    	super.onPreExecute();

    	mDialog = new ProgressDialog(context);
    	mDialog.setMessage("Please wait...");
    	mDialog.show();
    }
    
    @Override
    protected void onPostExecute(JSONObject result)
    {
    	super.onPostExecute(result);
    	mDialog.dismiss();
    	
    	try {
       		if (validUsername == true && result.get(HttpBackgroundTask.SUCCESS).toString().equals("false"))
       		{
       			System.out.println("IN IF STATEMENT");
       			AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(context);
       			dlgAlert.setTitle("Error");
       			dlgAlert.setMessage("Invalid email.");
       			dlgAlert.setPositiveButton("Ok",
       					new DialogInterface.OnClickListener() {
       				public void onClick(DialogInterface dialog, int which) {
       					//dismiss the dialog  
       				}
       			});
       			dlgAlert.setCancelable(true);
       			dlgAlert.create().show();
       			return;
       		}
       	} catch (JSONException e) {
       		// TODO Auto-generated catch block
       		e.printStackTrace();
       	}
    	
       	if (validUsername == false)
       	{
       		AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(context);
    		
    		dlgAlert.setTitle("Error");
    		dlgAlert.setMessage("Username already taken");
    		dlgAlert.setPositiveButton("Ok",
    			    new DialogInterface.OnClickListener() {
    			        public void onClick(DialogInterface dialog, int which) {
    			          //dismiss the dialog  
    			        }
    			    });
    		dlgAlert.setCancelable(true);
    		dlgAlert.create().show();
    		return;
       	}
       	mCallback.onRequestComplete(result);
       	((Activity)context).finish();
       	
    }
}

