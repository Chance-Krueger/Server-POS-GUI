import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.Test;

import model.Menu;

class MenuTest {

	private Menu menu = new Menu();
	
	@Test
	void testConstructor() {
		Menu constructorMenu = new Menu();
	}
	
	@Test
	void testGetDrinks() {
		String keys = "";
		String values = "";
		Map<String, Double> theDrinks = menu.getDrinks();
		for (Object key : theDrinks.keySet()) {
			keys += key + " ";
		}
		for (Double value : theDrinks.values()) {
			values += value + " ";
		}
		assertEquals(keys, "Dos Equis Ambar El Jefe Margarita Blackberry Mint Mojito Modelo Especial Lagar Spanish Sangria ");
		assertEquals(values, "7.99 13.99 13.99 7.99 10.99 ");
	}
	
	@Test
	void testGetAppetizers() {
		String keys = "";
		String values = "";
		Map<String, Double> theAppetizers = menu.getAppetizers();
		for (Object key : theAppetizers.keySet()) {
			keys += key + " ";
		}
		for (Double value : theAppetizers.values()) {
			values += value + " ";
		}
		assertEquals(keys, "Chile con Queso Chicken Salad Guacamole Tortilla Soup Bacon-Wrapped Stuffed Shrimp ");
		assertEquals(values, "10.59 16.99 13.29 10.59 14.89 ");
	}
	
	@Test
	void testGetEntrees() {
		String keys = "";
		String values = "";
		Map<String, Double> theEntrees = menu.getEntrees();
		for (Object key : theEntrees.keySet()) {
			keys += key + " ";
		}
		for (Double value : theEntrees.values()) {
			values += value + " ";
		}
		assertEquals(keys, "Green Chile Chicken Quesadilla Steak Fajitas Chile Rellonos Fajita Shrimp Tacos Chicken Fajitas Chicken Enchiladas Vegetable Fajitas Steak Chimichanga ");
		assertEquals(values, "19.69 29.49 22.99 19.69 26.49 16.49 21.89 25.19 ");
	}
	
	@Test
	void testGetSides() {
		String keys = "";
		String values = "";
		Map<String, Double> theSides = menu.getSides();
		for (Object key : theSides.keySet()) {
			keys += key + " ";
		}
		for (Double value : theSides.values()) {
			values += value + " ";
		}
		assertEquals(keys, "Flour Tortilla Mexican Rice Corn Tortilla Charro Beans ");
		assertEquals(values, "0.79 3.99 0.49 3.99 ");
	}
	
	@Test
	void testGetDesserts() {
		String keys = "";
		String values = "";
		Map<String, Double> theDesserts = menu.getDesserts();
		for (Object key : theDesserts.keySet()) {
			keys += key + " ";
		}
		for (Double value : theDesserts.values()) {
			values += value + " ";
		}
		assertEquals(keys, "Tres Leches Cake Dulce de Leche Cheesecake Traditional Flan ");
		assertEquals(values, "8.99 8.99 8.99 ");
	}
	
	@Test
	void testGetModifications() {
		String keys = "";
		String values = "";
		Map<String, Double> theModifications = menu.getModifications();
		for (Object key : theModifications.keySet()) {
			keys += key + " ";
		}
		for (Double value : theModifications.values()) {
			values += value + " ";
		}
		assertEquals(keys, "No Cheese Extra Guacamole Double Meat No Cilantro No Onions Extra Salsa No Peppers Add Tajin No Tomatoes ");
		assertEquals(values, "0.0 3.79 7.49 0.0 0.0 0.0 0.0 0.0 0.0 ");
	}
	
	@Test
	void testGetAllItems() {
		String keys = "";
		String values = "";
		Map<String, Double> all = menu.getAllItems();
		for (Object key : all.keySet()) {
			keys += key + " ";
		}
		for (Double value : all.values()) {
			values += value + " ";
		}
		
		assertEquals(keys, "Dos Equis Ambar Double Meat Charro Beans Traditional Flan Bacon-Wrapped Stuffed Shrimp Chile con Queso Tres Leches Cake Steak Fajitas El Jefe Margarita Chile Rellonos Corn Tortilla Guacamole Chicken Fajitas No Cheese Extra Guacamole No Cilantro Chicken Enchiladas No Onions Green Chile Chicken Quesadilla Flour Tortilla Blackberry Mint Mojito Fajita Shrimp Tacos Mexican Rice Tortilla Soup Modelo Especial Lagar Extra Salsa Vegetable Fajitas No Peppers Add Tajin No Tomatoes Chicken Salad Dulce de Leche Cheesecake Spanish Sangria Steak Chimichanga ");
		assertEquals(values, "7.99 7.49 3.99 8.99 14.89 10.59 8.99 29.49 13.99 22.99 0.49 13.29 26.49 0.0 3.79 0.0 16.49 0.0 19.69 0.79 13.99 19.69 3.99 10.59 7.99 0.0 21.89 0.0 0.0 0.0 16.99 8.99 10.99 25.19 ");
		
	}
	
	@Test
	void testGetCost() {
		assertEquals(menu.getCost("pizza"), 0.0);
		assertEquals(menu.getCost("Extra Guacamole"), 3.79);
		
	}

}
