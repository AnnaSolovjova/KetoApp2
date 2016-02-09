package com.example.anna.ketoapp2;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by Anna on 08/02/2016.
 */
public class FocusChange  implements View.OnFocusChangeListener {
    Context context;
    public FocusChange (Context context)
    {
        this.context=context;
    }

        public void onFocusChange(View v, boolean hasFocus){

            if(!hasFocus) {
                EditText edtView = (EditText) v.findViewById(v.getId());
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtView.getWindowToken(), 0);

            }
        }
    }

