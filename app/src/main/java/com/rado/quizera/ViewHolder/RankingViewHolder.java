package com.rado.quizera.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rado.quizera.Interface.ItemClickListener;
import com.rado.quizera.R;

public class RankingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView rankingName, rankingScore;
    private ItemClickListener itemClickListener;

    public RankingViewHolder(@NonNull View itemView) {
        super(itemView);
        rankingName = itemView.findViewById(R.id.rankingName);
        rankingScore = itemView.findViewById(R.id.rankingScore);
        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);

    }
}
