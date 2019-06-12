package com.example.socialnetwork;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUp extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private Button btnSignUp;
    private FirebaseAuth mAuth;
    DatabaseReference databaseReference;
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
//        mAuth.createUserWithEmailAndPassword(email, pass)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(SignUp.this,"Sign Up Success !",Toast.LENGTH_LONG).show();
//
//                            startActivity(new Intent(SignUp.this, MainActivity.class));
//                        } else {
//                            Toast.makeText(SignUp.this,"Sign Up Fail ! Try Again !",Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });
    }


    public void createUser( String email, String password){
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            //Toast.makeText(getContext(),"Thanh cong",Toast.LENGTH_LONG).show();

                            FirebaseUser firebaseUser=mAuth.getCurrentUser();

                            assert firebaseUser != null;
                            String userid=firebaseUser.getUid();
                            //Code cua Quan
                            String displayname = firebaseUser.getEmail();
                            displayname = displayname.substring(0, displayname.length() - 10);

                            databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            HashMap<String,String> hashMap=new HashMap<>();
                            hashMap.put("id",userid);
                            hashMap.put("account_name",displayname);
                            hashMap.put("imageURL","default");

                            databaseReference.setValue(hashMap);
                        }
                        else {
                            //Toast.makeText(getContext(),"That bai",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
