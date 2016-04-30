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
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.LinkedList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
//Declare java classes used within the activity
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
                //setup activity components
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
                        //Set up the text about the application's cycle to chow within the dialog on users request
                        if(protocolFragment.iteration==0)
                            i=1;
                            String iteration="This is your "+i+ " correction dose."+System.getProperty("line.separator");
                            String time= "";
                        // set up time information only if the timer was already set
                        if (!(protocolFragment.time.equals("")))
                        {
                           time="You need to check your blood sugar and ketones at " + protocolFragment.time;
                        }
                        //Build the dialog box with app context information
                       new AlertDialog.Builder(this)
                                .setTitle("Info")
                                .setMessage("" + iteration + time)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }}).show();

                    }
                break;
            case R.id.menu_start_again:
                new AlertDialog.Builder(this)
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
    }

    //Method initialises and sets up the toolbar
    private void setupToolbar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FocusChange focusChange=new FocusChange(this);
        toolbar.setOnTouchListener(focusChange);
        ((TextView)findViewById(R.id.menu_start_again)).setOnClickListener(this);

    }
    //Method initialises information button
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
                        //initialise fragment needed if it was not initialised before
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
    public void onBackPressed() {
            //call methods responsible for the on back pressed methods depending on the fragment currently displayed
        if(protocolFragment!=null&& protocolFragment.isVisible())
                protocolFragment.myOnKeyDown();

        else if (profileFragment!=null && profileFragment.isVisible())
        {
            profileFragment.myOnKeyDown();
        }
    }

    //Method responsiable for initialising the call from within the application using phone capabilities
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

    //Method called from within fragment class to start registration activity when user wants to register another user
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

    //Method that is called from within the profile fragment to reset the protocol fragment if the insulin regimen is changed
    public void startAgain()
    {
        protocolFragment=new ProtocolFragment();
    }

    //Method accesses the database and returns the last active user's information.
    public User getUser(){
            user = database.getCurrent();
            return user;
    }

    //Method accesses the database and returns the list of all registered users and their information.
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

    //Method shows the dialog box that asks user to confirm if he/she wants to delete the user.
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
    //Method deletes accesses the database and and asks to delete specified user
    private void deleteConfirmed(String username){
        database.deleteUser(username);
        if (getUser() == null) {
            protocolFragment=null;
            switchFragment=null;
            profileFragment=null;
            startActivity(new Intent(this, Registration.class));
        }
        else {
            user = database.getCurrent();
            protocolFragment = new ProtocolFragment();
            menu.check(R.id.radioAgain);
        }
    }


}
