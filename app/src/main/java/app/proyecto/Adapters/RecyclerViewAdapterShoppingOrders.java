package app.proyecto.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import app.proyecto.Models.CartOrdersItem;
import app.proyecto.Utils.OnClickListener;
import app.proyecto.Utils.OnRemoveItem;
import app.proyecto.databinding.CartAndOrdersItemBinding;

public class RecyclerViewAdapterShoppingOrders extends RecyclerView.Adapter<RecyclerViewAdapterShoppingOrders.ViewHolder> {
	private int layout;
	private List<CartOrdersItem> items;
	private OnClickListener listener;
	private OnRemoveItem remove;
	private ArrayAdapter<CharSequence> spinnerAdapter;
	private boolean cart;

	static class ViewHolder extends RecyclerView.ViewHolder {
		private CartAndOrdersItemBinding cartAndOrdersItemBinding;
		private View view;

		public ViewHolder(@NonNull View itemView) {
			super(itemView);
			cartAndOrdersItemBinding = CartAndOrdersItemBinding.bind(itemView);
			view = itemView;
		}

		public void setClickListener(OnClickListener listener, CartOrdersItem cartOrdersItem) {
			view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					listener.onClick(cartOrdersItem);
				}
			});
		}
	}

	public RecyclerViewAdapterShoppingOrders(int layout, List<CartOrdersItem> items, OnClickListener listener, OnRemoveItem remove, ArrayAdapter<CharSequence> spinnerAdapter, boolean cart) {
		this.layout = layout;
		this.items = items;
		this.listener = listener;
		this.remove = remove;
		this.spinnerAdapter = spinnerAdapter;
		this.cart = cart;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);

		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		CartOrdersItem cartOrdersItem = items.get(position);

		holder.setClickListener(listener, cartOrdersItem);

		holder.cartAndOrdersItemBinding.buttonDelete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				remove.onRemove(cartOrdersItem.getName());
				items.remove(cartOrdersItem);
				notifyDataSetChanged();
			}
		});

		holder.cartAndOrdersItemBinding.spinnerPieces.setAdapter(spinnerAdapter);
		holder.cartAndOrdersItemBinding.spinnerPieces.setSelection(cartOrdersItem.getPieces() - 1);

		if(!cart) {
			holder.cartAndOrdersItemBinding.spinnerPieces.setEnabled(false);
			holder.cartAndOrdersItemBinding.container.removeView(holder.cartAndOrdersItemBinding.buttonDelete);
		}

		holder.cartAndOrdersItemBinding.textViewProductName.setText(cartOrdersItem.getName());
		holder.cartAndOrdersItemBinding.textViewObservations.setText(cartOrdersItem.getObservations());
		holder.cartAndOrdersItemBinding.textViewPrice.setText(cartOrdersItem.getPrice() + "");
	}

	@Override
	public int getItemCount() {
		return items.size();
	}
}