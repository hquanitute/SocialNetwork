package com.example.socialnetwork;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.socialnetwork.Adapter.MessageAdapter;
import com.example.socialnetwork.Objects.Account;
import com.example.socialnetwork.Objects.Message;
import com.example.socialnetwork.Objects.Post;
import com.google.android.gms.tasks.Continuation;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
    View view;
    TextView username;
    EditText edt_Message;
    ImageButton btnSend;
    ImageButton btnImageSend;
    CircleImageView profile_image;
    RecyclerView recyclerView;
    ImageView imageView;
    VideoView videoView;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://social-network-14488.appspot.com");
    Account receiveAccount;
    Account senderAccount;
    String receiverUsername;
    String Linkimage;
    List<Message> mMessage;
    MessageAdapter messageAdapter;
    Intent intent;
    private static final int PICK_IMAGE=100;
    private static final int PICK_VIDEO=100;
    private static String imagename;
    Uri imageUri;
    Boolean check=false;
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
                    }
                    if(account.getAccount_name().equals(receiveAccount.getAccount_name())){
                        receiveAccount=account;
                        username.setText(receiveAccount.getAccount_name());
                        if(receiveAccount.getImageURL().equals("default")){
                            profile_image.setImageResource(R.mipmap.ic_launcher);
                        }
                        else {
                            Glide.with(ChatActivity.this).load(receiveAccount.getImageURL()).into(profile_image);
                        }
                    }
                }
                readMessage(senderAccount.getAccount_name(),receiverUsername);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnImageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opengallery();
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
        btnImageSend=findViewById(R.id.btnImageSend);
        imageView=findViewById(R.id.imageView);
        recyclerView=findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        videoView=findViewById(R.id.videoView);
    }

    public void singin(){

    }

    /*public void sendMessage (String idSender,String idReceive, String _message){


    }*/

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


    private void opengallery() {
       /* Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery,PICK_IMAGE);*/

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("*/*");
        startActivityForResult(galleryIntent, PICK_IMAGE);

       /* Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*,video/*,audio/*");
        startActivityForResult(Intent.createChooser(intent, "Select Media"), CameraUtils.REQUEST_GALLERY);*/
    }
    private  void opengalleryvideo()
    {
        Intent gallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_VIDEO);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode== Activity.RESULT_OK && requestCode==PICK_IMAGE)
        {
            String type=data.getType();
            imageUri = data.getData();

            ContentResolver cr = this.getContentResolver();
            String mime = cr.getType(imageUri);
            if(mime.equals("image/png")||mime.equals("image/jpeg")){
                imageView.setImageURI(imageUri);
                imageView.setVisibility(View.VISIBLE);
                Cursor cursor = this.getContentResolver().query(imageUri, null, null, null, null);
                int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                cursor.moveToFirst();
                imagename= cursor.getString(nameIndex);
                imagename= imagename.substring(0,imagename.length()-4);
            }
            if(mime.equals("video/mp4")){
                //imageView.setImageURI(imageUri);
                videoView.setVisibility(View.VISIBLE);
                Cursor cursor = this.getContentResolver().query(imageUri, null, null, null, null);
                int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                cursor.moveToFirst();
                imagename= cursor.getString(nameIndex);
                imagename= imagename.substring(0,imagename.length()-4);
                videoView.setVideoURI(imageUri);
                MediaController mediaController=new MediaController(this);
                videoView.setMediaController(mediaController);
                mediaController.setAnchorView(videoView);
                videoView.start();
                check=true;
            }
        }
    }

    public void sendMessage(final String idSender, final String idReceive, final String _message) {
        if (imageView.getDrawable()!=null) {
            imageView.setDrawingCacheEnabled(true);
            imageView.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] data = baos.toByteArray();
            final StorageReference mountainsRef = storageRef.child(imagename);
            final UploadTask uploadTask = mountainsRef.putBytes(data);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return mountainsRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        Linkimage = downloadUri.toString();

                        databaseReference=FirebaseDatabase.getInstance().getReference();

                        Message message=new Message();
                        message.setIdReceiver(idReceive);
                        message.setIdSender(idSender);
                        message.setMessage(_message);
                        message.setImage(Linkimage);
                        message.setVideo("default");


                        databaseReference.child("Chats").push().setValue(message);
                        edt_Message.setText("");
                        imageView.setVisibility(View.GONE);
                        imageView.setImageDrawable(null);
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });
        }
        else if(check){
            videoView.setDrawingCacheEnabled(true);
            videoView.buildDrawingCache();
            final StorageReference mountainsRef = storageRef.child(imagename);
            UploadTask uploadTask = mountainsRef.putFile(imageUri);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return mountainsRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        Linkimage = downloadUri.toString();

                        databaseReference=FirebaseDatabase.getInstance().getReference();

                        Message message=new Message();
                        message.setIdReceiver(idReceive);
                        message.setIdSender(idSender);
                        message.setMessage(_message);
                        message.setImage("default");
                        message.setVideo(Linkimage);


                        databaseReference.child("Chats").push().setValue(message);
                        edt_Message.setText("");
                        /*imageView.setVisibility(View.GONE);
                        imageView.setImageDrawable(null);*/

                        videoView.setVisibility(View.GONE);
                        videoView.setVideoURI(null);
                        check=false;
                        /*String status = et_status.getText().toString();
                        Post post = new Post();
                        post.setAccount_name(displayname);
                        post.setText(status);
                        String id = mDatabase.push().getKey();
                        post.setPost_id(id);
                        if (videoView.getDrawableState()!=null) {
                            post.setVideo(downloadUri.toString());
                            Toast.makeText(getContext(), "Co hinh ne", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Ko co hinh", Toast.LENGTH_SHORT).show();
                        }
                        mDatabase.child("Post").child(id).setValue(post);*/
                    }
                }
            });
        }
        else
        {
            databaseReference=FirebaseDatabase.getInstance().getReference();

            Message message=new Message();
            message.setIdReceiver(idReceive);
            message.setIdSender(idSender);
            message.setMessage(_message);
            message.setImage("default");
            message.setVideo("default");


            databaseReference.child("Chats").push().setValue(message);
            edt_Message.setText("");
            imageView.setVisibility(View.GONE);
            videoView.setVisibility(VideoView.GONE);

           /* String status = et_status.getText().toString();
            Post post = new Post();
            post.setAccount_name(displayname);
            post.setText(status);
            String id = mDatabase.push().getKey();
            post.setPost_id(id);
            post.setComments(null);
            mDatabase.child("Post").child(id).setValue(post);*/
        }


    }

}
