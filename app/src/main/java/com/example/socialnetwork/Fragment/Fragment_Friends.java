package com.example.socialnetwork.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.socialnetwork.Adapter.FriendAdapter;
import com.example.socialnetwork.Adapter.SearchFriendAdapter;
import com.example.socialnetwork.Interface.EventListener;
import com.example.socialnetwork.Objects.Account;
import com.example.socialnetwork.Objects.Friend;
import com.example.socialnetwork.Objects.Post;
import com.example.socialnetwork.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Friends extends Fragment implements EventListener {
    View view;
    String email,displayname;
    ListView lv_friend;
    ArrayList<Friend> friends;
    ArrayList<Account>accounts;
    DatabaseReference mDatabase ;
    List<String> keyList = new ArrayList<String>();
    FriendAdapter friendAdapter;
    EditText addfriend;
    ArrayList<Account> accteamp;
    SearchFriendAdapter searchFriendAdapter;
    boolean ignoreChange = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_fragment__friends, container, false);
        email = getArguments().getString("email");
        displayname = getArguments().getString("displayname");
        lv_friend= view.findViewById(R.id.lvFriend);
        addfriend=view.findViewById(R.id.addfriend);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        friends = new ArrayList<>();
        accounts = new ArrayList<>();
        accteamp = new ArrayList<>();
        mDatabase.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                accounts.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    accounts.add(0, snapshot.getValue(Account.class));
                    keyList.add(snapshot.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mDatabase.child("Friendship").child(displayname).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                friends.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    friends.add(snapshot.getValue(Friend.class));
                    //keyList.add(dataSnapshot.getKey());
                    friendAdapter.notifyDataSetChanged();
                }
                friendAdapter = new FriendAdapter(getContext(),R.layout.row_searchfriend,friends,accounts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        friendAdapter = new FriendAdapter(getContext(),R.layout.row_searchfriend,friends,accounts);
        lv_friend.setAdapter(friendAdapter);
        addfriend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               accteamp.clear();
               if (s.length()==0)
               {
                   friendAdapter.notifyDataSetChanged();
                   lv_friend.setAdapter(friendAdapter);
               }
               else
               {
                   for (int i=0;i<accounts.size();i++)
                   {
                       if (accounts.get(i).getAccount_name().toLowerCase().contains(s.toString().toLowerCase()))
                       {
                           accteamp.add(accounts.get(i));
                           searchFriendAdapter.notifyDataSetChanged();
                       }
                   }
                   lv_friend.setAdapter(searchFriendAdapter);
               }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        searchFriendAdapter = new SearchFriendAdapter(getContext(),R.layout.row_searchfriend,accteamp,friends,displayname,Fragment_Friends.this);

        return view;
    }

    @Override
    public void sendComment(Post oldPost, String comment, int position) {

    }

    @Override
    public void addfriend(Account account) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        String id = mDatabase.push().getKey();
        Friend friend = new Friend();
        friend.setIdfriend(id);
        friend.setName_friend(account.getAccount_name());
        mDatabase.child("Friendship").child(displayname).child(id).setValue(friend);
        friendAdapter.notifyDataSetChanged();
        lv_friend.setAdapter(friendAdapter);
        addfriend.setText("");
    }
}