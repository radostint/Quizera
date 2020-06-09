package com.rado.quizera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rado.quizera.Common.Common;
import com.squareup.picasso.Picasso;

public class PlayActivity extends AppCompatActivity implements View.OnClickListener {
    final static long INTERVAL = 1000;
    final static long TIMEOUT = 20000;
    int progressValue = 0;

    CountDownTimer mCountDown;

    int index = 0, score = 0, thisQuestion = 0, totalQuestion, correctAnswer;


    ProgressBar progressBar;
    ImageView questionImage;
    Button btnA, btnB, btnC, btnD;
    TextView txtScore, txtQuestionNum, questionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);


        txtScore = findViewById(R.id.txtScore);
        txtQuestionNum = findViewById(R.id.txtTotalQuestions);
        questionText = findViewById(R.id.question_text);
        questionImage = findViewById(R.id.question_image);
        progressBar = findViewById(R.id.progressBar);

        btnA = findViewById(R.id.btnAnswerA);
        btnB = findViewById(R.id.btnAnswerB);
        btnC = findViewById(R.id.btnAnswerC);
        btnD = findViewById(R.id.btnAnswerD);

        btnA.setOnClickListener(this);
        btnB.setOnClickListener(this);
        btnC.setOnClickListener(this);
        btnD.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        MediaPlayer mp = MediaPlayer.create(this, R.raw.correct);
        mCountDown.cancel();
        if (index < totalQuestion) {
            //ако е избран верния отговор се добавят 10 точки
            Button clickedButton = (Button) v;
            if (clickedButton.getText().equals(Common.questionList.get(index).getCorrectAnswer())) {
                mp.start();
                score += 10;
                correctAnswer++;
            }
            showQuestion(++index);
            txtScore.setText(String.format("%d", score));
        }
    }

    private void showQuestion(int index) {
        if (index < totalQuestion) {
            thisQuestion++;
            txtQuestionNum.setText(String.format("%d / %d", thisQuestion, totalQuestion));
            progressBar.setProgress(0);
            progressValue = 0;

            if (Common.questionList.get(index).getIsImageQuestion().equals("true")) {
                //ако въпросът е снимка
                Picasso.with(getBaseContext()).load(Common.questionList.get(index).getQuestion()).into(questionImage);
                questionImage.setVisibility(View.VISIBLE);
                questionText.setVisibility(View.INVISIBLE);
            } else {
                questionText.setText(Common.questionList.get(index).getQuestion());
                questionImage.setVisibility(View.INVISIBLE);
                questionText.setVisibility(View.VISIBLE);
            }
            btnA.setText(Common.questionList.get(index).getAnswerA());
            btnB.setText(Common.questionList.get(index).getAnswerB());
            btnC.setText(Common.questionList.get(index).getAnswerC());
            btnD.setText(Common.questionList.get(index).getAnswerD());
            mCountDown.start();
        } else {
            //ако е последен въпрос
            Intent intent = new Intent(this, DoneActivity.class);
            Bundle sendData = new Bundle();
            sendData.putInt("SCORE", score);
            sendData.putInt("TOTAL", totalQuestion);
            sendData.putInt("CORRECT", correctAnswer);
            intent.putExtras(sendData);
            startActivity(intent);
            finish();
        }
    }
    @Override
    public void onBackPressed()
    {
        mCountDown.cancel();
        Intent intent = new Intent(this, DoneActivity.class);
        Bundle sendData = new Bundle();
        sendData.putInt("SCORE", score);
        sendData.putInt("TOTAL", totalQuestion);
        sendData.putInt("CORRECT", correctAnswer);
        intent.putExtras(sendData);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onPostResume() {
        super.onPostResume();

        totalQuestion = Common.questionList.size();
        mCountDown = new CountDownTimer(TIMEOUT, INTERVAL) {
            @Override
            public void onTick(long milis) {
                progressBar.setProgress(progressValue);
                progressValue++;
            }

            @Override
            public void onFinish() {
                Toast.makeText(PlayActivity.this, "Времето изтече!", Toast.LENGTH_SHORT).show();
                showQuestion(++index);


            }
        };
        showQuestion(index);
    }
}
