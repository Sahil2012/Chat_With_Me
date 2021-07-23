package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    private EditText pass,conPass,signEmail,userName;
    private Button signUpBtn;
    private TextView loginBtn;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    FirebaseAuth auth;
    FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signEmail = findViewById(R.id.SignUpEmail);
        pass = findViewById(R.id.SignUpPass);
        conPass = findViewById(R.id.SignUpPassCon);
        signUpBtn = findViewById(R.id.SignUpBtn);
        loginBtn = findViewById(R.id.signLogBtn);
        userName = findViewById(R.id.nameUser);

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        signUpBtn.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {

                String email = signEmail.getText().toString().trim();
                String password = pass.getText().toString().trim();
                String passConf = conPass.getText().toString().trim();
                String name = userName.getText().toString().trim();

                if(email.equals("")){
                    Toast.makeText(SignUp.this,"Enter Email",Toast.LENGTH_SHORT).show();
                } else if(!email.matches(emailPattern)){
                    Toast.makeText(SignUp.this,"Enter valid Email",Toast.LENGTH_SHORT).show();
                } else if(name.equals("")){
                    Toast.makeText(SignUp.this,"Enter valid Name",Toast.LENGTH_SHORT).show();
                } else if(password.equals("") || password.length() < 6){
                    Toast.makeText(SignUp.this,"Password should be of minimum 6 charecters",Toast.LENGTH_SHORT).show();
                } else if(!password.equals(passConf)){
                    Toast.makeText(SignUp.this,"Confirm password failed",Toast.LENGTH_SHORT).show();
                } else {

                    auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            Map<String,String> hm = new HashMap<>();
                            hm.put("email",email);
                            hm.put("uid",auth.getUid());
                            hm.put("name",name);
                            hm.put("status","Hello");

                            firebaseFirestore.collection("users").document(auth.getUid()).set(hm).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SignUp.this,"Error to register",Toast.LENGTH_SHORT).show();
                                }
                            });
                            /*
                            firebaseFirestore.collection("users").add(hm).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SignUp.this,"Error to register",Toast.LENGTH_SHORT).show();
                                }
                            });*/
                        }
                    });
                }
            }
        });

    }
}