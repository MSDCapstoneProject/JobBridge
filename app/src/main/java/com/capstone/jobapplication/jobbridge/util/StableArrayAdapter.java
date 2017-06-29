package com.capstone.jobapplication.jobbridge.util;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.capstone.jobapplication.jobbridge.R;
import com.capstone.jobapplication.jobbridge.entity.Job;

import java.util.List;

/**
 * Created by Aicun on 6/9/2017.
 */

public class StableArrayAdapter extends ArrayAdapter<Job> {

    ListView listView;


    int[] colors = {R.color.colorGray,R.color.colorGray,R.color.colorGray};

    GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors);

    static class ViewHolder {
        public TextView title;
        public TextView company;
        public TextView location;
        public TextView wage;
        public TextView postTime;
    }


    public StableArrayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Job> objects, ListView listView) {
        super(context, resource, objects);
        this.listView = listView;
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
        holder.wage.setText(StringUtil.formatWage(job.getWage()));
        holder.postTime.setText(job.getPostDate());
        listView.setDivider(drawable);
        listView.setDividerHeight(50);
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
