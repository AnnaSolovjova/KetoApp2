package com.example.anna.ketoapp2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.style.StyleSpan;
import android.text.style.TextAppearanceSpan;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.LinkedList;


public class SwitchFragment extends Fragment implements View.OnClickListener{
    View view;
    DatabaseHelper db;
    LinkedList<User> users;
    public SwitchFragment() {
        users=new LinkedList<User>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_switch, container, false);
        ((Button)view.findViewById(R.id.registerbutton)).setOnClickListener(this);
        db=new DatabaseHelper(getActivity());
        getUsers();
        setButtons();
        return view;
    }
    public LinkedList<User> getUsers()
    {

        Activity activity = getActivity();
        MainActivity myactivity = (MainActivity) activity;
        users =myactivity.getUsers();
        return users;
    }
    private void setButtons()
    {


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
            startActivity(new Intent(getActivity(), Registration.class));
        }
        else {
            Activity activity = getActivity();
            MainActivity myactivity = (MainActivity) activity;
            String username = v.getTag().toString();
            myactivity.switchUser(username);
        }
        }


}
