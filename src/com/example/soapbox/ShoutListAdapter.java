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

public class ShoutListAdapter extends SimpleAdapter{

	public static final String RATEDUP = "up";
	public static final String RATEDDOWN = "down";
	public static final String RATEDNEUTRAL = "neutral";
	public static final String USERRATING = "rated";

	Context c;
	List<? extends Map<String, ?>> d;
	HashMap<String, String> votedMap; 
	

	public ShoutListAdapter(Context context, List<? extends Map<String, ?>> data,
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
		//		View view = convertView;
		View view = null;

		if (view == null) 
		{
			LayoutInflater vi;
			vi = LayoutInflater.from(c);

			view = vi.inflate(R.layout.shout_list_component, null);
		}

		@SuppressWarnings("unchecked")
		final Map<String, String> map = (Map<String, String>) d.get(position);
		//Item p = items.get(position);

		if(map != null)
		{
			TextView messageComp = (TextView) view.findViewById(R.id.message_component);
			TextView locationComp = (TextView) view.findViewById(R.id.location_component);
			final ImageButton upvote = (ImageButton) view.findViewById(R.id.upvote_component);
			final ImageButton downvote = (ImageButton) view.findViewById(R.id.downvote_component);
			final String id = (String) map.get(DisplayShoutListTask.ID);

			if(votedMap.containsKey(id))
			{
				if(votedMap.get(id).equals(RATEDUP))
				{
					upvote.setSelected(true);
					downvote.setSelected(false);
				}
				else if(votedMap.get(id).equals(RATEDDOWN))
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
				if(Locations.nameValueMap.containsKey(map.get(DisplayShoutListTask.TAG)))
				{
				locationComp.setText((String) Locations.nameValueMap.get(map.get(DisplayShoutListTask.TAG))
						+ ",  " + map.get(DisplayShoutListTask.CREATEDAT).replace('T', ' ').replace('Z', ' '));
				}
				else
				{
					locationComp.setText(Locations.globalName + ",  " + 
							map.get(DisplayShoutListTask.CREATEDAT).replace('T', ' ').replace('Z', ' '));
				}	
			}

			upvote.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					SharedPreferences prefs = c.getSharedPreferences("com.example.soapbox", Context.MODE_PRIVATE);
//					String id = (String) map.get(DisplayShoutListTask.ID);

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
						votedMap.put(id, RATEDNEUTRAL);
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
						votedMap.put(id, RATEDUP);
					}

					ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
					BasicNameValuePair tag = new BasicNameValuePair(RatingsTask.VOTE, voteType);
					params.add(tag);
					
//					String id = Integer.toString(prefs.getInt(LoginTask.ID, -1));
//					BasicNameValuePair userid = new BasicNameValuePair("userid", id);
//					params.add(userid);

					MainActivity mainActivity = (MainActivity) c;

					RatingsTask t = new RatingsTask(url, method, params, mainActivity, mainActivity);
					t.execute();
					if(twice)
					{
						t = new RatingsTask(url, method, params, mainActivity, mainActivity);
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

						votedMap.put(id, RATEDNEUTRAL);
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
						votedMap.put(id, RATEDDOWN);
					}

					ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
					BasicNameValuePair tag = new BasicNameValuePair(RatingsTask.VOTE, voteType);
					params.add(tag);

					MainActivity mainActivity = (MainActivity) c;

					RatingsTask t = new RatingsTask(url, method, params, mainActivity, mainActivity);
					t.execute();
					if(twice)
					{
						t = new RatingsTask(url, method, params, mainActivity, mainActivity);
						t.execute();
					}
					//  Use position parameter of your getView() in this method it will current position of Clicked row button
					// code for current Row deleted...  
					Toast.makeText(c, "One downvote for Shout ID " + 
							map.get(DisplayShoutListTask.ID), Toast.LENGTH_SHORT).show();
				}
			});
		}
		return view;
	}
}
