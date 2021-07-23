package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogIn extends AppCompatActivity {

    private EditText logInEmail,logInPass;
    private Button logInBtn;
    private TextView signUpBtn;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        auth = FirebaseAuth.getInstance();

        logInEmail = findViewById(R.id.logInEmail);
        logInPass = findViewById(R.id.logInPass);
        logInBtn = findViewById(R.id.logInBtn);
        signUpBtn = findViewById(R.id.signupBtn);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LogIn.this,SignUp.class));
            }
        });

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = logInEmail.getText().toString().trim();
                String pass = logInPass.getText().toString().trim();

                if(pass.length() < 6){
                    Toast.makeText(LogIn.this,"Password Should be of minimum 6 charecter",Toast.LENGTH_SHORT).show();
                } else if(email.equals("")){
                    Toast.makeText(LogIn.this,"Please Enter Email",Toast.LENGTH_SHORT).show();
                } else if(!email.matches(emailPattern)){
                    Toast.makeText(LogIn.this,"Enter Valid Email",Toast.LENGTH_SHORT).show();
                } else {
                    auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                startActivity(new Intent(LogIn.this,MainActivity.class));
                                finish();
                                return;
                            } else {
                                Toast.makeText(LogIn.this,"Invalid Credential",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}