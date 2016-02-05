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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment  implements View.OnClickListener {

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
        ((Button)view.findViewById(R.id.profile_edit_button)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.profile_del_button)).setOnClickListener(this);
        db=new DatabaseHelper(getActivity());
        setProfile(getUser());
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
        ImageRounder rounder =new ImageRounder(getActivity());
        ((ImageView) view.findViewById(R.id.profile_pic)).setImageBitmap(rounder.getCroppedBitmap(user.getProfilePic(), 200));
        ((Button)view.findViewById(R.id.profile_edit_button)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.profile_del_button)).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.username_text)).setText(user.getUsername());
        ((TextView) view.findViewById(R.id.insulin_text)).setText(user.getInsulinRegiment());
        ((TextView) view.findViewById(R.id.age_text)).setText(user.getDateOfBirth());

    }
    public void setEditProfile(User user)
    {

        ImageRounder rounder =new ImageRounder(getActivity());
        ((ImageButton) view.findViewById(R.id.profile_pic_edit)).setImageBitmap(rounder.getCroppedBitmap(user.getProfilePic(),200));
        ((Button)view.findViewById(R.id.profile_edit_comp_button)).setOnClickListener(this);
        ((ImageButton)view.findViewById(R.id.profile_pic_edit)).setOnClickListener(this);
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.insulin_regiment_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ((EditText) view.findViewById(R.id.username_edit)).setText(user.getUsername());
        ((Spinner) view.findViewById(R.id.insulinRegiment_edit)).setAdapter(staticAdapter);
        ((EditText) view.findViewById(R.id.dateOfBirth_edit)).setText(user.getDateOfBirth());
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()) {

            case R.id.profile_edit_button:
                setViewLayout(R.layout.edit_profile_fragment);
                setEditProfile(getUser());
                break;
            case R.id.profile_del_button:
                db.deleteUser(user.getUsername());
                if(getUser()==null)
                    startActivity(new Intent(getActivity(), Registration.class));
                else{
                    setViewLayout(R.layout.fragment_profile);
                    setProfile(getUser());
                }
                break;
            case R.id.profile_edit_comp_button:
                String newU=((EditText)view.findViewById(R.id.username_edit)).getText().toString();
                String newD=((EditText)view.findViewById(R.id.dateOfBirth_edit)).getText().toString();
                String newI=((Spinner)view.findViewById(R.id.insulinRegiment_edit)).getSelectedItem().toString();
                Bitmap newP=((BitmapDrawable)((ImageButton)view.findViewById(R.id.profile_pic_edit)).getDrawable()).getBitmap();
                if( db.editUser(user.getUsername(),newU,newD,newI,newP));
                setViewLayout(R.layout.fragment_profile);
                setProfile(getUser());
                break;
            case R.id.profile_pic_edit:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 1);
                break;
           // case R.id.profile_delete_comp_button:
             //   break;

        }

    }

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
