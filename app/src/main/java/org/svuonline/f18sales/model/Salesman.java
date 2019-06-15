package org.svuonline.f18sales.model;

import android.content.ContentValues;

import org.svuonline.f18sales.data.DatabaseHelper.SalesmanEntry;

public class Salesman {

    private final Integer id;
    private final String fullName;
    private final int regionId;
    private final String hiringDate;
    private final String imagePath;

    // constructor used for initializing a salesman for UPDATE sql command
    public Salesman(Integer id, String fullName, int regionId, String hiringDate, String imagePath) {
        this.id = id;
        this.fullName = fullName;
        this.regionId = regionId;
        this.hiringDate = hiringDate;
        this.imagePath = imagePath;
    }

    // constructor used for initializing a salesman for INSERT sql command
    // no need for an id --> auto-generated
    public Salesman(String fullName, int regionId, String hiringDate, String imagePath) {
        this(null, fullName, regionId, hiringDate, imagePath);
    }

    // for placeholder usage only
    public Salesman(Integer id, String fullName) {
        this(id, fullName, 1, null, null);
    }

    @Override
    public String toString() {
        return id + fullName + regionId + hiringDate;
    }

    public Integer getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public int getRegionId() {
        return regionId;
    }

    public String getHiringDate() {
        return hiringDate;
    }

    public String getImagePath() {
        return imagePath;
    }

    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        if (id != null) {
            contentValues.put(SalesmanEntry._ID, id);
        }
        contentValues.put(SalesmanEntry.FULL_NAME, fullName);
        contentValues.put(SalesmanEntry.REGION_ID, regionId);
        contentValues.put(SalesmanEntry.HIRING_DATE, hiringDate);
        contentValues.put(SalesmanEntry.IMAGE_PATH, imagePath);
        return contentValues;
    }
}
