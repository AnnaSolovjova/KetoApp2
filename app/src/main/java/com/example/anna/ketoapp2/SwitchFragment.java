package com.example.anna.ketoapp2;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.LinkedList;

/*
*This class is the handler for the switch user fragment
* and the operations connected to this action,
* including with populating the fragment with dynamically created views*/
public class SwitchFragment extends Fragment implements View.OnClickListener{
    View view;
    DatabaseHelper db;
    MainActivity myactivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_switch, container, false);
        ((Button)view.findViewById(R.id.registerbutton)).setOnClickListener(this);
        db=new DatabaseHelper(getActivity());
        Activity activity = getActivity();
        myactivity = (MainActivity) activity;
        setButtons();
        return view;
    }


    //This method create buttons that will be added to the view
    //and sets these buttons
    //One button for each user
    private void setButtons()
    {
        LinkedList<User> users =myactivity.getUsers();
        LinearLayout layout=(LinearLayout)view.findViewById(R.id.user_switch_buttons);
        for(User user : users) {
            LinearLayout listitem=new LinearLayout(getContext());
            listitem.setLayoutParams(new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.SCROLL_AXIS_HORIZONTAL));
            ImageRounder rounder =new ImageRounder(getActivity());
            TextView username= new TextView(getContext());
            if (Build.VERSION.SDK_INT < 23)
                username.setTextAppearance(getContext(), R.style.TextFont);
            else
                username.setTextAppearance(R.style.TextFont);

            username.setGravity(Gravity.CENTER_VERTICAL);
            username.setLayoutParams(new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,Gravity.CENTER_VERTICAL));
            username.setText(user.getUsername());
            ImageButton button = new ImageButton(this.getActivity());
            button.setImageBitmap(rounder.getCroppedBitmap(user.getProfilePic(),200));
            button.setBackgroundDrawable(null);
           button.setTag(user.getUsername());
            button.setOnClickListener(this);
            button.setLayoutParams(new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            listitem.addView(button);
            listitem.addView(username);
            layout.addView(listitem);

        }


    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.registerbutton)
        {
            myactivity.registerNew();
        }
        else {
            String username = v.getTag().toString();
            myactivity.switchUser(username);
        }
        }


}
