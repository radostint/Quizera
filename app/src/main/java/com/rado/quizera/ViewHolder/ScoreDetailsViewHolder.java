package com.rado.quizera.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rado.quizera.R;

public class ScoreDetailsViewHolder extends RecyclerView.ViewHolder {
    public TextView categoryName, categoryScore;

    public ScoreDetailsViewHolder(@NonNull View itemView) {
        super(itemView);
        categoryName = itemView.findViewById(R.id.categoryName);
        categoryScore = itemView.findViewById(R.id.categoryScore);
    }
}
