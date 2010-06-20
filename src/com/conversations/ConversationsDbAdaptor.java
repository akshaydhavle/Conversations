package com.conversations;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ConversationsDbAdaptor {
	private static final int DATABASE_VERSION = 5;
	private static final String DATABASE_NAME = "conversation_backlog";

	public static final String T1_TABLENAME = "notebook";
	public static final String T1_PK = "_id";
	public static final String T1_PERSON_ID = "person_id";
	public static final String T1_NOTES = "note";

	private static final String TABLE1_CREATE = "CREATE TABLE " + T1_TABLENAME
			+ " (" + T1_PK + " INTEGER PRIMARY KEY AUTOINCREMENT, " + T1_PERSON_ID
			+ " TEXT, " + T1_NOTES + " TEXT);";

	public static final String T2_TABLENAME = "people";
	public static final String T2_PK = "_id";
	public static final String T2_NAME = "name";

	private static final String TABLE2_CREATE = "CREATE TABLE " + T2_TABLENAME
			+ " (" + T2_PK + " INTEGER PRIMARY KEY AUTOINCREMENT, " + T2_NAME
			+ " TEXT);";

	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.v("Creating tables", "T2");
			db.execSQL(TABLE1_CREATE);
			db.execSQL(TABLE2_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.v("ConversationsDbHelper", "Upgrading database from version "
					+ oldVersion + " to " + newVersion
					+ ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS notebook");
			db.execSQL("DROP TABLE IF EXISTS people");
			onCreate(db);
		}
	}

	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;
	Context context;

	public ConversationsDbAdaptor(Context context) {
		this.context = context;
	}

	public ConversationsDbAdaptor open() throws SQLException {
		dbHelper = new DatabaseHelper(context);
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		dbHelper.close();
	}

	public Cursor fetchNoteForPerson(String person_id) {
		Cursor cursor = db.query(T1_TABLENAME, new String[] { T1_NOTES, T1_PK },
				T1_PERSON_ID+"=" + person_id, null, null, null, null);
		return cursor;
	}

	public long updateNote(ContentValues values,
			String whereClause, String[] whereArgs) {
		long success = db.update(T1_TABLENAME, values, whereClause, whereArgs);
		return success;
	}

	public long insertNote(String nullColumnHack,
			ContentValues values) {
		long success = db.insert(T1_TABLENAME, nullColumnHack, values);
		return success;
	}

	public Cursor fetchAllPeople() {
		Cursor cursor = db.query(T2_TABLENAME, new String[]{T2_PK,T2_NAME}, null, null, null, null, null);
		return cursor;
	}

	public long addNewPerson(ContentValues values) {
		return db.insert("people", null, values);
	}
}
