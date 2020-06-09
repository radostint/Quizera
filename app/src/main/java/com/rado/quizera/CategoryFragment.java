package com.rado.quizera;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rado.quizera.Common.Common;
import com.rado.quizera.Interface.ItemClickListener;
import com.rado.quizera.Model.Category;
import com.rado.quizera.ViewHolder.CategoryViewHolder;
import com.squareup.picasso.Picasso;


public class CategoryFragment extends Fragment {
    View fragment;

    RecyclerView listCategory;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Category, CategoryViewHolder> adapter;

    FirebaseDatabase db;
    DatabaseReference categories;

    public static CategoryFragment newInstance() {
        CategoryFragment categoryFragment = new CategoryFragment();
        return categoryFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = FirebaseDatabase.getInstance();
        categories = db.getReference("Categories");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragment = inflater.inflate(R.layout.fragment_category, container, false);

        listCategory = (RecyclerView) fragment.findViewById(R.id.categories);
        listCategory.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(container.getContext());
        listCategory.setLayoutManager(layoutManager);

        loadCategories();
        return fragment;
    }

    private void loadCategories() {
        adapter = new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(Category.class, R.layout.category_layout, CategoryViewHolder.class, categories) {
            @Override
            protected void populateViewHolder(CategoryViewHolder categoryViewHolder, final Category category, int i) {
                categoryViewHolder.category_name.setText(category.getName());
                Picasso.with(getActivity()).load(category.getImage()).into(categoryViewHolder.category_image);

                categoryViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent startGame = new Intent(getActivity(), StartActivity.class);
                        Common.categoryId = adapter.getRef(position).getKey();
                        Common.categoryName = category.getName();
                        startActivity(startGame);
                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        listCategory.setAdapter(adapter);
    }
}
