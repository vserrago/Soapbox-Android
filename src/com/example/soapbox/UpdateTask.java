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
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class UpdateTask extends AsyncTask<String, String, JSONObject>
{
	//HTTP Code names
	public static final String PUT = "PUT";
	public static final String SUCCESS = "success";

	//JSON key names
	public static final String CREATEDAT = "created_at";
	public static final String ID = "id";
	public static final String MESSAGE = "message";
	public static final String NAME = "name";
	public static final String TAG = "tag";
	public static final String TITLE = "title";
	public static final String UPDATEDAT = "updated_at";

	//Instance Vars
	ProgressDialog mDialog;
	List<NameValuePair> postparams= new ArrayList<NameValuePair>();
	String URL=null;
	String method = null;
	String json = null;
	Context context;
	UpdateCallbackInterface callBack;
	Dialog dialog;

	JSONObject jObj = null;
	//	JSONArray jArray = null;

	public interface UpdateCallbackInterface 
	{
		public void onUpdateComplete(JSONObject result, Dialog dialog);
	}

	public UpdateTask(String url, String method, ArrayList<NameValuePair> params, Context context, UpdateCallbackInterface callBack, Dialog dialog) 
	{
		this.dialog = dialog;
		this.context = context;
		URL=url;
		postparams=params;
		this.method = method;
		this.callBack = callBack;
	}

	@Override
	protected JSONObject doInBackground(String... params) 
	{
		try 
		{
			// Making HTTP request 
			// check for request method
			HttpEntity httpEntity = null;
			if(method.equals(PUT))
			{
				DefaultHttpClient httpClient = new DefaultHttpClient();
				
				HttpPut httpPut = new HttpPut(URL);
				
				httpPut.setEntity(new UrlEncodedFormEntity(postparams));

				HttpResponse httpResponse = httpClient.execute(httpPut);
				httpEntity = httpResponse.getEntity();
			}
			else 
			{
				System.out.println("Error: Incorrect HTTP method");
			}
			json = EntityUtils.toString(httpEntity);
		} 
		catch (UnsupportedEncodingException e) 
		{
			e.printStackTrace();
		} 
		catch (ClientProtocolException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		try {
			
			System.out.println(json);
			System.out.println("------------");
			jObj = new JSONObject(json);
			
		} catch (JSONException e) {
			e.printStackTrace();
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

		callBack.onUpdateComplete(result, dialog);
		mDialog.dismiss();
	}
}