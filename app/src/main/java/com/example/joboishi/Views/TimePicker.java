package com.example.joboishi.Views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.NumberPicker;

import com.example.joboishi.R;


@SuppressLint("CustomViewStyleable")
public class TimePicker extends NumberPicker {

    private int current = 0;
    private int max = 0;
    private int min = 0;


    public TimePicker(Context context) {
        super(context);
        initValue();
    }

    public TimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, com.example.joboishi.R.styleable.MyTimerPicker);
        final int n = a.getIndexCount();
        for (int i = 0; i < n; ++i) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.MyTimerPicker_min) {
                min = a.getInt(attr, 0);
                setMinValue(min);
            } else if (attr == R.styleable.MyTimerPicker_max) {
                max = a.getInt(attr, 0);
                setMaxValue(max);
            } else if (attr == R.styleable.MyTimerPicker_current) {
                current = a.getInt(attr, 0);
                setValue(current);
            }
        }
        a.recycle();
    }

    public TimePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initValue();
    }

    public void initValue() {
        setValue(current);
        setMinValue(min);
        setMaxValue(max);
    }
}
