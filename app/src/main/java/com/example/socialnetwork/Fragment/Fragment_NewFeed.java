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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


public class Fragment_NewFeed extends Fragment {
    View view;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
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
    StatusAdapter adapter;
    List<String> keyList = new ArrayList<String>();
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

        //Hien thi danh sach status
        lv_listStatus= view.findViewById(R.id.lv_ListStatus);
        lv_listStatus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Cái này xóa cũng dc @@
            }
        });
        mDatabase = FirebaseDatabase.getInstance().getReference();
        posts= new ArrayList<>();
        mDatabase.child("Post").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    posts.add(0, dataSnapshot.getValue(Post.class));
                   // keyList.add(dataSnapshot.getKey());
                    adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
             /*   int index = keyList.indexOf(dataSnapshot.getKey());
                posts.remove( index);
                keyList.remove(index);
                adapter.notifyDataSetChanged();*/
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        adapter= new StatusAdapter(view.getContext(),R.layout.status_row,posts);
        lv_listStatus.setAdapter(adapter);
        return view;
    }


    private void afterPostStatus(){
        // reset EditText and ImageView
        imageView.setVisibility(View.GONE);
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
            imagename= imagename.substring(1,imagename.length()-4);
           // Toast.makeText(getContext(),imagename.toString(),Toast.LENGTH_LONG).show();
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
                        post.setAccount_name("Quan");
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
            post.setAccount_name("Quan");
            post.setText(status);
            String id = mDatabase.push().getKey();
            post.setPost_id(id);
            mDatabase.child("Post").child(id).setValue(post);
        }
    }
}
