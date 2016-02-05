package com.example.anna.ketoapp2;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class SwitchFragment extends Fragment {
    View view;
    DatabaseHelper db;
    User user;
    public SwitchFragment() {
        user=new User();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_switch, container, false);
      //  Button editbutton=(Button)view.findViewById(R.id.profile_switch_button);
        //editbutton.setOnClickListener(this);
        db=new DatabaseHelper(getActivity());
        return view;
    }

}
