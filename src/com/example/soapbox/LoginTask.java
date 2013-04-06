package com.example.soapbox;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
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

public class LoginTask extends AsyncTask<String, String, JSONObject>{
	
	public static final String GET = "GET";
	public static final String POST = "POST";
	public static final String SUCCESS = "success";
	
	public static final String USERNAMEKEY = "[user][name]";
	public static final String EMAILKEY = "[user][email]";
	public static final String PASSWORDKEY = "[user][password]";
	public static final String PASSWORD_CKEY = "[user][password_confirmation]";
	public static final String TAGKEY = "[user][tag]";
	
	public static final String DATA = "data";
	public static final String ID = "id";
	public static final String USER = "user";
	public static final String EMAIL = "email";
	public static final String NAME = "name";
	public static final String AUTH = "auth_token";
	public static final String TAG = "tag";
	
	public static final String DEFAULT_TAG_VALUE = "global";
	
	public static final String AUTH_KEY = "[data][auth_token]";
	
	public static final String INFO = "info";
	
	public static final String INFOLOGIN = "Logged in";
	public static final String INFOREGISTER = "Registered";
	
	public static final String LOGINSTATUSKEY = "lg";
	
	ProgressDialog mDialog;
	List<NameValuePair> postparams= new ArrayList<NameValuePair>();
	String URL=null;
	String method = null;
	Context context;
	String json = null;
	JSONObject jObj = null;
	
	boolean validUsername = true;
	
	public interface MyCallbackInterface {
        public void onLoginRequestComplete(JSONObject result);
    }
	private MyCallbackInterface mCallback;
	
	public LoginTask(String url, String method, ArrayList<NameValuePair> params, Context context, MyCallbackInterface mCallback) {
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
    	
    	
    	try {
       		if (validUsername == true && result.get(LoginTask.SUCCESS).toString().equals("false"))
       		{
       			System.out.println("IN IF STATEMENT");
       			AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(context);
       			dlgAlert.setTitle("Error");
       			dlgAlert.setMessage("Invalid email or password.");
       			dlgAlert.setPositiveButton("Ok",
       					new DialogInterface.OnClickListener() {
       				public void onClick(DialogInterface dialog, int which) {
       					//dismiss the dialog  
       				}
       			});
       			dlgAlert.setCancelable(true);
       			dlgAlert.create().show();
       			mDialog.dismiss();
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
    		mDialog.dismiss();
    		return;
       	}
       	mCallback.onLoginRequestComplete(result);
       	mDialog.dismiss();
       	((Activity)context).finish();
    }
}

