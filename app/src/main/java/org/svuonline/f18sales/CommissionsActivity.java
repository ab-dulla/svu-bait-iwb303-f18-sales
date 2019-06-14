package org.svuonline.f18sales;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import org.svuonline.f18sales.data.DatabaseHelper;
import org.svuonline.f18sales.data.Utilities;
import org.svuonline.f18sales.model.Commission;
import org.svuonline.f18sales.model.Salesman;
import org.svuonline.f18sales.salesmen.management.SalesmenSpinnerArrayAdapter;

import java.io.File;
import java.util.ArrayList;


public class CommissionsActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private TableLayout tableRegionsCommissions;
    private DatabaseHelper dbHelper;

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
    private int totalCommission;

    /******************************************************
     **************      Controls Events      *************
     ******************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commissions);
        getSupportActionBar().setTitle("العمولات");
        dbHelper = new DatabaseHelper(this);

        findElements();
        fillSalesmen();
        fillYears();
        fillMonths();
        setControlsEvents();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSearch) {
            if (ValidateInputs()) {
                Salesman selectedSalesman = (Salesman) spinnerSalesmen.getSelectedItem();
                selectedSalesman = dbHelper.getSalesmenById(selectedSalesman.getId());
                fillCommissionInfo(selectedSalesman.getId());
            } else {
                Utilities.showMessage(this, "", "عفواً، يجب أن تقوم بإدخال جميع معايير البحث");
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            clearSalesmanInfo();
        } else {
            Salesman selectedSalesman = (Salesman) spinnerSalesmen.getSelectedItem();
            selectedSalesman = dbHelper.getSalesmenById(selectedSalesman.getId());
            fillSalesmanInfo(selectedSalesman);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // do nothing
    }

    /******************************************************
     **************      Private Methods      *************
     ******************************************************/
    private boolean ValidateInputs() {
        return spinnerSalesmen.getSelectedItemPosition() != 0 &&
                spinnerYears.getSelectedItemPosition() != 0 &&
                spinnerMonths.getSelectedItemPosition() != 0;
    }

    private void findElements() {
        spinnerSalesmen = findViewById(R.id.spinnerSalesmen);
        spinnerYears = findViewById(R.id.spinnerYears);
        spinnerMonths = findViewById(R.id.spinnerMonths);
        btnSearch = findViewById(R.id.btnSearch);
        txtId = findViewById(R.id.txtId);
        txtName = findViewById(R.id.txtName);
        txtYear = findViewById(R.id.txtYear);
        txtMonth = findViewById(R.id.txtMonth);
        txtHiringDate = findViewById(R.id.txtHiringDate);
        imgSalesman = findViewById(R.id.imgSalesman);
        tableRegionsCommissions = findViewById(R.id.tableRegionsCommissions);
    }

    private void setControlsEvents() {
        btnSearch.setOnClickListener(this);
        spinnerSalesmen.setOnItemSelectedListener(this);
    }

    private void fillSalesmen() {
        ArrayList<Salesman> salesmenList = dbHelper.getSalesmenList();
        SalesmenSpinnerArrayAdapter salesmenAdapter = new SalesmenSpinnerArrayAdapter(this, salesmenList);
        spinnerSalesmen.setAdapter(salesmenAdapter);
    }

    private void fillYears() {
        ArrayList<String> years = Utilities.GetYearsList();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, years);
        spinnerYears.setAdapter(adapter);
    }

    private void fillMonths() {
        ArrayList<String> months = Utilities.GetMonthsList();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, months);
        spinnerMonths.setAdapter(adapter);
    }

    private void fillSalesmanInfo(Salesman salesman) {
        txtId.setText(salesman.getId().toString());
        txtName.setText(salesman.getFullName());
        txtHiringDate.setText(salesman.getHiringDate());
        // read image from internal storage and show it on the UI
        File imgFile = new File(salesman.getImagePath());
        if (imgFile.exists()) {
            Bitmap image = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imgSalesman.setImageBitmap(image);
        }
    }

    private void fillCommissionInfo(int salesmanId) {
        totalCommission = 0;
        txtYear.setText(spinnerYears.getSelectedItem().toString());
        txtMonth.setText(spinnerMonths.getSelectedItem().toString());
        ArrayList<Commission> CommissionsList = dbHelper.getSalesmanCommissions(salesmanId, spinnerYears.getSelectedItem().toString(), spinnerMonths.getSelectedItem().toString());
        tableRegionsCommissions.removeAllViews();
        addHeaders();
        addData(CommissionsList);
        addFooter();
    }

    private void addHeaders() {
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(getLayoutParams());
        tr.addView(getTextView(0, "المنطقة", Color.WHITE, Typeface.BOLD, Color.parseColor("#2b7e72")));
        tr.addView(getTextView(0, "العمولات", Color.WHITE, Typeface.BOLD, Color.parseColor("#2b7e72")));
        tableRegionsCommissions.addView(tr, getTblLayoutParams());
    }

    private void addData(ArrayList<Commission> commissionsList) {
        int numCompanies = commissionsList.size();
        for (int i = 0; i < numCompanies; i++) {
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(getLayoutParams());
            tr.addView(getTextView(i + 1, commissionsList.get(i).getRegionName(), Color.parseColor("#04433a"), Typeface.NORMAL, Color.parseColor("#dbf7f3")));
            tr.addView(getTextView(i + numCompanies, Integer.toString(commissionsList.get(i).getCommissionAmount()), Color.parseColor("#04433a"), Typeface.NORMAL, Color.parseColor("#dbf7f3")));
            tableRegionsCommissions.addView(tr, getTblLayoutParams());
            totalCommission = totalCommission + commissionsList.get(i).getCommissionAmount();
        }
    }

    private void addFooter() {
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(getLayoutParams());
        tr.addView(getTextView(0, "إجمالي العمولات", Color.WHITE, Typeface.BOLD, Color.parseColor("#2b7e72")));
        tr.addView(getTextView(0, Integer.toString(totalCommission), Color.WHITE, Typeface.BOLD, Color.parseColor("#2b7e72")));
        tableRegionsCommissions.addView(tr, getTblLayoutParams());
    }

    @NonNull
    private LayoutParams getLayoutParams() {
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(2, 10, 0, 2);
        return params;
    }

    @NonNull
    private TableLayout.LayoutParams getTblLayoutParams() {
        return new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }

    private TextView getTextView(int id, String title, int color, int typeface, int bgColor) {
        TextView tv = new TextView(this);
        tv.setId(id);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tv.setText(title.toUpperCase());
        tv.setTextColor(color);
        tv.setPadding(20, 20, 20, 20);
        tv.setTypeface(Typeface.DEFAULT, typeface);
        tv.setBackgroundColor(bgColor);
        tv.setLayoutParams(getLayoutParams());
        return tv;
    }

    private void clearSalesmanInfo() {
        txtId.setText("");
        txtName.setText("");
        txtHiringDate.setText("");
        imgSalesman.setImageBitmap(null);
    }
}
