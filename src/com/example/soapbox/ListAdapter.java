package com.example.soapbox;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ListAdapter extends SimpleAdapter{
	
	Context c;
	List<? extends Map<String, ?>> d;
	
	public ListAdapter(Context context, List<? extends Map<String, ?>> data,
			int resource, String[] from, int[] to) 
	{
		super(context, data, resource, from, to);
		// TODO Auto-generated constructor stub
		c = context;
		d = data;
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
		
		final Map<String, ?> map = d.get(position);
	    //Item p = items.get(position);

		if(map != null)
		{
			TextView messageComp = (TextView) view.findViewById(R.id.message_component);
			TextView locationComp = (TextView) view.findViewById(R.id.location_component);
			final ImageButton upvote = (ImageButton) view.findViewById(R.id.upvote_component);
			final ImageButton downvote = (ImageButton) view.findViewById(R.id.downvote_component);
			
//			if("1".equals(map.get(DisplayShoutListTask.ID)))
//			{
//				upvote.setVisibility(upvote.INVISIBLE);
//				this.notifyDataSetChanged();
//			}
			
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
					  if(upvote.isSelected())
					  {
						  upvote.setSelected(false);
					  }
					  else
					  {
						  downvote.setSelected(false);
						  upvote.setSelected(true);
					  }
					  Toast.makeText(c, "One upvote for Shout ID " + 
							  map.get(DisplayShoutListTask.ID), Toast.LENGTH_SHORT).show();
				   }
			});
			
			downvote.setOnClickListener(new OnClickListener()
			{
				  @Override
				  public void onClick(View v)
				   {
					  if(downvote.isSelected())
					  {
						  downvote.setSelected(false);
					  }
					  else
					  {
						  upvote.setSelected(false);
						  downvote.setSelected(true);
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
