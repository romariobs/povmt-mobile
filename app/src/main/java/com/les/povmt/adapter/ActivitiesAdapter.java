package com.les.povmt.adapter;

import android.content.Context;
import android.content.Intent;
import android.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.les.povmt.R;
import com.les.povmt.RegisterTi;
import com.les.povmt.WeekReportActivity;
import com.les.povmt.listUserActivity;
import com.les.povmt.models.Activity;

import java.util.List;

/**
 * Created by Lucas on 24/11/2016.
 */
public class ActivitiesAdapter extends RecyclerView.Adapter<ActivitiesAdapter.viewHolder> implements PopupMenu.OnMenuItemClickListener{
    private Context context;
    private List<Activity> activities;
    private int group;
    private int child;

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

        holder.imageViewMenuOverflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v, 0);
            }
        });
    }

    private void showPopup(View v, int groupPosition) {
        PopupMenu popup = new PopupMenu(context, v);
        MenuInflater inflater = popup.getMenuInflater();
        popup.setOnMenuItemClickListener(this);
        inflater.inflate(R.menu.menu_activity, popup.getMenu());
        popup.show();
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }


    public void update(List<Activity> list) {
        this.activities = list;
        notifyDataSetChanged();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option1:
                Intent intent = new Intent(context, RegisterTi.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                return true;
            case R.id.option2:
                Toast.makeText(context, "option2", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.option3:
                Toast.makeText(context, "option3", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.option4:
                Toast.makeText(context, "option4", Toast.LENGTH_SHORT).show();
                return true;
            default:
                Toast.makeText(context, "You do not have proposals for this game", Toast.LENGTH_SHORT).show();
                return false;
        }
    }


    public class viewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView description;
        private ImageView imageViewMenuOverflow;

        public viewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title_activity);
            imageViewMenuOverflow = (ImageView) itemView.findViewById(R.id.imageViewMenuOverflow);
            description = (TextView) itemView.findViewById(R.id.description);
        }
    }
}
