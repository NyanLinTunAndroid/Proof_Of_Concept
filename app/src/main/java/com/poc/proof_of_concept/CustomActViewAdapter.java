package com.poc.proof_of_concept;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.poc.proof_of_concept.Job;

import java.util.ArrayList;

public class CustomActViewAdapter extends ArrayAdapter<Job> {

    private Context mContext;
    private ArrayList<Job> jobs;
    private int mResourse;

    public CustomActViewAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Job> objects) {
        super(context, resource, objects);
        mContext = context;
        mResourse = resource;
        jobs = objects;
    }

    private Filter newFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            //To check User Input

            FilterResults results = new FilterResults();
            ArrayList<Job> suggestions = new ArrayList<>();
            jobs = Data.jobs;
            if (constraint == null || constraint.length()==0) {
                suggestions.addAll(jobs);
            }
            else {
                for (Job job : jobs) {
                    if (job.getCompany().toLowerCase().trim().contains(constraint.toString().toLowerCase().trim())) {
                        suggestions.add(job);
                    }
                }
            }
            results.values = suggestions;
            results.count = suggestions.size();
            return results;
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Job) resultValue).getCompany();
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(mResourse, parent, false);
        }
        Job job = getItem(position);
        TextView iName = (TextView) convertView.findViewById(R.id.company);
        iName.setText(job.getCompany());
        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return newFilter;
    }
}
