package com.example.socialnetwork.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.socialnetwork.Objects.Post;
import com.example.socialnetwork.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Fragment_NewFeed extends Fragment {
    View view;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabase = database.getReference("Post");
    EditText et_status;
    Button btnPush;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_fragment__new_feed, container, false);
        et_status= view.findViewById(R.id.etStatus);
        btnPush=view.findViewById(R.id.btnGui);
        btnPush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushPost();
            }
        });
        return view;
    }

    public void pushPost(){
        String status=et_status.getText().toString();
        Post post= new Post();
        post.setAccount_name("Quan");
        post.setText(status);
        String id= mDatabase.push().getKey();
        post.setPost_id(id);
        mDatabase.child(id).setValue(post);
    }
}
