package org.svuonline.f18sales;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.svuonline.f18sales.data.DatabaseHelper;
import org.svuonline.f18sales.data.Utilities;
import org.svuonline.f18sales.model.Commission;
import org.svuonline.f18sales.model.Region;
import org.svuonline.f18sales.model.Sale;
import org.svuonline.f18sales.model.Salesman;
import org.svuonline.f18sales.salesmen.management.RegionSpinnerArrayAdapter;
import org.svuonline.f18sales.salesmen.management.SalesmenSpinnerArrayAdapter;

import java.io.File;
import java.util.ArrayList;

import static android.text.TextUtils.isEmpty;

public class AddSalesActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private DatabaseHelper dbHelper;

    private Spinner spinnerSalesmen;
    private Spinner spinnerRegions;
    private EditText txtSaleDate;
    private EditText txtAmount;
    private Button btnSave;
    private int regionId;
    private String year;
    private String month;
    private TextView txtName;
    private TextView txtId;
    private TextView txtHiringDate;
    private ImageView imgSalesman;
    private int salesmanRegionId;

    /******************************************************
     **************      Controls Events      *************
     ******************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sales);
        getSupportActionBar().setTitle("إضافة مبيعات");

        dbHelper = new DatabaseHelper(this);
        findElements();
        fillSalesmen();
        fillRegions();
        Utilities.initCalendarElement(this, txtSaleDate);
        setControlsEvents();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSave) {
            if (ValidateInputs()) {
                Salesman selectedSalesman = (Salesman) spinnerSalesmen.getSelectedItem();
                int salesmanId = selectedSalesman.getId();
                Region selectedRegion = (Region) spinnerRegions.getSelectedItem();
                regionId = selectedRegion.getId();
                year = txtSaleDate.getText().toString().substring(6);
                month = txtSaleDate.getText().toString().substring(3, 5);

                if (addSale(salesmanId, regionId)) {
                    addCommission(salesmanId, regionId);
                    ClearInputs();
                } else {
                    Utilities.showMessage(this, "", "عفواً، حدث خطأ أثناء حفظ عملية البيع");
                }
            } else {
                Utilities.showMessage(this, "", "عفواً، يجب أن تقوم بإدخال جميع الحقول");
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            ClearSalesmanInfo();
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

    private void findElements() {
        spinnerSalesmen = findViewById(R.id.spinnerSalesmen);
        spinnerRegions = findViewById(R.id.spinnerRegoins);
        txtSaleDate = findViewById(R.id.txtSaleDate);
        txtAmount = findViewById(R.id.txtAmount);
        txtId = findViewById(R.id.txtId);
        txtName = findViewById(R.id.txtName);
        txtHiringDate = findViewById(R.id.txtHiringDate);
        imgSalesman = findViewById(R.id.imgSalesman);
        btnSave = findViewById(R.id.btnSave);
    }

    private boolean ValidateInputs() {
        return spinnerSalesmen.getSelectedItemPosition() != 0 &&
                !isEmpty(txtSaleDate.getText().toString()) &&
                !isEmpty(txtAmount.getText().toString());
    }

    private void setControlsEvents() {
        btnSave.setOnClickListener(this);
        spinnerSalesmen.setOnItemSelectedListener(this);
    }

    private void fillSalesmen() {
        ArrayList<Salesman> salesmenList = dbHelper.getSalesmenList();
        SalesmenSpinnerArrayAdapter salesmenAdapter = new SalesmenSpinnerArrayAdapter(this, salesmenList);
        spinnerSalesmen.setAdapter(salesmenAdapter);
    }

    private void fillRegions() {
        ArrayList<Region> regionsList = dbHelper.getAllRegions();
        RegionSpinnerArrayAdapter regionAdapter = new RegionSpinnerArrayAdapter(this, regionsList);
        spinnerRegions.setAdapter(regionAdapter);

    }

    private void fillSalesmanInfo(Salesman salesman) {
        txtId.setText(salesman.getId().toString());
        txtName.setText(salesman.getFullName());
        txtHiringDate.setText(salesman.getHiringDate());
        salesmanRegionId = salesman.getRegionId();
        // read image from internal storage and show it on the UI
        File imgFile = new File(salesman.getImagePath());
        if (imgFile.exists()) {
            Bitmap image = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imgSalesman.setImageBitmap(image);
        }
    }

    private boolean addSale(int salesmanId, int regionId) {
        String saleDate = txtSaleDate.getText().toString();
        int amount = Integer.parseInt(txtAmount.getText().toString());
        Sale sale = new Sale(salesmanId, regionId, saleDate, amount);
        return dbHelper.InsertSale(sale) != -1;
    }

    private void ClearInputs() {
        spinnerSalesmen.setSelection(0);
        spinnerRegions.setSelection(0);
        txtAmount.setText("");
        txtSaleDate.setText("");
    }

    private void ClearSalesmanInfo() {
        txtId.setText("");
        txtName.setText("");
        txtHiringDate.setText("");
        salesmanRegionId = 0;
        imgSalesman.setImageBitmap(null);
    }

    private boolean addCommission(int salesmanId, int regionId) {
        int commission;
        //Get total sales for sales man by region and year and month
        int totalAmount = dbHelper.getSalesmanTotalSalesByRegionAndYearAndMoth(salesmanId, regionId, year, month);

        //  <= 10 000 000  5% in same region   &  3%  in different region
        //  >  10 000 000  7% in same region   &  4%  in different region
        if (regionId == salesmanRegionId) {
            if (totalAmount > 10000000) {
                commission = (int) (((totalAmount - 10000000) * 0.07) + (10000000 * 0.05));
            } else {
                commission = (int) ((totalAmount * 0.05));
            }
        } else {
            if (totalAmount > 10000000) {
                commission = (int) (((totalAmount - 10000000) * 0.04) + (10000000 * 0.03));
            } else {
                commission = (int) ((totalAmount * 0.03));
            }
        }

        int oldCommission = dbHelper.getSalesmanCommissionByRegionAndYearAndMoth(salesmanId, regionId, year, month);
        if (oldCommission == 0) {
            Commission newCommission = new Commission(salesmanId, regionId, year, month, commission);
            if (dbHelper.insertCommission(newCommission) == -1) {
                Utilities.showMessage(this, "", "عفواً، حدث خطأ أثناء تحديث بيانات العمولة");
                return false;
            }
        } else {
            updateCommission(salesmanId, commission);
        }
        return true;
    }

    private boolean updateCommission(final int salesmanId, final int commission) {
        final boolean[] updateResult = {true};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setMessage("لقد تم احتساب عمولة بشكل مسبق لهذا المندوب في المنطقة التي تم اختيارها في هذا الشهر وهذا السنة، هل تريد إعادة الاحتساب مرة أخرى؟")
                .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Commission newCommission = new Commission(salesmanId, regionId, year, month, commission);
                        if (dbHelper.updateCommission(newCommission) == -1) {
                            updateResult[0] = false;
                            //Utilities.showMessage(context, "", "عفواً، حدث خطأ أثناء تحديث بيانات العمولة");
                        }
                    }
                })
                .setNegativeButton("لا", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .show();

        return updateResult[0];
    }
}
