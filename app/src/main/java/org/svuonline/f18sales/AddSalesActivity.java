package org.svuonline.f18sales;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.EditText;
import org.svuonline.f18sales.data.DatabaseHelper;
import org.svuonline.f18sales.data.Utilities;
import org.svuonline.f18sales.model.Region;
import org.svuonline.f18sales.model.Sale;
import org.svuonline.f18sales.model.Salesman;
import org.svuonline.f18sales.salesmen.management.RegionSpinnerArrayAdapter;
import org.svuonline.f18sales.salesmen.management.SalesmenSpinnerArrayAdapter;
import java.util.ArrayList;
import java.util.Locale;
import static android.text.TextUtils.isEmpty;

public class AddSalesActivity extends AppCompatActivity implements View.OnClickListener {


    /******************************************************
     **************        Declarations       *************
     ******************************************************/
    private Spinner spinnerSalesmen;
    private Spinner spinnerRegoins;
    private EditText txtSaleDate;
    private EditText txtAmount;
    private Button btnSave;
    DatabaseHelper dbHelper;
    SQLiteDatabase db;


    /******************************************************
     **************      Controls Events      *************
     ******************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sales);
        getSupportActionBar().setTitle("إضافة مبيعات");
        dbHelper = new DatabaseHelper(this);
        db = openOrCreateDatabase("f18SalesDb",Context.MODE_PRIVATE, null);
        FindElements();
        SetControlsEvents();
        FillSalesmen();
        FillRegions();
        Utilities.InitSaleDateCalender(this,txtSaleDate);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.btnSave:
                if(ValidateInputs())
                {
                    Salesman selectedSalesman = (Salesman) spinnerSalesmen.getSelectedItem();
                    int salesmanId = selectedSalesman.getId();

                    Region selectedRegion = (Region) spinnerRegoins.getSelectedItem();
                    int regionId = selectedRegion.getId();

                    if (addSale(salesmanId,regionId)) {
                        AddCommession(salesmanId,regionId);
                        Utilities.ShowMessage(this,"","تم حفظ بيانات عملية البيع بنجاح");
                        ClearInputs();
                    }
                    else {
                        Utilities.ShowMessage(this,"","عفواً، حدث خطأ أثناء حفظ عملية البيع");
                    }
                }
                else
                {
                    Utilities.ShowMessage(this,"","عفواً، يجب أن تقوم بإدخال جميع الحقول");
                }
                break;
        }
    }

    /******************************************************
     **************      Private Methods      *************
     ******************************************************/
    private boolean ValidateInputs(){

        return spinnerSalesmen.getSelectedItemPosition() != 0 &&
                spinnerRegoins.getSelectedItemPosition() != 0 &&
                !isEmpty(txtSaleDate.getText().toString()) &&
                !isEmpty(txtAmount.getText().toString()) ;
    }

    private void FindElements(){
        spinnerSalesmen = findViewById(R.id.spinnerSalesmen);
        spinnerRegoins = findViewById(R.id.spinnerRegoins);
        txtSaleDate =findViewById(R.id.txtSaleDate);
        txtAmount =findViewById(R.id.txtAmount);
        btnSave = findViewById(R.id.btnSave);

    }

    private void SetControlsEvents() {
        btnSave.setOnClickListener(this);
    }

    private void FillSalesmen() {
        //db = openOrCreateDatabase("f18SalesDb",Context.MODE_PRIVATE, null);
//        ArrayList<Salesman> salesmenList =  dbHelper.getSalesmenList(db);
        ArrayList<Salesman> salesmenList =  dbHelper.getSalesmenList();
        SalesmenSpinnerArrayAdapter salesmenAdapter = new SalesmenSpinnerArrayAdapter(this, salesmenList);
        spinnerSalesmen.setAdapter(salesmenAdapter);
    }

    private void FillRegions() {
        //db = openOrCreateDatabase("f18SalesDb",Context.MODE_PRIVATE, null);
        ArrayList<Region> regionsList =  dbHelper.getAllRegions();
        RegionSpinnerArrayAdapter regionAdapter = new RegionSpinnerArrayAdapter(this, regionsList);
        spinnerRegoins.setAdapter(regionAdapter);

    }

    private boolean addSale(int salesmanId,int regionId) {
        String saleDate = txtSaleDate.getText().toString();
        int amount =Integer.parseInt(txtAmount.getText().toString());
        Sale sale = new Sale(salesmanId,regionId,saleDate,amount);
        return dbHelper.InsertSale(sale) != -1;
    }

    private boolean AddCommession(int salesmanId,int regionId)
    {
        int totalAmount = dbHelper.GetSalesmanTotalSalesByRegionAndYearAndMoth(salesmanId,regionId,txtSaleDate.getText().toString().substring(6),txtSaleDate.getText().toString().substring(0,2));

        return true;
    }

    private void ClearInputs()
    {
        spinnerSalesmen.setSelection(0);
        spinnerRegoins.setSelection(0);
        txtAmount.setText("");
        txtSaleDate.setText("");
    }
}
