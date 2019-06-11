package org.svuonline.f18sales.model;

import android.content.ContentValues;

import org.svuonline.f18sales.data.DatabaseHelper;

public class Commission {
    private int Id;
    private int SalesmanId;
    private int RegionId;
    private String Year;
    private String Month;
    private int Amount;
    private String RegionName;

    public Commission(int salesmanId, int regionId, String year, String month, int amount) {
        this.SalesmanId = salesmanId;
        this.RegionId = regionId;
        this.Year = year;
        this.Month = month;
        this.Amount = amount;
    }

    public int getId() {
        return Id;
    }

    public int getSalesmanId() {
        return SalesmanId;
    }

    public int getRegionIdId() {
        return RegionId;
    }

    public String getYear() {
        return Year;
    }

    public String getMonth() {
        return Month;
    }

    public int getCommissionAmount() {
        return Amount;
    }

    public String getRegionName() {
        return RegionName;
    }

    public ContentValues ToContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.CommissionsEntry.SALESMAN_ID, SalesmanId);
        contentValues.put(DatabaseHelper.CommissionsEntry.REGION_ID, RegionId);
        contentValues.put(DatabaseHelper.CommissionsEntry.YEAR, Year);
        contentValues.put(DatabaseHelper.CommissionsEntry.MONTH, Month);
        contentValues.put(DatabaseHelper.SalesEntry.AMOUNT, Amount);
        return contentValues;
    }
}
