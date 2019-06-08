package org.svuonline.f18sales;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AddSalesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sales);
        getSupportActionBar().setTitle("إضافة مبيعات");
    }
}
