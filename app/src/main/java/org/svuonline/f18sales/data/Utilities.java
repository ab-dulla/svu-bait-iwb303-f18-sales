package org.svuonline.f18sales.data;

import android.app.Activity;
import android.app.AlertDialog;
import android.widget.Spinner;
import java.util.Calendar;
import java.util.ArrayList;
import android.widget.ArrayAdapter;
import android.support.v7.app.AppCompatActivity;
import android.app.Application;
import android.content.Context;

public class Utilities extends Activity {

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
}
