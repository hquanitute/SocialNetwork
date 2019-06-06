package com.example.socialnetwork.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.socialnetwork.Objects.Message;
import com.example.socialnetwork.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    Context mContext;
    List<Message> mMessage;
    String profile_Image;

    public static final int MSG_STATUS_LEFT=0;
    public static final int MSG_STATUS_RIGHT=1;

    public MessageAdapter (Context context,List<Message> messages,String profile_Image){
        this.mContext=context;
        this.mMessage=messages;
        this.profile_Image=profile_Image;
    }

    @Override
    public int getItemViewType(int position) {
        if(mMessage.get(position).getIdSender().equals("phucvo")){
            return MSG_STATUS_RIGHT;
        } else {
            return MSG_STATUS_LEFT;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if(viewType==MSG_STATUS_LEFT){
            View view= LayoutInflater.from(mContext).inflate(R.layout.message_left,viewGroup,false);
            return new MessageAdapter.ViewHolder(view);
        }
        else
        {
            View view= LayoutInflater.from(mContext).inflate(R.layout.message_right,viewGroup,false);
            return new MessageAdapter.ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Message message=mMessage.get(i);

        viewHolder.tvMessage.setText(message.getMessage());
        if(profile_Image.equals("default")){
            viewHolder.profile_Image.setImageResource(R.mipmap.ic_launcher);
        }
        else {
            Glide.with(mContext).load(profile_Image).into(viewHolder.profile_Image);
        }

    }

    @Override
    public int getItemCount() {
        return mMessage.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView profile_Image;
        TextView tvMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profile_Image=itemView.findViewById(R.id.profile_image);
            tvMessage=itemView.findViewById(R.id.showmessage);


        }
    }
}
