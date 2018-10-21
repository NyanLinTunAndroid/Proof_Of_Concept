package com.poc.proof_of_concept;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Nyan Linn Htun on 10/20/2018.
 */

public class ListViewAdapter extends ArrayAdapter<Job> {
    Context context;
    ArrayList<Job> jobs;
    LayoutInflater inflater;
    public ListViewAdapter(Context context, ArrayList<Job> objects) {
        super(context, 0, objects);
        this.context = context;
        this.jobs = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        TextView job_id = (TextView) view.findViewById(R.id.job_id);
        TextView company = (TextView) view.findViewById(R.id.company);
        TextView address = (TextView) view.findViewById(R.id.address);
        Button accept = (Button) view.findViewById(R.id.accept);
        job_id.setText("Job Number: " + jobs.get(position).getJob_id());
        company.setText("Company: " + jobs.get(position).getCompany());
        address.setText("Address: " + jobs.get(position).getAddress());
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MapsActivity.class);
                intent.putExtra("company", jobs.get(position).getCompany());
                context.startActivity(intent);
            }
        });
        return view;
    }
}
