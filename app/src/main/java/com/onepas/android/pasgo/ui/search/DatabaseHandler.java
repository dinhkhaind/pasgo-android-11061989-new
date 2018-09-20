package com.onepas.android.pasgo.ui.search;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.onepas.android.pasgo.models.Place;
import com.onepas.android.pasgo.models.SearchItem;
import com.onepas.android.pasgo.utils.StringUtils;
import com.onepas.android.pasgo.utils.Utils;

public class DatabaseHandler extends SQLiteOpenHelper {

	// Database Version
	private static final int DATABASE_VERSION = 2;

	// Database Name
	private static final String DATABASE_NAME = "SearchHistoryData";

	// Contacts table name
	private static final String TABLE_PLACE_TABLE = "PlaceTable";
	private static final String TABLE_Read_Tin_KM = "ReadTinKM";
	private static final String TABLE_Favorite = "TableFavorite";

	// Contacts Table Columns names
	private static final String KEY_ID_Tin_KM = "iD";
	private static final String KEY_Read_Tin_KM = "read";

	// Contacts Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_VICINITY = "vicinity";
	private static final String KEY_LAT = "latitude";
	private static final String KEY_LNG = "longitude";
	private static final String KEY_CHECK_STAR = "checkStar";

	private String tag = "Database";

	private SQLiteDatabase mDatabaseHandler;

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_TABLE_FAVORITE = "CREATE TABLE " + TABLE_Favorite + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
				+ KEY_LAT + " TEXT," + KEY_LNG + " TEXT," + KEY_VICINITY
				+ " TEXT," + KEY_CHECK_STAR + " TEXT" + ")";
		db.execSQL(CREATE_TABLE_FAVORITE);

		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_PLACE_TABLE
				+ "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
				+ KEY_LAT + " TEXT," + KEY_LNG + " TEXT," + KEY_VICINITY
				+ " TEXT," + KEY_CHECK_STAR + " TEXT" + ")";
		db.execSQL(CREATE_CONTACTS_TABLE);

		Utils.Log(tag, "TABLE_CONTACTS : " + "on create");

