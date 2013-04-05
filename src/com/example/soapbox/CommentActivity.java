package com.example.soapbox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.soapbox.CommentTask.CommentCallbackInterface;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListAdapter;
import android.widget.ListView;

public class CommentActivity extends Activity implements CommentCallbackInterface
{
	public static final String MAP = "object";
	
	public static final String HOSTNAME = "http://acx0.dyndns.org:3000/";
	public static final String SHOUTS = "shouts";
	public static final String SLASH = "/";
	public static final String COMMENTS = "comments";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		
		refreshComments();
	}
	
	public void refreshComments()
	{
//		title= getIntent().getExtras().getString("Title");
		String id = getIntent().getExtras().getString(DisplayShoutListTask.ID);
		//http://acx0.dyndns.org:3000/shouts/10/comments.json
		String url = HOSTNAME + SHOUTS + SLASH + id + SLASH + COMMENTS;
		String method = DisplayShoutListTask.GET;
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		
		System.out.println("Pre Execute");

//		DisplayShoutListTask t = new DisplayShoutListTask(url,method,params,this,this);
		CommentTask t = new CommentTask(url, method, params, this, this);
		t.execute();
		System.out.println("Post Execute");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.comment, menu);
		return true;
	}

	@Override
	public void onRequestComplete(JSONArray result) 
	{
//		System.out.println(result.toString());
		LinkedList<HashMap<String, String>> list = new LinkedList<HashMap<String,String>>();
		try 
		{
			for(int i=0; i<result.length(); i++)
			{
				JSONObject o = result.getJSONObject(i);
				HashMap<String, String> map = new HashMap<String, String>();

				map.put(CommentTask.SHOUTID, o.getString(CommentTask.SHOUTID));
				map.put(CommentTask.COMMENTID, o.getString(CommentTask.COMMENTID));
				map.put(CommentTask.NAME, o.getString(CommentTask.NAME));
				map.put(CommentTask.BODY, o.getString(CommentTask.BODY));
				list.addFirst(map);
			}
			
			//Add OP
			HashMap<String, String> map = new HashMap<String, String>();
			@SuppressWarnings("unchecked")
			HashMap<String, String> o = (HashMap<String, String>) getIntent().getExtras().get(MAP);
			map.put(DisplayShoutListTask.ID, o.get(DisplayShoutListTask.ID));
			map.put(DisplayShoutListTask.NAME, o.get(DisplayShoutListTask.NAME));
			map.put(DisplayShoutListTask.TAG, o.get(DisplayShoutListTask.TAG));
			map.put(DisplayShoutListTask.MESSAGE, o.get(DisplayShoutListTask.MESSAGE));
			list.addFirst(map);
		} 
		catch (JSONException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		final ListView listView = (ListView) findViewById(R.id.comment_list);

		// get data from the table by the ListAdapter
		CommentListAdapter adapter = new CommentListAdapter(this, list, R.layout.activity_comment, null, null);

		listView.setAdapter(adapter);
	}

}
