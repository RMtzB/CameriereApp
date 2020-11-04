package app.proyecto.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;
import app.proyecto.Models.Product;
import app.proyecto.Utils.OnClickListener;
import app.proyecto.databinding.MenuItemBinding;

public class RecyclerViewAdapterMenu extends RecyclerView.Adapter<RecyclerViewAdapterMenu.ViewHolder> {
	private List<Product> items;
	private int layout;
	private OnClickListener listener;

	static class ViewHolder extends RecyclerView.ViewHolder {
		private MenuItemBinding menuItemBinding;
		private View view;

		public ViewHolder(@NonNull View itemView) {
			super(itemView);
			menuItemBinding = MenuItemBinding.bind(itemView);
			view = itemView;
		}

		public void setClickListener(OnClickListener listener, Product product) {
			view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					listener.onClick(product);
				}
			});
		}
	}

	public RecyclerViewAdapterMenu(List<Product> items, int layout, OnClickListener listener) {
		this.items = items;
		this.layout = layout;
		this.listener = listener;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);

		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		Product product = items.get(position);

		holder.setClickListener(listener, product);

//		Picasso.get().load(product.getImage()).fit().into(holder.menuItemBinding.imageViewProduct);

		holder.menuItemBinding.textViewProductName.setText(product.getName());
		holder.menuItemBinding.textViewProductDescription.setText(product.getDescription());
		holder.menuItemBinding.textViewPrice.setText(product.getPrice() + "");
	}

	@Override
	public int getItemCount() {
		return items.size();
	}
}