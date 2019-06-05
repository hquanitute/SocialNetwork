package com.example.socialnetwork.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.socialnetwork.Objects.Post;
import com.example.socialnetwork.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;

import static android.content.ContentValues.TAG;

public class Fragment_NewFeed extends Fragment {
    View view;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabase = database.getReference("Post");
    EditText et_status;
    Button btnPush;

    ImageView imageView;
    Button btnimage;
    private static final int PICK_IMAGE=100;
    Uri imageUri;
    private static String imagename;
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
        imageView=view.findViewById(R.id.imageView);
        btnimage=view.findViewById(R.id.btnimage);
        btnimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opengallery();
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

    private void opengallery()
    {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery,PICK_IMAGE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode== Activity.RESULT_OK && requestCode==PICK_IMAGE)
        {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
            Cursor cursor = getActivity().getContentResolver().query(imageUri, null, null, null, null);
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            cursor.moveToFirst();
            //Toast.makeText(getContext(),cursor.getString(nameIndex),Toast.LENGTH_LONG).show();
            imagename= cursor.getString(nameIndex);
        }
    }
}
