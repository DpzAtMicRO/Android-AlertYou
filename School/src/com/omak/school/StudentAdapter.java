package com.omak.school;

import java.util.ArrayList;

import android.R.drawable;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class StudentAdapter extends ArrayAdapter<Student>{

	ArrayList<Student> studentList;
	Activity context;
	boolean from;
	
	public StudentAdapter(Activity context, int textViewResourceId,
			ArrayList<Student> objects,boolean from) {
		super(context, textViewResourceId, objects);
		this.studentList = objects;
		this.context = context;
		this.from = from;
	}
	
	static class ViewHolder {
		ImageView image;
		TextView name;
		TextView parent;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		ViewHolder holder;

		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.single_row_item, null);

			holder = new ViewHolder();
			holder.image = (ImageView) v.findViewById(R.id.img);
			holder.name = (TextView) v.findViewById(R.id.name);
			holder.parent = (TextView) v.findViewById(R.id.parents);
			if(from) {
				holder.image.setVisibility(View.VISIBLE); 
			} else{
				holder.image.setVisibility(View.GONE);
			}
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
		Student model = studentList.get(position);
		holder.name.setText(model.firstName + " " + model.lastName);
		holder.parent.setText(model.fatherName);
		if(model.attStatus) {
			holder.image.setImageResource(com.omak.school.R.drawable.green);
		} else {
			holder.image.setImageResource(android.R.drawable.ic_delete);
		}
		
		return v;
	}

}
