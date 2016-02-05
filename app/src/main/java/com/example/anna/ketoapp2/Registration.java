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
import android.widget.Spinner;
import android.widget.TextView;

public class Registration extends AppCompatActivity implements View.OnClickListener {
    TextView error;
    EditText dateOfBirth,username;
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
        dateOfBirth=(EditText)findViewById(R.id.dateOfBirth);
        insulinRegiment=(Spinner) findViewById(R.id.insulinRegiment);
        insulinRegiment.setAdapter(staticAdapter);
        registbutton=(Button)findViewById(R.id.registerbutton);
        registbutton.setOnClickListener(this);
        registbutton.setOnClickListener(this);

        db=new DatabaseHelper(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.registerbutton:
                if(username.getText().toString().matches("")|| insulinRegiment.getSelectedItem().toString().matches("") ||  dateOfBirth.getText().toString().matches(""))
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(this).create(); //Read Update
                    alertDialog.setTitle("Error");
                    alertDialog.setMessage("Please fill all the fields");

                    alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // here you can add functions
                        }
                    });

                    alertDialog.show();
                }
                else {
                    boolean success = db.Register(username.getText().toString(), dateOfBirth.getText().toString(), insulinRegiment.getSelectedItem().toString());
                    if (success)
                        startActivity(new Intent(this, MainActivity.class));
                }
                break;
        }
    }

}
