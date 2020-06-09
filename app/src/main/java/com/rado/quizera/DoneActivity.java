package com.rado.quizera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rado.quizera.Common.Common;
import com.rado.quizera.Model.QuestionScore;

public class DoneActivity extends AppCompatActivity {

    Button btnPlayAgain;
    TextView txtResultScore, getTxtResultQuestion;
    ProgressBar progressBar;

    FirebaseDatabase db;
    DatabaseReference questionScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);

        db = FirebaseDatabase.getInstance();
        questionScore = db.getReference("Question_Score");

        txtResultScore = findViewById(R.id.txtTotalScore);
        getTxtResultQuestion = findViewById(R.id.txtTotalQuestions);
        progressBar = findViewById(R.id.doneProgressBar);
        btnPlayAgain = findViewById(R.id.btnPlayAgain);

        btnPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoneActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            int score = extra.getInt("SCORE");
            int totalQuestion = extra.getInt("TOTAL");
            int correctAnswer = extra.getInt("CORRECT");

            //показване на резултатите на играча
            txtResultScore.setText(String.format("SCORE: %d", score));
            getTxtResultQuestion.setText(String.format("PASSED: %d / %d", correctAnswer, totalQuestion));
            progressBar.setMax(totalQuestion);
            progressBar.setProgress(correctAnswer);

            //записване във firebaseDb
            questionScore.child(String.format("%s_%s", Common.currentUser.getUsername(), Common.categoryId))
                    .setValue(new QuestionScore(String.format("%s_%s", Common.currentUser.getUsername()
                            , Common.categoryId), Common.currentUser.getUsername(), String.valueOf(score)
                    ,Common.categoryId,Common.categoryName));
        }
    }
}
