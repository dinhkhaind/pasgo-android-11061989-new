package com.onepas.android.pasgo;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.onepas.android.pasgo.models.ItemNhomKM;
import com.onepas.android.pasgo.models.LocationMessageDriver;
import com.onepas.android.pasgo.models.PTLocationInfo;

public class PastaxiDB extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "PastaxiManage";
	private static final String TABLE_INFO_DRIVERS = "Driver";
	private static final String TABLE_REASON_GROUPS = "ReasonGroups";
	private static final String KEY_ID = "id";
	private static final String KEY_FULLNAME = "fullName";
	private static final String KEY_NUMBERPLATE = "numberPlate";
	private static final String KEY_TAXI_COMPANY = "taxiCompany";
	private static final String KEY_NUMBERPHONE = "numberPhone";
	private static final String KEY_ADDRESS = "address";
	private static final String KEY_LAT = "lat";
	private static final String KEY_LNG = "lng";
	private static final String KEY_DISTANCE = "distance";

	private static final String KEY_REASON_GROUPS_NAME = "groupName";
	private static final String KEY_REASON_GROUPS_CODE = "groupCode";
	private static final String KEY_REASON_GROUPS_ID = "groupId";

	protected PastaxiDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_INFO_CHATS_TABLE = "CREATE TABLE IF NOT EXISTS "
				+ TABLE_INFO_DRIVERS + "(" + KEY_ID + " TEXT," + KEY_FULLNAME
				+ " TEXT," + KEY_NUMBERPLATE + " TEXT," + KEY_TAXI_COMPANY
				+ " TEXT," + KEY_NUMBERPHONE + " TEXT," + KEY_ADDRESS
				+ " TEXT," + KEY_LAT + " DOUBLE," + KEY_LNG + " DOUBLE,"
				+ KEY_DISTANCE + " DOUBLE" + ")";
		String CREATE_REASON_GROUPS_TABLE = "CREATE TABLE "
				+ TABLE_REASON_GROUPS + "(" + KEY_REASON_GROUPS_ID
				+ " INTEGER," + KEY_REASON_GROUPS_CODE + " TEXT,"
				+ KEY_REASON_GROUPS_NAME + " TEXT" + ")";
		db.execSQL(CREATE_INFO_CHATS_TABLE);
		db.execSQL(CREATE_REASON_GROUPS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	public synchronized void deleteAll() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_INFO_DRIVERS);
		onCreate(db);
	}

	public synchronized void deleteReasonGroups() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_REASON_GROUPS);
		onCreate(db);
	}

	synchronized public void addReasonGroups(ArrayList<ItemNhomKM> reasonGroups) {
		if (reasonGroups == null)
			return;
		SQLiteDatabase db = this.getWritableDatabase();
		for (ItemNhomKM reasonGroup : reasonGroups) {
			ContentValues values = new ContentValues();
			values.put(KEY_REASON_GROUPS_ID, reasonGroup.getId());
			values.put(KEY_REASON_GROUPS_CODE, reasonGroup.getMa());
			values.put(KEY_REASON_GROUPS_NAME, reasonGroup.getTen());
			db.insert(TABLE_REASON_GROUPS, null, values);
		}
		db.close();
	}

	synchronized public ArrayList<ItemNhomKM> getReasonGroups() {
		String selectQuery = "SELECT  * FROM " + TABLE_REASON_GROUPS;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, new String[] {});
		ArrayList<ItemNhomKM> reasonGroups = null;
		if (cursor.moveToFirst()) {
			reasonGroups = new ArrayList<ItemNhomKM>();
			do {
				ItemNhomKM reasonGroup = new ItemNhomKM();
				reasonGroup.setId(cursor.getInt(0));
				reasonGroup.setMa(cursor.getString(1));
				reasonGroup.setTen(cursor.getString(2));
				reasonGroups.add(reasonGroup);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return reasonGroups;
	}

	synchronized public void addDriver(
			LocationMessageDriver locationMessageDriver) {
		if (locationMessageDriver == null
				|| locationMessageDriver.getLocation() == null)
			return;
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_ID, locationMessageDriver.getId());
		values.put(KEY_FULLNAME, locationMessageDriver.getFullName());
		values.put(KEY_NUMBERPLATE, locationMessageDriver.getNumberPlate());
		values.put(KEY_TAXI_COMPANY, locationMessageDriver.getTaxiCompany());
		values.put(KEY_NUMBERPHONE, locationMessageDriver.getNumberPhone());
		values.put(KEY_ADDRESS, locationMessageDriver.getLocation()
				.getAddress());
		values.put(KEY_LAT, locationMessageDriver.getLocation().getLat());
		values.put(KEY_LNG, locationMessageDriver.getLocation().getLng());
		values.put(KEY_DISTANCE, locationMessageDriver.getDistance());
		db.insert(TABLE_INFO_DRIVERS, null, values);
		db.close();
	}

	synchronized public LocationMessageDriver getDriver() {
		LocationMessageDriver locationMessageDriver = new LocationMessageDriver();
		String selectQuery = "SELECT  * FROM " + TABLE_INFO_DRIVERS;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, new String[] {});
		if (cursor.moveToFirst()) {
			do {
				locationMessageDriver.setId(cursor.getString(0));
				locationMessageDriver.setFullName(cursor.getString(1));
				locationMessageDriver.setNumberPlate(cursor.getString(2));
				locationMessageDriver.setTaxiCompany(cursor.getString(3));
				locationMessageDriver.setNumberPhone(cursor.getString(4));
				PTLocationInfo locationInfo = new PTLocationInfo();
				locationInfo.setAddress(cursor.getString(5));
				locationInfo.setLat(cursor.getDouble(6));
				locationInfo.setLng(cursor.getDouble(7));
				locationMessageDriver.setLocation(locationInfo);
				locationMessageDriver.setDistance(cursor.getDouble(8));
				return locationMessageDriver;
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return null;
	}

	synchronized public void deleteInfoChat(
			LocationMessageDriver locationMessageDriver) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_INFO_DRIVERS, KEY_ID + " = ?",
				new String[] { locationMessageDriver.getId() });
		db.close();
	}

	public int getInfoChatsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_INFO_DRIVERS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();
		return cursor.getCount();
	}
}