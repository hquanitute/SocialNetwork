package com.example.socialnetwork.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.socialnetwork.ChatActivity;
import com.example.socialnetwork.Objects.Account;
import com.example.socialnetwork.Objects.Message;
import com.example.socialnetwork.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    Context mContext;
    List<Message> mMessage;
    Account sender;
    Account receive;

    String imageURL_sender;
    String getImageURL_receiver;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;

    public static final int MSG_STATUS_LEFT=0;
    public static final int MSG_STATUS_RIGHT=1;

    public MessageAdapter (Context context,List<Message> messages,Account sender,Account receiver){
        this.mContext=context;
        this.mMessage=messages;
        this.sender=sender;
        this.receive=receiver;
    }

    @Override
    public int getItemViewType(int position) {
        if(mMessage.get(position).getIdSender().equals(sender.getAccount_name())){
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
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
       Message message=mMessage.get(i);
        viewHolder.tvMessage.setText(message.getMessage());
        if(message.getIdSender().equals(sender.getAccount_name())){
            if(sender.getImageURL().equals("default")){
                viewHolder.profile_Image.setImageResource(R.mipmap.ic_launcher);
            }
            else {
                Glide.with(mContext).load(sender.getImageURL()).into(viewHolder.profile_Image);
            }

        }
        else{
            if(receive.getImageURL().equals("default")){
                viewHolder.profile_Image.setImageResource(R.mipmap.ic_launcher);
            }
            else {
                Glide.with(mContext).load(receive.getImageURL()).into(viewHolder.profile_Image);
            }
        }
        if(!message.getImage().equals("default")){
            Glide.with(mContext).load(message.getImage()).into(viewHolder.imageView);
            viewHolder.imageView.setVisibility(View.VISIBLE);
        }
        if(!message.getVideo().equals("default")){
            viewHolder.videoView.setVisibility(View.VISIBLE);
            viewHolder.videoView.setVideoPath(message.getVideo());
            viewHolder.videoView.seekTo(1);
            MediaController mediaController=new MediaController(mContext);
            viewHolder.videoView.setMediaController(mediaController);
            mediaController.setAnchorView(viewHolder.videoView);
            viewHolder.videoView.start();
        }
        if(message.getMessage().equals("")){
            viewHolder.tvMessage.setVisibility(View.GONE);
        }
    }
    @Override
    public int getItemCount() {
        return mMessage.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView profile_Image;
        TextView tvMessage;
        ImageView imageView;
        VideoView videoView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profile_Image=itemView.findViewById(R.id.profile_image);
            tvMessage=itemView.findViewById(R.id.showmessage);
            imageView=itemView.findViewById(R.id.imageView);
            videoView=itemView.findViewById(R.id.videoView);
        }
    }
}
