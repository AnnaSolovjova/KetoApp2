package com.example.anna.ketoapp2;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;;
import android.widget.Toast;
import java.text.ParseException;

public class Registration extends AppCompatActivity implements View.OnClickListener  {
    //Declaration of the variables and classes needed within the activity
    EditText dateOfBirthD,dateOfBirthM,dateOfBirthY,username;
    Button registbutton;
    DatabaseHelper db;
    RadioGroup regiment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter.createFromResource(this, R.array.insulin_regiment_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        regiment=(RadioGroup)findViewById(R.id.radioGroup2);
        setRadioButtons(regiment);
        username=(EditText)findViewById(R.id.username);
        dateOfBirthD=(EditText)findViewById(R.id.dateOfBirthDay);
        dateOfBirthM=(EditText)findViewById(R.id.dateOfBirthMonth);
        dateOfBirthY=(EditText)findViewById(R.id.dateOfBirthYear);
        registbutton=(Button)findViewById(R.id.registerbutton);
        registbutton.setOnClickListener(this);
        registbutton.setOnClickListener(this);
        FocusChange focusChange=new FocusChange(this);
        setNextFocus(new EditText[] {dateOfBirthD, dateOfBirthM,dateOfBirthY});
        ((RelativeLayout)findViewById(R.id.registration_layout)).setOnTouchListener(focusChange);
        db=new DatabaseHelper(this);
    }

    private void setRadioButtons(RadioGroup reg)
    {
        reg.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch (checkedId)
                {
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

    //Method that is responsiable for changing the focus of the cursor to the next edit text when one edit text is filled
    private void setNextFocus(EditText [] array)
    {
    final EditText [] finalArray= array.clone();;
        for(int i=0;i<array.length-1;i++){
            final int k=i;

            finalArray[k].addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (finalArray[k].length() == 2) {
                    finalArray[k].clearFocus();
                    finalArray[k+1].requestFocus();
                    finalArray[k+1].setCursorVisible(true);

                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        }
    }

    @Override
    public void onClick(View v) {
        String output;
        try {

            Validation validation = new Validation();
            switch(v.getId()) {
                case R.id.registerbutton:
                    if(username.getText().toString().matches("")||   dateOfBirthD.getText().toString().matches("")||dateOfBirthM.getText().toString().matches("")||dateOfBirthY.getText().toString().matches(""))
                    {
                        Toast.makeText(this, "Please fill all the fields",
                                Toast.LENGTH_LONG).show();
                    }
                    else if((output=validation.dateValidation(dateOfBirthD.getText().toString(),dateOfBirthM.getText().toString(),dateOfBirthY.getText().toString()))!=null)
                    {
                        Toast.makeText(this, output,
                                Toast.LENGTH_LONG).show();
                    }
                    else if(db.userExists(username.getText().toString()))
                    {
                        Toast.makeText(this, "Such username already exists",
                                Toast.LENGTH_LONG).show();
                    }
                    else {
                        String dateOfBirth=dateOfBirthD.getText().toString()+dateOfBirthM.getText().toString()+dateOfBirthY.getText().toString();
                        boolean success = db.Register(username.getText().toString(), dateOfBirth, ((RadioButton)findViewById(regiment.getCheckedRadioButtonId())).getText().toString());
                        if (success)
                            startActivity(new Intent(this, MainActivity.class));
                    }
                    break;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        //if back button pressed terurn to the main activity
        startActivity(new Intent(this, MainActivity.class));
    }

}
