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

import com.example.soapbox.LoginTask.MyCallbackInterface;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class DisplayShoutListTask extends AsyncTask<String, String, JSONObject>
{
	//HTTP Code names
	public static final String GET = "GET";
	public static final String POST = "POST";
	public static final String SUCCESS = "success";

	//JSON key names
	public static final String CREATEDAT = "created_at";
	public static final String ID = "id";
	public static final String MESSAGE = "message";
	public static final String NAME = "name";
	public static final String TAG = "tag";
	public static final String TITLE = "title";
	public static final String UPDATEDAT = "updated_at";
	
	
	List<NameValuePair> postparams= new ArrayList<NameValuePair>();
	String URL=null;
	String method = null;
	String json = null;
	JSONObject jObj = null;
	
	
	public DisplayShoutListTask(String url, String method, ArrayList<NameValuePair> params) 
	{
		URL=url;
		postparams=params;
		this.method = method;
	}

	@Override
	protected JSONObject doInBackground(String... params) 
	{
		try {
			// Making HTTP request 
			// check for request method
			HttpEntity httpEntity = null;
			if(method.equals(POST))
			{
				// request method is POST
				// defaultHttpClient
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(URL);
				httpPost.setEntity(new UrlEncodedFormEntity(postparams));

				HttpResponse httpResponse = httpClient.execute(httpPost);
				httpEntity = httpResponse.getEntity();
			//	is = httpEntity.getContent();

			}
			else if(method.equals(GET))
			{
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
		
		// try parse the string to a JSON object
		try 
		{
			//System.out.println(json);
			jObj = new JSONObject(json);
		} 
		catch (JSONException e) 
		{
			
		}

		// return JSON String
		return jObj;
	}
}