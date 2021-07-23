package com.example.chatapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Map;

public class ChatListAdap extends RecyclerView.Adapter<ViewHold> {

    ArrayList<Map<String,String>> arr;
    Context context;
    onChatClick click;

    public ChatListAdap(Context context,ArrayList<Map<String,String>> arr,onChatClick click){
        this.arr = arr;
        this.click = click;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list,parent,false);

        return new ViewHold(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHold holder, int position) {

        holder.name.setText(arr.get(position).get("name"));
        holder.stat.setText(arr.get(position).get("status"));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click.clickChat(arr.get(position).get("uid"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }
}

class ViewHold extends RecyclerView.ViewHolder{

    TextView name;
    TextView stat;

    public ViewHold(@NonNull View itemView) {
        super(itemView);
        this.name = itemView.findViewById(R.id.nameChat);
        this.stat = itemView.findViewById(R.id.statChat);
    }
}

interface onChatClick {

    void clickChat(String uid);
}