package app.proyecto.DataAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;
import app.proyecto.Models.CartOrdersItem;
import app.proyecto.Models.Product;
import app.proyecto.Utils.HelperDB;
import app.proyecto.Utils.SQLiteContract;

public class DAO {
	private HelperDB helperDB;
	private SQLiteDatabase dataBase;

	public DAO(Context context) {
		helperDB = new HelperDB(context, "CartAndOrders", null, 1);
		dataBase = helperDB.getWritableDatabase();
	}

	public boolean addItem(Product product, String observation) {
		ContentValues values = new ContentValues();
		Long rowId;

		values.put(SQLiteContract.ShoppingOrdersTable.COLUMN_NAME_NAME, product.getName());
		values.put(SQLiteContract.ShoppingOrdersTable.COLUMN_NAME_DESCRIPTION, product.getDescription());
		values.put(SQLiteContract.ShoppingOrdersTable.COLUMN_NAME_INGREDIENTS, product.getIngredients());
		values.put(SQLiteContract.ShoppingOrdersTable.COLUMN_NAME_IMAGE, product.getImage());
		values.put(SQLiteContract.ShoppingOrdersTable.COLUMN_NAME_PRICE, product.getPrice());
		values.put(SQLiteContract.ShoppingOrdersTable.COLUMN_NAME_PIECES, 1);
		values.put(SQLiteContract.ShoppingOrdersTable.COLUMN_NAME_OBSERVATIONS, observation);
		values.put(SQLiteContract.ShoppingOrdersTable.COLUMN_NAME_CART, 1);

		rowId = dataBase.insert(SQLiteContract.ShoppingOrdersTable.TABLE_NAME, null, values);

		return rowId != -1;
	}

	public boolean existItem(String name) {
		int rowsNumber;
		Cursor cursor = dataBase.rawQuery("SELECT * FROM " +
			SQLiteContract.ShoppingOrdersTable.TABLE_NAME + " WHERE " +
			SQLiteContract.ShoppingOrdersTable.COLUMN_NAME_NAME + "= " + "'" + name + "'" + " and " +
			SQLiteContract.ShoppingOrdersTable.COLUMN_NAME_CART + "= " + "1", null);

		rowsNumber = cursor.getCount();

		cursor.close();

		return rowsNumber != -1 && rowsNumber != 0;
	}

	public void setItemPieces(String name, int pieces) {

		dataBase.execSQL("UPDATE " +
			SQLiteContract.ShoppingOrdersTable.TABLE_NAME + " SET " +
			SQLiteContract.ShoppingOrdersTable.COLUMN_NAME_PIECES + "= " + pieces + " WHERE " +
			SQLiteContract.ShoppingOrdersTable.COLUMN_NAME_NAME + "= " + "'" + name + "'" + " and " +
			SQLiteContract.ShoppingOrdersTable.COLUMN_NAME_CART + "= 1");
	}

	public int getPieces(String name) {
		int pieces;
		Cursor cursor = dataBase.rawQuery("SELECT " +
			SQLiteContract.ShoppingOrdersTable.COLUMN_NAME_PIECES + " FROM " +
			SQLiteContract.ShoppingOrdersTable.TABLE_NAME + " WHERE " +
			SQLiteContract.ShoppingOrdersTable.COLUMN_NAME_NAME + "= " + "'" + name + "'" + " and " +
			SQLiteContract.ShoppingOrdersTable.COLUMN_NAME_CART + "= 1", null);

		if(cursor.moveToFirst()) {
			pieces = cursor.getInt(cursor.getColumnIndex(SQLiteContract.ShoppingOrdersTable.COLUMN_NAME_PIECES));
			cursor.close();

			return pieces;
		} else  {
			cursor.close();

			return -1;
		}
	}

	public List<CartOrdersItem> getAllItems(int cart) {
		List<CartOrdersItem> items = new ArrayList<>();
		Cursor cursor = dataBase.rawQuery("SELECT * FROM " +
			SQLiteContract.ShoppingOrdersTable.TABLE_NAME + " WHERE " +
			SQLiteContract.ShoppingOrdersTable.COLUMN_NAME_CART + "= " + cart, null);

		if(cursor.moveToFirst()) {
			while (!cursor.isAfterLast()) {
				items.add(new CartOrdersItem(
					cursor.getString(cursor.getColumnIndex(SQLiteContract.ShoppingOrdersTable.COLUMN_NAME_IMAGE)),
					cursor.getString(cursor.getColumnIndex(SQLiteContract.ShoppingOrdersTable.COLUMN_NAME_NAME)),
					cursor.getString(cursor.getColumnIndex(SQLiteContract.ShoppingOrdersTable.COLUMN_NAME_DESCRIPTION)),
					cursor.getDouble(cursor.getColumnIndex(SQLiteContract.ShoppingOrdersTable.COLUMN_NAME_PRICE)),
					cursor.getString(cursor.getColumnIndex(SQLiteContract.ShoppingOrdersTable.COLUMN_NAME_OBSERVATIONS)),
					cursor.getInt(cursor.getColumnIndex(SQLiteContract.ShoppingOrdersTable.COLUMN_NAME_PIECES)),
					cursor.getString(cursor.getColumnIndex(SQLiteContract.ShoppingOrdersTable.COLUMN_NAME_INGREDIENTS))));

				cursor.moveToNext();
			}
		}

		cursor.close();

		return items;
	}

	public void removeItem(String name) {
		dataBase.execSQL("DELETE FROM " + SQLiteContract.ShoppingOrdersTable.TABLE_NAME + " WHERE " + SQLiteContract.ShoppingOrdersTable.COLUMN_NAME_NAME + "= " + "'" + name + "'" + " and " + SQLiteContract.ShoppingOrdersTable.COLUMN_NAME_CART + "= 1");
	}

	public void closeDB() {
		helperDB.close();
	}
}