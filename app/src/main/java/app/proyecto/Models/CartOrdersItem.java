package app.proyecto.Models;

public class CartOrdersItem extends Product{
	private String observations;
	private int pieces;

	public CartOrdersItem(String image,
								 String name,
								 String description,
								 double price,
								 String observations,
								 int pieces,
								 String ingredients) {
		super(image, name, description, price, ingredients);
		this.observations = observations;
		this.pieces = pieces;
	}

	public String getObservations() {
		return observations;
	}

	public void setObservations(String observations) {
		this.observations = observations;
	}

	public int getPieces() {
		return pieces;
	}

	public void setPieces(int pieces) {
		this.pieces = pieces;
	}
}

