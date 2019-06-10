package com.example.socialnetwork.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.socialnetwork.ChatActivity;
import com.example.socialnetwork.Objects.Account;
import com.example.socialnetwork.Objects.Message;
import com.example.socialnetwork.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserMessageAdapter extends RecyclerView.Adapter<UserMessageAdapter.ViewHolder> {
    Context mContext;
    List<Message> mMessage;
    Account sender;
    Account receive;
    List<Account> accounts=new ArrayList<>();

    String imageURL_sender;
    String getImageURL_receiver;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;



    public UserMessageAdapter (Context context,List<Message> messages,Account sender,Account receiver,List<Account> accounts){
        this.mContext=context;
        this.mMessage=messages;
        this.sender=sender;
        this.receive=receiver;
        this.accounts=accounts;

    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

            View view= LayoutInflater.from(mContext).inflate(R.layout.user_message,viewGroup,false);
            return new UserMessageAdapter.ViewHolder(view);


    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        Message message=mMessage.get(i);

        viewHolder.tvMessage.setText(message.getMessage());
        if(!message.getIdSender().equals(sender.getAccount_name())){
            viewHolder.tvName.setText(message.getIdSender());
            for(Account _account:accounts){
                if(_account.getAccount_name().equals(message.getIdSender())){
                    receive=_account;
                    if(_account.getImageURL().equals("default")){
                        viewHolder.profile_Image.setImageResource(R.mipmap.ic_launcher);
                    }
                    else {
                        Glide.with(mContext).load(_account.getImageURL()).into(viewHolder.profile_Image);
                    }
                }
            }
        }
        if(!message.getIdReceiver().equals(sender.getAccount_name())){
            viewHolder.tvName.setText(message.getIdReceiver());
            for(Account _account:accounts){
                if(_account.getAccount_name().equals(message.getIdReceiver())){
                    receive=_account;
                    if(_account.getImageURL().equals("default")){
                        viewHolder.profile_Image.setImageResource(R.mipmap.ic_launcher);
                    }
                    else {
                        Glide.with(mContext).load(_account.getImageURL()).into(viewHolder.profile_Image);
                    }
                }
            }
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent _intent=new Intent(mContext, ChatActivity.class);
                _intent.putExtra("receiver",receive.getAccount_name());
                mContext.startActivity(_intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mMessage.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView profile_Image;
        TextView tvMessage;
        TextView tvName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profile_Image=itemView.findViewById(R.id.profile_image);
            tvMessage=itemView.findViewById(R.id.tvMessage);
            tvName=itemView.findViewById(R.id.tvName);


        }
    }
}