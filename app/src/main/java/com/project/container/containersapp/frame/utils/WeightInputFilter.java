package com.project.container.containersapp.frame.utils;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 重量输入过滤器
 */
public class WeightInputFilter implements InputFilter {
    Pattern mPattern;
    //小数点后的位数
    private static final int POINTER_LENGTH = 2;
    private static final String POINTER = ".";
    private static final String ZERO = "0";

    public WeightInputFilter() {
        mPattern = Pattern.compile("([0-9]|\\.)*");
    }

    /**
     * @param source 新输入的字符串
     * @param start  新输入的字符串起始下标，一般为0
     * @param end    新输入的字符串终点下标，一般为source长度-1
     * @param dest   输入之前文本框内容
     * @param dstart 原内容起始坐标，一般为0
     * @param dend   原内容终点坐标，一般为dest长度-1
     * @return 输入内容
     */
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        String sourceText = source.toString();
        String destText = dest.toString();
        //验证删除等按键
        if (TextUtils.isEmpty(sourceText)) {
            return "";
        }
        Matcher matcher = mPattern.matcher(source);
        //已经输入小数点的情况下，只能输入数字
        if (destText.contains(POINTER)) {
            if (!matcher.matches()) {
                return "";
            } else {
                if (POINTER.equals(source)) {  //只能输入一个小数点
                    return "";
                }
            }

        } else {
            //没有输入小数点的情况下，只能输入小数点和数字，但首位不能输入小数点和0
            if (!matcher.matches()) {
                return "";
            } else {
                if ((POINTER.equals(source)) && TextUtils.isEmpty(destText)) {
                    return "";
                }
                //如果首位为“0”，则只能再输“.”
                if (ZERO.equals(destText)) {
                    if (!POINTER.equals(sourceText)) {
                        return "";
                    }
                } else {
                    //首位不为0，只能输入整数
//                    if (POINTER.equals(sourceText)) {
//                        return "";
//                    }
                }
            }
        }
        return dest.subSequence(dstart, dend) + sourceText;
    }
}