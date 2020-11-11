package app.proyecto.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import java.util.List;
import app.proyecto.Adapters.RecyclerViewAdapterShoppingOrders;
import app.proyecto.DataAccess.DBUser;
import app.proyecto.Models.CartOrdersItem;
import app.proyecto.R;
import app.proyecto.Utils.BusinessLogic;
import app.proyecto.Utils.OnClickListener;
import app.proyecto.Utils.OnRemoveItem;
import app.proyecto.Utils.OnSetItem;
import app.proyecto.databinding.ActivityShoppingOrdersBinding;


import static app.proyecto.Utils.Constants.REQUEST_CODE_POPUP_MENU;

public class ShoppingOrdersActivity extends AppCompatActivity implements OnClickListener, OnRemoveItem, OnSetItem {
	private ActivityShoppingOrdersBinding activityShoppingOrdersBinding;
	private List<CartOrdersItem> items;
	private LinearLayoutManager layoutManager;
	private RecyclerViewAdapterShoppingOrders recyclerViewAdapterShoppingOrders;
	private BusinessLogic businessLogic;
	private boolean cart;
	private String[] code;
	private ArrayAdapter<CharSequence> spinnerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activityShoppingOrdersBinding = ActivityShoppingOrdersBinding.inflate(getLayoutInflater());
		setContentView(activityShoppingOrdersBinding.getRoot());

		businessLogic = new BusinessLogic(this);

		getExtras();

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		viewConfiguration();

		onClickListenerConfigurations();

		recyclerViewConfiguration();
	}

	private void getExtras() {
		Bundle bundle = getIntent().getExtras();

		if(bundle != null) {
			cart = bundle.getBoolean("Cart", false);

			if (!cart) {
				code = bundle.getStringArray("Code");
			}
		}
	}

	private void viewConfiguration() {
		if(cart) {
			items = businessLogic.getCart();

			this.setTitle("Mi carrito");
		} else {
			items = businessLogic.getOrders();

			this.setTitle("Mis ordenes");
			activityShoppingOrdersBinding.buttonSendOrPay.setText(R.string.pay);
			activityShoppingOrdersBinding.buttonSendOrPay.setIcon(getDrawable(R.drawable.ic_payment_24dp));
		}
	}

	private void onClickListenerConfigurations() {
		activityShoppingOrdersBinding.buttonSendOrPay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(cart) {
					businessLogic.sendOrder(items, recyclerViewAdapterShoppingOrders);

					Toast.makeText(ShoppingOrdersActivity.this, "Su pedido fue enviado correctamente", Toast.LENGTH_SHORT).show();
				} else {
					businessLogic.sendTotal(code, DBUser.miUsuario.getNombre(), items, ShoppingOrdersActivity.this, ShoppingOrdersActivity.this);
				}
			}
		});
	}

	private void recyclerViewConfiguration() {
		spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.pieces, android.R.layout.simple_spinner_dropdown_item);

		layoutManager = new LinearLayoutManager(this);

		recyclerViewAdapterShoppingOrders = new RecyclerViewAdapterShoppingOrders(R.layout.cart_and_orders_item, items, this, this, spinnerAdapter, cart, this);

		activityShoppingOrdersBinding.recyclerViewCartOrders.setLayoutManager(layoutManager);
		activityShoppingOrdersBinding.recyclerViewCartOrders.setAdapter(recyclerViewAdapterShoppingOrders);
	}

	@Override
	public void onClick(Object object) {
		Intent intent = new Intent(this, PopupActivity.class);

		intent.putExtra("object", (CartOrdersItem)object);

		startActivityForResult(intent, REQUEST_CODE_POPUP_MENU);
	}

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		finish();
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onRemove(String name) {
		businessLogic.removeItemFromCart(name);
	}

	@Override
	public void onSetPieces(String name, int pieces) {
		businessLogic.setPieces(name, pieces);
	}

	@Override
	protected void onDestroy() {
		businessLogic.closeConnection();
		super.onDestroy();
	}
}