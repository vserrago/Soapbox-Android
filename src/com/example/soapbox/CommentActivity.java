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
import com.example.soapbox.RatingsTask.RatingsCallbackInterface;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CommentActivity extends Activity implements CommentCallbackInterface, RatingsCallbackInterface
{
	public static final String MAP = "object";
	public static final String VOTEDMAP = "votedmap";
	
	public static final String HOSTNAME = "http://acx0.dyndns.org:3000/";
	public static final String SHOUTS = "shouts";
	public static final String SLASH = "/";
	public static final String COMMENTS = "comments";
	private SharedPreferences prefs;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		View v = (View)findViewById(R.layout.activity_comment);
		refreshComments(v);
	}
	
	public void refreshComments(View view)
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
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		View v = (View)findViewById(R.layout.activity_comment);
		switch (item.getItemId()) 
		{
		case R.id.comment_menu_action_refresh:
			refreshComments(v);
			break;
		case R.id.comment_menu_action_reply:
			makeReply(v);
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}
		
	public void makeReply(View v) {
		// TODO Auto-generated method stub
		
		prefs = this.getSharedPreferences("com.example.soapbox", Context.MODE_PRIVATE);
		
		final Dialog dialog = new Dialog(this);
		final CommentActivity context = this;
		
		dialog.setContentView(R.layout.comment_reply);
		dialog.setTitle("Reply");
		Button cancelReply = (Button)dialog.findViewById(R.id.cancelReply);
		Button postReply = (Button)dialog.findViewById(R.id.postReply);
		final EditText commentReply = (EditText)dialog.findViewById(R.id.commentReply);
		
		cancelReply.setOnClickListener(new OnClickListener()
		{
				@Override
				public void onClick(View v) { dialog.dismiss(); }
		});
		
		postReply.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String reply = commentReply.getText().toString();
				if (reply.isEmpty())
				{
					Toast.makeText(context, "Please enter a comment", Toast.LENGTH_SHORT).show();
					return;
				}
				
				if (reply.length() >= MainActivity.SHOUT_LENGTH)
				{
					Toast.makeText(context, "Comment too long", Toast.LENGTH_SHORT).show();
					return;
				}
				
				String name = prefs.getString(LoginTask.NAME, "");
				String id = getIntent().getExtras().getString(DisplayShoutListTask.ID);
				
				String url = HOSTNAME + SHOUTS + SLASH + id + SLASH + COMMENTS;
				
				ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
				BasicNameValuePair user = new BasicNameValuePair(CommentTask.NAME_CREATE, name);
				BasicNameValuePair body = new BasicNameValuePair(CommentTask.BODY_CREATE, reply);
				params.add(user);
				params.add(body);

				CommentTask t = new CommentTask(url, CommentTask.POST, params, context, context);
				t.execute();

				dialog.dismiss();
				View v = (View)findViewById(R.layout.activity_comment);
				context.refreshComments(v);
			}	
		});
		dialog.show();
	}

	@Override
	public void onCommentRequestComplete(JSONArray result) 
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
			map.put(com.example.soapbox.ShoutListAdapter.USERRATING, o.get(com.example.soapbox.ShoutListAdapter.USERRATING));
			list.addFirst(map);
		} 
		catch (JSONException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		final ListView listView = (ListView) findViewById(R.id.comment_list);
		
		// get data from the table by the ListAdapter
		@SuppressWarnings("unchecked")
		CommentListAdapter adapter = new CommentListAdapter(this, list, R.layout.activity_comment, 
				null, null, (HashMap<String, String>) getIntent().getExtras().getSerializable(VOTEDMAP));

		listView.setAdapter(adapter);
	}

	@Override
	public void onRatingComplete() 
	{
		// TODO Auto-generated method stub
		
	}
}
