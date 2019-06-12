package com.example.socialnetwork;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.socialnetwork.Adapter.MessageAdapter;
import com.example.socialnetwork.Objects.Account;
import com.example.socialnetwork.Objects.Message;
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

public class ChatActivity extends AppCompatActivity {
    View view;
    TextView username;
    EditText edt_Message;
    ImageButton btnSend;
    CircleImageView profile_image;
    RecyclerView recyclerView;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;

    Account receiveAccount;
    Account senderAccount;



    String receiverUsername;

    List<Message> mMessage;
    MessageAdapter messageAdapter;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        connectView();

        intent= getIntent();
        receiverUsername=intent.getStringExtra("receiver");

        receiveAccount=new Account();
        senderAccount=new Account();
        receiveAccount.setAccount_name(receiverUsername);
        receiverUsername=receiveAccount.getAccount_name();


        profile_image.setImageResource(R.mipmap.ic_launcher);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        final String userid=firebaseUser.getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Account account=snapshot.getValue(Account.class);
                    if(account.getId().equals(userid)){
                        senderAccount=account;
                        username.setText(senderAccount.getAccount_name());
                        if(account.getImageURL().equals("default")){
                            profile_image.setImageResource(R.mipmap.ic_launcher);
                        }
                        else {
                            Glide.with(ChatActivity.this).load(account.getImageURL()).into(profile_image);
                        }


                    }
                    if(account.getAccount_name().equals(receiveAccount.getAccount_name())){
                        receiveAccount=account;
                    }
                }
                readMessage(senderAccount.getAccount_name(),receiverUsername);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edt_Message.getText().toString().equals("")){
                    sendMessage(senderAccount.getAccount_name(),receiverUsername,edt_Message.getText().toString());

                }
                else {
                    Toast.makeText(ChatActivity.this,"Nhập nội dung tin nhắn!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void connectView() {
        username=findViewById(R.id.tv_profile);
        profile_image=findViewById(R.id.profile_image);
        edt_Message=findViewById(R.id.edt_Message);
        btnSend=findViewById(R.id.btnSend);
        recyclerView=findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
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

                            Toast.makeText(ChatActivity.this,"Thanh cong",Toast.LENGTH_LONG).show();

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
                            Toast.makeText(ChatActivity.this,"That bai",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void readMessage (final String myid, final String receiverId){
        mMessage=new ArrayList<>();
        mMessage.clear();
        databaseReference=FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mMessage.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Message message=snapshot.getValue(Message.class);
                    if(message.getIdReceiver().equals(myid) && message.getIdSender().equals(receiverId) ||
                            message.getIdSender().equals(myid)&&message.getIdReceiver().equals(receiverId)){
                        mMessage.add(message);
                    }
                    messageAdapter=new MessageAdapter(ChatActivity.this,mMessage,senderAccount,receiveAccount);
                    recyclerView.setAdapter(messageAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
