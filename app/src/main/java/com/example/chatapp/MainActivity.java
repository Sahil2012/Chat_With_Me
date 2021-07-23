package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements onChatClick{

    private ImageView logOut;
    private RecyclerView chatList;
    ChatListAdap chatListAdap;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth auth;
    ArrayList<Map<String,String>> arr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        if(auth.getCurrentUser() == null) {
            startActivity(new Intent(MainActivity.this,LogIn.class));
            finish();
            return;
        }

        logOut = findViewById(R.id.logOut);
        chatList = findViewById(R.id.chatList);

        arr = new ArrayList<>();

        chatListAdap = new ChatListAdap(MainActivity.this,arr,MainActivity.this);
        chatList.setLayoutManager(new LinearLayoutManager(MainActivity.this,LinearLayoutManager.VERTICAL,false));

        chatList.setAdapter(chatListAdap);
        getUserList();

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(MainActivity.this,LogIn.class));
                finish();
                return;
            }
        });



    }

    private void getUserList() {

        firebaseFirestore.collection("users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for(QueryDocumentSnapshot document : queryDocumentSnapshots){

                    if(document.getString("uid").equals(auth.getCurrentUser().getUid())){
                        continue;
                    }
                    Map<String,String> hm = new HashMap<>();

                    hm.put("name",document.getString("name"));
                    hm.put("status",document.getString("status"));
                    hm.put("uid",document.getString("uid"));

                    arr.add(hm);
                }

                chatListAdap.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void clickChat(String rUid) {

        Intent it = new Intent(MainActivity.this,chatActivity.class);
        it.putExtra("rId",rUid);

        startActivity(it);
    }
}