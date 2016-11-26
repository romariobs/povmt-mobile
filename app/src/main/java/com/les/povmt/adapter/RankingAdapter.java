package com.les.povmt.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.les.povmt.R;
import com.les.povmt.RegisterTiActivity;
import com.les.povmt.models.RankingItem;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Lucas on 24/11/2016.
 */
public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.viewHolder>{
    private Context context;
    private List<RankingItem> activities;
    private float totalTimeInvested;

    public RankingAdapter(Context context, List<RankingItem> activities, float totalTimeInvested) {
        this.context = context;
        this.activities = activities;
        this.totalTimeInvested = totalTimeInvested;

    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ranking_card, parent, false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        RankingItem rankingItem = activities.get(position);
        DecimalFormat df = new DecimalFormat("0.00");
        String percentTime = df.format(100 * (rankingItem.getTimeSpend()/ totalTimeInvested));
        holder.timeSpend.setText("Total time invested: " + (int)rankingItem.getTimeSpend() + " h (%" + percentTime + ")");
        holder.title.setText(rankingItem.getActivity().getTitle());
        holder.position.setText("" + (position + 1));

        holder.color.setBackgroundColor(rankingItem.getColor());
        // holder.color.setImageDrawable();
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }


    public void update(List<RankingItem> list) {
        this.activities = list;
        notifyDataSetChanged();
    }



    public class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView title;
        private TextView timeSpend;
        private TextView position;
        private ImageView color;

        public viewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title_activity);
            timeSpend = (TextView) itemView.findViewById(R.id.timeSpend);
            position = (TextView) itemView.findViewById(R.id.positionRanking);
            color = (ImageView) itemView.findViewById(R.id.color_pie);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, RegisterTiActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
