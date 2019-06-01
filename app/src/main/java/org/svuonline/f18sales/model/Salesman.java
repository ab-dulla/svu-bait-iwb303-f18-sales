package org.svuonline.f18sales.model;

import android.content.ContentValues;

import org.svuonline.f18sales.data.DatabaseHelper.SalesmanEntry;

public class Salesman {

    @Override
    public String toString() {
        return id + fullName + regionId + hiringDate + image;
    }

    private final Integer id;
    private final String fullName;
    private final int regionId;
    private final String hiringDate;
    private final byte[] image;
    private final Integer newId;

    // constructor used for initializing a salesman for UPDATE sql command
    public Salesman(Integer id, String fullName, int regionId, String hiringDate, byte[] image, Integer newId) {
        this.id = id;
        this.fullName = fullName;
        this.regionId = regionId;
        this.hiringDate = hiringDate;
        this.image = image;
        this.newId = newId;
    }

    // constructor used for initializing a salesman for CREATE sql command
    public Salesman(String fullName, int regionId, String hiringDate, byte[] image) {
        this(null, fullName, regionId, hiringDate, image, null);
    }

    // constructor used for initializing salesmen after reading them from the DB
    public Salesman(Integer id, String fullName, int regionId, String hiringDate, byte[] image) {
        this(id, fullName, regionId, hiringDate, image, null);
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

    public byte[] getImage() {
        return image;
    }

    // Used for updating the `id` in the UPDATE sql command
    // Both `id` and `newId` are needed in the UPDATE command
    public Integer getNewId() {
        return newId;
    }

    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();

        // Add `newId` to contentValues only if we UPDATE the salesman in the DB
        // `newId` is not needed for salesman INSERT sql command
        if (newId != null) {
            contentValues.put(SalesmanEntry._ID, newId);
        }
        contentValues.put(SalesmanEntry.FULL_NAME, fullName);
        contentValues.put(SalesmanEntry.REGION_ID, regionId);
        contentValues.put(SalesmanEntry.HIRING_DATE, hiringDate);
        contentValues.put(SalesmanEntry.IMAGE, image);
        return contentValues;
    }
}
