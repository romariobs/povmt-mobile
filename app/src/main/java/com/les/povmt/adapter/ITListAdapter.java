package com.les.povmt.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.les.povmt.ListUserActivity;
import com.les.povmt.R;
import com.les.povmt.RegisterTiActivity;
import com.les.povmt.fragment.ITListFragment;
import com.les.povmt.models.InvestedTime;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Lucas on 24/11/2016.
 */
public class ITListAdapter extends RecyclerView.Adapter<ITListAdapter.viewHolder>{
    private Context context;
    private List<InvestedTime> investedTimes;
    private List<String> selectedItens;
    private ITListFragment fragment;
    private AdapterAction currentAction;
    private FloatingActionButton f;

    public enum AdapterAction {
        CREATE, DELETE, EDIT
    }

    public ITListAdapter(Context context, List<InvestedTime> investedTimes, ITListFragment fragment) {
        this.context = context;
        this.investedTimes = investedTimes;
        this.fragment = fragment;
        this.currentAction = AdapterAction.CREATE;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_it_card, parent, false);
        selectedItens = new LinkedList<>();
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        InvestedTime itItem = investedTimes.get(position);

        String time = "";
        int timeDurationMinutes = itItem.getDuration(); //in minutes
        long horas = TimeUnit.MINUTES.toHours(timeDurationMinutes);

        timeDurationMinutes -= TimeUnit.HOURS.toMinutes(horas);

        time = String.format("%02d:%02d", horas, timeDurationMinutes);

        holder.description.setText(itItem.getDate() + "  •  " + time);
    }

    @Override
    public int getItemCount() {
        return investedTimes.size();
    }


    public void update(List<InvestedTime> list) {
        this.investedTimes = list;
        notifyDataSetChanged();
    }

    public class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView description;
        private CheckBox selected;

        public viewHolder(View itemView) {
            super(itemView);

            description = (TextView) itemView.findViewById(R.id.description);
            selected = (CheckBox) itemView.findViewById(R.id.checkBox);
            selected.setClickable(true);

            selected.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (selected.isChecked()) {
                            selectedItens.add(String.valueOf(investedTimes.get(getAdapterPosition()).getId()));
                        } else {
                            selectedItens.remove(String.valueOf(investedTimes.get(getAdapterPosition()).getId()));
                        }

                        int resource = R.drawable.ic_add_white_18dp;

                        if (selectedItens.size() > 0) {
                            resource = R.drawable.ic_delete_white_18dp;
                            currentAction = AdapterAction.DELETE;
                        } else {
                            currentAction = AdapterAction.CREATE;
                        }

                        f = (FloatingActionButton) ((Activity) context).findViewById(R.id.addFab);
                        f.setImageResource(resource);
                    }
                }
            );

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, RegisterTiActivity.class);
            intent.putExtra("id", investedTimes.get(getAdapterPosition()).getActivityId());
            intent.putExtra("time", investedTimes.get(getAdapterPosition()).getDuration());
            intent.putExtra("date", investedTimes.get(getAdapterPosition()).getDate());
            intent.putExtra("idTI", investedTimes.get(getAdapterPosition()).getId());
            intent.putExtra("editar", true);
            context.startActivity(intent);
        }
    }

    public AdapterAction getCurrentAction() {
        return  currentAction;
    }

    public List<String> getSelected() {
        return this.selectedItens;
    }

    public void clearSelected(){
        selectedItens = new ArrayList<>();
        int resource = R.drawable.ic_add_white_18dp;
        f.setImageResource(resource);
    }
}
