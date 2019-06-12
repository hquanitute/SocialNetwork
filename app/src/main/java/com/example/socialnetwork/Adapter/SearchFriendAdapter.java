package com.example.socialnetwork.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.socialnetwork.Interface.EventListener;
import com.example.socialnetwork.Objects.Account;
import com.example.socialnetwork.Objects.Friend;
import com.example.socialnetwork.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchFriendAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Account> accounts;
    String displayName;
    DatabaseReference mDatabase ;
    ArrayList<Friend> friends;
    EventListener listener;
    public SearchFriendAdapter() {
    }

    public SearchFriendAdapter(Context context, int layout, List<Account> accounts,ArrayList<Friend> friends,String displayName,EventListener listener) {
        this.context = context;
        this.layout = layout;
        this.accounts = accounts;
        this.friends=friends;
        this.displayName=displayName;
        this.listener=listener;
    }

    @Override
    public int getCount() {
        return accounts.size();
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
        mDatabase = FirebaseDatabase.getInstance().getReference();

        final ViewHolder viewHolder;
        getdata(displayName);

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.row_searchfriend,parent,false);
            viewHolder.tvNamesearch=convertView.findViewById(R.id.tvNamesearch);
            viewHolder.tvAvatarsearch=convertView.findViewById(R.id.tvAvatarsearch);
            viewHolder.btnaddfriend=convertView.findViewById(R.id.btnaddfriend);

            final Account account = accounts.get(position);
            viewHolder.tvAvatarsearch.setText(account.getId());
            viewHolder.tvNamesearch.setText(account.getAccount_name());
            int flag=0;
            for (int i=0;i<friends.size();i++)
            {
                if (friends.get(i).getName_friend().equals(accounts.get(position).getAccount_name()))
                {
                    flag=1;
                    break;
                }
            }
            if(flag==1){
                viewHolder.btnaddfriend.setVisibility(View.INVISIBLE);
            }
            viewHolder.btnaddfriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.addfriend(account);
                }
            });

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    private class ViewHolder {
        TextView tvAvatarsearch, tvNamesearch,tvAvatar,tvName;
        Button btnaddfriend;
    }
    private void getdata(String displayName)
    {
        mDatabase.child("Friendship").child(displayName).addChildEventListener(new ChildEventListener() {
            @Override

            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                friends.clear();
                friends.add(dataSnapshot.getValue(Friend.class));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
