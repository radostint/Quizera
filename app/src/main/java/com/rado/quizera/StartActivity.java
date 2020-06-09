package com.rado.quizera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rado.quizera.Common.Common;
import com.rado.quizera.Model.Question;

import java.util.Collection;
import java.util.Collections;

public class StartActivity extends AppCompatActivity {
    Button btnPlay;
    FirebaseDatabase db;
    DatabaseReference questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        db = FirebaseDatabase.getInstance();
        questions = db.getReference("Questions");

        loadQuestion(Common.categoryId);

        btnPlay = findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, PlayActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadQuestion(String categoryId) {
        if (Common.questionList.size() > 0) {
            Common.questionList.clear();
        }

        questions.orderByChild("categoryId").equalTo(categoryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Question question = postSnapshot.getValue(Question.class);
                    Common.questionList.add(question);
                }
                Collections.shuffle(Common.questionList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //Collections.shuffle(Common.questionList);
    }
}
