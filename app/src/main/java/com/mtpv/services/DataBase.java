package com.mtpv.services;

import java.util.LinkedHashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBase {

	Context context;
	Databasehelpers myDatabase;
	public static LinkedHashMap<String, String> secFinalMap;
	public static SQLiteDatabase db;

	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "ghmc_data.db";

	public static final String IP_TABLE = "IP_TABLE";
	public static final String IP_ADDRESS = "IP_ADDRESS";

	public static final String Bluetooth = "bluetooth";

	// bluetooth column names
	static final String BT_Name = "BT_Name";

	// AuthenticateUser column names
	public static String USER_TABLE = "USER_TABLE";
	static final String PID_CODE = "PID_CODE";
	static final String PID_NAME = "PID_NAME";
	static final String PS_CODE = "PS_CODE";
	static final String PS_NAME = "Ps_NAME";
	static final String CADRE_CODE = "CADRE_CODE";
	static final String CADRE_NAME = "CADRE_NAME";
	static final String SECURITY_CD = "SECURITY_CD";
	static final String OFFICER_TYPE = "OFFICER_TYPE";
	public static String PASSWORD = "PASSWORD";
	public static String UNIT_CODE = "UNIT_CODE";
	public static String UNIT_NAME = "UNIT_NAME";

	// PS NAMES
	public static String psName_table = "PSNAME_TABLE";
	public static String ps_code_settings = "PS_CODE";
	public static String ps_name_settings = "PS_NAME";

	// LOCATION NAMES and CODES
	public static String location_masters_table = "LOCATION_MASTERS_TABLE";
	public static String zone_code = "ZONE_CODE";
	public static String zone_name = "ZONE_NAME";

	public static String ward_code = "WARD_CODE";
	public static String ward_name = "WARD_NAME";

	public static String circle_code = "CIRCLE_CODE";
	public static String circle_name = "CIRCLE_NAME";

	public static String location_code = "LOCATION_CODE";
	public static String location_name = "LOCATION_NAME";

	// PS NAMES
	public static String pointName_table = "POINT_NAME_TABLE";
	public static String pointName = "POINT_NAME";
	public static String pointCode = "POINT_CODE";

	// IDPROOFS
	public static String Id_details_table = "ID_DETAILS_TABLE";
	public static String idCode = "ID_CODE";
	public static String idName = "ID_NAME";

	/* SECTION NAMES */
	public static String sections_table = "SECTION_CODE_TABLE";
	public static String section_code = "SECTION_CODE";
	public static String section_name = "SECTION_NAME";

	/* SECTION CODES : SECTION NAMES : FINE AMOUNT */
	public static String temp_sections_table = "TEMP_SECTION_CODE_TABLE";
	public static String fine_amnt = "FINE_AMOUNT";
	public static String section_desc = "SEC_DESC";

	/******************* DATABASE TABLE NAMES *****************/

	public static final String CREATE_Bluetooth = "CREATE TABLE " + Bluetooth
			+ "(" + BT_Name + " VARCHAR )";

	public static String SectionTableCreation = "create table "
			+ sections_table + " ( SNO INTEGER PRIMARY KEY AUTOINCREMENT , "
			+ section_code + " varchar2(20), " + section_name
			+ " varchar2(40));";

	public static String TEmp_SectionTableCreation = "create table "
			+ temp_sections_table
			+ " ( SNO INTEGER PRIMARY KEY AUTOINCREMENT , " + section_code
			+ " varchar2(20), " + section_name + " varchar2(20), " + fine_amnt
			+ " varchar2(40), " + section_desc + " varchar2(40));";

	public static final String CREATE_IP_TABLE = "CREATE TABLE  IF NOT EXISTS "
			+ IP_TABLE + "(IP_ADDRESS  VARCHAR )";

	public static String psNamesCreation = "create table " + psName_table
			+ " ( SNO INTEGER PRIMARY KEY AUTOINCREMENT , " + ps_code_settings
			+ " varchar2(20), " + ps_name_settings + " varchar2(40));";

	public static String pointNamesCreation = "create table " + pointName_table
			+ " ( SNO INTEGER PRIMARY KEY AUTOINCREMENT , " + pointCode
			+ " varchar2(20), " + pointName + " varchar2(40));";

	public static String idDetailsCreation = "create table " + Id_details_table
			+ " ( SNO INTEGER PRIMARY KEY AUTOINCREMENT , " + idCode
			+ " varchar2(20), " + idName + " varchar2(40));";

	// ZONE_CD:ZONE_NAME:WARD_CD:WARD_NAME:CIRCLE_CD:CIRCLE_NAME:LOCATION_CD:LOCATION_NAME@
	public static String locationMastersCreation = "create table "
			+ location_masters_table
			+ " ( SNO INTEGER PRIMARY KEY AUTOINCREMENT , " + zone_code
			+ " varchar2(30), " + zone_name + " varchar2(40), " + ward_code
			+ " varchar2(30), " + ward_name + " varchar2(30), " + circle_code
			+ " varchar2(30), " + circle_name + " varchar2(30), "
			+ location_code + " varchar2(30), " + location_name
			+ " varchar2(30));";

	public static final String CREATE_USER_TABLE = "CREATE TABLE " + USER_TABLE
			+ "(" + PID_CODE + " VARCHAR," + PID_NAME + " VARCHAR," + PS_CODE
			+ " VARCHAR," + PS_NAME + " VARCHAR," + CADRE_CODE + " VARCHAR,"
			+ CADRE_NAME + " VARCHAR," + SECURITY_CD + " VARCHAR ,"
			+ OFFICER_TYPE + " VARCHAR )";

	class Databasehelpers extends SQLiteOpenHelper {

		public Databasehelpers(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL(CREATE_IP_TABLE);
			db.execSQL(psNamesCreation);
			db.execSQL(pointNamesCreation);
			db.execSQL(idDetailsCreation);
			db.execSQL(CREATE_Bluetooth);
			db.execSQL(SectionTableCreation);
			db.execSQL(TEmp_SectionTableCreation);
			db.execSQL(locationMastersCreation);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS " + IP_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + psName_table);
			db.execSQL("DROP TABLE IF EXISTS " + pointName_table);
			db.execSQL("DROP TABLE IF EXISTS " + sections_table);
			db.execSQL("DROP TABLE IF EXISTS " + Bluetooth);
			db.execSQL("DROP TABLE IF EXISTS " + Id_details_table);
			db.execSQL("DROP TABLE IF EXISTS " + temp_sections_table);
			db.execSQL("DROP TABLE IF EXISTS " + location_masters_table);
			onCreate(db);
		}
	}

	public static void Delete_ReadingData(String tblename) {
		db.execSQL("delete from " + tblename);

	}

	public DataBase(Context context) {
		this.context = context;
		myDatabase = new Databasehelpers(context);
	}

	public static void insertIPDetails(String IpdAddress) {
		ContentValues con = new ContentValues();
		con.put(IP_ADDRESS, IpdAddress);
		db.insert(IP_TABLE, null, con);
	}

	public DataBase open() throws SQLException {
		// TODO Auto-generated method stub
		db = myDatabase.getWritableDatabase();
		return this;
	}

	public void close() {
		myDatabase.close();
	}

	public static void insertPsNameDetails(String ps_code, String ps_name) {
		ContentValues con = new ContentValues();
		con.put(ps_code_settings, ps_code);
		con.put(ps_name_settings, ps_name);
		db.insert(psName_table, null, con);
	}

	public static void insertBluettoth(String ps_code, String ps_name) {
		ContentValues con = new ContentValues();
		con.put(ps_code_settings, ps_code);
		con.put(ps_name_settings, ps_name);
		db.insert(psName_table, null, con);
	}

	public static void insertPointNamesDetails(String point_code,
			String point_name) {
		ContentValues con = new ContentValues();
		con.put(pointCode, point_code);
		con.put(pointName, point_name);
		db.insert(pointName_table, null, con);
	}

	public static void insertIdProofDetails(String id_code, String id_name) {
		Log.i("DATABASE VAL ::", "" + id_code + "--" + id_name);
		ContentValues con = new ContentValues();
		con.put(idCode, id_code);
		con.put(idName, id_name);
		db.insert(Id_details_table, null, con);
	}

	public static void insertLocationDetails(String zoneCode, String zoneName,
			String wardCode, String wardName, String circleCode,
			String circleName, String locationCode, String locationName) {
		ContentValues con = new ContentValues();
		con.put(zone_code, zoneCode);
		con.put(zone_name, zoneName);
		con.put(ward_code, wardCode);
		con.put(ward_name, wardName);
		con.put(circle_code, circleCode);
		con.put(circle_name, circleName);
		con.put(location_code, locationCode);
		con.put(location_name, locationName);
		db.insert(location_masters_table, null, con);
	}

	public void insertSectionDetails(String section_code,
			String section_name) {
		ContentValues con = new ContentValues();
		con.put(DataBase.section_code, section_code);
		con.put(DataBase.section_name, section_name);
		db.insert(sections_table, null, con);
	}

	public void insertTempSectionDetails(String section_code,
			String section_name, String fine_amnt, String SectionDesc) {
		ContentValues con = new ContentValues();
		con.put(DataBase.section_code, section_code);
		con.put(DataBase.section_name, section_name);
		con.put(DataBase.fine_amnt, fine_amnt);
		con.put(DataBase.section_desc, SectionDesc);
		db.insert(temp_sections_table, null, con);
	}

	public Map<String, String> getSecMap(Context applicationContext) {
		
		Map<String, String> secMap = new LinkedHashMap<String, String>();
		secFinalMap = new LinkedHashMap<String, String>();

		try {
			String selectQuery = "SELECT * FROM " + DataBase.temp_sections_table;
			Cursor cursor = db.rawQuery(selectQuery, null);
			int i = 0;
			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					i++;
					Log.i("SECTION CODE FROM TABLE :", "" + cursor.getString(0));
					Log.i("SECTION NAME FROM TABLE :", "" + cursor.getString(1));
					Log.i("SECTION DESC FROM TABLE :", "" + cursor.getString(2));
					Log.i("SECTION DESC FROM TABLE :", "" + cursor.getString(3));
					Log.i("SECTION DESC FROM TABLE :", "" + cursor.getString(4));
					// section code
					// "\t\tSectionname \t\t Fine \t\t Section Description"
					secMap.put("" + cursor.getString(1), "" + cursor.getString(2).toString().trim()
							+ " \t\t" + cursor.getString(3).trim() + "\t " + cursor.getString(4).trim());

					secFinalMap.put("" + cursor.getString(2).toString().trim() + " \t\t" + cursor.getString(3).trim() + "\t "
							+ cursor.getString(4).trim(), cursor.getString(1) + ":" + cursor.getString(2) + ":" + cursor.getString(3));
					for (String secCode : secMap.keySet()) {
						Log.i("SECTION CODE E:", secCode + "\t sec view  :" + secMap.get(secCode));
					}
				} while (cursor.moveToNext());
			}
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return secMap;
	}
}
