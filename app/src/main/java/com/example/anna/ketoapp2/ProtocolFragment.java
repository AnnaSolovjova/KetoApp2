package com.example.anna.ketoapp2;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ProtocolFragment extends Fragment implements View.OnClickListener {
View view;
DatabaseHelper db;
User user;
RelativeLayout layout[]=new RelativeLayout[14];
    public ProtocolFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_protocol, container, false);
        db=new DatabaseHelper(getActivity());
        setProfile(getUser());
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
                setUpProtocolProcedure();
                break;
            case R.id.unwell_yes:
                layout[0].setVisibility(View.INVISIBLE);
                layout[1].setVisibility(View.VISIBLE);
                break;
            case R.id.danger_signs_button_yes:
                layout[1].setVisibility(View.INVISIBLE);
                layout[6].setVisibility(View.VISIBLE);
                break;
            case R.id.danger_signs_button_no:
                layout[1].setVisibility(View.INVISIBLE);
                layout[2].setVisibility(View.VISIBLE);
                break;
            case R.id.unwell_no:
                layout[1].setVisibility(View.INVISIBLE);
                layout[2].setVisibility(View.VISIBLE);
                break;
            case R.id.glucose_next:
                layout[2].setVisibility(View.INVISIBLE);
                layout[3].setVisibility(View.VISIBLE);
                break;
            case R.id.premeal_yes:
                layout[3].setVisibility(View.INVISIBLE);
                layout[4].setVisibility(View.VISIBLE);
                break;
            case R.id.premeal_no:
                layout[3].setVisibility(View.INVISIBLE);
                layout[5].setVisibility(View.VISIBLE);
                break;
            case R.id.ketones_yes:
                layout[1].setVisibility(View.INVISIBLE);
                layout[2].setVisibility(View.VISIBLE);
                break;
            case R.id.ketones_no:
                layout[1].setVisibility(View.INVISIBLE);
                layout[2].setVisibility(View.VISIBLE);
                break;


        }
    }
    public void setProfile(User user)
    {
        ((Button)view.findViewById(R.id.profile_correct_yes)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.profile_correct_no)).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.username_text)).setText(user.getUsername());
        ((TextView) view.findViewById(R.id.insulin_text)).setText(user.getInsulinRegiment());
        ((TextView) view.findViewById(R.id.age_text)).setText(user.getDateOfBirth());

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
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.fragment_protocol_process, null);
        ViewGroup rootView = (ViewGroup) getView();
        view.setLayoutParams(rootView.getRootView().getLayoutParams());
        rootView.removeAllViews();
        rootView.addView(view);
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
        ((Button)view.findViewById(R.id.emergency_contact)).setOnClickListener(this);

        //((Button)view.findViewById(R.id.insulin_next)).setOnClickListener(this);
        layout[0]=(RelativeLayout)view.findViewById(R.id.part1);
        layout[1]=(RelativeLayout)view.findViewById(R.id.part2);
        layout[2]=(RelativeLayout)view.findViewById(R.id.part3);
        layout[3]=(RelativeLayout)view.findViewById(R.id.part4);
        layout[4]=(RelativeLayout)view.findViewById(R.id.part5);
     /*   layout[5]=(RelativeLayout)view.findViewById(R.id.part6);
        layout[6]=(RelativeLayout)view.findViewById(R.id.part7);
        layout[7]=(RelativeLayout)view.findViewById(R.id.part8);
        layout[8]=(RelativeLayout)view.findViewById(R.id.part9);
        layout[9]=(RelativeLayout)view.findViewById(R.id.part10);
        layout[10]=(RelativeLayout)view.findViewById(R.id.part11);
        layout[11]=(RelativeLayout)view.findViewById(R.id.part12);
        layout[12]=(RelativeLayout)view.findViewById(R.id.part13);
        layout[13]=(RelativeLayout)view.findViewById(R.id.part14);*/

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


}