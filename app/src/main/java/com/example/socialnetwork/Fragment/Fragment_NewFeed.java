package com.example.socialnetwork.Fragment;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import com.example.socialnetwork.Adapter.StatusAdapter;
import com.example.socialnetwork.Interface.EventListener;
import com.example.socialnetwork.Objects.Account;
import com.example.socialnetwork.Objects.Comment;
import com.example.socialnetwork.Objects.Friend;
import com.example.socialnetwork.Objects.Post;
import com.example.socialnetwork.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
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
import java.util.Date;
import java.util.List;
public class Fragment_NewFeed extends Fragment implements EventListener {
    String email,displayname;
    View view;
    DatabaseReference mDatabase ;
    EditText et_status;
    Button btnPush;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://social-network-14488.appspot.com");
    ImageView imageView;
    Button btnimage;
    private static final int PICK_IMAGE=100;
    Uri imageUri;
    String Linkimage;
    private static String imagename;
    ListView lv_listStatus;
    ArrayList<Post> posts;
    ArrayList<Post> dspost= new ArrayList<>();
    StatusAdapter adapter;
    List<String> keyList = new ArrayList<String>();
    private int vitri;
    ArrayList<Friend> friends;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_fragment__new_feed, container, false);
        email = getArguments().getString("email");
        displayname = getArguments().getString("displayname");
        et_status= view.findViewById(R.id.etStatus);
        btnPush=view.findViewById(R.id.btnGui);
        btnPush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               uploadimage();
               afterPostStatus();
	    }
	});
        imageView=view.findViewById(R.id.imageView);
        btnimage=view.findViewById(R.id.btnimage);
        btnimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opengallery();
            }
        });
        friends= new ArrayList<>();
        //Hien thi danh sach status
        lv_listStatus= view.findViewById(R.id.lv_ListStatus);
        lv_listStatus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Cái này xóa cũng dc @@
            }
        });
        mDatabase = FirebaseDatabase.getInstance().getReference();
        posts = new ArrayList<>();
        /*mDatabase.child("Post").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        posts.add(0, dataSnapshot.getValue(Post.class));
                        keyList.add(dataSnapshot.getKey());
                        imageView.setImageResource(0);
                        imageView.setVisibility(View.GONE);
                        et_status.setText("");
                        adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //int index = keyList.indexOf(dataSnapshot.getKey());
                dspost.set(vitri,dataSnapshot.getValue(Post.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
             *//*   int index = keyList.indexOf(dataSnapshot.getKey());
                posts.remove( index);
                keyList.remove(index);
                adapter.notifyDataSetChanged();*//*
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
        mDatabase.child("Post").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    posts.add( snapshot.getValue(Post.class));
                    keyList.add(dataSnapshot.getKey());
                    imageView.setImageResource(0);
                    imageView.setVisibility(View.GONE);
                    et_status.setText("");
                }
                reset();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
        mDatabase.child("Friendship").child(displayname).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    friends.add(snapshot.getValue(Friend.class));
                    Toast.makeText(getContext(), "aaaa", Toast.LENGTH_LONG).show();
                }
                reset();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        /*mDatabase.child("Friendship").child(displayname).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                friends.add(dataSnapshot.getValue(Friend.class));
                Toast.makeText(getContext(),"aaaa",Toast.LENGTH_LONG).show();
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
        });*/
        reset();
        return view;
    }
    private void reset()
    {
        loadpost();
        adapter= new StatusAdapter(view.getContext(),R.layout.status_row,dspost,Fragment_NewFeed.this);
        lv_listStatus.setAdapter(adapter);
    }


    private void afterPostStatus(){
        // reset EditText and ImageView
        //.setVisibility(View.GONE);
    }

    private void opengallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery,PICK_IMAGE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode== Activity.RESULT_OK && requestCode==PICK_IMAGE)
        {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
            imageView.setVisibility(View.VISIBLE);
            Cursor cursor = getActivity().getContentResolver().query(imageUri, null, null, null, null);
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            cursor.moveToFirst();
            imagename= cursor.getString(nameIndex);
            imagename= imagename.substring(0,imagename.length()-4);
            //Toast.makeText(getContext(),imagename.toString(),Toast.LENGTH_LONG).show();

        }
    }
    public void uploadimage() {
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
                        // Log.d(TAG, "onComplete: Url: "+ downloadUri.toString());
                        String status = et_status.getText().toString();
                        Post post = new Post();
                        post.setAccount_name(displayname);
                        post.setText(status);
                        String id = mDatabase.push().getKey();
                        post.setPost_id(id);
                        if (imageView.getDrawable() != null) {
                            post.setImage(Linkimage);
                            Toast.makeText(getContext(), "Co hinh ne", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Ko co hinh", Toast.LENGTH_SHORT).show();
                        }
                        mDatabase.child("Post").child(id).setValue(post);
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });
        }
        else
        {
            String status = et_status.getText().toString();
            Post post = new Post();
            post.setAccount_name(displayname);
            post.setText(status);
            String id = mDatabase.push().getKey();
            post.setPost_id(id);
            post.setComments(null);
            mDatabase.child("Post").child(id).setValue(post);
        }
    }
    public void sendComment(Post oldPost, String comment,int position){
        vitri=position;
        mDatabase = FirebaseDatabase.getInstance().getReference("Post");
        Date date = new Date();
        Comment comment1 = new Comment(String.valueOf(System.currentTimeMillis()),displayname,oldPost.getPost_id(),date,comment);
        if(oldPost.getComments()!=null){
            oldPost.getComments().add(comment1);
        }
        else {
            //code dài dòng
            List<Comment> comments = new ArrayList<Comment>();
            comments.add(comment1);
            oldPost.getComments().addAll(comments);
        }
        mDatabase.child(oldPost.getPost_id()).setValue(oldPost);
        adapter.notifyDataSetChanged();
      //  lv_listStatus.setAdapter(adapter);
    }

    @Override
    public void addfriend(Account account) {

    }
    public void loadpost() {

        for (int i=0;i<friends.size();i++)
            for (int j=0;j<posts.size();j++) {
                if (posts.get(j).getAccount_name().toLowerCase().equals(friends.get(i).getName_friend().toLowerCase())) {
                    dspost.add(posts.get(j));
                    Toast.makeText(getContext(),"aaaa",Toast.LENGTH_LONG).show();
                }
            }
    }
}
