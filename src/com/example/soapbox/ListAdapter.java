package com.example.soapbox;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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
		View view = convertView;
		
		if (view == null) 
		{
	        LayoutInflater vi;
	        vi = LayoutInflater.from(c);
	        
	        view = vi.inflate(R.layout.shout_list_component, null);
	    }
		
		Map<String, ?> map = d.get(position);
	    //Item p = items.get(position);

		if(map != null)
		{
			TextView messageComp = (TextView) view.findViewById(R.id.message_component);
			TextView locationComp = (TextView) view.findViewById(R.id.location_component);
			
			if(messageComp != null)
			{
				messageComp.setText((String) map.get(DisplayShoutListTask.NAME) 
						+ ": " + (String) map.get(DisplayShoutListTask.MESSAGE));
			}
			
			if(locationComp != null)
			{
				locationComp.setText((String) map.get(DisplayShoutListTask.TAG));
			}
		}
//	    if (p != null) {
//
//	        TextView tt = (TextView) v.findViewById(R.id.id);
//	        TextView tt1 = (TextView) v.findViewById(R.id.categoryId);
//	        TextView tt3 = (TextView) v.findViewById(R.id.description);
//
//	        if (tt != null) {
//	            tt.setText(p.getId());
//	        }
//	        if (tt1 != null) {
//
//	            tt1.setText(p.getCategory().getId());
//	        }
//	        if (tt3 != null) {
//
//	            tt3.setText(p.getDescription());
//	        }
//	    }
		return view;
	}
}
