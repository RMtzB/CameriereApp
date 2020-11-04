package app.proyecto.Utils;

import android.provider.BaseColumns;

public final class SQLiteContract {
	public static class ShoppingOrdersTable implements BaseColumns {
		public static final String TABLE_NAME = "ShoppingOrdersTable";
		public static final String COLUMN_NAME_ID = "Id";
		public static final String COLUMN_NAME_NAME = "Name";
		public static final String COLUMN_NAME_DESCRIPTION = "Description";
		public static final String COLUMN_NAME_INGREDIENTS = "Ingredients";
		public static final String COLUMN_NAME_IMAGE = "Image";
		public static final String COLUMN_NAME_PRICE = "Price";
		public static final String COLUMN_NAME_PIECES = "Pieces";
		public static final String COLUMN_NAME_OBSERVATIONS = "Observations";
		public static final String COLUMN_NAME_CART = "Cart";



		public static final String SQL_CREATE_TABLE =
			"CREATE TABLE " +
				TABLE_NAME + " (" +
				COLUMN_NAME_ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
				COLUMN_NAME_NAME + " TEXT, " +
				COLUMN_NAME_DESCRIPTION + " TEXT, " +
				COLUMN_NAME_INGREDIENTS + " TEXT, " +
				COLUMN_NAME_IMAGE + " TEXT, " +
				COLUMN_NAME_PRICE + " REAL, " +
				COLUMN_NAME_PIECES + " INTEGER, " +
				COLUMN_NAME_OBSERVATIONS + " TEXT, " +
				COLUMN_NAME_CART + " INTEGER)";


		public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;
	}
}