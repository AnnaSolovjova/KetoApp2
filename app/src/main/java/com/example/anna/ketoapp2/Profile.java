package com.example.anna.ketoapp2;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }
    public void setProfile(User user,View view)
    {
        TextView name = (TextView) view.findViewById(R.id.username_text);
        TextView surname = (TextView) view.findViewById(R.id.insulin_text);
        TextView age = (TextView) view.findViewById(R.id.age_text);
        name.setText(user.getUsername());
        surname.setText(user.getInsulinRegiment());
        age.setText(user.getDateOfBirth());

    }

}
