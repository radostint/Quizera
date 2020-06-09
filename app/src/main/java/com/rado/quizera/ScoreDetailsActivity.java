package com.rado.quizera;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rado.quizera.Model.QuestionScore;
import com.rado.quizera.ViewHolder.ScoreDetailsViewHolder;

public class ScoreDetailsActivity extends AppCompatActivity {
    FirebaseDatabase db;
    DatabaseReference question_score;

    String viewUser = "";
    RecyclerView scoreList;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<QuestionScore, ScoreDetailsViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_details);
        db = FirebaseDatabase.getInstance();
        question_score = db.getReference("Question_Score");

        scoreList = findViewById(R.id.scoreList);
        scoreList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        scoreList.setLayoutManager(layoutManager);


        if (getIntent() != null) {
            viewUser = getIntent().getStringExtra("viewUser");
        }
        if (!viewUser.isEmpty()) {
            loadScoreDetails(viewUser);
        }
    }

    private void loadScoreDetails(String viewUser) {
        adapter = new FirebaseRecyclerAdapter<QuestionScore, ScoreDetailsViewHolder>(QuestionScore.class
                , R.layout.score_details_layout, ScoreDetailsViewHolder.class, question_score.orderByChild("user").equalTo(viewUser)) {
            @Override
            protected void populateViewHolder(ScoreDetailsViewHolder scoreDetailsViewHolder, QuestionScore questionScore, int i) {
                scoreDetailsViewHolder.categoryName.setText(questionScore.getCategoryName());
                scoreDetailsViewHolder.categoryScore.setText(questionScore.getScore());
            }
        };
        adapter.notifyDataSetChanged();
        scoreList.setAdapter(adapter);
    }
}
