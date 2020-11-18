package app.proyecto.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import app.proyecto.Adapters.RecyclerViewAdapterMenu;
import app.proyecto.Models.Product;
import app.proyecto.R;
import app.proyecto.Utils.BusinessLogic;
import app.proyecto.Utils.OnClickListener;
import app.proyecto.databinding.ActivityMainBinding;




import static app.proyecto.Utils.BusinessLogic.getRestaurantIdentifier;
import static app.proyecto.Utils.Constants.DISABLE_BUTTONS;
import static app.proyecto.Utils.Constants.REQUEST_CODE_POPUP_MENU;

public class MainActivity extends AppCompatActivity implements OnClickListener {
	private ActivityMainBinding activityMainBinding;
	private LinearLayoutManager layoutManager;
	private RecyclerViewAdapterMenu recyclerViewAdapterMenu;
	private List<Product> items;
	private String[] code;
	private String restaurantIdentifier;
	private BusinessLogic businessLogic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
		setContentView(activityMainBinding.getRoot());

		businessLogic = new BusinessLogic(this);
		code = getExtras();
		items = new ArrayList<>();

		activityMainBinding.topAppBar.setTitle(code[1]);
		activityMainBinding.floatingButtonExitRestaurant.setColorFilter(Color.WHITE);

		menuConfiguration();
		recyclerViewConfiguration();

		restaurantIdentifier = getRestaurantIdentifier(code[1]);

		businessLogic.getRestaurantMenu(restaurantIdentifier, items, recyclerViewAdapterMenu);
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
						intent.putExtra("Code", code);

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

		activityMainBinding.floatingButtonExitRestaurant.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(businessLogic.isOrdersEmpty()) {
					Intent intent = new Intent(MainActivity.this, HomeActivity.class);
					SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.CCLOM.PREFERENCE_CODIGO", MODE_PRIVATE);

					Editor editor = sharedPreferences.edit();

					editor.clear();
					editor.apply();
					businessLogic.deleteLists();

					startActivity(intent);
					finish();
				} else {
					Toast.makeText(MainActivity.this, "Favor de pagar su cuenta", Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if(resultCode == RESULT_OK) {
			Toast.makeText(this, "Se agrego al carrito correctamente", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onClick(Object object) {
		Intent intent = new Intent(this, PopupActivity.class);

		intent.putExtra("object", (Product)object);
		intent.putExtra("disableButtons", DISABLE_BUTTONS);

		startActivityForResult(intent, REQUEST_CODE_POPUP_MENU);
	}

	@Override
	protected void onDestroy() {
		businessLogic.closeConnection();
		super.onDestroy();
	}
}