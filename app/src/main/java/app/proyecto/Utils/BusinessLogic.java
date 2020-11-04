package app.proyecto.Utils;

import android.content.Context;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;
import app.proyecto.DataAccess.DAO;
import app.proyecto.Models.CartOrdersItem;
import app.proyecto.Models.Product;
import app.proyecto.R;

public class BusinessLogic {
	private final DAO dao;
	public BusinessLogic(Context context) {
		dao = new DAO(context);
	}

	public static List<Product> getRestaurantMenu(String restaurantIdentifier) {
		FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
		List<Product> items = new ArrayList<>();

		firebaseFirestore.collection(restaurantIdentifier).get().addOnCanceledListener(new OnCompleteListener<Task>());

//		items.add(new Product(R.drawable.img01 + "", "Nombre del producto", "Esta es la descripcion del producto", 34.00, "dsdfsd, dfsdf, dsfdsfs, dsfsdfsdfsdfsd, dsf"));
//		items.add(new Product(R.drawable.img02 + "", "Nombre del producto", "Esta es la descripcion del producto", 34.00, "dsdfsd, dfsdf, dsfdsfs, dsfsdfsdfsdfsd, dsf"));

		return items;
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

	public void removeItemFromCart(String name) {
		dao.removeItem(name);
	}

	public void closeConnection() {
		dao.closeDB();
	}
}
