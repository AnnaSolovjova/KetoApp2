package com.example.anna.ketoapp2;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {

public User user;
DatabaseHelper db;
public PendingIntent pendingIntent;
private View view;
    EditText username ,insulin ,age;
    public ProfileFragment() {
       user=new User();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_profile, container, false);
        Button editbutton=(Button)view.findViewById(R.id.profile_edit_button);
        editbutton.setOnClickListener(this);
        getUser();
        db=new DatabaseHelper(getActivity());
        setProfile(user);
        return view;
    }

    public User getUser()
    {
        Activity activity = getActivity();
        MainActivity myactivity = (MainActivity) activity;
        user =myactivity.getUser();
        return user;
    }

    public void setProfile(User user)
    {
        User user2=getUser();
        ((Button)view.findViewById(R.id.profile_edit_button)).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.username_text)).setText(user2.getUsername());
        ((TextView) view.findViewById(R.id.insulin_text)).setText(user2.getInsulinRegiment());
        ((TextView) view.findViewById(R.id.age_text)).setText(user2.getDateOfBirth());

    }
    public void setEditProfile(User user)
    {
        user= getUser();
        Button editbutton=(Button)view.findViewById(R.id.profile_edit_comp_button);
        editbutton.setOnClickListener(this);
        ((EditText) view.findViewById(R.id.username_edit)).setText(user.getUsername());
        ((EditText) view.findViewById(R.id.insulinRegiment_edit)).setText(user.getInsulinRegiment());
        ((EditText) view.findViewById(R.id.dateOfBirth_edit)).setText(user.getDateOfBirth());
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()) {

            case R.id.profile_edit_button:
                setViewLayout(R.layout.edit_profile_fragment);
                setEditProfile(user);
                break;
            case R.id.profile_del_button:
               // Button editbutton=(Button)view.findViewById(R.id.profile_del_comp_button);
                //editbutton.setOnClickListener(this);
                break;
            case R.id.profile_edit_comp_button:
                String newU=((EditText)view.findViewById(R.id.username_edit)).getText().toString();
                String newD=((EditText)view.findViewById(R.id.dateOfBirth_edit)).getText().toString();
                String newI=((EditText)view.findViewById(R.id.insulinRegiment_edit)).getText().toString();
                if( db.editUser(user.getUsername(),newU,"blah",newD,newI));
                setViewLayout(R.layout.fragment_profile);
                setProfile(user);
                break;
           // case R.id.profile_delete_comp_button:
             //   break;

        }

    }

    private void setViewLayout(int id){
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(id, null);
        ViewGroup rootView = (ViewGroup) getView();
        view.setLayoutParams(rootView.getRootView().getLayoutParams());
        rootView.removeAllViews();
        rootView.addView(view);
    }

}
