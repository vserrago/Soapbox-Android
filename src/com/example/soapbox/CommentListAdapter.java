package com.example.soapbox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class CommentListAdapter extends SimpleAdapter {

	Context c;
	List<? extends Map<String, ?>> d;
	HashMap<String, String> votedMap; 

	public CommentListAdapter(Context context, List<? extends Map<String, ?>> data,
			int resource, String[] from, int[] to, HashMap<String, String> votedMap ) 
	{
		super(context, data, resource, from, to);
		// TODO Auto-generated constructor stub
		c = context;
		d = data;
		this.votedMap = votedMap;
		
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View view = convertView;

		//		if (view == null) 
		{
			LayoutInflater vi;
			vi = LayoutInflater.from(c);

			if(position == 0)
			{
				view = vi.inflate(R.layout.shout_list_component, null);
			}
			else
			{
				view = vi.inflate(R.layout.comment_component, null);
			}
		}

		@SuppressWarnings("unchecked")
		final Map<String, String> map = (Map<String, String>) d.get(position);
		//Item p = items.get(position);

		if(position == 0 && map != null)
		{
			TextView messageComp = (TextView) view.findViewById(R.id.message_component);
			TextView locationComp = (TextView) view.findViewById(R.id.location_component);
			final ImageButton upvote = (ImageButton) view.findViewById(R.id.upvote_component);
			final ImageButton downvote = (ImageButton) view.findViewById(R.id.downvote_component);
			final String id = (String) map.get(DisplayShoutListTask.ID);

			if(votedMap.containsKey(id))
			{
				if(votedMap.get(id).equals(ShoutListAdapter.RATEDUP))
				{
					upvote.setSelected(true);
					downvote.setSelected(false);
				}
				else if(votedMap.get(id).equals(ShoutListAdapter.RATEDDOWN))
				{
					upvote.setSelected(false);
					downvote.setSelected(true);
				}
				else
				{
					upvote.setSelected(false);
					downvote.setSelected(false);
				}
			}
			//Rated Neutral
			else
			{
				upvote.setSelected(false);
				downvote.setSelected(false);
			}
			
			if(messageComp != null)
			{
				messageComp.setText((String) map.get(DisplayShoutListTask.NAME) 
						+ ": " + (String) map.get(DisplayShoutListTask.MESSAGE));
			}

			if(locationComp != null)
			{
				locationComp.setText((String) map.get(DisplayShoutListTask.TAG));
			}

			upvote.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					SharedPreferences prefs = c.getSharedPreferences("com.example.soapbox", Context.MODE_PRIVATE);
					String id = (String) map.get(DisplayShoutListTask.ID);

					// PUT /shouts/id?vote=add
					String url = MainActivity.HOSTNAME + MainActivity.SHOUTS + MainActivity.SLASH + id;
					String method = UpdateTask.PUT;

					boolean twice = false;

					String voteType;
					if(upvote.isSelected())
					{
						//Revoke upvote
						voteType = RatingsTask.DONWVOTE;
						upvote.setSelected(false);
						votedMap.put(id, ShoutListAdapter.RATEDNEUTRAL);
					}
					else
					{
						//Add upvote
						voteType = RatingsTask.UPVOTE;
						if(downvote.isSelected())
						{
							downvote.setSelected(false);
							twice = true;
						}
						upvote.setSelected(true);
						votedMap.put(id, ShoutListAdapter.RATEDUP);
					}

					ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
					BasicNameValuePair tag = new BasicNameValuePair(RatingsTask.VOTE, voteType);
					params.add(tag);

					CommentActivity commentActivity = (CommentActivity) c;

					RatingsTask t = new RatingsTask(url, method, params, commentActivity, commentActivity);
					t.execute();
					if(twice)
					{
						t = new RatingsTask(url, method, params, commentActivity, commentActivity);
						t.execute();
					}
					//					Toast.makeText(c, "One upvote for Shout ID " + 
					//							map.get(DisplayShoutListTask.ID), Toast.LENGTH_SHORT).show();
				}
			});

			downvote.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					SharedPreferences prefs = c.getSharedPreferences("com.example.soapbox", Context.MODE_PRIVATE);
					String id = (String) map.get(DisplayShoutListTask.ID);

					// PUT /shouts/id?vote=add
					String url = MainActivity.HOSTNAME + MainActivity.SHOUTS + MainActivity.SLASH + id;
					String method = UpdateTask.PUT;

					boolean twice = false;

					String voteType;
					if(downvote.isSelected())
					{
						//revoke downvote
						voteType = RatingsTask.UPVOTE;
						downvote.setSelected(false);

						votedMap.put(id, ShoutListAdapter.RATEDNEUTRAL);
					}
					else
					{
						//Add downvote
						voteType = RatingsTask.DONWVOTE;
						if(upvote.isSelected())
						{
							upvote.setSelected(false);
							twice = true;
						}
						downvote.setSelected(true);
						votedMap.put(id, ShoutListAdapter.RATEDDOWN);
					}

					ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
					BasicNameValuePair tag = new BasicNameValuePair(RatingsTask.VOTE, voteType);
					params.add(tag);

					CommentActivity commentActivity = (CommentActivity) c;

					RatingsTask t = new RatingsTask(url, method, params, commentActivity, commentActivity);
					t.execute();
					if(twice)
					{
						t = new RatingsTask(url, method, params, commentActivity, commentActivity);
						t.execute();
					}
					//  Use position parameter of your getView() in this method it will current position of Clicked row button
					// code for current Row deleted...  
					Toast.makeText(c, "One downvote for Shout ID " + 
							map.get(DisplayShoutListTask.ID), Toast.LENGTH_SHORT).show();
				}
			});
		}
		else if(map != null)
		{
			TextView messageComp = (TextView) view.findViewById(R.id.comment_textview);

			if(messageComp != null)
			{
				messageComp.setText((String) map.get(CommentTask.NAME) 
						+ ": " + (String) map.get(CommentTask.BODY));
			}
		}
		return view;
	}
}