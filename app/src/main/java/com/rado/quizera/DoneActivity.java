package com.rado.quizera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
            final int score = extra.getInt("SCORE");
            int totalQuestion = extra.getInt("TOTAL");
            int correctAnswer = extra.getInt("CORRECT");

            //показване на резултатите на играча
            txtResultScore.setText(String.format("РЕЗУЛТАТ: %d", score));
            getTxtResultQuestion.setText(String.format("ВЕРНИ ОТГОВОРИ: %d / %d", correctAnswer, totalQuestion));
            progressBar.setMax(totalQuestion);
            progressBar.setProgress(correctAnswer);

            questionScore.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(String.format("%s_%s", Common.currentUser.getUsername(), Common.categoryId)).exists()) {
                        QuestionScore rankingFromDb = dataSnapshot.child(String.format("%s_%s", Common.currentUser.getUsername(), Common.categoryId)).getValue(QuestionScore.class);
                        if (Integer.parseInt(rankingFromDb.getScore()) < score) {
                            questionScore.child(String.format("%s_%s", Common.currentUser.getUsername(), Common.categoryId))
                                    .setValue(new QuestionScore(String.format("%s_%s", Common.currentUser.getUsername()
                                            , Common.categoryId), Common.currentUser.getUsername(), String.valueOf(score)
                                            , Common.categoryId, Common.categoryName));
                        }
                    } else {
                        questionScore.child(String.format("%s_%s", Common.currentUser.getUsername(), Common.categoryId))
                                .setValue(new QuestionScore(String.format("%s_%s", Common.currentUser.getUsername()
                                        , Common.categoryId), Common.currentUser.getUsername(), String.valueOf(score)
                                        , Common.categoryId, Common.categoryName));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            //записване във firebaseDb
            //questionScore.child(String.format("%s_%s", Common.currentUser.getUsername(), Common.categoryId))
            //       .setValue(new QuestionScore(String.format("%s_%s", Common.currentUser.getUsername()
            //          , Common.categoryId), Common.currentUser.getUsername(), String.valueOf(score)
            //   ,Common.categoryId,Common.categoryName));
        }
    }
}
