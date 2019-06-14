package org.svuonline.f18sales.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import org.svuonline.f18sales.model.Commission;
import org.svuonline.f18sales.model.Region;
import org.svuonline.f18sales.model.Sale;
import org.svuonline.f18sales.model.Salesman;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "f18SalesDb";

    private static final String SQL_CREATE_REGIONS = String.format(
            "CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT NOT NULL);",
            RegionEntry.TABLE_NAME, RegionEntry._ID, RegionEntry.NAME);

    private static final String SQL_CREATE_SALESMAN = "CREATE TABLE " + SalesmanEntry.TABLE_NAME + "("
            + SalesmanEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + SalesmanEntry.FULL_NAME + " TEXT NOT NULL, "
            + SalesmanEntry.HIRING_DATE + " TEXT, "
            + SalesmanEntry.REGION_ID + " INTEGER, "
            + SalesmanEntry.IMAGE_PATH + " TEXT, "
            + "FOREIGN KEY(" + SalesmanEntry.REGION_ID + ") REFERENCES " + RegionEntry.TABLE_NAME + "(" + RegionEntry._ID + ")"
            + ");";

    private static final String SQL_CREATE_SALES = "CREATE TABLE " + SalesEntry.TABLE_NAME + "("
            + SalesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + SalesEntry.SALESMAN_ID + " INTEGER NOT NULL, "
            + SalesEntry.REGION_ID + " INTEGER NOT NULL, "
            + SalesEntry.SALE_DATE + " TEXT NOT NULL, "
            + SalesEntry.AMOUNT + " INTEGER,"
            + " FOREIGN KEY(" + SalesEntry.SALESMAN_ID + ") REFERENCES " + SalesmanEntry.TABLE_NAME + "(" + SalesmanEntry._ID + ") ON UPDATE CASCADE,"
            + " FOREIGN KEY(" + SalesEntry.REGION_ID + ") REFERENCES " + RegionEntry.TABLE_NAME + "(" + RegionEntry._ID + ") ON UPDATE CASCADE"
            + ");";

    private static final String SQL_CREATE_COMMISSIONS = "CREATE TABLE " + CommissionsEntry.TABLE_NAME + "("
            + CommissionsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CommissionsEntry.SALESMAN_ID + " INTEGER NOT NULL, "
            + CommissionsEntry.REGION_ID + " INTEGER NOT NULL, "
            + CommissionsEntry.YEAR + " TEXT NOT NULL, "
            + CommissionsEntry.MONTH + " TEXT NOT NULL, "
            + CommissionsEntry.AMOUNT + " INTEGER,"
            + " FOREIGN KEY(" + CommissionsEntry.SALESMAN_ID + ") REFERENCES " + SalesmanEntry.TABLE_NAME + "(" + SalesmanEntry._ID + "),"
            + " FOREIGN KEY(" + CommissionsEntry.REGION_ID + ") REFERENCES " + RegionEntry.TABLE_NAME + "(" + RegionEntry._ID + ")"
            + ");";

    private static final String SQL_DELETE_REGIONS = "DROP TABLE IF EXISTS " + RegionEntry.TABLE_NAME;
    private static final String SQL_DELETE_SALESMAN = "DROP TABLE IF EXISTS " + SalesmanEntry.TABLE_NAME;
    private static final String SQL_DELETE_SALES = "DROP TABLE IF EXISTS " + SalesEntry.TABLE_NAME;
    private static final String SQL_DELETE_COMMESSIONS = "DROP TABLE IF EXISTS " + CommissionsEntry.TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_REGIONS);
        db.execSQL(SQL_CREATE_SALESMAN);
        db.execSQL(SQL_CREATE_SALES);
        db.execSQL(SQL_CREATE_COMMISSIONS);
//        db.execSQL("PRAGMA foreign_keys = ON;");
        insertDefaultRegions(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_REGIONS);
        db.execSQL(SQL_DELETE_SALESMAN);
        db.execSQL(SQL_DELETE_SALES);
        db.execSQL(SQL_DELETE_COMMESSIONS);
