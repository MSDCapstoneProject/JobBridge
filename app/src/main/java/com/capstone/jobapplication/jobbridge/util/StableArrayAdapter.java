package com.capstone.jobapplication.jobbridge.util;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.capstone.jobapplication.jobbridge.R;
import com.capstone.jobapplication.jobbridge.entity.Job;

import java.util.List;

/**
 * Created by Aicun on 6/9/2017.
 */

public class StableArrayAdapter extends ArrayAdapter<Job> {

    static class ViewHolder {
        public TextView title;
        public TextView company;
        public TextView location;
        public TextView wage;
        public TextView postTime;
    }


    public StableArrayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Job> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = convertView;
        if (rowView == null) {
            rowView = inflater.inflate(R.layout.jobs_list_fragment, parent,false);
            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.title = (TextView) rowView.findViewById(R.id.title);
            viewHolder.company = (TextView) rowView.findViewById(R.id.company);
            viewHolder.location = (TextView) rowView.findViewById(R.id.location);
            viewHolder.wage = (TextView) rowView.findViewById(R.id.wage);
            viewHolder.postTime = (TextView) rowView.findViewById(R.id.postTime);
            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();
        Job job = getItem(position);
        holder.title.setText(job.getTitle());
        holder.company.setText(job.getEmployer().getName());
        holder.location.setText(job.getJobLocation());
        holder.wage.setText(job.getWage());
        holder.postTime.setText(job.getPostDate());

        return rowView;
    }

    @Nullable
    @Override
    public Job getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}