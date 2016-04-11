package com.example.anna.ketoapp2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
DatabaseHelper database;
User user;
String imageSelested;
public static  ProtocolFragment protocolFragment;
public static  ProfileFragment profileFragment;
public static  SwitchFragment switchFragment;
FragmentTransaction fragmentTransaction;
RadioGroup menu;
Fragment mContent;

       @Override
       protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                final Bundle bundle=savedInstanceState;
                setContentView(R.layout.activity_main);
                database = new DatabaseHelper(this);
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                menu = (RadioGroup) findViewById(R.id.radioGroup1);
                final FloatingActionButton fab = (FloatingActionButton) this.findViewById(R.id.fab);
                fab.setOnClickListener(this);
                startActivity();

           // Checked change Listener for RadioGroup 1
               menu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
               {
                   @Override
                   public void onCheckedChanged(RadioGroup group, int checkedId)
                   {

                       switch (checkedId)
                       {
                           case R.id.radioAgain:
                               fab.show();
                               if(protocolFragment==null) {
                                   protocolFragment=new ProtocolFragment();
                                   getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, protocolFragment, "protocol").commit();
                               }
                               else
                                   getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, protocolFragment, "protocol").commit();
                               break;
                           case R.id.radioProfile:
                               fab.hide();
                               if(profileFragment==null) {

                                   profileFragment=new ProfileFragment();
                                   getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, profileFragment, "profile").commit();
                               }
                               else

                                   getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, profileFragment, "profile").commit();
                               break;
                           case R.id.radioSwitch:
                               fab.hide();
                               if(switchFragment==null) {
                                   switchFragment=new SwitchFragment();
                                   getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, switchFragment, "switch").commit();
                               }
                               else

                               getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, switchFragment, "switch").commit();

                               break;
                           default:
                               break;
                       }
                   }
               });



       }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.fab:
                    if (protocolFragment!=null)
                    {
                        int i=protocolFragment.iteration;
                        if(protocolFragment.iteration==0)
                            i=1;
                        String iteration="This is your "+i+ " cycle in the app."+System.getProperty("line.separator");
                        String time= "";
                        if (!(protocolFragment.time.equals("")))
                        {
                           time="You need to check your blood sugar and ketones at " + protocolFragment.time;
                        }

                        AlertDialog dialog=new AlertDialog.Builder(this)
                                .setTitle("Info")
                                .setMessage(""+iteration +time)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {


                                    }
                                })

                                .setIcon(android.R.drawable.ic_dialog_alert).show();
                    }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public void onBackPressed() {
            if(protocolFragment!=null)
                protocolFragment.myOnKeyDown();
    }

    @Override
    protected void onRestoreInstanceState(Bundle inState) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_protocol) {
            AlertDialog dialog=new AlertDialog.Builder(this)
                    .setTitle("Alert")
                    .setMessage("Are you sure you want to start again?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            protocolFragment=new ProtocolFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, protocolFragment, "profile").commit();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert).show();
        }
        return super.onOptionsItemSelected(item);
    }
    private  void startActivity()
    {
        LinkedList<User> users= database.getUsers();
        if(database.getUsers().size()==0)
        {
            startActivity(new Intent(this, Registration.class));
        }
        else {

            user=users.getLast();
            database.increaseUsage(user.getUsername());
            if(protocolFragment==null) {
                protocolFragment=new ProtocolFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, protocolFragment, "protocol").commit();
            }
            else
                protocolFragment = (ProtocolFragment)getSupportFragmentManager().findFragmentByTag("protocol");
        }
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


    public RelativeLayout returnScreen()
    {
        return (RelativeLayout)findViewById(R.id.main_view);
    }

}
