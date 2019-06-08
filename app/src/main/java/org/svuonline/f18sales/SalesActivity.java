package org.svuonline.f18sales;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.support.v7.widget.AppCompatSpinner;

import org.svuonline.f18sales.data.DatabaseHelper;
import org.svuonline.f18sales.data.Utilities;
import org.svuonline.f18sales.model.Region;
import org.svuonline.f18sales.model.Salesman;
import org.svuonline.f18sales.salesmen.management.AddSalesmanFragment;
import org.svuonline.f18sales.salesmen.management.RegionSpinnerArrayAdapter;
import org.svuonline.f18sales.salesmen.management.SalesmenSpinnerArrayAdapter;

import java.util.ArrayList;
import java.util.Locale;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.support.v4.app.Fragment;
import android.widget.Toast;

import org.svuonline.f18sales.data.Utilities;

public class SalesActivity extends AppCompatActivity implements View.OnClickListener {

/******************************************************
 **************        Declarations       *************
 ******************************************************/
    private Spinner spinnerSalesmen;
    private Spinner spinnerYears;
    private Spinner spinnerMonths;
    private Button btnSearch;
    private TextView txtName;
    private TextView txtId;
    private TextView txtYear;
    private TextView txtMonth;
    private TextView txtHiringDate;
    private ImageView imgSalesman;
    DatabaseHelper dbHelper;
    SQLiteDatabase db;


    /******************************************************
     **************      Controls Events      *************
     ******************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);
        getSupportActionBar().setTitle("المبيعات والعمولات");
        dbHelper = new DatabaseHelper(this);

        FindElements();
        SetControlsEvents();
        fillSalesmen();
        FillYears();
        FillMonths();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.btnSearch:
                if(ValidateInputs())
                {
                    Salesman selectedSalesman = (Salesman) spinnerSalesmen.getSelectedItem();
                    selectedSalesman = dbHelper.getSalesmenById(selectedSalesman.getId());

                    txtId.setText(selectedSalesman.getId().toString());
                    txtName.setText(selectedSalesman.getFullName());
                    txtHiringDate.setText(selectedSalesman.getHiringDate().toString());
                    txtYear.setText(spinnerYears.getSelectedItem().toString());
                    txtMonth.setText(spinnerMonths.getSelectedItem().toString());
                    byte[] image = selectedSalesman.getImage();
                    Bitmap bmp= BitmapFactory.decodeByteArray(image, 0 , image.length);
                    imgSalesman.setImageBitmap(bmp);

                }
                else
                {
                    Utilities.ShowMessage(this,"","عفواً، يجب أن تقوم بإدخال جميع معايير البحث");
                }
                break;
        }
    }


    /******************************************************
     **************      Private Methods      *************
     ******************************************************/
    private boolean ValidateInputs(){
        boolean result = true;
        if(spinnerSalesmen.getSelectedItemPosition() == 0 || spinnerYears.getSelectedItemPosition() == 0 || spinnerMonths.getSelectedItemPosition() == 0 )
        {
            result = false;
        }
        return result;
    }

    private void FindElements(){
        spinnerSalesmen = findViewById(R.id.spinnerSalesmen);
        spinnerYears = findViewById(R.id.spinnerYears);
        spinnerMonths = findViewById(R.id.spinnerMonths);
        btnSearch = findViewById(R.id.btnSearch);
        txtId =findViewById(R.id.txtId);
        txtName =findViewById(R.id.txtName);
        txtYear =findViewById(R.id.txtYear);
        txtMonth =findViewById(R.id.txtMonth);
        txtHiringDate =findViewById(R.id.txtHiringDate);
        imgSalesman = findViewById(R.id.imgSalesman);
    }

    private void SetControlsEvents() {
        btnSearch.setOnClickListener(this);
    }

    private void fillSalesmen() {
        db = openOrCreateDatabase("f18SalesDb",Context.MODE_PRIVATE, null);
        ArrayList<Salesman> salesmenList =  dbHelper.getSalesmenList(db);
        SalesmenSpinnerArrayAdapter salesmenAdapter = new SalesmenSpinnerArrayAdapter(this, salesmenList);
        spinnerSalesmen.setAdapter(salesmenAdapter);
    }

    private void FillYears(){
        ArrayList<String> years = Utilities.GetYearsList();
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,years);
        spinnerYears.setAdapter(adapter);
    }

    private void FillMonths(){
        ArrayList<String> months = Utilities.GetMonthsList();
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,months);
        spinnerMonths.setAdapter(adapter);
    }
}
