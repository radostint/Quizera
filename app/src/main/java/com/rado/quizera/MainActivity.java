package com.rado.quizera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rado.quizera.Common.Common;
import com.rado.quizera.Model.User;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private static final String CHANNEL_ID = "BAIGOSHO";
    EditText registerUsername, registerEmail, registerPassword;
    EditText username, password;
    Button btnSignUp, btnSignIn;
    FirebaseDatabase db;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");

        username = (EditText) findViewById(R.id.fieldUsername);
        password = (EditText) findViewById(R.id.fieldPassword);
        btnSignIn = (Button) findViewById(R.id.btn_signIn);
        btnSignUp = (Button) findViewById(R.id.btn_signUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignUpDialog();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(username.getText().toString(), password.getText().toString());
            }
        });
    }


    private void signIn(final String user, final String pass) {
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(user).exists()) {
                    if (!user.isEmpty()) {
                        User loginUser = dataSnapshot.child(user).getValue(User.class);
                        if (loginUser.getPassword().equals(pass)) {
                            Common.currentUser = loginUser;
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "Login failed!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Please enter your username", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "User does not exist!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showSignUpDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Sign Up");
        alertDialog.setMessage("Please fill in your information");

        LayoutInflater inflater = this.getLayoutInflater();
        View signUp_layout = inflater.inflate(R.layout.sign_up_layout, null);

        registerUsername = (EditText) signUp_layout.findViewById(R.id.registerUsername);
        registerEmail = (EditText) signUp_layout.findViewById(R.id.registerEmail);
        registerPassword = (EditText) signUp_layout.findViewById(R.id.registerPassword);

        alertDialog.setView(signUp_layout);
        alertDialog.setIcon(R.drawable.ic_account_circle_black_24dp);

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final User user = new User(registerUsername.getText().toString(), registerPassword.getText().toString(), registerEmail.getText().toString());

                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(user.getUsername()).exists()) {
                            Toast.makeText(MainActivity.this, "User already exists!", Toast.LENGTH_LONG).show();
                        } else {
                            users.child(user.getUsername()).setValue(user);
                            Toast.makeText(MainActivity.this, "Registration successfull!", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
