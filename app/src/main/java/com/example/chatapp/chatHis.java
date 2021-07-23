package com.example.chatapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.Map;

public class chatHis extends RecyclerView.Adapter{

    ArrayList<Map<String,String>> arr;
    Context context;
    String myUid = FirebaseAuth.getInstance().getUid();

    public chatHis(Context context,ArrayList<Map<String,String>> arr){
        this.arr = arr;
        this.context = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == 1){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.s_chat,parent,false);
            return new ViewHoldS(v);
        }

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.r_chat,parent,false);
        return new ViewHoldR(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder.getClass() == ViewHoldR.class){

            ViewHoldR viewHoldR = (ViewHoldR)holder;

            viewHoldR.rM.setText(arr.get(position).get("message"));
            viewHoldR.circularImageView.setImageResource(R.drawable.ic_baseline_chat_24);
        } else {

            ViewHoldS viewHoldS = (ViewHoldS)holder;

            viewHoldS.sM.setText(arr.get(position).get("message"));
            viewHoldS.circularImageView.setImageResource(R.drawable.user);
        }
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(arr.get(position).get("sId").equals(myUid)){
            return 1;
        }

        return 2;
    }
}

class ViewHoldR extends RecyclerView.ViewHolder {

    TextView rM;
    CircularImageView circularImageView;

    public ViewHoldR(@NonNull View itemView) {
        super(itemView);
        this.rM = itemView.findViewById(R.id.rMsg);
        this.circularImageView = itemView.findViewById(R.id.rImg);
    }
}

class ViewHoldS extends RecyclerView.ViewHolder {

    TextView sM;
    CircularImageView circularImageView;

    public ViewHoldS(@NonNull View itemView) {
        super(itemView);
        this.sM = itemView.findViewById(R.id.sMsg);
        this.circularImageView = itemView.findViewById(R.id.sImg);
    }
}


