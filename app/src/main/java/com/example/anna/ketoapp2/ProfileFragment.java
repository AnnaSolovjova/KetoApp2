package com.example.anna.ketoapp2;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;


/**
 *This class is the handler for the profile fragment
 * and the operations connected to this action,
 * including with populating the views with the user data,
 * handling user input for data change.
 */
public class ProfileFragment extends Fragment  implements View.OnClickListener {

public User user;
DatabaseHelper db;
public PendingIntent pendingIntent;
private View view;
RadioGroup regiment;
EditText username ,insulin ,age;
MainActivity myactivity;
Validation validation;
    public ProfileFragment() {
       user=new User();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        ((Button)view.findViewById(R.id.profile_edit_button)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.profile_del_button)).setOnClickListener(this);
        db=new DatabaseHelper(getActivity());
        Activity activity = getActivity();
        myactivity = (MainActivity) activity;
        validation =new Validation();
        setProfile(getUser());
        return view;
    }

    public User getUser()
    {
        return myactivity.getUser();
    }

    //This method populates the views with the data from database
    //when displaying profile
    public void setProfile(User user)
    {
        ImageRounder rounder =new ImageRounder(getActivity());
        ((ImageView) view.findViewById(R.id.profile_pic)).setImageBitmap(rounder.getCroppedBitmap(user.getProfilePic(), 200));
        ((Button)view.findViewById(R.id.profile_edit_button)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.profile_del_button)).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.username_text)).setText(user.getUsername());
        ((TextView) view.findViewById(R.id.insulin_text)).setText(user.getInsulinRegiment());
        ((TextView) view.findViewById(R.id.age_text)).setText(user.getDateOfBirth().substring(0, 2) + "/" + user.getDateOfBirth().substring(2, 4) + "/" + user.getDateOfBirth().substring(4, 8));
    }
    //This method populates the views with the data from database
    //when displaying edit profile profile
    public void setEditProfile(User user)
    {

        regiment=(RadioGroup)view.findViewById(R.id.radioGroup2);
        setRadioButtons(regiment);
        FocusChange focusChange=new FocusChange(getContext());
        ((RelativeLayout)view.findViewById(R.id.profile_edit)).setOnTouchListener(focusChange);
        ImageRounder rounder =new ImageRounder(getActivity());
        ((ImageButton) view.findViewById(R.id.profile_pic_edit)).setImageBitmap(rounder.getCroppedBitmap(user.getProfilePic(),200));
        ((Button)view.findViewById(R.id.profile_edit_comp_button)).setOnClickListener(this);
        ((ImageButton)view.findViewById(R.id.profile_pic_edit)).setOnClickListener(this);
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.insulin_regiment_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ((EditText) view.findViewById(R.id.username_edit)).setText(user.getUsername());
        if(user.getInsulinRegiment().equals("Insulin Pen"))
            regiment.check(R.id.pen);
        else
            regiment.check(R.id.pump);
        ((EditText) view.findViewById(R.id.dateOfBirthDay_edit)).setText(user.getDateOfBirth().substring(0, 2));
        ((EditText) view.findViewById(R.id.dateOfBirthMonth_edit)).setText(user.getDateOfBirth().substring(2, 4));
        ((EditText) view.findViewById(R.id.dateOfBirthYear_edit)).setText(user.getDateOfBirth().substring(4,8));
    }

    @Override
    public void onClick(View v) {
        String output;
        switch(v.getId()) {

            case R.id.profile_edit_button:
                setViewLayout(R.layout.edit_profile_fragment);
                setEditProfile(getUser());
                break;
            case R.id.profile_del_button:
                db.deleteUser(user.getUsername());
                //TODO:move to activity
                if (getUser() == null)
                    startActivity(new Intent(getActivity(), Registration.class));
                else {
                    setViewLayout(R.layout.fragment_profile);
                    setProfile(getUser());
                }
                break;
            case R.id.profile_edit_comp_button:
                String newU = ((EditText) view.findViewById(R.id.username_edit)).getText().toString();
                String newD = ((EditText) view.findViewById(R.id.dateOfBirthDay_edit)).getText().toString();
                String newM = ((EditText) view.findViewById(R.id.dateOfBirthMonth_edit)).getText().toString();
                String newY = ((EditText) view.findViewById(R.id.dateOfBirthYear_edit)).getText().toString();
                String newI = ((RadioButton) view.findViewById(regiment.getCheckedRadioButtonId())).getText().toString();
                Bitmap newP = ((BitmapDrawable) ((ImageButton) view.findViewById(R.id.profile_pic_edit)).getDrawable()).getBitmap();
                if (newU.matches("") || newD.matches("") || newM.matches("") || newY.matches("") || newI.matches("")) {
                    Toast.makeText(getActivity(), "Please don't leave any blank fields",
                            Toast.LENGTH_LONG).show();
                } else if (db.userExists(newU) && (!newU.equals(user.getUsername()))) {
                    Toast.makeText(getActivity(), "Such username already exists",
                            Toast.LENGTH_LONG).show();}

                else try {
                        if ((output = validation.dateValidation(newD, newM, newY)) != null) {
                                        Toast.makeText(getActivity(), output,
                                                Toast.LENGTH_LONG).show();
                                    }


                        else{
                            if( db.editUser(user.getUsername(),newU,newD+newM+newY,newI,newP));
                            setViewLayout(R.layout.fragment_profile);
                            setProfile(getUser());}
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                break;
            case R.id.profile_pic_edit:
                //Opens the system media storage for selecting the image
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 1);
                break;
        }

    }


    private void setRadioButtons(RadioGroup reg) {
        reg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.pen:
                        break;
                    case R.id.pump:

                        break;
                    default:
                        break;
                }
            }
        });
    }

    //Method that handles data received when image file is selected from the gallery
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( requestCode == 1 &&resultCode == Activity.RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            // Get the cursor
            Cursor cursor = this.getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            // Move to first row
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String imgDecodableString = cursor.getString(columnIndex);
            cursor.close();
            ImageRounder rounder =new ImageRounder(getActivity());
            Bitmap image=BitmapFactory.decodeFile(imgDecodableString);
                    ((ImageButton) view.findViewById(R.id.profile_pic_edit)).setImageBitmap(rounder.getCroppedBitmap(image, 200));

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
