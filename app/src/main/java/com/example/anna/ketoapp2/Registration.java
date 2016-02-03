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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Registration extends AppCompatActivity implements View.OnClickListener {
    TextView error;
    EditText password, passwordr,dateOfBirth,insulinRegiment,username;
    Button registbutton;
    DatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        username=(EditText)findViewById(R.id.username);
        password=(EditText)findViewById(R.id.password);
        passwordr=(EditText)findViewById(R.id.newpassword);
        dateOfBirth=(EditText)findViewById(R.id.dateOfBirth);
        insulinRegiment=(EditText)findViewById(R.id.insulinRegiment);
        registbutton=(Button)findViewById(R.id.registerbutton);
        registbutton.setOnClickListener(this);
        registbutton.setOnClickListener(this);

        db=new DatabaseHelper(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.registerbutton:
                if(username.getText().toString().matches("")|| password.getText().toString().matches("")|| insulinRegiment.getText().toString().matches("") ||  dateOfBirth.getText().toString().matches(""))
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
                    boolean success = db.Register(username.getText().toString(), password.getText().toString(), dateOfBirth.getText().toString(), insulinRegiment.getText().toString());
                    if (success)
                        startActivity(new Intent(this, Protocol.class));
                }
                break;
        }
    }

}
