package app.proyecto.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class HelperDB extends SQLiteOpenHelper {
	public HelperDB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		sqLiteDatabase.execSQL(SQLiteContract.ShoppingOrdersTable.SQL_CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
		sqLiteDatabase.execSQL(SQLiteContract.ShoppingOrdersTable.SQL_DELETE_ENTRIES);
		onCreate(sqLiteDatabase);
	}
}
