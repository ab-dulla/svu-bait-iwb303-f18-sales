package org.svuonline.f18sales.data;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Utilities {
    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final Locale LOCAL_DATE_FORMAT = Locale.US;

    public static ArrayList<String> GetYearsList() {
        ArrayList<String> years = new ArrayList<>();
        years.add("-- السنة --");
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear; i >= 1980; i--) {
            years.add(Integer.toString(i));
        }
        return years;
    }

    public static ArrayList<String> GetMonthsList() {
        ArrayList<String> months = new ArrayList<>();
        months.add("-- الشهر --");
        months.add("01");
        months.add("02");
        months.add("03");
        months.add("04");
        months.add("05");
        months.add("06");
        months.add("07");
        months.add("08");
        months.add("09");
        months.add("10");
        months.add("11");
        months.add("12");
        return months;
    }

    public static void initCalendarElement(Context context1, EditText tDate) {
        final EditText txtDate = tDate;
        final Context context = context1;
        final Calendar calendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                // update calender
                SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, LOCAL_DATE_FORMAT);
                txtDate.setText(sdf.format(calendar.getTime()));
            }
        };
        // set on click listener
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(context, date, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    public static void showMessage(Context context, String title, String Message) {
        new AlertDialog.Builder(context)
                .setCancelable(true)
                .setTitle(title)
                .setMessage(Message)
                .show();
    }
}
