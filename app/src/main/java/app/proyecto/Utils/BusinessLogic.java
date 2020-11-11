package app.proyecto.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.proyecto.Activities.PagoActivity;
import app.proyecto.Activities.ShoppingOrdersActivity;
import app.proyecto.Adapters.RecyclerViewAdapterMenu;
import app.proyecto.Adapters.RecyclerViewAdapterShoppingOrders;
import app.proyecto.DataAccess.DAO;
import app.proyecto.Models.CartOrdersItem;
import app.proyecto.Models.Product;




import static android.content.ContentValues.TAG;

public class BusinessLogic {
	private final DAO dao;
	public BusinessLogic(Context context) {
		dao = new DAO(context);
	}

	public void getRestaurantMenu(String restaurantIdentifier, List<Product> items, RecyclerViewAdapterMenu recyclerViewAdapterMenu) {
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
				return true;
			} else {
				return false;
			}
		}
	}

	public static String getRestaurantIdentifier(String name) {
		return "menu" + name.replace(" ", "");
	}

	public boolean isOrdersEmpty() {
		return dao.isOrdersEmpty();
	}

	public void deleteLists() {
		dao.deleteTable();
	}

	public void removeItemFromCart(String name) {
		dao.removeItem(name);
	}

	public void setPieces(String name, int pieces) {
		dao.setItemPieces(name, pieces);
	}

	public void sendOrder(List<CartOrdersItem> itemsInCart, RecyclerViewAdapterShoppingOrders recyclerViewAdapterShoppingOrders) {
		for(int i = 0; i < itemsInCart.size(); i++) {
			dao.setCart(itemsInCart.get(i).getName());
		}

		itemsInCart.clear();
		recyclerViewAdapterShoppingOrders.notifyDataSetChanged();
	}

	public void sendTotal(String[] code, String name, List<CartOrdersItem> items, Activity activity, Context context) {
		if(!items.isEmpty()) {
			double total = 0;
			CartOrdersItem item;
			Map<String, Object> cuenta = new HashMap<>();
			FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

			for(int i = 0; i < items.size(); i++) {
				item = items.get(i);

				total += item.getPieces() * item.getPrice();
			}

			cuenta.put("Nombre", name);
			cuenta.put("Total", total);

			firebaseFirestore
				.collection("Restaurants")
				.document(code[1])
				.collection(code[2])
				.document(name)
				.set(cuenta).addOnSuccessListener(new OnSuccessListener<Void>() {
				@Override
				public void onSuccess(Void aVoid) {
					Intent intent = new Intent(context, PagoActivity.class);

					intent.putExtra("Code", code);

					activity.startActivity(intent);
				}
			}).addOnFailureListener(new OnFailureListener() {
				@Override
				public void onFailure(@NonNull Exception e) {
					Toast.makeText(context, "Algo salio mal!! :s", Toast.LENGTH_SHORT).show();
				}
			});
		}
	}

	public void closeConnection() {
		dao.closeDB();
	}
}
