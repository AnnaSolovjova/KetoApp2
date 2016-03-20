package com.example.anna.ketoapp2;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class ProtocolFragment extends Fragment implements View.OnClickListener {
View view;
DatabaseHelper db;
User user;
int iteration;
int insulin,ketones,glucose;
RelativeLayout layout[]=new RelativeLayout[16];
    public ProtocolFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_protocol_process, container, false);
        db=new DatabaseHelper(getActivity());
        setProfile(getUser());
        setUpProtocolProcedure();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile_correct_no:
                ProfileFragment nextFrag = new ProfileFragment();
                this.getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, nextFrag, "")
                        .addToBackStack(null)
                        .commit();
                    break;
            case R.id.profile_correct_yes:
                setVisibility(0);
                break;
            case R.id.unwell_yes:
                setVisibility(1);
                break;
            case R.id.danger_signs_button_yes:
                setVisibility(6);
                break;
            case R.id.danger_signs_button_no:
                setVisibility(2);
                ((EditText)view.findViewById(R.id.glucose_input_edit)).setText("");
                break;
            case R.id.unwell_no:
                setVisibility(2);
                ((EditText)view.findViewById(R.id.glucose_input_edit)).setText("");

                break;
            case R.id.glucose_next:
                if(((EditText)view.findViewById(R.id.glucose_input_edit)).getText().toString().matches("")){
                    Toast.makeText(getActivity(), "Please don't leave the field blank",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    glucose=Integer.parseInt(((EditText)view.findViewById(R.id.glucose_input_edit)).getText().toString());
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
                setVisibility(4);
                break;
            case R.id.premeal_no:
                setVisibility(5);
                break;
            case R.id.ketones_yes:
                ketones=1;
                if(iteration==0){ setVisibility(7);}
                else{ setVisibility(8);}
                break;
            case R.id.ketones_no:
                ketones = 0;
                if(iteration==0){ setVisibility(7);}
                else{ setVisibility(8);}
                break;
            case R.id.insulin_next:
                if(((EditText)view.findViewById(R.id.insulin_input_edit)).getText().toString().matches(""))
                {
                    Toast.makeText(getActivity(), "Please don't leave the field blank",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    setInsulin();
                    setVisibility(8);
                    ((TextView) view.findViewById(R.id.dosage_text)).setText("Take " + Dosage() + " units of fast-acting insulin.");

                }
                break;
            case R.id.dosage_next:
                setVisibility(9);
                break;
            case R.id.start_again1:
               setVisibility(12);
                StartAgain();
                break;
            case R.id.start_again2:
                setVisibility(12);
                StartAgain();
                break;
            case R.id.start_again3:
                setVisibility(12);
                StartAgain();
                break;
            case R.id.start_again4:
                setVisibility(12);
                StartAgain();
                break;
            case R.id.start_again5:
                setVisibility(12);
                StartAgain();
                break;
            case R.id.start_again6:
                setVisibility(12);
                StartAgain();
                break;
            case R.id.emergency_contact:
                setVisibility(14);
                break;
            case R.id.emergency_contact2:
                setVisibility(14);
                break;
            case R.id.reminder_yes:
                setVisibility(15);
                final MediaPlayer mediaPlayer= MediaPlayer.create(getContext().getApplicationContext(), R.raw.alarm); //replace 'sound' by your    music/sound
                AlertDialog dialog=new AlertDialog.Builder(getContext())
                        .setTitle("Alarm")
                        .setMessage("4 hours passed, please click ok to proceed")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ((EditText)view.findViewById(R.id.glucose_input_edit)).setText("");
                                mediaPlayer.stop();
                                mediaPlayer.release();
                                setVisibility(2);

                            }
                        })

                        .setIcon(android.R.drawable.ic_dialog_alert).create();

                AppNotifications notifier= new AppNotifications(50000,1000,getContext(),dialog,(TextView)view.findViewById(R.id.timer_text),mediaPlayer);
                iteration++;
                break;
            case R.id.reminder_no:
                setVisibility(2);
                iteration++;
                break;



        }
    }
    public void startAlarm()
    {
        Intent intent = new Intent(getContext(), ProtocolFragment.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // You need this if starting
        //  the activity from a service
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        startActivity(intent);
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
    public void setProfile(User user)
    {
        ((Button)view.findViewById(R.id.profile_correct_yes)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.profile_correct_no)).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.username_text)).setText(user.getUsername());
        ((TextView) view.findViewById(R.id.age_text)).setText(user.getDateOfBirth().substring(0,2)+"/"+user.getDateOfBirth().substring(2,4)+"/"+user.getDateOfBirth().substring(4,8));
        ((TextView) view.findViewById(R.id.insulin_text)).setText(user.getInsulinRegiment());
    }
    public User getUser()
    {
        Activity activity = getActivity();
        MainActivity myactivity = (MainActivity) activity;
        user =myactivity.getUser();
        return user;
    }

    private void setUpProtocolProcedure()
    {
        FocusChange focusChange=new FocusChange(getContext());
        ((Button)view.findViewById(R.id.unwell_yes)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.unwell_no)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.danger_signs_button_yes)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.danger_signs_button_no)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.ketones_yes)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.ketones_no)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.premeal_yes)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.premeal_no)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.glucose_next)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.start_again1)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.start_again2)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.start_again3)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.emergency_contact)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.emergency_contact2)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.glucose_next)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.insulin_next)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.reminder_yes)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.reminder_no)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.reminder_no)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.dosage_next)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.start_again4)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.start_again5)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.start_again6)).setOnClickListener(this);
        //((Button)view.findViewById(R.id.insulin_next)).setOnClickListener(this);
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
       layout[13]=(RelativeLayout)view.findViewById(R.id.part13);
        layout[14]=(RelativeLayout)view.findViewById(R.id.part14);
        layout[15]=(RelativeLayout)view.findViewById(R.id.part15);
    }

    public void setUpProfileFragment()
    {
        FocusChange focusChange=new FocusChange(getContext());
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.fragment_protocol, null);
        ViewGroup rootView = (ViewGroup) getView();
        view.setLayoutParams(rootView.getRootView().getLayoutParams());
        rootView.removeAllViews();
        rootView.addView(view);
        setProfile(getUser());
    }

    private void setVisibility(int visiable)
    {
        for (int i=0;i<layout.length;i++){
            if(i==visiable)
                layout[i].setVisibility(view.VISIBLE);
            else
                layout[i].setVisibility(view.INVISIBLE);
        }
    }

    public int getInsulin() {
        return insulin;
    }

    public void setInsulin() {
        insulin=Integer.parseInt(((EditText) view.findViewById(R.id.insulin_input_edit)).getText().toString());
    }
    private int Dosage()
    {
        int dosage=0;
        if(glucose>=17&&ketones==1)
            dosage=insulin/6;
        else if((glucose>=17&&ketones==0)||(glucose<17&&glucose>=10))
            dosage=(glucose-8)/(100/insulin);
        if(dosage>15)
            dosage=15;
        return dosage;
    }
}