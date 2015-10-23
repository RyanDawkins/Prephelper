package com.ryanddawkins.prephelper.ui.items.detail;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import java.util.Calendar;

/**
 * Created by ryan on 10/23/15.
 */
public class DatePickerFragment extends DialogFragment {

    private ItemDetailActivity activity;

    public DatePickerFragment(ItemDetailActivity activity) {
        this.activity = activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(this.activity, this.activity, year, month, day);
    }
}