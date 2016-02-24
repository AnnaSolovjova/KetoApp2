package com.example.anna.ketoapp2;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * http://karimvarela.com/2012/07/24/android-how-to-hide-keyboard-by-touching-screen-outside-keyboard/
 * Created by Anna on 08/02/2016.
 */

public class FocusChange  implements View.OnTouchListener {
    Context context;
    public FocusChange (Context context)
    {
        this.context=context;
    }

        public boolean onTouch(View view, MotionEvent ev){

            if(!(view instanceof EditText)) {
                InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            }
            return false;
        }
    }

