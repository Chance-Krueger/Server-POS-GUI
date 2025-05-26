package model;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
public class Bill {
	private double total;
	private boolean paid;
	// key is item, value is quantity of item
	private HashMap<String, Integer> items;
	private String name;
	private double tip;
	private static Menu menu;
	private String serverName;
	
	public Bill(String name, String serverName) {
		total = 0.0;
		paid = false;
		items = new HashMap<String, Integer>();
		this.name = name;
		tip = 0.0;
		menu = new Menu();
		this.serverName = serverName;
	}

	// item is an item from the menu
	// item is pre-checked to make sure it is in fact an item from the menu
	public void addItem(String item) {
		double cost = menu.getCost(item);
		// null means the item is not already on the bill
		if (items.get(item) == null) {
			items.put(item, 1);
		} else {
			// item is on the bill and the count is incremented
			int count = items.get(item) + 1;
			items.put(item, count);
		}
		// the bill total is also updated to include this item
		total += cost;
	}
	
	public void setPaid() {
		paid = true;
	}
	public void setTip(double number) {
		tip += number;
	}
	public boolean getPaid() {
		return paid;
	}
	public double getTotal() {
		return total;
	}
	public String getName() {
		return name;
	}
	public double getTip() {
		return tip;
	}
	public String getServerName() {
		return serverName;
	}
	
	public HashMap<String, Integer> getItems(){
		HashMap<String, Integer> itemCopy = new HashMap<String, Integer>();
		for(String i : items.keySet()) {
			int count = items.get(i);
			itemCopy.put(i, count);
		}
		return itemCopy;
	}
	
	private void setItems(HashMap<String, Integer> copy) {
		items = copy;
	}
	
	private void setTotal(double total) {
		this.total = total;
	}
	
	public Bill makeCopy() {
		Bill newBill = new Bill(getName(),getServerName());
		newBill.setItems(getItems());
		newBill.setPaid();
		newBill.setTip(getTip());
		newBill.setTotal(getTotal());
		return newBill;
	}
	@Override
	public String toString() {
		String result = "Quantity  Item  Price\n";
		for (String i : items.keySet()) {
			result += String.valueOf(String.valueOf(items.get(i))) + "  " + i + "   $"
					+ String.format("%.2f", menu.getCost(i)) + "\n";
		}
		result += "SUBTOTAL:   $" + String.format("%.2f", total) + "\n";
		result += "Tip options provided for your conveniece:\n";
		result += "20%  -   $" + String.format("%.2f", getTipByPercentage(20.00, total)) + "\n";
		result += "18%  -   $" + String.format("%.2f", getTipByPercentage(18.00, total)) + "\n";
		result += "15%  -   $" + String.format("%.2f", getTipByPercentage(15.00, total)) + "\n";
		return result;
	}
	
	public String printItemsOnly() {
		String result = "Quantity  Item  Price\n";
		for (String i : items.keySet()) {
			result += String.valueOf(String.valueOf(items.get(i))) + "  " + i + "   $"
					+ String.format("%.2f", menu.getCost(i)) + "\n";
		}
		result += "SUBTOTAL:   $" + String.format("%.2f", total) + "\n";
		return result;
	}
	
	private static double getTipByPercentage(double percentage, double total) {
		double decimal = percentage / 100;
		return (decimal * total);
	}
	
	// static methods to deal with multiple bills
	public static double split(ArrayList<Bill> bills, int numPeople) {
		double tableTotal = 0.0;
		for (Bill b : bills) {
			tableTotal += b.getTotal();
		}
		return (tableTotal/numPeople);
	}
	
	// static method to print bill after splitting by multiple people
	public static String splitString(ArrayList<Bill> bills, int numPeople) {
		if (numPeople == 0) {
			return ("ERROR 0 input, your bill must at the least be split by 1 person\n");
		}
		String result = "Your table's bill split by " + numPeople + " people is:" + "\nQuantity  Item  Price\n";
		for (Bill b : bills) {
			HashMap<String, Integer> itemCopy = b.getItems();
			for (String i : itemCopy.keySet()) {
				result += String.valueOf(String.valueOf(itemCopy.get(i))) + "  " + i + "   $"
						+ String.format("%.2f", menu.getCost(i)) + "\n";
			}
		}
		result += "YOUR SUBTOTAL PER PERSON (" + numPeople + "):   $" + String.format("%.2f", split(bills,numPeople)) + "\n";
		result += "Tip options (per person) provided for your conveniece:\n";
		result += "20%  -   $" + String.format("%.2f", getTipByPercentage(20.00,split(bills,numPeople))) + "\n";
		result += "18%  -   $" + String.format("%.2f", getTipByPercentage(18.00,split(bills,numPeople))) + "\n";
		result += "15%  -   $" + String.format("%.2f", getTipByPercentage(15.00,split(bills,numPeople))) + "\n";
		return result;
	}

}