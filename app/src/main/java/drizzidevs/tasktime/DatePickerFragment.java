package drizzidevs.tasktime;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;

import java.util.Date;
import java.util.GregorianCalendar;




public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = "DatePickerFragment";

    public static final String DATE_PICKER_ID = "ID";
    public static final String DATE_PICKER_TITLE = "TITLE";
    public static final String DATE_PICKER_DATE = "DATE";

    int mDialogId = 0;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // use the current date initially
        final GregorianCalendar cal = new GregorianCalendar();
        String title = null;

        Bundle arguments = getArguments();
        if (arguments != null) {
            mDialogId = arguments.getInt(DATE_PICKER_ID);
            title = arguments.getString(DATE_PICKER_TITLE);

            // if we passed a date, use it ; otherwise leave cal set to the current date;
            Date givenDate = (Date) arguments.getSerializable(DATE_PICKER_DATE);
            if (givenDate != null) {
                cal.setTime(givenDate);
                Log.d(TAG, "in onCreateDialog, retrieved date = " + givenDate.toString());
            }
        }

        int year = cal.get(GregorianCalendar.YEAR);
        int month = cal.get(GregorianCalendar.MONTH);
        int day = cal.get(GregorianCalendar.DAY_OF_MONTH);

        @SuppressWarnings("ConstantConditions")
        DatePickerDialog dpd = new DatePickerDialog(getContext(), this,year,month,day);
        if (title != null) {
            dpd.setTitle(title);
        }
        return dpd;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (!(context instanceof  DatePickerDialog.OnDateSetListener)) {
            throw new ClassCastException(context.toString() + " must implement DatePickerDialog.OnDateSetListener interface");
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Log.d(TAG, "Entering on date set");
        DatePickerDialog.OnDateSetListener listener = (DatePickerDialog.OnDateSetListener)getActivity();
        if (listener != null) {
            // notify caller of the user-selected values
            view.setTag(mDialogId);
            listener.onDateSet(view, year, month, dayOfMonth);
        }
        Log.d(TAG, "Exiting onDateSet");

    }
}














