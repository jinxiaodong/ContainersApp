package com.project.container.containersapp.frame.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

/**
 *
 * 输入框的监听器，判断输入框中内容是否为空
 */
public class EtTextWitcher implements TextWatcher {

    private List<EditText> editTexts;
    private Button button;
    private TextView textview;

    public EtTextWitcher(List<EditText> editTexts, Button button)
    {
        this.editTexts = editTexts;
        this.button = button;
    }


    public EtTextWitcher(List<EditText> editTexts, TextView textView)
    {
        this.editTexts = editTexts;
        this.textview = textView;
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        boolean canGo = true;
        for (int i = 0;i<editTexts.size();i++)
        {
            EditText editText = editTexts.get(i);
            if(editText.length() < 1)
            {
                canGo = false;
                break;
            }
        }

        if(canGo)
        {
            button.setEnabled(true);
        }
        else
        {
            button.setEnabled(false);
        }

    }
}
