package com.example.socialnetwork.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.socialnetwork.ChatActivity;
import com.example.socialnetwork.Objects.Account;
import com.example.socialnetwork.Objects.Friend;
import com.example.socialnetwork.R;

import java.util.List;

public class FriendAdapter  extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Friend> friends;
    private List<Account> accounts;
    public FriendAdapter(Context context, int layout, List<Friend> friends, List<Account> accounts) {
        this.context = context;
        this.layout = layout;
        this.friends = friends;
        this.accounts=accounts;
    }

    @Override
    public int getCount() {
        return friends.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        if (convertView==null)
        {
            convertView= LayoutInflater.from(context).inflate(R.layout.row_friend,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.tvAvatar=convertView.findViewById(R.id.tvAvatar);
            viewHolder.tvName=convertView.findViewById(R.id.tvName);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Friend friend = friends.get(position);
        //viewHolder.tvAvatar.setText(friend.getIdfriend());
        for(Account _account:accounts){
            if(_account.getAccount_name().equals(friend.getName_friend())){
                if(_account.getImageURL().equals("default")){
                    viewHolder.tvAvatar.setImageResource(R.mipmap.ic_launcher);
                }
                else {
                    Glide.with(context).load(_account.getImageURL()).into(viewHolder.tvAvatar);
                }
                break;
            }
        }
        viewHolder.tvName.setText(friend.getName_friend());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ChatActivity.class);
                intent.putExtra("receiver",friend.getName_friend());
                context.startActivity(intent);
            }
        });
        return convertView;
    }
    private class ViewHolder {
        TextView tvName;
        ImageView tvAvatar;
    }
}
