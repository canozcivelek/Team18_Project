package tr.edu.bilkent.ctis.team18.adapters;

import java.util.Calendar;
import java.util.Date;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class DBAdapter extends Activity {

	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;
	private final Context context;

	public static final String KEY_ID = "id";
	public static final String KEY_NAME = "name";
	public static final String KEY_LASTNAME = "lastname";
	public static final String KEY_EMAIL = "email";
	public static final String KEY_PASSWORD = "password";
	private static final String TAG = "DBAdapter";

	private static final String DATABASE_NAME = "my_db";
	private static final String DATABASE_TABLE = "User";
	private static final int DATABASE_VERSION = 3;

	private static final String DATABASE_CREATE = "create table User (id integer primary key autoincrement, "
			+ " name text not null, lastname text not null, email text not null,"
			+ "password text not null);";


	// Constructor
	public DBAdapter(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}

	// To create and upgrade a database in an Android application SQLiteOpenHelper subclass is usually created 
	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// onCreate() is called by the framework, if the database does not exist
			Log.d("Create", "Creating the database");

			try {
				db.execSQL(DATABASE_CREATE);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// Sends a Warn log message
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");

			// Method to execute an SQL statement directly
			db.execSQL("DROP TABLE IF EXISTS department");
			onCreate(db);
		}
	}

	// Opens the database
	public DBAdapter open() throws SQLException {
		// Create and/or open a database that will be used for reading only	
		db = DBHelper.getReadableDatabase();
		return this;
	}

	// Closes the database
	public void close() {
		// Closes the database
		DBHelper.close();
	}
	

	public long insertContact(String name, String lastname) {
		// The class ContentValues allows to define key/values. The "key" represents the
		// table column identifier and the "value" represents the content for the table
		// record in this column. ContentValues can be used for inserts and updates of database entries. 
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NAME, name);
		initialValues.put(KEY_LASTNAME, lastname);
		return db.insert(DATABASE_TABLE, null, initialValues);
	}
	
	// Retrieves all the contacts
	public Cursor getAllContacts() {
		Cursor mCursor;
		
		mCursor = db.query(DATABASE_TABLE, new String[] { KEY_ID, 
				KEY_NAME, KEY_LASTNAME, KEY_EMAIL, KEY_PASSWORD}, null, null, null, null, null);
		
		if (mCursor != null){
			mCursor.moveToFirst();
			Log.d("","Size of database " + mCursor.getCount());
		}
		
		return mCursor;
	}

}
