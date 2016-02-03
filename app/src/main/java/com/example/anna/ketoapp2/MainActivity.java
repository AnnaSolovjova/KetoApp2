package com.example.anna.ketoapp2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
DatabaseHelper database;
User user;
       @Override
       protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                database = new DatabaseHelper(this);
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                LinkedList<User> users= database.getUsers();
                if(database.getUsers().size()==0)
                {
                 startActivity(new Intent(this, Registration.class));
                }
                else {
                    user=users.getLast();
                    ProtocolFragment protocolFragment=new ProtocolFragment();
                    android.support.v4.app.FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, protocolFragment);
                    fragmentTransaction.commit();
                 //startActivity(new Intent(this, Protocol.class));
                }

       }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_profile) {
            ProfileFragment profileFragment=new ProfileFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,profileFragment);
            fragmentTransaction.commit();
        }
        else if(id == R.id.action_protocol)
        {
            ProtocolFragment protocolFragment=new ProtocolFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, protocolFragment);
            fragmentTransaction.commit();

        }
        else if(id == R.id.action_switch)
        {
            /*SwitchUserFragment switchFragment=new SwitchUserFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, switchFragment);
            fragmentTransaction.commit();*/
        }

        return super.onOptionsItemSelected(item);
    }

    public User getUser(){
        LinkedList<User> users= database.getUsers();
        user=users.getLast();
        return user;
    }
}
