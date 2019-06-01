package org.svuonline.f18sales;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SalesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);
        getSupportActionBar().setTitle("Sales");
    }
}
