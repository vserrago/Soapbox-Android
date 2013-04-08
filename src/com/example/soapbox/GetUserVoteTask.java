package com.example.soapbox;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.example.soapbox.DisplayShoutListTask.ShoutListCallbackInterface;

public class GetUserVoteTask extends AsyncTask<String, String, JSONArray>
{
	//HTTP Code names
	public static final String GET = "GET";
	public static final String POST = "POST";
	public static final String SUCCESS = "success";

	//JSON key names
	public static final String CREATEDAT = "created_at";
	public static final String ID = "id";
	public static final String SHOUTID = "shout_id";
	public static final String UPDATEDAT = "updated_at";
	public static final String USERID = "user_id";
	public static final String VOTE = "vote";
	
	public static final String USER_ID_GET_PARAM = "userid";
	
	//Instance Vars
	ProgressDialog mDialog;
	List<NameValuePair> postparams= new ArrayList<NameValuePair>();
	String URL=null;
	String method = null;
	String json = null;
	Context context;
	VoteTaskCallbackInterface callBack;
	
	JSONArray jArray = null;
	
	public interface VoteTaskCallbackInterface 
	{
        public void onVoteRequestComplete(JSONArray result);
    }
	
	public GetUserVoteTask(String url, String method, ArrayList<NameValuePair> params, Context context, VoteTaskCallbackInterface callBack) 
	{
		this.context = context;
		URL=url;
		postparams=params;
		this.method = method;
		this.callBack = callBack;
	}
	
	@Override
	protected JSONArray doInBackground(String... params) 
	{
		try 
		{
			// Making HTTP request 
			// check for request method
			HttpEntity httpEntity = null;
			if(method.equals(POST))
			{
				// request method is POST
				// defaultHttpClient
				URL += ".json";
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(URL);
//				httpPost.setEntity(new UrlEncodedFormEntity(postparams));

				HttpResponse httpResponse = httpClient.execute(httpPost);
				httpEntity = httpResponse.getEntity();
			//	is = httpEntity.getContent();

			}
			else if(method.equals(GET))
			{
				System.out.println("Pre Execute TASK");
				try
				{
				// request method is GET
				DefaultHttpClient httpClient = new DefaultHttpClient();
				

				URL += ".json";	//Retrieve json
				
				//Encode params
				String paramString = URLEncodedUtils.format(postparams, "utf-8");
				URL += "?" + paramString;
				
				System.out.println("VoteTask URL: " + URL);
				
				HttpGet httpGet = new HttpGet(URL);

				HttpResponse httpResponse = httpClient.execute(httpGet);
				httpEntity = httpResponse.getEntity();
				//is = httpEntity.getContent();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}      
			
			json = EntityUtils.toString(httpEntity);
			try 
			{
				//TODO String json is a JSON array, not a single object
				jArray = new JSONArray(json);
//				jObj = new JSONObject(json);
			} 
			catch (JSONException e) 
			{

				e.printStackTrace();
			}
			System.out.println(json.toString());
			System.out.println("Post Execute TASK");
			
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
		return jArray;
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
    protected void onPostExecute(JSONArray result)
    {
    	super.onPostExecute(result);

       	callBack.onVoteRequestComplete(result);
       	mDialog.dismiss();
    }
}
