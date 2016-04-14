package com.example.anna.ketoapp2;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DecimalFormat;
import java.text.ParseException;

import java.util.Calendar;
import java.util.Stack;
/*
*This class is the handler for the protocol user fragment
* and the operations connected to this action,
* including changing visibility of the layouts within the fragment,
* populating views with the data, handling user input etc*/
public class ProtocolFragment extends Fragment implements View.OnClickListener {
    Validation validation;
    View view;
    DatabaseHelper db;
    User user;
    AppNotifications notifier;
    int iteration,ketones,insulin,previous = 0;
    String time="";
    int visibility =12;
    double glucose;
    Stack pr = new Stack();
    RelativeLayout layout[]=new RelativeLayout[16];
    MainActivity myactivity;
    InputMethodManager inputMethodManager;
    Calendar cal;

    @Override
    public void onStart() {
        super.onStart();
        setUpProtocolProcedure();
        setVisibility(visibility);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideKeyboard(layout[visibility]);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_protocol_process, container, false);
        Activity activity = getActivity();
        myactivity = (MainActivity) activity;
         db=new DatabaseHelper(getActivity());
         validation=new Validation();
        inputMethodManager=(InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (savedInstanceState == null) {
           /* visibility=this.visibility;
            ketones=this.ketones;
            iteration=this.iteration;
            glucose=this.glucose;
            insulin=this.insulin;
            pr=this.pr;
            time=this.time;*/
           //TODO
            if(glucose!=0)
                ((EditText)view.findViewById(R.id.glucose_input_edit)).setText(""+glucose);
        }
        else
        {
            //set variables with the values saved on orientation change
            iteration=savedInstanceState.getInt("iteration");
            glucose=savedInstanceState.getDouble("glucose");;
            insulin=savedInstanceState.getInt("insulin");;
            ketones=savedInstanceState.getInt("ketones");;
            visibility=savedInstanceState.getInt("visibility");
            time=savedInstanceState.getString("time");
            pr=(Stack)savedInstanceState.getSerializable("stack");
        }

        return view;
    }