//        db.execSQL("PRAGMA foreign_keys = ON;");
        onCreate(db);
    }

    public ArrayList<Region> getAllRegions() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectSql = "SELECT " + RegionEntry._ID + ", " + RegionEntry.NAME + " FROM " + RegionEntry.TABLE_NAME;
        Cursor cursor = db.rawQuery(selectSql, null);
        ArrayList<Region> regions = parseRegions(cursor);
        cursor.close();
        return regions;
    }

    private ArrayList<Region> parseRegions(Cursor cursor) {
        ArrayList<Region> regions = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            regions.add(new Region(id, name));
        }
        cursor.close();
        return regions;
    }

    private void insertDefaultRegions(SQLiteDatabase db) {
        insertRegion(db, "المنطقة الجنوبية");
        insertRegion(db, "المنطقة الساحلية");
        insertRegion(db, "المنطقة الشمالية");
        insertRegion(db, "المنطقة الشرقية");
        insertRegion(db, "لبنان");
    }

    private void insertRegion(SQLiteDatabase regionDb, String region) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(RegionEntry.NAME, region);
        regionDb.insert(RegionEntry.TABLE_NAME, null, contentValues);
    }

    public ArrayList<Salesman> getAllSalesmen() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectSql = "SELECT "
                + SalesmanEntry._ID + ", "
                + SalesmanEntry.FULL_NAME + ", "
                + SalesmanEntry.REGION_ID + ", "
                + SalesmanEntry.HIRING_DATE + ", "
                + SalesmanEntry.IMAGE_PATH
                + " FROM " + SalesmanEntry.TABLE_NAME;
        Cursor cursor = db.rawQuery(selectSql, null);
        ArrayList<Salesman> salesmen = parseSalesmen(cursor);
        cursor.close();
        return salesmen;
    }

    private ArrayList<Salesman> parseSalesmen(Cursor cursor) {
        ArrayList<Salesman> salesmen = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String fullName = cursor.getString(1);
            int regionId = cursor.getInt(2);
            String hiringDate = cursor.getString(3);
            String imagePath = cursor.getString(4);
            salesmen.add(new Salesman(id, fullName, regionId, hiringDate, imagePath));
        }
        cursor.close();
        return salesmen;
    }

    public long insertSalesman(Salesman salesman) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.insert(SalesmanEntry.TABLE_NAME, null, salesman.toContentValues());
    }

    public int updateSalesman(Salesman salesman) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.update(SalesmanEntry.TABLE_NAME,
                salesman.toContentValues(),
                SalesmanEntry._ID + " = ?",
                new String[]{salesman.getId().toString()});
    }

    public int updateCommission(Commission commission) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.update(CommissionsEntry.TABLE_NAME,
                commission.ToContentValues(),
                CommissionsEntry.SALESMAN_ID + " = ? and " + CommissionsEntry.REGION_ID + " = ? and " + CommissionsEntry.YEAR + " = ? and " + CommissionsEntry.MONTH + " = ?",
                new String[]{Integer.toString(commission.getSalesmanId()), Integer.toString(commission.getRegionId()), commission.getYear(), commission.getMonth()});
    }

    public int deleteSalesman(Salesman salesman) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(SalesmanEntry.TABLE_NAME,
                SalesmanEntry._ID + " = ?",
                new String[]{salesman.getId().toString()});
    }

    public Salesman getSalesmenById(Integer id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectSql = "SELECT "
                + SalesmanEntry._ID + ", "
                + SalesmanEntry.FULL_NAME + ", "
                + SalesmanEntry.REGION_ID + ", "
                + SalesmanEntry.HIRING_DATE + ", "
                + SalesmanEntry.IMAGE_PATH
                + " FROM " + SalesmanEntry.TABLE_NAME
                + " where _id=" + id.toString();
        Cursor cursor = db.rawQuery(selectSql, null);
        ArrayList<Salesman> salesmen = parseSalesmen(cursor);
        cursor.close();
        return salesmen.get(0);
    }

    public ArrayList<Salesman> getSalesmenList() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectSql = "SELECT "
                + DatabaseHelper.SalesmanEntry._ID + ", "
                + DatabaseHelper.SalesmanEntry.FULL_NAME
                + " FROM " + DatabaseHelper.SalesmanEntry.TABLE_NAME;
        Cursor cursor = db.rawQuery(selectSql, null);
        ArrayList<Salesman> salesmen = new ArrayList<>();
        salesmen.add(new Salesman(-1, "-- اسم المندوب --"));
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String fullName = cursor.getString(1);
            salesmen.add(new Salesman(id, fullName));
        }
        cursor.close();
        return salesmen;
    }

    public long InsertSale(Sale sale) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.insert(SalesEntry.TABLE_NAME, null, sale.ToContentValues());
    }

    public long insertCommission(Commission commission) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.insert(CommissionsEntry.TABLE_NAME, null, commission.ToContentValues());
    }

    public ArrayList<Sale> getSalesmanSales(int salesmanId, String year, String month) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectSql =
                "SELECT R.name as RegionName, IFNULL(SUM(S.amount),0) AS 'Amount'" +
                        " FROM region as R" +
                        " LEFT JOIN " +
                        "(" +
                        "SELECT region_id,amount FROM sales" +
                        " where salesman_id=" + salesmanId +
                        " and substr( sale_date,7)='" + year +
                        "' and substr(sale_date,4,2) ='" + month + "'" +
                        ") as S" +
                        " ON  S.region_id=R._id" +
                        " GROUP BY R.name" +
                        " order by Amount desc";
        Cursor cursor = db.rawQuery(selectSql, null);
        ArrayList<Sale> salesList = new ArrayList<>();
        while (cursor.moveToNext()) {
            String RegionName = cursor.getString(0);
            int Amount = cursor.getInt(1);
            salesList.add(new Sale(RegionName, Amount));
        }
        cursor.close();
        return salesList;
    }

    public ArrayList<Commission> getSalesmanCommissions(int salesmanId, String year, String month) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectSql =
                "SELECT R.name as RegionName, IFNULL(SUM(S.amount),0) AS 'Amount'" +
                        " FROM region as R" +
                        " LEFT JOIN " +
                        "(" +
                        "SELECT regionId,amount FROM commissions" +
                        " where salesmanId=" + salesmanId +
                        " and year='" + year +
                        "' and month ='" + month + "'" +
                        ") as S" +
                        " ON  S.regionId=R._id" +
                        " GROUP BY R.name" +
                        " order by Amount desc";
        Cursor cursor = db.rawQuery(selectSql, null);
        ArrayList<Commission> commissionList = new ArrayList<>();
        while (cursor.moveToNext()) {
            String RegionName = cursor.getString(0);
            int Amount = cursor.getInt(1);
            commissionList.add(new Commission(RegionName, Amount));
        }
        cursor.close();
        return commissionList;
    }

    public int getSalesmanTotalSalesByRegionAndYearAndMoth(int salesmanId, int regionId, String year, String month) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectSql =
                "SELECT SUM(S.amount)as Amount " +
                        "FROM region as R " +
                        "inner join sales as S on S.region_id=R._id " +
                        " where S.salesman_id=" + salesmanId +
                        " and substr( sale_date,7)='" + year +
                        "' and substr(sale_date,4,2) ='" + month + "'" +
                        " and R._id = " + regionId +
                        " group by R._id";
        Cursor cursor = db.rawQuery(selectSql, null);
        int totalAmount = 0;
        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            totalAmount = cursor.getInt(0);
        }
        cursor.close();
        return totalAmount;
    }


    public int getSalesmanCommissionByRegionAndYearAndMoth(int salesmanId, int regionId, String year, String month) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectSql =
                "SELECT amount " +
                        "FROM " + CommissionsEntry.TABLE_NAME +
                        " where " + CommissionsEntry.SALESMAN_ID + "=" + salesmanId +
                        " and " + CommissionsEntry.REGION_ID + "=" + regionId +
                        " and " + CommissionsEntry.YEAR + "='" + year + "'" +
                        " and " + CommissionsEntry.MONTH + "='" + month + "'";
        Cursor cursor = db.rawQuery(selectSql, null);
        int commessionAmount = 0;
        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            commessionAmount = cursor.getInt(0);
        }
        cursor.close();
        return commessionAmount;
    }

    public static class RegionEntry implements BaseColumns {
        public static final String TABLE_NAME = "region";
        public static final String NAME = "name";
    }

    public static class SalesmanEntry implements BaseColumns {
        public static final String TABLE_NAME = "salesman";
        public static final String FULL_NAME = "full_name";
        public static final String HIRING_DATE = "hiring_date";
        public static final String IMAGE_PATH = "image_path";
        public static final String REGION_ID = "region_id";
    }

    public static class CommissionsEntry implements BaseColumns {
        public static final String TABLE_NAME = "commissions";
        public static final String SALESMAN_ID = "salesmanId";
        public static final String REGION_ID = "regionId";
        public static final String AMOUNT = "amount";
        public static final String MONTH = "month";
        public static final String YEAR = "year";
    }

    public static class SalesEntry implements BaseColumns {
        public static final String TABLE_NAME = "sales";
        public static final String SALESMAN_ID = "salesman_id";
        public static final String REGION_ID = "region_id";
        public static final String AMOUNT = "amount";
        public static final String SALE_DATE = "sale_date";
    }
}
