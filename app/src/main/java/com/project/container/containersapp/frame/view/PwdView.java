package com.project.container.containersapp.frame.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.project.container.containersapp.R;


/**
 * 密码自定义组件.
 */
public class PwdView extends LinearLayout {

    private String hint_string;
    private String text_string;
    public ClearEditText et_pwd;

    public PwdView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PwdView);
        hint_string = ta.getString(R.styleable.PwdView_hint_text);
        text_string = ta.getString(R.styleable.PwdView_text);
        ta.recycle();
        initView(context);
    }

    public PwdView(Context context) {
        super(context);
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initView(Context context) {
        this.setOrientation(HORIZONTAL);
        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45, context.getResources().getDisplayMetrics());
        int pad = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics());
        et_pwd = (ClearEditText) LayoutInflater.from(context).inflate(R.layout.pwd_edit, null);
        LayoutParams layout = new LayoutParams(0, size);
        layout.weight = 1;
        et_pwd.setText(text_string);
        et_pwd.setHint(hint_string);
        addView(et_pwd, layout);
        CheckBox checkBox = new CheckBox(context);
        checkBox.setButtonDrawable(R.drawable.pwd_eye_selector);
        checkBox.setPadding(pad, pad, pad, pad);
        LayoutParams layout1 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        addView(checkBox, layout1);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    et_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    et_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                CharSequence text = et_pwd.getText();
                et_pwd.setSelection(text.length());
            }
        });
    }

    public void setText(String text_string) {
        this.text_string = text_string;
        et_pwd.setText(text_string);
    }

    public void setHint(String hint_string) {
        this.hint_string = hint_string;
        et_pwd.setHint(hint_string);
    }

    //2016/07/13 chy 添加获取编辑框的方法。来今天编辑框中内容的变化
    public EditText getEt_pwd()
    {
        return et_pwd;
    }

    public String getText() {
        return et_pwd.getText().toString().trim();
    }

}
