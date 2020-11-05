package app.proyecto.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import app.proyecto.Adapters.RecyclerViewAdapterMenu;
import app.proyecto.Models.Product;
import app.proyecto.R;
import app.proyecto.Utils.OnClickListener;
import app.proyecto.databinding.ActivityMainBinding;



import static app.proyecto.Utils.BusinessLogic.getRestaurantIdentifier;
import static app.proyecto.Utils.BusinessLogic.getRestaurantMenu;
import static app.proyecto.Utils.Constants.DISABLE_BUTTONS;
import static app.proyecto.Utils.Constants.REQUEST_CODE_POPUP_MENU;

public class MainActivity extends AppCompatActivity implements OnClickListener {
	private ActivityMainBinding activityMainBinding;
	private LinearLayoutManager layoutManager;
	private RecyclerViewAdapterMenu recyclerViewAdapterMenu;
	private List<Product> items;
	private String[] code;
	private String restaurantIdentifier;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
		setContentView(activityMainBinding.getRoot());

		code = getExtras();
		items = new ArrayList<>();

		activityMainBinding.topAppBar.setTitle(code[1]);

		menuConfiguration();
		recyclerViewConfiguration();

		restaurantIdentifier = getRestaurantIdentifier(code[1]);

		getRestaurantMenu(restaurantIdentifier, items, recyclerViewAdapterMenu);
	}

	private String[] getExtras() {
		Intent intent = getIntent();
		String[] code;

		Bundle bundle = intent.getExtras();

		if(bundle != null && bundle.getString("codigo") != null) {
			code = bundle.getString("codigo").split("-");
		} else {
			code = new String[]{"", "Hola", "", ""};
		}

		return code;
	}

	private void recyclerViewConfiguration() {
		layoutManager = new LinearLayoutManager(this);
		recyclerViewAdapterMenu = new RecyclerViewAdapterMenu(items, R.layout.menu_item, this, this);

		activityMainBinding.recyclerViewMenu.setLayoutManager(layoutManager);
		activityMainBinding.recyclerViewMenu.setAdapter(recyclerViewAdapterMenu);
	}

	private void menuConfiguration() {
		activityMainBinding.topAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Intent intent = new Intent(MainActivity.this, ShoppingOrdersActivity.class);
				switch(item.getItemId()) {
					case R.id.ordersList:
						startActivity(intent);
						break;
					case R.id.shoppingCart:
						intent.putExtra("Cart", true);

						startActivity(intent);
						break;
				}

				return false;
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if(resultCode == RESULT_OK) {
			Toast.makeText(this, "Hola", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onClick(Object object) {
		Intent intent = new Intent(this, PopupActivity.class);

		intent.putExtra("object", (Product)object);
		intent.putExtra("disableButtons", DISABLE_BUTTONS);

		startActivityForResult(intent, REQUEST_CODE_POPUP_MENU);
	}
}