package com.example.socialnetwork;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.socialnetwork.Adapter.MainView;
import com.example.socialnetwork.Fragment.Fragment_Chating;
import com.example.socialnetwork.Fragment.Fragment_Friends;
import com.example.socialnetwork.Fragment.Fragment_NewFeed;
import com.example.socialnetwork.Objects.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private Button btnlogin;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        this.username = (EditText)this.findViewById(R.id.Username);
        this.password = (EditText)this.findViewById(R.id.Password);
        this.btnlogin =(Button)this.findViewById(R.id.btnLogin);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckLogin();
            }
        });


    }
    private void CheckLogin()
    {
        final String email = username.getText().toString();
        String pass = password.getText().toString();
        mAuth.signOut();
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(MainActivity.this,"Login Success !",Toast.LENGTH_LONG).show();
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
                            /*Intent intent1 = ;
                            intent1.putExtra("email",email);*/
                            startActivity(new Intent(MainActivity.this,Index.class));
                        } else {
                            Toast.makeText(MainActivity.this,"Email or Password is wrong ! Try again !",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    public void SignUp(View view)
    {
        Intent intent = new Intent(this,SignUp.class);
        startActivity(intent);
    }
}
