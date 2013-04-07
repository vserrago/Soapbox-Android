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
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class RatingsTask extends AsyncTask<String, String, JSONObject>
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
	public static final String VOTE = "vote";
	public static final String UPVOTE = "add";
	public static final String DONWVOTE = "sub";

	//Instance Vars
	ProgressDialog mDialog;
	List<NameValuePair> postparams= new ArrayList<NameValuePair>();
	String URL=null;
	String method = null;
	String json = null;
	Context context;
	RatingsCallbackInterface callBack;

	JSONObject jObj = null;
	//	JSONArray jArray = null;

	public interface RatingsCallbackInterface 
	{
		public void onRatingComplete();
	}

	public RatingsTask(String url, String method, ArrayList<NameValuePair> params, Context context, RatingsCallbackInterface callBack) 
	{
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

		// return JSON String
		return jObj;
	}

	@Override
	protected void onPreExecute()
	{
		super.onPreExecute();

		//		mDialog = new ProgressDialog(context);
		//		mDialog.setMessage("Please wait...");
		//		mDialog.show();
	}

	@Override
	protected void onPostExecute(JSONObject result)
	{
		super.onPostExecute(result);

		callBack.onRatingComplete();
		//		mDialog.dismiss();
		System.out.println("Rating complete");
	}
}