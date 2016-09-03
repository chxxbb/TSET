package com.example.chen.tset.View;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.chen.tset.R;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2016/9/3 0003.
 */
public class CustomTimePicker extends TimePicker {
    public CustomTimePicker(Context context) {
        super(context);
        init();
    }

    public CustomTimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTimePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public void init() {
        getNumberPicker(this);
    }


    public void getNumberPicker(TimePicker timePicker) {
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$id");
            Field fieldHour = clazz.getField("hour");
            fieldHour.setAccessible(true);
            int hourId = fieldHour.getInt(null);
            NumberPicker hourNumberPicker = (NumberPicker) timePicker.findViewById(hourId);
            setDividerColor(hourNumberPicker);

            Field fieldminute = clazz.getField("minute");
            fieldminute.setAccessible(true);
            int minuteId = fieldminute.getInt(null);
            NumberPicker minuteNumberPicker = (NumberPicker) timePicker.findViewById(minuteId);
            setDividerColor(minuteNumberPicker);


            Field fieldDivider = clazz.getField("divider");
            fieldDivider.setAccessible(true);
            int dividerId = fieldDivider.getInt(null);
            TextView textView = (TextView) timePicker.findViewById(dividerId);
            textView.setTextColor(getResources().getColor(R.color.white));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setDividerColor(NumberPicker picker) {
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (java.lang.reflect.Field pf : pickerFields) {
            Log.v("setDividerColor", "pf:" + pf.getName() + " type :" + pf.getGenericType());
            if (pf.getName().equals("mSelectionDivider")) {
                Log.v("setDividerColor", "find......mSelectionDivider");
                pf.setAccessible(true);
                try {
                    ColorDrawable colorDrawable = new ColorDrawable();
                    colorDrawable.setColor(getResources().getColor(R.color.white));
                    pf.set(picker, colorDrawable);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            if (pf.getName().equals("mSelectionDividerHeight")) {
                Log.v("PowerSet", "find......mSelectionDividerHeight.");
            }
        }
    }
}
