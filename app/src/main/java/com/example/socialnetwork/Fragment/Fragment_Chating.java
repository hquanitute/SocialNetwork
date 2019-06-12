package com.example.socialnetwork.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.socialnetwork.Adapter.UserMessageAdapter;
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
import java.util.ArrayList;
import java.util.List;


import de.hdodenhof.circleimageview.CircleImageView;

public class Fragment_Chating extends Fragment {
    View view;
    String email,displayname;
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



    List<Account> _account=new ArrayList<>();



    String receiverUsername;
    private boolean isViewShown=false;
    private boolean isStarted=false;

    List<Message> mMessage;
    UserMessageAdapter userMessageAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_fragment__chating, container, false);

        isStarted=true;

        email = getArguments().getString("email");
        displayname = getArguments().getString("displayname");

        connectView();
        profile_image.setImageResource(R.mipmap.ic_launcher);

        if(isViewShown){
            getData();
        }

     /*   setUserVisibleHint(isViewShown);
        isViewShown=true;*/

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        isStarted = false;
    }

    public void getData(){
        _account.clear();
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        final String userid=firebaseUser.getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Account account=snapshot.getValue(Account.class);
                    _account.add(account);
                    if(account.getId().equals(userid)){
                        senderAccount=account;
                        username.setText(senderAccount.getAccount_name());
                        if(account.getImageURL().equals("default")){
                            profile_image.setImageResource(R.mipmap.ic_launcher);
                        }
                        else {
                            Glide.with(getContext()).load(senderAccount.getImageURL()).into(profile_image);
                        }
                    }
                  /*  if(account.getAccount_name().equals(receiveAccount.getAccount_name())){
                        receiveAccount=account;
                    }*/

                }
                getMessage();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getMessage() {
        mMessage=new ArrayList<>();
        mMessage.clear();
        databaseReference= FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Message message=snapshot.getValue(Message.class);
//                    if(message.getIdSender().equals(senderAccount.getAccount_name()) || message.getIdReceiver().equals(senderAccount.getAccount_name())){
//                        mMessage.add(message);
//                    }
                }
                xulyMessage();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isViewShown=isVisibleToUser;
        if(isViewShown && isStarted){
            mMessage.clear();
            getData();
        }

    }

    private void xulyMessage() {
        List<Message> _message=new ArrayList<>();
        ArrayList<ArrayList<Message>> lsMessage_User=new ArrayList<>();
        _message.clear();
        lsMessage_User.clear();
        for(Account _mAccount:_account){
            ArrayList<Message> tempMessage=new ArrayList<>();
            for(Message _mMessage:mMessage){
                if(((_mMessage.getIdSender().equals(_mAccount.getAccount_name())&&_mMessage.getIdReceiver().equals(senderAccount.getAccount_name())) ||
                        ( _mMessage.getIdReceiver().equals(_mAccount.getAccount_name())&&_mMessage.getIdSender().equals(senderAccount.getAccount_name())))
                        &&!_mAccount.getAccount_name().equals(senderAccount.getAccount_name())){
                    tempMessage.add(_mMessage);
                }
            }
            if(tempMessage.size()!=0){
                lsMessage_User.add(tempMessage);
            }

        }
        for(ArrayList lstemp:lsMessage_User){
            Message mstemp=(Message) lstemp.get(lstemp.size()-1);
            _message.add(mstemp);

        }
        userMessageAdapter=new UserMessageAdapter(getContext(),_message,senderAccount,receiveAccount,_account);
        recyclerView.setAdapter(userMessageAdapter);
    }

    private void connectView() {
        username=view.findViewById(R.id.tv_profile);
        profile_image=view.findViewById(R.id.profile_image);

        recyclerView=view.findViewById(R.id.userMessager_recycle);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
    }

  /*  public void singin(){

    }*/

   /* public void sendMessage (String idSender,String idReceive, String _message){
        databaseReference=FirebaseDatabase.getInstance().getReference();

        Message message=new Message();
        message.setIdReceiver(idReceive);
        message.setIdSender(idSender);
        message.setMessage(_message);

        databaseReference.child("Chats").push().setValue(message);
        edt_Message.setText("");

    }*/

    /*public void createUser(final String username, String email, String password){
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
    }*/


    }



