package com.omak.school;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MarkAdapter extends ArrayAdapter<Mark>{

	ArrayList<Mark> markList;
	Activity context;
	
	public MarkAdapter(Activity context, int textViewResourceId,
			ArrayList<Mark> objects) {
		super(context, textViewResourceId, objects);
		this.markList = objects;
		this.context = context;
	}
	
	static class ViewHolder {
		TextView subject;
		TextView mark;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		ViewHolder holder;

		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.mark_row, null);

			holder = new ViewHolder();
			holder.subject = (TextView) v.findViewById(R.id.subject);
			holder.mark = (TextView) v.findViewById(R.id.mark_row);
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
		Mark model = markList.get(position);
		holder.subject.setText(model.subject);
		holder.mark.setText(model.mark);
		return v;
	}

}
