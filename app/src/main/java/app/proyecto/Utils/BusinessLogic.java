package app.proyecto.Utils;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

import app.proyecto.Adapters.RecyclerViewAdapterMenu;
import app.proyecto.DataAccess.DAO;
import app.proyecto.Models.CartOrdersItem;
import app.proyecto.Models.Product;
import app.proyecto.R;

import static android.content.ContentValues.TAG;

public class BusinessLogic {
	private final DAO dao;
	public BusinessLogic(Context context) {
		dao = new DAO(context);
	}

	public static void getRestaurantMenu(String restaurantIdentifier, List<Product> items, RecyclerViewAdapterMenu recyclerViewAdapterMenu) {
		FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

		firebaseFirestore.collection(restaurantIdentifier).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
			@Override
			public void onComplete(@NonNull Task<QuerySnapshot> task) {
				if(task.isSuccessful()) {
					for(QueryDocumentSnapshot document : task.getResult()) {
						items.add(new Product(document.getString("Imagen"), document.getString("Nombre"), document.getString("Descripcion"), document.getDouble("Precio"), document.getString("Ingredientes")));
					}

					recyclerViewAdapterMenu.notifyDataSetChanged();
				} else {
					Log.d(TAG, "Error getting documents: ", task.getException());
				}
			}
		});
	}

	public List<CartOrdersItem> getCart() {
		return dao.getAllItems(1);
	}

	public List<CartOrdersItem> getOrders() {
		return dao.getAllItems(0);
	}

	public boolean addProductToCart(Product product, String observations) {
		if(!dao.existItem(product.getName())) {
			return dao.addItem(product, observations);
		} else {
			int pieces = dao.getPieces(product.getName()) + 1;

			if(pieces < 10) {
				dao.setItemPieces(product.getName(), pieces);
			} else {
				return false;
			}

			return true;
		}
	}

	public static String getRestaurantIdentifier(String name) {
		return "menu" + name.replace(" ", "");
	}

	public void removeItemFromCart(String name) {
		dao.removeItem(name);
	}

	public void closeConnection() {
		dao.closeDB();
	}
}
