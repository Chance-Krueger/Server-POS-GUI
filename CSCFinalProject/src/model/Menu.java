package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Menu {
	
	// instance variables (maps item name to cost; separated by category)
	private HashMap<String, Double> drinks;
	private HashMap<String, Double> appetizers;
	private HashMap<String, Double> entrees;
	private HashMap<String, Double> sides;
	private HashMap<String, Double> desserts;
	private HashMap<String, Double> allItems;
	private HashMap<String, Double> modifications;
	
	// to populate the instance variables, we read from the menu file
	public Menu() {
		drinks = new HashMap<String, Double>();
		appetizers = new HashMap<String, Double>();
		entrees = new HashMap<String, Double>();
		sides = new HashMap<String, Double>();
		desserts = new HashMap<String, Double>();
		allItems = new HashMap<String, Double>();
		modifications = new HashMap<String, Double>();
		readFiles();
	}
	
	/* getters */
	public Map<String, Double> getDrinks() {
		return Collections.unmodifiableMap(drinks);
	}
	
	public Map<String, Double> getAppetizers() {
		return Collections.unmodifiableMap(appetizers);
	}
	
	public Map<String, Double> getEntrees() {
		return Collections.unmodifiableMap(entrees);
	}
	
	public Map<String, Double> getSides() {
		return Collections.unmodifiableMap(sides);
	}
	
	public Map<String, Double> getDesserts() {
		return Collections.unmodifiableMap(desserts);
	}
	
	public Map<String, Double> getModifications() {
		return Collections.unmodifiableMap(modifications);
	}
	
	public Map<String, Double> getAllItems() {
		return Collections.unmodifiableMap(allItems);
	}
	
	public double getCost(String item) {
		double cost  = 0.0;
		if(allItems.get(item) != null){
			cost +=allItems.get(item);
		}
		return cost;
	}
	
	// this reads the menu file and fills HashMaps
	private void readFiles() {
		try (BufferedReader reader = new BufferedReader(new FileReader("MenuItems.txt"))) {
			String line;
			String section = "";
			String[] allItems;
			String[] information;

			while ((line = reader.readLine()) != null) {
				if (line.equals("Drinks") || line.equals("Appetizers") || line.equals("Entrees") || line.equals("Sides") || line.equals("Desserts") || line.equals("Modifications")) {
					section = line;
					continue;
				}
				switch (section) {
				case "Drinks":
					allItems = line.split(";");
					if (!(allItems[0].equals(""))) {
						for (String i : allItems) {
							information = i.split(",");
							this.drinks.put(information[0], Double.parseDouble(information[1]));
							this.allItems.put(information[0], Double.parseDouble(information[1]));
						}
					}

					break;

				case "Appetizers":
					allItems = line.split(";");
					if (!(allItems[0].equals(""))) {
						for (String i : allItems) {
							information = i.split(",");
							this.appetizers.put(information[0], Double.parseDouble(information[1]));
							this.allItems.put(information[0], Double.parseDouble(information[1]));
						}
					}

					break;

				case "Entrees":
					allItems = line.split(";");
					if (!(allItems[0].equals(""))) {
						for (String i : allItems) {
							information = i.split(",");
							this.entrees.put(information[0], Double.parseDouble(information[1]));
							this.allItems.put(information[0], Double.parseDouble(information[1]));
						}
					}

					break;
					
				case "Sides":
					allItems = line.split(";");
					if (!(allItems[0].equals(""))) {
						for (String i : allItems) {
							information = i.split(",");
							this.sides.put(information[0], Double.parseDouble(information[1]));
							this.allItems.put(information[0], Double.parseDouble(information[1]));
						}
					}

					break;
					
				case "Desserts":
					allItems = line.split(";");
					if (!(allItems[0].equals(""))) {
						for (String i : allItems) {
							information = i.split(",");
							this.desserts.put(information[0], Double.parseDouble(information[1]));
							this.allItems.put(information[0], Double.parseDouble(information[1]));
						}
					}

					break;
					
				case "Modifications":
					allItems = line.split(";");
					if (!(allItems[0].equals(""))) {
						for (String i : allItems) {
							information = i.split(",");
							this.modifications.put(information[0], Double.parseDouble(information[1]));
							this.allItems.put(information[0], Double.parseDouble(information[1]));
						}
					}
					
					break;
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}