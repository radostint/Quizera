package com.rado.quizera.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.rado.quizera.Interface.ItemClickListener;
import com.rado.quizera.R;

import org.w3c.dom.Text;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public ImageView category_image;
    public TextView category_name;

    private ItemClickListener itemClickListener;

    public CategoryViewHolder(View itemView) {
        super(itemView);
        category_image = (ImageView) itemView.findViewById(R.id.category_image);
        category_name = (TextView) itemView.findViewById(R.id.category_name);
        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }
}
