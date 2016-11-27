package com.les.povmt.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.les.povmt.R;
import com.les.povmt.RegisterTiActivity;
import com.les.povmt.fragment.ITListFragment;
import com.les.povmt.models.InvestedTime;
import com.les.povmt.models.RankingItem;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Lucas on 24/11/2016.
 */
public class ITListAdapter extends RecyclerView.Adapter<ITListAdapter.viewHolder>{
    private Context context;
    private List<InvestedTime> investedTimes;
    private ITListFragment fragment;

    public ITListAdapter(Context context, List<InvestedTime> investedTimes, ITListFragment fragment) {
        this.context = context;
        this.investedTimes = investedTimes;
        this.fragment = fragment;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_it_card, parent, false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        InvestedTime itItem = investedTimes.get(position);

        holder.description.setText("27/11/2016" + " - " + "00:" + itItem.getDuration());
    }

    @Override
    public int getItemCount() {
        return investedTimes.size();
    }


    public void update(List<InvestedTime> list) {
        this.investedTimes = list;
        notifyDataSetChanged();
    }

    public class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView description;
        private CheckBox selected;

        public viewHolder(View itemView) {
            super(itemView);

            description = (TextView) itemView.findViewById(R.id.description);
            selected = (CheckBox) itemView.findViewById(R.id.checkBox);
            selected.setClickable(false);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if(selected.isChecked()) {
                fragment.itDeselect(0);
                selected.setChecked(false);
            } else {
                fragment.itSelect(0);
                selected.setChecked(true);
            }
        }
    }
}
