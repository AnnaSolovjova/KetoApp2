package com.example.anna.ketoapp2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;


import java.util.LinkedList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
//Declare classes
DatabaseHelper database;
User user;
//declare the fragments within the activity
public static  ProtocolFragment protocolFragment;
public static  ProfileFragment profileFragment;
public static  SwitchFragment switchFragment;
//Declare activity layouts that are same within all 3 fragments
RadioGroup menu;
FloatingActionButton fab;

       @Override
       protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                database = new DatabaseHelper(this);
                setupToolbar();
                setupFloatingActionButton();
                setupTabMenu();
                startActivity();

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
                        //show time information only if the timer was already set
                        if (!(protocolFragment.time.equals("")))
                        {
                           time="You need to check your blood sugar and ketones at " + protocolFragment.time;
                        }

                        AlertDialog dialog=new AlertDialog.Builder(this)
                                .setTitle("Info")
                                .setMessage("" + iteration + time)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).show();
                    }
                break;
        }
    }

    private void setupToolbar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FocusChange focusChange=new FocusChange(this);
        toolbar.setOnTouchListener(focusChange);

    }
    private void setupFloatingActionButton(){
        fab= (FloatingActionButton) this.findViewById(R.id.fab);
        fab.setOnClickListener(this);
    }

    //This method initialises the main menu and set on click listener for navifating in the tabs
    private void setupTabMenu()
    {
        menu = (RadioGroup) findViewById(R.id.radioGroup1);
        menu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.radioAgain:
                        fab.setVisibility(View.VISIBLE);
                        if (protocolFragment == null) {
                            protocolFragment = new ProtocolFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, protocolFragment, "protocol").commit();
                        } else
                            //do not initialise new protocol fragment if one was already initialised, use the existing one
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, protocolFragment, "protocol").commit();
                        break;
                    case R.id.radioProfile:
                        fab.setVisibility(View.INVISIBLE);
                        if (profileFragment == null) {
                            profileFragment = new ProfileFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, profileFragment, "profile").commit();
                        } else
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, profileFragment, "profile").commit();
                        break;
                    case R.id.radioSwitch:
                        fab.setVisibility(View.INVISIBLE);
                        if (switchFragment == null) {
                            switchFragment = new SwitchFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, switchFragment, "switch").commit();
                        } else
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, switchFragment, "switch").commit();

                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (protocolFragment != null && protocolFragment.isVisible()) {
            // add your code here
         if(protocolFragment!=null)
                protocolFragment.myOnKeyDown();
        }
        else if (profileFragment!=null && profileFragment.isVisible())
        {
            profileFragment.myOnKeyDown();
        }

    }
    public void emergencyCall()
    {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:999"));
        startActivity(callIntent);
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
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            protocolFragment = new ProtocolFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, protocolFragment, "profile").commit();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
        }
        return super.onOptionsItemSelected(item);
    }
    public void registerNew(){
        switchFragment=null;
        profileFragment=null;
        protocolFragment=null;
        startActivity(new Intent(this, Registration.class));
    }
    //Method that checks which activity should be used first
    //if there are no users registered, the regestration activity should be opened
    //if not this activity should be used
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

    //Method switches user to the one selected
    public void switchUser(String username)
    {

        database.updateCurrent(username);
        user=database.getCurrent();
        protocolFragment = new ProtocolFragment();
        menu.check(R.id.radioAgain);
        fab.show();

    }

    public void deleteUser(String username)
    {
    final String name=username;
        AlertDialog dialog=new AlertDialog.Builder(this)
                .setTitle("Alert")
                .setMessage("Are you sure you want to delete this user?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                       deleteConfirmed(name);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();


    }
    private void deleteConfirmed(String username){
        database.deleteUser(username);
        if (getUser() == null)
            startActivity(new Intent(this, Registration.class));
        else {
            user = database.getCurrent();
            protocolFragment = new ProtocolFragment();
            menu.check(R.id.radioAgain);
        }
    }


    public RelativeLayout returnScreen()
        {
                return (RelativeLayout)findViewById(R.id.main_screen);
    }

}
