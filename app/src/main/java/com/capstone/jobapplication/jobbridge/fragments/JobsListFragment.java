package com.capstone.jobapplication.jobbridge.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.capstone.jobapplication.jobbridge.R;
import com.capstone.jobapplication.jobbridge.entity.Job;
import com.capstone.jobapplication.jobbridge.entity.JobRating;
import com.capstone.jobapplication.jobbridge.util.CacheData;
import com.capstone.jobapplication.jobbridge.util.HttpClientPost;
import com.capstone.jobapplication.jobbridge.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by Aicun on 10/16/2017.
 */

public class JobsListFragment extends Fragment {

    private RecyclerView myCrimeRecyclerView;
    private JobAdapter jobAdapter;

    private int like;
    private int dislike;

    private List<Job> jobLists = new ArrayList<>();

    public void setJobLists(List<Job> jobLists) {
        this.jobLists = jobLists;
        if(jobAdapter != null)
            jobAdapter.setJobs(jobLists);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jobs_list, container, false);
        myCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.jobs_recycler_view);
        myCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        like = getContext().getResources().getColor(R.color.color_highlight);
        dislike = getContext().getResources().getColor(R.color.colorGray);

        jobAdapter = new JobAdapter(jobLists);
        myCrimeRecyclerView.setAdapter(jobAdapter);

        return view;
    }

    private class JobHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView title;
        private TextView company;
        private TextView location;
        private TextView wage;
        private TextView postTime;
        private ImageView shiftType;
        private ImageView likeThisJob;
        private ImageView dislikeThisJob;
        private Job job;

        public JobHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            company = (TextView) itemView.findViewById(R.id.company);
            location = (TextView) itemView.findViewById(R.id.location);
            wage = (TextView) itemView.findViewById(R.id.wage);
            postTime = (TextView) itemView.findViewById(R.id.postTime);
            shiftType = (ImageView) itemView.findViewById(R.id.shiftType);
            likeThisJob = (ImageView) itemView.findViewById(R.id.job_like);
            dislikeThisJob = (ImageView) itemView.findViewById(R.id.job_dislike);

            itemView.setOnClickListener(this);
        }

        public void bindJob(final Job job) {
            this.job = job;
            title.setText(job.getTitle());
            company.setText(job.getEmployer().getName());
            location.setText(StringUtil.formatLocation(job.getCity(),job.getProvince()));
            wage.setText(StringUtil.formatWage(job.getWage()));
            postTime.setText(job.getPostDate());
            final boolean isNightShift = StringUtil.isNightShift(job.getStartTime());
            shiftType.setImageResource(isNightShift ? R.drawable.nightshift : R.drawable.dayshift);
            int isRated = isRated(job);
            likeThisJob.setColorFilter(isRated == 1 ? like : dislike);
            dislikeThisJob.setColorFilter(isRated == 0 ? like : dislike);
            likeThisJob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JobRating rating = CacheData.getJobRating(job.getId());
                    HttpClientPost post = new HttpClientPost(getContext().getString(R.string.url_rating));
                    Map<String,String> keyValue = new HashMap<>();
                    int status = 1;
                    if(rating!=null) {
                        post = new HttpClientPost(getContext().getString(R.string.url_ratingUpdate));
                        keyValue.put("id",String.valueOf(rating.getId()));
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

                    likeThisJob.setColorFilter(like);
                    dislikeThisJob.setColorFilter(dislike);
                }
            });

            dislikeThisJob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JobRating rating = CacheData.getJobRating(job.getId());
                    HttpClientPost post = new HttpClientPost(getContext().getString(R.string.url_rating));
                    Map<String,String> keyValue = new HashMap<>();
                    int status = 0;
                    if(rating!=null) {
                        post = new HttpClientPost(getContext().getString(R.string.url_ratingUpdate));
                        keyValue.put("id",String.valueOf(rating.getId()));
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

                    dislikeThisJob.setColorFilter(like);
                    likeThisJob.setColorFilter(dislike);
                }
            });
        }

        @Override
        public void onClick(View v) {
            //Toast.makeText(getActivity(),mCrime.getmTitle() + " clicked!",Toast.LENGTH_SHORT).show();

            //first version, for phone user
            //Intent intent = CrimeActivity.newIntent(getActivity(),mCrime.getmId());
            //startActivity(intent);
            //Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getmId());
            //startActivityForResult(intent, REQUEST_CRIME);

            //second version, for phone and tablet user.
            //mCallbacks.onCrimeSeleted(mCrime);
            //updatedPosition = getAdapterPosition();
        }

        private int isRated(Job job) {
            JobRating rating = CacheData.getJobRating(job.getId());
            if(rating == null) {
                return -1;
            }
            return rating.getStatus();
        }
    }

    private class EmptyHoder extends RecyclerView.ViewHolder{

        private TextView emptyListTextView;

        public EmptyHoder(View itemView) {
            super(itemView);
            emptyListTextView = (TextView) itemView.findViewById(R.id.list_empty_view);
        }
    }


    private class JobAdapter extends RecyclerView.Adapter {

        public static final int VIEW_TYPE_ITEM = 1;
        public static final int VIEW_TYPE_EMPTY = 0;

        private List<Job> jobList;

        public JobAdapter(List<Job> jobList) {
            this.jobList = jobList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            if(viewType == VIEW_TYPE_EMPTY) {
                View view = layoutInflater.inflate(R.layout.list_empty,parent,false);
                return new EmptyHoder(view);
            }
            View view = layoutInflater.inflate(R.layout.jobs_list_fragment, parent, false);
            return new JobHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(holder instanceof JobHolder) {
                Job job = jobList.get(position);
                JobHolder jobHolder = (JobHolder) holder;
                jobHolder.bindJob(job);
            }
        }

        @Override
        public int getItemCount() {
            if(jobList.size() == 0) return 1;
            return jobList.size();
        }

        @Override
        public int getItemViewType(int position) {
            if(jobList.size() == 0) return VIEW_TYPE_EMPTY;
            return VIEW_TYPE_ITEM;
        }

        public void setJobs(List<Job> jobList) {
            this.jobList = jobList;
        }
    }

}
