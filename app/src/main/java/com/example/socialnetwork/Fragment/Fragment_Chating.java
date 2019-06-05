package com.example.socialnetwork.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialnetwork.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Fragment_Chating extends Fragment {
    View view;
    TextView textView;
    Button btnDangki;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    String userid;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_fragment__chating, container, false);


        connectView();
        firebaseAuth=FirebaseAuth.getInstance();
//        userid="phucvo";
//        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
//        databaseReference= FirebaseDatabase.getInstance().getReference("User").child(userid);
        btnDangki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser("phuckv","123","vophuc3@gmail.com");
            }
        });

        return view;
    }

    private void connectView() {
        textView=view.findViewById(R.id.textView);
        btnDangki=view.findViewById(R.id.btnDangki);
    }

    public void createUser(final String username, String password, String email){
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    firebaseUser=firebaseAuth.getCurrentUser();
                    String userid=firebaseUser.getUid();
                    databaseReference=FirebaseDatabase.getInstance().getReference("Users").child(userid);

                    HashMap<String,String> hashMap=new HashMap<>();
                    hashMap.put("id",userid);
                    hashMap.put("username","phucvo");
                    hashMap.put("imageURL","default");

                    databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                textView.setText("Thanh cong!");
                            }
                        }
                    });
                }
            }
        });
    }


}