    //This method saves variables in the bundle to prevent loosing data on orientation chage
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("ketones", ketones);
        outState.putInt("iteration",iteration);
        outState.putInt("visibility", visibility);
        outState.putDouble("glucose", glucose);
        outState.putInt("insulin", insulin);
        outState.putString("time", time);
        outState.putSerializable("stack",pr);
    }


    //Method that tells what actions to do when the back button is pressed
    public void myOnKeyDown()
    {
        if(!pr.empty()) {
            previous = (int) pr.pop();
            if (previous==9)
            {
                if(notifier!=null)
                    notifier.canceled=true;
                iteration--;

                setVisibility(previous);
            }
            else if (previous != 100) {
                setVisibility(previous);
            } else
                pr.clear();
        }
    }



    @Override
    public void onClick(View v) {
        String output;
        switch (v.getId()) {
            case R.id.profile_correct_next:
                try {
                    if((output=validation.dateValidation(((EditText) view.findViewById(R.id.age_day)).getText().toString(),((EditText) view.findViewById(R.id.age_month)).getText().toString(),((EditText) view.findViewById(R.id.age_year)).getText().toString()))!=null)
                    {
                        Toast.makeText(getContext(), output, Toast.LENGTH_LONG).show();
                    }
                    else if(!user.getUsername().equals((((EditText) view.findViewById(R.id.username_text)).getText().toString()))&&(db.userExists(((EditText) view.findViewById(R.id.username_text)).getText().toString())))
                    {
                        Toast.makeText(getContext(), "User already exists", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        db.editUser(user.getUsername(), ((EditText) view.findViewById(R.id.username_text)).getText().toString(), ((EditText) view.findViewById(R.id.age_day)).getText().toString()+((EditText) view.findViewById(R.id.age_month)).getText().toString()+((EditText) view.findViewById(R.id.age_year)).getText().toString(), ((EditText) view.findViewById(R.id.insulin_text)).getText().toString());
                        pr.push(new Integer(12));
                        setVisibility(0);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.unwell_yes:
                pr.push(new Integer(0));
                setVisibility(1);
                break;
            case R.id.danger_signs_button_yes:
                pr.push(new Integer(1));
                setVisibility(6);
                break;
            case R.id.danger_signs_button_no:
                pr.push(new Integer(1));
                setVisibility(2);
                ((EditText)view.findViewById(R.id.glucose_input_edit)).setText("");
                break;
            case R.id.unwell_no:
                pr.push(new Integer(0));
                previous=0;
                setVisibility(2);
                ((EditText)view.findViewById(R.id.glucose_input_edit)).setText("");

                break;
            case R.id.glucose_next:
                if(!time.equals(""))
                    time="";

                if(((EditText)view.findViewById(R.id.glucose_input_edit)).getText().toString().matches("")){
                    Toast.makeText(getActivity(), "Please don't leave the field blank",
                            Toast.LENGTH_LONG).show();
                }
                else if (Float.parseFloat(((EditText)view.findViewById(R.id.glucose_input_edit)).getText().toString())>33.3)
                {
                    Toast.makeText(getActivity(), "If your meter identify HI glucose please write 33.3",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    pr.push(new Integer(2));
                        glucose=Double.parseDouble(((EditText) view.findViewById(R.id.glucose_input_edit)).getText().toString());
                    if(glucose<10){
                        setVisibility(11);
                    }
                    else {

                        if(iteration==0)
                        setVisibility(3);
                        else if(iteration==3)
                        setVisibility(13);
                        else
                        setVisibility(4);
                    }
                }
                break;
            case R.id.premeal_yes:
                pr.push(new Integer(3));
                setVisibility(4);
                if(iteration==0)
                 iteration++;
                break;
            case R.id.premeal_no:
                pr.push(new Integer(3));
                setVisibility(5);
                break;
            case R.id.ketones_yes:
                ketones=1;
                pr.push(new Integer(4));
                if(iteration==1){ setVisibility(7);}
                else{
                    dosageShow();
                    setVisibility(8);
                }
                break;
            case R.id.ketones_no:
                ketones = 0;
                pr.push(new Integer(4));
                if(iteration==1){ setVisibility(7);}
                else{
                    dosageShow();
                    setVisibility(8);

                }
                break;
            case R.id.insulin_next:

                if(((EditText)view.findViewById(R.id.insulin_input_edit)).getText().toString().matches("")&&insulin==0)
                {
                    Toast.makeText(getActivity(), "Please don't leave the field blank",
                            Toast.LENGTH_LONG).show();
                }
                else if(Integer.parseInt(((EditText) view.findViewById(R.id.insulin_input_edit)).getText().toString())>300)
                {
                    Toast.makeText(getActivity(), "Insulin dosage is too high",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    pr.push(new Integer(7));
                    dosageShow();
                    setVisibility(8);
                }
                break;
            case R.id.dosage_next:
                pr.push(new Integer(8));
                setVisibility(9);
                break;
            case R.id.emergency_contact:
                pr.push(new Integer(6));
                setVisibility(14);
                break;
            case R.id.emergency_contact2:
                pr.push(new Integer(13));
                setVisibility(14);
                break;
            case R.id.reminder_yes:

                pr.push(new Integer(9));
                setVisibility(2);
                iteration++;
                //replace 'sound' by your    music/sound
                final MediaPlayer mediaPlayer = MediaPlayer.create(getContext().getApplicationContext(), R.raw.alarm);
                AlertDialog dialog=new AlertDialog.Builder(getContext())
                        .setTitle("Alarm")
                        .setMessage("4 hours passed, please click ok to proceed")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ((EditText)view.findViewById(R.id.glucose_input_edit)).setText("");
                                mediaPlayer.stop();
                                mediaPlayer.release();


                            }
                        })

                        .setIcon(android.R.drawable.ic_dialog_alert).create();
                cal = Calendar.getInstance();
                cal.add(Calendar.HOUR_OF_DAY, 4);
                time=validation.timeValidation(cal);
                notifier= new AppNotifications(5000,1000,getContext(),dialog,(TextView)view.findViewById(R.id.timer_text),mediaPlayer);
                break;
            case R.id.reminder_no:
                pr.push(new Integer(9));
                cal = Calendar.getInstance();
                cal.add(Calendar.HOUR_OF_DAY, 4);
                time=validation.timeValidation(cal);
                setVisibility(2);
                iteration++;
                break;



        }
    }


    private void StartAgain()
    {
        ((EditText)view.findViewById(R.id.glucose_input_edit)).setText("");
        ((EditText) view.findViewById(R.id.insulin_input_edit)).setText("");
        iteration=0;
        glucose=0;
        insulin=0;
        ketones=0;
    }
    public User getUser()
    {
        user =myactivity.getUser();
        return user;
    }

    //Method makes necessary setup for the layout
    //including assignment of listeners
    private void setUpProtocolProcedure()
    {
        user=getUser();
        FocusChange focusChange=new FocusChange(getContext());
        ((Button)view.findViewById(R.id.profile_correct_next)).setOnClickListener(this);
        ((EditText) view.findViewById(R.id.username_text)).setText(user.getUsername());
        ((EditText) view.findViewById(R.id.age_day)).setText(user.getDateOfBirth().substring(0, 2));
        ((EditText) view.findViewById(R.id.age_month)).setText(user.getDateOfBirth().substring(2, 4));
        ((EditText) view.findViewById(R.id.age_day)).setText(user.getDateOfBirth().substring(4, 8));
        ((EditText) view.findViewById(R.id.insulin_text)).setText(user.getInsulinRegiment());
        ((Button)view.findViewById(R.id.unwell_yes)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.unwell_no)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.danger_signs_button_yes)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.danger_signs_button_no)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.ketones_yes)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.ketones_no)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.premeal_yes)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.premeal_no)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.glucose_next)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.emergency_contact)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.emergency_contact2)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.glucose_next)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.insulin_next)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.reminder_yes)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.reminder_no)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.reminder_no)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.dosage_next)).setOnClickListener(this);
        layout[0]=(RelativeLayout)view.findViewById(R.id.part1);
        layout[1]=(RelativeLayout)view.findViewById(R.id.part2);
        layout[2]=(RelativeLayout)view.findViewById(R.id.part3);
        layout[2].setOnTouchListener(focusChange);
        layout[3]=(RelativeLayout)view.findViewById(R.id.part4);
        layout[4]=(RelativeLayout)view.findViewById(R.id.part5);
        layout[5]=(RelativeLayout)view.findViewById(R.id.part6);
        layout[6]=(RelativeLayout)view.findViewById(R.id.part7);
        layout[7]=(RelativeLayout)view.findViewById(R.id.part8);
        layout[7].setOnTouchListener(focusChange);
        layout[8]=(RelativeLayout)view.findViewById(R.id.part9);
        layout[9]=(RelativeLayout)view.findViewById(R.id.part10);
        layout[10]=(RelativeLayout)view.findViewById(R.id.part11);
        layout[11]=(RelativeLayout)view.findViewById(R.id.part12);
        layout[12]=(RelativeLayout)view.findViewById(R.id.part0);
        layout[12].setOnTouchListener(focusChange);
        layout[13]=(RelativeLayout)view.findViewById(R.id.part13);
        layout[14]=(RelativeLayout)view.findViewById(R.id.part14);
        layout[15]=(RelativeLayout)view.findViewById(R.id.part15);
    }


    //This method is responsible for the visibility of the layouts within the view
    //and calling the method to open the keyboard on the views when necessary
    private void setVisibility(int visiable)
    {
        visibility = visiable;
        if (visibility == 2||visibility == 7) {
              showKeyboard(layout[visiable]);
        }
        else
            hideKeyboard(layout[visiable]);
        for (int i=0;i<layout.length;i++){
            if(i==visiable)
                layout[i].setVisibility(view.VISIBLE);
            else
                layout[i].setVisibility(view.INVISIBLE);
        }
    }




    //This method is responsible for generating the message about the insulin dosage shown on the screen
    private void dosageShow()
    {
        insulin=Integer.parseInt(((EditText) view.findViewById(R.id.insulin_input_edit)).getText().toString());
        ((TextView) view.findViewById(R.id.dosage_text)).setText("Take " + new DecimalFormat("#.00").format(Dosage()) + " units of fast-acting insulin.");
    }

    //This method is responsiable for calculating the dosage of the insulin
    //according to the formula chosen considering several different values specified
    private double Dosage()
    {
        double dosage=0;
        if(glucose>=17&&ketones==1)
            dosage=insulin/6;
        else if((glucose>=17&&ketones==0)||(glucose<17&&glucose>=10)) {
            double divider = (100 / insulin);
            if (divider==0)
                divider=1;
            dosage = (double)((glucose - 8) / divider);
        }
        if(dosage>15)
            dosage = 15;
        return dosage;
    }

    //Method that shows the soft keyboard on the screen
    private void showKeyboard(RelativeLayout linearLayout)
    {
            inputMethodManager.toggleSoftInputFromWindow(linearLayout.getApplicationWindowToken(), InputMethodManager.SHOW_IMPLICIT, 0);
    }
    //Method that tells not to open the keyboard if not explicitly stated.
    //TODO:might check if that can be done once
    private void hideKeyboard(RelativeLayout linearLayout) {

            inputMethodManager.hideSoftInputFromWindow(linearLayout.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

    }
}