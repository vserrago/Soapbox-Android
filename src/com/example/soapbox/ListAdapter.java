package com.example.soapbox;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

public class ListAdapter extends SimpleAdapter{

	public ListAdapter(Context context, List<? extends Map<String, ?>> data,
			int resource, String[] from, int[] to) 
	{
		super(context, data, resource, from, to);
		// TODO Auto-generated constructor stub
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View view = convertView;
		return view;
	}
}
