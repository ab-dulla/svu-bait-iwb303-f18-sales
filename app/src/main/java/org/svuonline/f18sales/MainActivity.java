package org.svuonline.f18sales;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
//import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initNavigation();
    }

    private void initNavigation() {
//        Button btnManageSalesmen = findViewById(R.id.button_manage_salesmen);
//        Button btnAddSales = findViewById(R.id.button_add_sales);
//        Button btnShowSales = findViewById(R.id.button_show_sales);
//        Button btnShowCommissions = findViewById(R.id.button_show_commissions);
        ImageView imgManageSalesmen = findViewById(R.id.imgManageSalesmen);
        ImageView imgAddSales = findViewById(R.id.imgAddSales);
        ImageView imgShowSales = findViewById(R.id.imgShowSales);
        ImageView imgShowCommissions = findViewById(R.id.imgShowCommissions);

        imgManageSalesmen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SalesmenManagementActivity.class);
                startActivity(intent);
            }
        });
        imgAddSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddSalesActivity.class);
                startActivity(intent);
            }
        });
        imgShowSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SalesActivity.class);
                startActivity(intent);
            }
        });
        imgShowCommissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CommissionsActivity.class);
                startActivity(intent);
            }
        });









//        btnManageSalesmen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, SalesmenManagementActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        btnAddSales.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, AddSalesActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        btnShowSales.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, SalesActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        btnShowCommissions.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, CommissionsActivity.class);
//                startActivity(intent);
//            }
//        });

    }
}
