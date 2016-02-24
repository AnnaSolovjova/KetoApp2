package com.example.anna.ketoapp2;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Registration extends AppCompatActivity implements View.OnClickListener {
    TextView error;
    EditText dateOfBirthD,dateOfBirthM,dateOfBirthY,username;
    Spinner insulinRegiment;
    Button registbutton;
    DatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter.createFromResource(this, R.array.insulin_regiment_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        username=(EditText)findViewById(R.id.username);
        dateOfBirthD=(EditText)findViewById(R.id.dateOfBirthDay);
        dateOfBirthM=(EditText)findViewById(R.id.dateOfBirthMonth);
        dateOfBirthY=(EditText)findViewById(R.id.dateOfBirthYear);
        insulinRegiment=(Spinner) findViewById(R.id.insulinRegiment);
        insulinRegiment.setAdapter(staticAdapter);
        registbutton=(Button)findViewById(R.id.registerbutton);
        registbutton.setOnClickListener(this);
        registbutton.setOnClickListener(this);
        FocusChange focusChange=new FocusChange(this);
        ((RelativeLayout)findViewById(R.id.registration_layout)).setOnTouchListener(focusChange);
        db=new DatabaseHelper(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.registerbutton:
                if(username.getText().toString().matches("")|| insulinRegiment.getSelectedItem().toString().matches("") ||  dateOfBirthD.getText().toString().matches("")||dateOfBirthM.getText().toString().matches("")||dateOfBirthY.getText().toString().matches(""))
                {
                    Toast.makeText(this, "Please fill all the fields",
                            Toast.LENGTH_LONG).show();
                }
                else if(db.userExists(username.getText().toString()))
                {
                    Toast.makeText(this, "Such username already exists",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    String dateOfBirth=dateOfBirthD.getText().toString()+dateOfBirthM.getText().toString()+dateOfBirthY.getText().toString();
                    boolean success = db.Register(username.getText().toString(),dateOfBirth , insulinRegiment.getSelectedItem().toString());
                    if (success)
                        startActivity(new Intent(this, MainActivity.class));
                }
                break;
        }
    }

}
