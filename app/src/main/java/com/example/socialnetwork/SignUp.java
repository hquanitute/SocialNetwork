package com.example.socialnetwork;

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
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.socialnetwork.Objects.Friend;
import com.example.socialnetwork.Objects.Post;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class SignUp extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private Button btnSignUp;
    private FirebaseAuth mAuth;
    private static final int PICK_IMAGE=100;
    private static String imagename;
    String displayname;
    ImageView avatar;
    Uri imageUri;
    String Linkimage=null;
    DatabaseReference databaseReference;
    boolean check=false;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://social-16d30.appspot.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        this.username = (EditText)this.findViewById(R.id.Username);
        this.password = (EditText)this.findViewById(R.id.Password);
        this.btnSignUp =(Button)this.findViewById(R.id.btnSignUp);
        avatar = this.findViewById(R.id.avatar);
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opengallery();
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUp();
            }
        });
    }
    public void SignUp()
    {
        String email = username.getText().toString();
        String pass = password.getText().toString();
        createUser(email,pass);
    }
    private void opengallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            avatar.setImageURI(imageUri);
            avatar.setVisibility(View.VISIBLE);
            Cursor cursor = SignUp.this.getContentResolver().query(imageUri, null, null, null, null);
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            cursor.moveToFirst();
            imagename = cursor.getString(nameIndex);
            imagename = imagename.substring(0, imagename.length() - 4);
            //Toast.makeText(getContext(),imagename.toString(),Toast.LENGTH_LONG).show();
            check=true;
        }
    }
    public void uploadimage() {

    }
    public void createUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //Toast.makeText(getContext(),"Thanh cong",Toast.LENGTH_LONG).show();
                            FirebaseUser firebaseUser=mAuth.getCurrentUser();
                            assert firebaseUser != null;
                            final String userid=firebaseUser.getUid();
                            //Code cua Quan
                            displayname = firebaseUser.getEmail();
                            displayname = displayname.substring(0, displayname.length() - 10);

                            if (check) {
                                avatar.setDrawingCacheEnabled(true);
                                avatar.buildDrawingCache();
                                Bitmap bitmap = ((BitmapDrawable) avatar.getDrawable()).getBitmap();
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
                                            databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(userid);
                                            uploadimage();
                                            HashMap<String,String> hashMap=new HashMap<>();
                                            hashMap.put("id",userid);
                                            hashMap.put("account_name",displayname);
                                            hashMap.put("imageURL",Linkimage);
                                            databaseReference.setValue(hashMap);
                                            databaseReference= FirebaseDatabase.getInstance().getReference();
                                            String id = databaseReference.push().getKey();
                                            Friend friend = new Friend();
                                            friend.setIdfriend(userid);
                                            friend.setName_friend(displayname);
                                            databaseReference.child("Friendship").child(displayname).child(id).setValue(friend);
                                            check=false;
                                            startActivity(new Intent(SignUp.this, MainActivity.class));
                                            Toast.makeText(SignUp.this,"Đăng Ký Thành Công",Toast.LENGTH_LONG).show();
                                        } else {
                                            // Handle failures
                                            // ...
                                        }
                                    }
                                });
                            }
                            else {
                                databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(userid);
                                HashMap<String,String> hashMap=new HashMap<>();
                                hashMap.put("id",userid);
                                hashMap.put("account_name",displayname);
                                hashMap.put("imageURL", "default");
                                databaseReference.setValue(hashMap);
                                databaseReference= FirebaseDatabase.getInstance().getReference();
                                String id = databaseReference.push().getKey();
                                Friend friend = new Friend();
                                friend.setIdfriend(userid);
                                friend.setName_friend(displayname);
                                databaseReference.child("Friendship").child(displayname).child(id).setValue(friend);
                                startActivity(new Intent(SignUp.this, MainActivity.class));
                                Toast.makeText(SignUp.this,"Đăng Ký Thành Công",Toast.LENGTH_LONG).show();
                            }
                        }
                        else {
                            Toast.makeText(SignUp.this,"That bai",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
