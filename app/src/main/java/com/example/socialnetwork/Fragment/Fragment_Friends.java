package com.example.socialnetwork.Fragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.socialnetwork.R;

public class Fragment_Friends extends Fragment {
    View view;
    String email,displayname;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_fragment__friends, container, false);
        email = getArguments().getString("email");
        displayname = getArguments().getString("displayname");

        return view;
    }
}