		String CREATE_TABLE_Read_Tin_KM = "CREATE TABLE " + TABLE_Read_Tin_KM
				+ "(" + KEY_ID + " TEXT PRIMARY KEY," + KEY_Read_Tin_KM
				+ " TEXT" + ")";
		db.execSQL(CREATE_TABLE_Read_Tin_KM);
		Utils.Log(tag, "TABLE_Read_Tin_KM : " + "on create");
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// db.execSQL("DROP TABLE IF EXISTS " + TABLE_LICH_SU_CHUYEN_DI);
		// onCreate(db);
		if (oldVersion == 1 && newVersion == 2) {
			db.execSQL("ALTER TABLE " + TABLE_PLACE_TABLE + " ADD "
					+ KEY_CHECK_STAR + " TEXT;");
			Utils.Log(tag, "TABLE_CONTACTS : " + "on Upgrade");
		}
	}

	public void insertItemPlace(Place place) {

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(KEY_NAME, place.getName());
		values.put(KEY_VICINITY, place.getVicinity());
		values.put(KEY_CHECK_STAR, place.isCheckStar());
		values.put(KEY_LAT, place.getLatitude());
		values.put(KEY_LNG, place.getLongitude());

		db.insert(TABLE_PLACE_TABLE, null, values);
		db.close();
	}

	public void deleteFavoriteItem(Place placech) {
		String name = placech.getName();
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_PLACE_TABLE, KEY_NAME + " = ?", new String[] { name });
		db.close();
	}

	// Getting single contact
		SearchItem getContact(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_PLACE_TABLE, new String[] { KEY_ID,
						KEY_NAME }, KEY_ID + "=?", new String[] { String.valueOf(id) },
				null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		SearchItem contact = null;
		// return contact
		return contact;
	}

	public List<Place> getFavorites() {

		List<Place> contactList = new ArrayList<Place>();

		String selectQuery = "SELECT DISTINCT " + KEY_NAME + "," + KEY_VICINITY
				+ "," + KEY_LAT + "," + KEY_LNG + "," + KEY_CHECK_STAR
				+ " FROM " + TABLE_PLACE_TABLE + " " + "ORDER BY " + KEY_ID
				+ " DESC ";

		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				Place contact = new Place();
				contact.setName(cursor.getString(0));
				contact.setVicinity(cursor.getString(1));
				contact.setLatitude(cursor.getDouble(2));
				contact.setLongitude(cursor.getDouble(3));
				contact.setCheckStar(true);
				contactList.add(contact);
			} while (cursor.moveToNext());
		}
		db.close();

		return contactList;
	}

	public void insertItemReadTinKM(String iD, int read) {
		if(StringUtils.isEmpty(iD))
			return;
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ID_Tin_KM, iD.toLowerCase());
		values.put(KEY_Read_Tin_KM, read);
		try {
			db.insert(TABLE_Read_Tin_KM, null, values);
		} catch (Exception e) {
			Utils.Log("Insert table tin KM", "Insert table read tin KM :" + e);
		}
		db.close();
	}

	// Getting single contact
	public int getReadTinKM(String id) {
		id = id.toLowerCase();
		int read = 0;
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_Read_Tin_KM,
				new String[] { KEY_Read_Tin_KM }, KEY_ID_Tin_KM + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			if (cursor.getColumnNames().length > 0) {
				try {
					String strread = cursor.getString(0);
					read = Integer.parseInt(strread);

				} catch (Exception e) {
					Utils.Log("ReadTinKM", "ReadTinKM : " + e);
				}
			}
		}

		return read;
	}

	public boolean existsTable() {
		try {
			SQLiteDatabase db = this.getReadableDatabase();
			String selectQuery = "SELECT  * FROM " + TABLE_Read_Tin_KM;
			Cursor cursor = db.rawQuery(selectQuery, null);
			return cursor.getCount() > 0;
		} catch (SQLException e) {
			return false;
		}
	}

	public boolean isTableExists(boolean openDb) {
		if (openDb) {
			if (mDatabaseHandler == null || !mDatabaseHandler.isOpen()) {
				mDatabaseHandler = getReadableDatabase();
			}

			if (!mDatabaseHandler.isReadOnly()) {
				mDatabaseHandler.close();
				mDatabaseHandler = getReadableDatabase();
			}
		}

		Cursor cursor = mDatabaseHandler.rawQuery(
				"select DISTINCT tbl_name from sqlite_master where tbl_name = '"
						+ TABLE_Read_Tin_KM + "'", null);
		if (cursor != null) {
			if (cursor.getCount() > 0) {
				cursor.close();
				return true;
			}
			cursor.close();
		}
		return false;
	}

	public void insertItemFavorite(Place place) {

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(KEY_ID, place.getId());
		values.put(KEY_NAME, place.getName());
		values.put(KEY_VICINITY, place.getVicinity());
		values.put(KEY_CHECK_STAR, place.isCheckStar());
		values.put(KEY_LAT, place.getLatitude());
		values.put(KEY_LNG, place.getLongitude());

		db.insert(TABLE_PLACE_TABLE, null, values);
		db.close();
	}

	public List<Place> getAllFavorite() {

		List<Place> contactList = new ArrayList<Place>();

		String selectQuery = "SELECT DISTINCT " + KEY_NAME + "," + KEY_VICINITY
				+ "," + KEY_LAT + "," + KEY_LNG + "," + KEY_CHECK_STAR
				+ " FROM " + TABLE_Favorite + " " + "ORDER BY " + KEY_ID
				+ " DESC ";

		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				Place contact = new Place();
				contact.setName(cursor.getString(0));
				contact.setVicinity(cursor.getString(1));
				contact.setLatitude(cursor.getDouble(2));
				contact.setLongitude(cursor.getDouble(3));
				contact.setCheckStar(true);
				contactList.add(contact);
			} while (cursor.moveToNext());
		}
		db.close();

		return contactList;
	}

	public List<Place> getAddress() {
		SQLiteDatabase db = this.getReadableDatabase();
		List<Place> contactList = new ArrayList<Place>();
		// Cursor cursor1 = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
		// KEY_ADDRESS, KEY_DATE }, KEY_ID + "=?",
		// new String[] { String.valueOf(id) }, null, null, null, KEY_ID +" "
		// +"DESC");
		Cursor cursor = db.query(TABLE_PLACE_TABLE, new String[] { KEY_ID,
				KEY_NAME, KEY_VICINITY }, null, null, null, null, KEY_ID + " "
				+ "DESC");
		if (cursor != null)
			cursor.moveToFirst();
		{
			do {
				Place contact = new Place();
				contact.setName(cursor.getString(1));
				contact.setLatitude(cursor.getDouble(2));
				contact.setLongitude(cursor.getDouble(3));
				contact.setVicinity(cursor.getString(4));
				// contact.setCheckStar(cursor.getB(4));
				contactList.add(contact);

			} while (cursor.moveToNext());
		}

		return contactList;
	}

	public List<Place> getAllSearchLimit(int top) {

		List<Place> contactList = new ArrayList<Place>();

		String selectQuery = "SELECT DISTINCT " + KEY_NAME + "," + KEY_VICINITY
				+ "," + KEY_LAT + "," + KEY_LNG + "," + KEY_CHECK_STAR
				+ "  FROM " + TABLE_PLACE_TABLE + " " + " ORDER BY " + KEY_ID
				+ " DESC " + " LIMIT " + String.valueOf(top);

		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				Place contact = new Place();
				contact.setName(cursor.getString(0));
				contact.setVicinity(cursor.getString(1));
				contact.setLatitude(cursor.getDouble(2));
				contact.setLongitude(cursor.getDouble(3));
				contact.setCheckStar(true);
				contactList.add(contact);
			} while (cursor.moveToNext());
		}
		db.close();

		return contactList;
	}

	public int getCount() {

		int count = 0;

		String selectQuery = "SELECT COUNT(*) FROM" + TABLE_PLACE_TABLE;

		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			count = cursor.getInt(0);
		}
		db.close();

		return count;
	}

	public List<SearchItem> getSearchTop(int top) {

		List<SearchItem> contactList = new ArrayList<SearchItem>();

		String selectQuery = "SELECT DISTINCT " + KEY_NAME + "," + KEY_VICINITY
				+ "  FROM " + TABLE_PLACE_TABLE + " " + " ORDER BY " + KEY_ID
				+ " DESC " + " LIMIT " + String.valueOf(top);

		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				SearchItem contact = new SearchItem();
				contact.setAddress(cursor.getString(0));
				contact.setReference(cursor.getString(1));
				contactList.add(contact);
			} while (cursor.moveToNext());
		}
		db.close();

		return contactList;
	}

	// Updating single contact
	public int updateSearchItem(SearchItem searchItem) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		// updating row
		return db.update(TABLE_PLACE_TABLE, values, KEY_ID + " = ?",
				new String[] { String.valueOf(searchItem.getId()) });
	}

	// Deleting single contact
	public void deleteSearchItem(SearchItem searchItem) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_PLACE_TABLE, KEY_ID + " = ?",
				new String[] { String.valueOf(searchItem.getId()) });
		db.close();
	}// Deleting single contact

	public void deletePlace() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_PLACE_TABLE, null, null);
		db.close();
	}

	public int getContactsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_PLACE_TABLE;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		return cursor.getCount();
	}

	public void deleteAll() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_Favorite, null, null);
		db.delete(TABLE_PLACE_TABLE, null, null);
		db.delete(TABLE_Read_Tin_KM, null, null);
		db.close();
	}

}
