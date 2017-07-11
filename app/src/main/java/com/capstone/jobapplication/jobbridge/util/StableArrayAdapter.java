package com.capstone.jobapplication.jobbridge.util;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.capstone.jobapplication.jobbridge.R;
import com.capstone.jobapplication.jobbridge.entity.Job;
import com.capstone.jobapplication.jobbridge.entity.JobRating;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by Aicun on 6/9/2017.
 */

public class StableArrayAdapter extends ArrayAdapter<Job> {
    private static int DIVIDOR_HEIGHT = 50;
    ListView listView;

    ColorDrawable drawable = new ColorDrawable(getContext().getResources().getColor(R.color.dividor));

    static class ViewHolder {
        public TextView title;
        public TextView company;
        public TextView location;
        public TextView wage;
        public TextView postTime;
        public ImageView shiftType;
        public ImageView rateJob;
    }


    public StableArrayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Job> objects, ListView listView) {
        super(context, resource, objects);
        this.listView = listView;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = convertView;
        if (rowView == null) {
            rowView = inflater.inflate(R.layout.jobs_list_fragment, parent,false);
            // configure view holder
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.title = (TextView) rowView.findViewById(R.id.title);
            viewHolder.company = (TextView) rowView.findViewById(R.id.company);
            viewHolder.location = (TextView) rowView.findViewById(R.id.location);
            viewHolder.wage = (TextView) rowView.findViewById(R.id.wage);
            viewHolder.postTime = (TextView) rowView.findViewById(R.id.postTime);
            viewHolder.shiftType = (ImageView) rowView.findViewById(R.id.shiftType);
            viewHolder.rateJob = (ImageView) rowView.findViewById(R.id.job_rate);
            rowView.setTag(viewHolder);
        }

        final ViewHolder holder = (ViewHolder) rowView.getTag();
        final Job job = getItem(position);
        holder.title.setText(job.getTitle());
        holder.company.setText(job.getEmployer().getName());
        holder.location.setText(StringUtil.formatLocation(job.getCity(),job.getProvince()));
        holder.wage.setText(StringUtil.formatWage(job.getWage()));
        holder.postTime.setText(job.getPostDate());
        final boolean isNightShift = StringUtil.isNightShift(job.getStartTime());
        holder.shiftType.setImageResource(isNightShift ? R.drawable.nightshift : R.drawable.dayshift);
        final boolean isRated = isRated(job);
        holder.rateJob.setImageResource(isRated ? R.drawable.job_like : R.drawable.job_dislike);
        holder.rateJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JobRating rating = CacheData.getJobRating(job.getId());
                HttpClientPost post = new HttpClientPost("/jobRatings/add");
                Map<String,String> keyValue = new HashMap<>();
                int status = 1;
                if(rating!=null) {
                    post = new HttpClientPost("/jobRatings/update");
                    keyValue.put("id",String.valueOf(rating.getId()));
                    status = rating.getStatus() == 1 ? 0 : 1;
                }
                keyValue.put("status",String.valueOf(status));
                keyValue.put("jobId", String.valueOf(job.getId()));
                //// TODO: 7/10/2017 change to real job seeker
                keyValue.put("jobSeekerId","3");

                try {
                    post.doPost(keyValue);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }

                //// TODO: 7/10/2017 change to real job seeker 
                CacheData.updateOrAddRating(job.getId(),status,3);

                holder.rateJob.setImageResource(status == 0 ? R.drawable.job_dislike : R.drawable.job_like);
            }
        });

        listView.setDivider(drawable);
        listView.setDividerHeight(DIVIDOR_HEIGHT);
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

    private boolean isRated(Job job) {
        JobRating rating = CacheData.getJobRating(job.getId());
        if(rating == null || rating.getStatus() ==0) {
            return false;
        }
        return true;
    }
}
