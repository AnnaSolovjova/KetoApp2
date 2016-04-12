package com.example.anna.ketoapp2;

import android.content.Intent;
import android.widget.RadioButton;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Anna on 08/04/2016.
 * This class is used by other classes for calculations, formatting, and valitadion of the data
 */
public class Validation {

SimpleDateFormat formatter ;

    public Validation()
    {
        formatter= new SimpleDateFormat("dd-MM-yyyy");
    }

    public String dateValidation(String day, String month, String year) throws ParseException {

       if(day.length()<2||month.length()<2||year.length()<4)
        {
            return "Please fill fields in a right format";
        }
        else if(Integer.parseInt(month, 10)>12||Integer.parseInt(month, 10)==0)
        {
            return "Please Month should be a number from 01 to 12";
        }
        else if(Integer.parseInt(day, 10)>31||Integer.parseInt(day, 10)==0)
        {
            return "Please Day should be a number from 01 to 31";
        }
        else if(Integer.parseInt(day, 10)==31&&((Math.abs(Integer.parseInt(month, 10)-5) == 1) || (Math.abs(Integer.parseInt(month, 10)-10) == 1)))
        {
            return "Chosen month has only 30 days";
        }
        else if(Integer.parseInt(month, 10)==2&&Integer.parseInt(day, 10)>29)
        {
            return "Chosen month has maximum of 29 days";
        }
        else if(formatter.parse(day+'-'+month+'-'+year).getTime()>formatter.parse(formatter.format(new Date())).getTime())
        {
            return "Date cannot of birth cannot be bigger than current date";
        }
        else {
            return null;
        }

    }
    //This method specifies the time format when calendar returns the analogue time unspecified
    public String timeValidation (Calendar cal)
    {
        String time =cal.get(Calendar.HOUR)+":"+cal.get(Calendar.MINUTE);
        if(cal.get(Calendar.AM_PM)==0)
            time=time+" AM";
        else
            time=time+" PM";
        return time;
    }

}
