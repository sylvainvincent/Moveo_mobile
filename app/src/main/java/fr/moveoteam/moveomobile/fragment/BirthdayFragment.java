package fr.moveoteam.moveomobile.fragment;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.widget.Button;
import android.widget.DatePicker;

/**
 * Created by Sylvain on 30/05/15.
 */
public class BirthdayFragment extends DatePickerDialog {

    private int day,month,year;

    public BirthdayFragment(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
        super(context, callBack, year, monthOfYear, dayOfMonth);
    }

    public BirthdayFragment(Context context, int theme, OnDateSetListener listener, int year, int monthOfYear, int dayOfMonth) {
        super(context, theme, listener, year, monthOfYear, dayOfMonth);
    }

    @Override
    public Button getButton(int whichButton) {
        return super.getButton(whichButton);
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
