package com.example.anna.ketoapp2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
DatabaseHelper database;
User user;
String imageSelested;

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
                    database.increaseUsage(user.getUsername());
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
            SwitchFragment switchFragment=new SwitchFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, switchFragment);
            fragmentTransaction.commit();
        }

        return super.onOptionsItemSelected(item);
    }

    public User getUser(){
            user = database.getCurrent();
            return user;
    }

    public LinkedList<User> getUsers(){
        LinkedList<User> users= database.getUsers();
        if(users.size()!=0) {
           return users;
        }
        else
            return null;
    }

    public void switchUser(String username)
    {
        database.updateCurrent(username);
        ProtocolFragment protocolFragment=new ProtocolFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, protocolFragment);
        fragmentTransaction.commit();
    }


    public void loadImagefromGallery(View view) {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, 1);
    }



    public String getImageSelected()
    {
        return imageSelested;
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}
