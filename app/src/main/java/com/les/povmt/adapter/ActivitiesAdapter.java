package com.les.povmt.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.les.povmt.R;
import com.les.povmt.listUserActivity;
import com.les.povmt.models.Activity;

import java.util.List;

/**
 * Created by Lucas on 24/11/2016.
 */
public class ActivitiesAdapter extends RecyclerView.Adapter<ActivitiesAdapter.viewHolder>{
    private Context context;
    private List<Activity> activities;

    public ActivitiesAdapter(Context context, List<Activity> activities) {
        this.context = context;
        this.activities = activities;

    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_card, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        Activity activity = activities.get(position);

        holder.title.setText(activity.getTitle());
        holder.description.setText(activity.getDescription());
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }


    public void update(List<Activity> list) {
        this.activities = list;
        notifyDataSetChanged();
    }


    public class viewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView description;

        public viewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title_activity);
            description = (TextView) itemView.findViewById(R.id.description);
        }
    }
}
