package com.example.socialnetwork.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.socialnetwork.Adapter.MessageAdapter;
import com.example.socialnetwork.Objects.Account;
import com.example.socialnetwork.Objects.Message;
import com.example.socialnetwork.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Fragment_Chating extends Fragment {
    View view;
    TextView username;
    EditText edt_Message;
    ImageButton btnSend;
    CircleImageView profile_image;
    RecyclerView recyclerView;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;

    String userid;
    String s_username;
    List<Message> mMessage;
    MessageAdapter messageAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_fragment__chating, container, false);


        connectView();
        profile_image.setImageResource(R.mipmap.ic_launcher);

        /*firebase.auth().onAuthStateChanged(function(user) {
            if (user) {
                // User is signed in.
            } else {
                // No user is signed in.
            }
        });*/

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        userid=firebaseUser.getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Account account=snapshot.getValue(Account.class);
                    if(account.getId().equals(userid)){
                        s_username=account.getAccount_name();
                        username.setText(s_username);
                        if(account.getImageURL().equals("default")){
                            profile_image.setImageResource(R.mipmap.ic_launcher);
                        }
                        else {
                            Glide.with(getContext()).load(account.getImageURL()).into(profile_image);
                        }
                        readMessage(s_username,"quanoccho","default");
                        return;
                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edt_Message.getText().toString().equals("")){
                    sendMessage(s_username,"quanoccho",edt_Message.getText().toString());

                }
                else {
                    Toast.makeText(getContext(),"Nhập nội dung tin nhắn!",Toast.LENGTH_LONG).show();
                }
            }
        });


        return view;
    }

    private void connectView() {
        username=view.findViewById(R.id.tv_profile);
        profile_image=view.findViewById(R.id.profile_image);
        edt_Message=view.findViewById(R.id.edt_Message);
        btnSend=view.findViewById(R.id.btnSend);
        recyclerView=view.findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity().getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public void singin(){

    }

    public void sendMessage (String idSender,String idReceive, String _message){
        databaseReference=FirebaseDatabase.getInstance().getReference();

        Message message=new Message();
        message.setIdReceiver(idReceive);
        message.setIdSender(idSender);
        message.setMessage(_message);

        databaseReference.child("Chats").push().setValue(message);
        edt_Message.setText("");

    }

    public void createUser(final String username, String email, String password){
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            Toast.makeText(getContext(),"Thanh cong",Toast.LENGTH_LONG).show();

                            FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();

                            assert firebaseUser != null;
                            String userid=firebaseUser.getUid();

                            databaseReference=FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            HashMap<String,String> hashMap=new HashMap<>();
                            hashMap.put("id",userid);
                            hashMap.put("account_name",username);
                            hashMap.put("imageURL","default");

                            databaseReference.setValue(hashMap);
                        }
                        else {
                            Toast.makeText(getContext(),"That bai",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void readMessage (final String myid, final String userid, final String profile_image){
        mMessage=new ArrayList<>();
        databaseReference=FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mMessage.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Message message=snapshot.getValue(Message.class);
                    if(message.getIdReceiver().equals(myid) && message.getIdSender().equals(userid) ||
                    message.getIdSender().equals(myid)&&message.getIdReceiver().equals(userid)){
                        mMessage.add(message);
                    }
                    messageAdapter=new MessageAdapter(getContext(),mMessage,profile_image);
                    recyclerView.setAdapter(messageAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
