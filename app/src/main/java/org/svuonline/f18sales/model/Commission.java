package org.svuonline.f18sales.model;

import android.content.ContentValues;

import org.svuonline.f18sales.data.DatabaseHelper;

public class Commission {
    private int salesmanId;
    private int regionId;
    private String year;
    private String month;
    private int amount;
    private String regionName;

    public Commission(int salesmanId, int regionId, String year, String month, int amount) {
        this.salesmanId = salesmanId;
        this.regionId = regionId;
        this.year = year;
        this.month = month;
        this.amount = amount;
    }

    public Commission(String regionName, int amount) {
        this.regionName = regionName;
        this.amount = amount;
    }

    public int getSalesmanId() {
        return salesmanId;
    }

    public int getRegionId() {
        return regionId;
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public int getCommissionAmount() {
        return amount;
    }

    public String getRegionName() {
        return regionName;
    }

    public ContentValues ToContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.CommissionsEntry.SALESMAN_ID, salesmanId);
        contentValues.put(DatabaseHelper.CommissionsEntry.REGION_ID, regionId);
        contentValues.put(DatabaseHelper.CommissionsEntry.YEAR, year);
        contentValues.put(DatabaseHelper.CommissionsEntry.MONTH, month);
        contentValues.put(DatabaseHelper.SalesEntry.AMOUNT, amount);
        return contentValues;
    }
}
