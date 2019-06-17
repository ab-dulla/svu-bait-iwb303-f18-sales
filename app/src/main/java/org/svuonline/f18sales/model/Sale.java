package org.svuonline.f18sales.model;

import android.content.ContentValues;

import org.svuonline.f18sales.data.DatabaseHelper;

public class Sale {
    private int salesmanId;
    private int regionId;
    private String saleDate;
    private int amount;
    private String regionName;

    public Sale(int salesmanId, int regionId, String saleDate, int amount) {
        this.salesmanId = salesmanId;
        this.regionId = regionId;
        this.saleDate = saleDate;
        this.amount = amount;
    }

    public Sale(String regionName, int amount) {
        this.regionName = regionName;
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public String getRegionName() {
        return regionName;
    }

    public ContentValues ToContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.SalesEntry.SALESMAN_ID, salesmanId);
        contentValues.put(DatabaseHelper.SalesEntry.REGION_ID, regionId);
        contentValues.put(DatabaseHelper.SalesEntry.SALE_DATE, saleDate);
        contentValues.put(DatabaseHelper.SalesEntry.AMOUNT, amount);
        return contentValues;
    }
}
