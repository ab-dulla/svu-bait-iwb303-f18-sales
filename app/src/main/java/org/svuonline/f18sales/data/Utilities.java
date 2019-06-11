package org.svuonline.f18sales.data;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Locale;

import android.widget.ArrayAdapter;
import android.support.v7.app.AppCompatActivity;
import android.app.Application;
import android.content.Context;

import org.svuonline.f18sales.salesmen.management.AddSalesmanFragment;

import android.widget.EditText;

public class Utilities extends Activity {

    private static final String DATE_FORMAT = "MM/dd/yyyy";
    private static final Locale LOCAL_DATE_FORMAT = Locale.US;



    public static ArrayList<String> GetYearsList()
    {
        ArrayList<String> years = new ArrayList<>();
        years.add("-- السنة --");

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        for(int i = currentYear; i >=1980 ; i--){
            years.add(Integer.toString(i));
        }
        return years;
    }

    public static ArrayList<String> GetMonthsList()
    {
        ArrayList<String> monts = new ArrayList<>();
        monts.add("-- الشهر --");
        monts.add("01");
        monts.add("02");
        monts.add("03");
        monts.add("04");
        monts.add("05");
        monts.add("06");
        monts.add("07");
        monts.add("08");
        monts.add("09");
        monts.add("10");
        monts.add("11");
        monts.add("12");
        return monts;
    }

    public static void ShowMessage(Context context, String title, String Message) {
        new AlertDialog.Builder(context)
                .setCancelable(true)
                .setTitle(title)
                .setMessage(Message)
                .show();
    }


    public static void InitSaleDateCalender(Context cont,EditText tDate) {

        final EditText txtDate = tDate;
        final Context context = cont;
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
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(context, date, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }


}
