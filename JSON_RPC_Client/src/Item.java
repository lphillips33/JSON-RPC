
public class Item {

	String name;
	double price;
	int stock;
	
	Item(String name, double price, int stock) {
		this.name = name;
		this.price = price;
		this.stock = stock;
	}
	
	private String getName() {
		return this.name;
	}
	
	private double getPrice() {
		return this.price;
	}
	
	private int getStock() {
		return this.stock;
	}
	
	public String toString() {
		String temp = "Name: " + this.name + " " + " Price: " 
				+ price + " Stock: " + stock;
		
		return temp;
	}
}
