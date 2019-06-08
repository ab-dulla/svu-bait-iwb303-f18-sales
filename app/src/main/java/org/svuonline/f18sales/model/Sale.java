package org.svuonline.f18sales.model;

import android.content.ContentValues;

import org.svuonline.f18sales.data.DatabaseHelper;

public class Sale {
    private int Id;
    private int SalesmanId;
    private String SaleDate;
    private int Amount;

    public Sale(int salesmanId, String saleDate,int amount) {
        this.SalesmanId = salesmanId;
        this.SaleDate = saleDate;
        this.Amount = amount;
    }

    public int getId() {
        return Id;
    }
    public int getSalesmanId() {
        return SalesmanId;
    }
    public String getSaleDate() {
        return SaleDate;
    }
    public int getAmount() {
        return Amount;
    }


    public ContentValues ToContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.SalesEntry.SALESMAN_ID, SalesmanId);
        contentValues.put(DatabaseHelper.SalesEntry.SALE_DATE, SaleDate);
        contentValues.put(DatabaseHelper.SalesEntry.AMOUNT, Amount);
        return contentValues;
    }
}
