package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class chatActivity extends AppCompatActivity {

    private EditText typMsg;
    private ImageView sendMsg;
    private TextView textView;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String sender,reciever,sRoom,rRoom;
    private RecyclerView chatDisList;
    private chatHis chatHis;
    ArrayList<Map<String,String>> arr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        String uid = getIntent().getStringExtra("rId");

        typMsg = findViewById(R.id.typMsg);
        sendMsg = findViewById(R.id.sndMsg);
        textView = findViewById(R.id.rId);
        chatDisList = findViewById(R.id.chatDisList);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        reciever = uid;
        sender = firebaseAuth.getCurrentUser().getUid();

        arr = new ArrayList<>();
        chatHis = new chatHis(chatActivity.this,arr);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        chatDisList.setLayoutManager(linearLayoutManager);

        chatDisList.setAdapter(chatHis);

        firebaseFirestore.collection("users").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                textView.setText(documentSnapshot.getString("name"));
                return;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


        sRoom = sender + reciever;
        rRoom = reciever  + sender;

        sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = typMsg.getText().toString();


                if(msg.equals("")){
                    return;
                } else {

                    Date date = new Date();
                    long time = date.getTime();

                    typMsg.setText("");

                    Map<String,String> hm = new HashMap<>();
                    hm.put("message",msg);
                    hm.put("sId",sender);
                    hm.put("rId",reciever);
                    hm.put("time",""+time);

                    firebaseFirestore.collection("chats").document(sRoom).collection("message").document(time+"").set(hm).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            firebaseFirestore.collection("chats").document(rRoom).collection("message").document(time+"").set(hm);
                            getChat();
                        }
                    });
                }
            }
        });

        getChat();
    }

    private void getChat() {

        arr.clear();
        firebaseFirestore.collection("chats").document(sRoom).collection("message").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for(QueryDocumentSnapshot documentReference : queryDocumentSnapshots){
                    Map<String,String> hm = new HashMap<>();

                    hm.put("message",documentReference.getString("message"));
                    hm.put("sId",documentReference.getString("sId"));
                    hm.put("rId",documentReference.getString("rId"));

                    arr.add(hm);
                }
                chatHis.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(chatActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}