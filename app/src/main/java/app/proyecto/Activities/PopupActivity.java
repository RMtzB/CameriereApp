package app.proyecto.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.bumptech.glide.Glide;
import app.proyecto.Models.CartOrdersItem;
import app.proyecto.Models.Product;
import app.proyecto.Utils.BusinessLogic;
import app.proyecto.databinding.ActivityPopupBinding;

public class PopupActivity extends AppCompatActivity {
	private ActivityPopupBinding activityPopupBinding;
	private int disableButtons;
	private Object object;
	private Intent intent;
	private Bundle bundle;
	private BusinessLogic businessLogic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activityPopupBinding = ActivityPopupBinding.inflate(getLayoutInflater());
		setContentView(activityPopupBinding.getRoot());

		businessLogic = new BusinessLogic(this);
		object = getExtras();
		insertInfoInView();

		onClickListenersConfiguration();
	}

	private Object getExtras() {
		intent = getIntent();
		bundle = intent.getExtras();

		if(bundle != null && bundle.getSerializable("object") != null) {
			disableButtons = bundle.getInt("disableButtons", 1);

			if(disableButtons == 1) {
				activityPopupBinding.popupMenuLayout.removeView(activityPopupBinding.buttonsSendOrderAddToCartLayout);
			} else {
				activityPopupBinding.popupMenuTopBar.removeView(activityPopupBinding.buttonDonePopup);
			}

			return bundle.getSerializable("object");
		}

		return null;
	}

	private void insertInfoInView() {
		if(disableButtons == 1) {
			CartOrdersItem cartOrdersItem = (CartOrdersItem)object;

			Glide.with(this).load(cartOrdersItem.getImage()).fitCenter().centerCrop().into(activityPopupBinding.imageViewProduct);

			activityPopupBinding.textViewProductName.setText(cartOrdersItem.getName());
			activityPopupBinding.textViewProductDescription.setText(cartOrdersItem.getDescription());
			activityPopupBinding.textViewProductIngredients.setText(cartOrdersItem.getIngredients());
			activityPopupBinding.editTextObservations.setText(cartOrdersItem.getObservations());
		} else {
			Product product = (Product)object;

			Glide.with(this).load(product.getImage()).fitCenter().centerCrop().into(activityPopupBinding.imageViewProduct);

			activityPopupBinding.textViewProductName.setText(product.getName());
			activityPopupBinding.textViewProductDescription.setText(product.getDescription());
			activityPopupBinding.textViewProductIngredients.setText(product.getIngredients());
		}
	}

	private void onClickListenersConfiguration() {
		activityPopupBinding.buttonClosePopup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});

		if(disableButtons == 1) {
			activityPopupBinding.buttonDonePopup.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
				}
			});
		} else  {
			activityPopupBinding.buttonAddToCart.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					if(businessLogic.addProductToCart((Product) object, String.valueOf(activityPopupBinding.editTextObservations.getText()))) {
						setResult(RESULT_OK);
					} else  {
						setResult(RESULT_CANCELED);
					}

					finish();
				}
			});

			activityPopupBinding.buttonSendOrder.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
				}
			});
		}
	}
}