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
import org.svuonline.f18sales.model.Sale;
import org.svuonline.f18sales.model.Salesman;
import org.svuonline.f18sales.salesmen.management.SalesmenSpinnerArrayAdapter;
import java.util.ArrayList;
import java.util.Locale;
import static android.text.TextUtils.isEmpty;

public class AddSalesActivity extends AppCompatActivity implements View.OnClickListener {


    /******************************************************
     **************        Declarations       *************
     ******************************************************/
    private Spinner spinnerSalesmen;
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
        FindElements();
        SetControlsEvents();
        fillSalesmen();
        Utilities.InitSaleDateCalender(this,txtSaleDate);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.btnSave:
                if(ValidateInputs())
                {
                    if (addSale()) {
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
                !isEmpty(txtSaleDate.getText().toString()) &&
                !isEmpty(txtAmount.getText().toString()) ;
    }

    private void FindElements(){
        spinnerSalesmen = findViewById(R.id.spinnerSalesmen);
        txtSaleDate =findViewById(R.id.txtSaleDate);
        txtAmount =findViewById(R.id.txtAmount);
        btnSave = findViewById(R.id.btnSave);

    }

    private void SetControlsEvents() {
        btnSave.setOnClickListener(this);
    }

    private void fillSalesmen() {
        db = openOrCreateDatabase("f18SalesDb",Context.MODE_PRIVATE, null);
        ArrayList<Salesman> salesmenList =  dbHelper.getSalesmenList(db);
        SalesmenSpinnerArrayAdapter salesmenAdapter = new SalesmenSpinnerArrayAdapter(this, salesmenList);
        spinnerSalesmen.setAdapter(salesmenAdapter);
    }

    private boolean addSale() {
        Salesman selectedSalesman = (Salesman) spinnerSalesmen.getSelectedItem();
        int salesmanId = selectedSalesman.getId();
        String saleDate = txtSaleDate.getText().toString();
        int amount =Integer.parseInt(txtAmount.getText().toString());
        Sale sale = new Sale(salesmanId,saleDate,amount);
        return dbHelper.InsertSale(sale) != -1;
    }

    private void ClearInputs()
    {
        spinnerSalesmen.setSelection(0);
        txtAmount.setText("");
        txtSaleDate.setText("");
    }
}
