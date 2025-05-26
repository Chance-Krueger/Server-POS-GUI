import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import model.Bill;
import model.Server;
import model.Table;
import model.Management;
import model.Menu;

public class ManagementTest {
	private Menu menu = new Menu();
	private Management management = new Management(menu);
	
	@Test
	void testConstructor() {
		Management testManage = new Management(menu);
	}
	
	@Test
	void testNextTable() {
		assertNotNull(management.nextTable());
		for (int i = 1; i <= 30; i++) {
			management.seatTable(Table.getTable(i));
		}
		assertNull(management.nextTable());
	}
	
	@Test
	void testTakenandSeatAndFreeTable() {
	    assertTrue(management.seatTable(Table.ONE));    
	    assertTrue(management.isTaken(Table.ONE));      
	    assertFalse(management.seatTable(Table.ONE));   

	    assertTrue(management.freeTable(Table.ONE));    
	    assertFalse(management.isTaken(Table.ONE));     
	    assertFalse(management.freeTable(Table.ONE));   
	}
	
	@Test
	void testAddServer() {
		management.addServer("Ethan");
		
		ArrayList<Bill> bills = management.getBillsForServer("Ethan");
		assertEquals(0, bills.size());
		
		assertEquals(null, management.getBillsForServer("Chance")); // chance not in server list yet
	}
	
	@Test
	void testGetServer() {
		management.addServer("Chance");
		management.addServer("Ethan");
		management.addServer("Teddie");
		management.addServer("Aisiri");
		
		Server server = management.getServer("Aisiri");
		
		assertEquals("Aisiri", server.getName());
		assertNull(management.getServer("bob"));
	}
	
	@Test
	void testGetAllServers() {
		
		management.addServer("Chance");
		management.addServer("Ethan");
		management.addServer("Teddie");
		management.addServer("Aisiri");
		management.addServer("Aisiri");
		
		ArrayList<Server> s = management.getAllServers();
		
		assertEquals(s.size(),4);
		
		assertFalse(s.contains(new Server("bob")));
		
	}
	
	@Test
	void testHighestTip() {
		Table table1 = Table.ONE;
	    Table table2 = Table.TWO;
	    Table table3 = Table.THREE;
	    
	    String highest1 = management.getHighestTipCount();
	    
	    assertEquals(null, highest1);
		
		management.addServer("Ethan");
		management.addServer("Chance");
		
		Server ethan = management.getServer("Ethan");
	    Server chance = management.getServer("Chance");
	    
	    ethan.addTable(table1, 1);
	    chance.addTable(table2, 1);
	    
	    ethan.addItemToBill(table1, 1, "Steak Fajitas");
	    chance.addItemToBill(table2, 1, "Tortilla Soup");
	    
	    ethan.addTipToBill(table1, 1, 3.00);  
	    chance.addTipToBill(table2, 1, 5.00); 
	    
	    ethan.closeCheck(table1, 1);
	    chance.closeCheck(table2, 1);
	    
	    String highest = management.getHighestTipCount();
	    
	    assertEquals("Chance has the highest tip count: 5.0", highest);
	    
	    management.addServer("Teddie");
	    Server teddie = management.getServer("Teddie");
	    
	    teddie.addTable(table3, 2);
	    teddie.addItemToBill(table3, 1, "Steak Fajitas");
	    teddie.addItemToBill(table3, 1, "El Jefe Margarita");
	    teddie.addTipToBill(table3, 1, 10.00);
	    teddie.addItemToBill(table3, 2, "Blackberry Mint Mojito");
	    teddie.addTipToBill(table3, 2, 5.00);
	    
	    teddie.closeCheck(table3, 1);
	    teddie.closeCheck(table3, 2);
	    
	    highest = management.getHighestTipCount();
	    assertEquals("Teddie has the highest tip count: 15.0", highest);
	}
	
	@Test
	void testGetBillsForServer() {
		Table table1 = Table.ONE;
		management.addServer("Chance");
		
		Server chance = management.getServer("Chance");
		
		chance.addTable(table1, 4);
		
		chance.addItemToBill(table1, 1, "Guacamole");
		chance.addItemToBill(table1, 2, "Chicken Enchiladas");
		
		chance.closeCheck(table1, 1);
		chance.closeCheck(table1, 2);
		
		assertEquals(2, management.getBillsForServer("Chance").size());
	}
	
	@Test
	void testAddBill() {
		management.addServer("Ethan");
	    Server ethan = management.getServer("Ethan");
	    
	    Table table1 = Table.ONE;
	    ethan.addTable(table1, 1);
	    
	    ethan.addItemToBill(table1, 1, "Chicken Enchiladas");
	    ethan.closeCheck(table1, 1);
	    
	    management.addBill();
	    
	    assertEquals(1, management.getBillsPaid().size());

	}
	
	@Test
	void testSortMostOrdered() {
		management.addBill();
		
		//System.out.println(management.sortMostOrdered());
		assertEquals("Most Frequently Ordered Items: \n"
				+ "Dos Equis Ambar: 0\n"
				+ "Double Meat: 0\n"
				+ "Charro Beans: 0\n"
				+ "Traditional Flan: 0\n"
				+ "Bacon-Wrapped Stuffed Shrimp: 0\n"
				+ "Chile con Queso: 0\n"
				+ "Tres Leches Cake: 0\n"
				+ "Steak Fajitas: 0\n"
				+ "El Jefe Margarita: 0\n"
				+ "Chile Rellonos: 0\n"
				+ "Corn Tortilla: 0\n"
				+ "Guacamole: 0\n"
				+ "Chicken Fajitas: 0\n"
				+ "No Cheese: 0\n"
				+ "Extra Guacamole: 0\n"
				+ "No Cilantro: 0\n"
				+ "Chicken Enchiladas: 0\n"
				+ "No Onions: 0\n"
				+ "Green Chile Chicken Quesadilla: 0\n"
				+ "Flour Tortilla: 0\n"
				+ "Blackberry Mint Mojito: 0\n"
				+ "Fajita Shrimp Tacos: 0\n"
				+ "Mexican Rice: 0\n"
				+ "Tortilla Soup: 0\n"
				+ "Modelo Especial Lagar: 0\n"
				+ "Extra Salsa: 0\n"
				+ "Vegetable Fajitas: 0\n"
				+ "No Peppers: 0\n"
				+ "Add Tajin: 0\n"
				+ "No Tomatoes: 0\n"
				+ "Chicken Salad: 0\n"
				+ "Dulce de Leche Cheesecake: 0\n"
				+ "Spanish Sangria: 0\n"
				+ "Steak Chimichanga: 0\n", management.sortMostOrdered());
		
		management.addServer("Ethan");
	    Server ethan = management.getServer("Ethan");
	    
	    Table table1 = Table.ONE;
	    ethan.addTable(table1, 1);
	    
	    ethan.addItemToBill(table1, 1, "Chicken Enchiladas");
	    ethan.addItemToBill(table1, 1, "Steak Fajitas");
	    ethan.addItemToBill(table1, 1, "Guacamole");
	    ethan.addItemToBill(table1, 1, "Guacamole");
	    ethan.addItemToBill(table1, 1, "Chicken Enchiladas");
	    ethan.addItemToBill(table1, 1, "Guacamole");
	    ethan.addItemToBill(table1, 1, "Steak Chimichanga");
	    ethan.closeCheck(table1, 1);
	    
	   
	   
	    management.addBill();
	    
	    assertEquals("Most Frequently Ordered Items: \n"
	    		+ "Guacamole: 3\n"
	    		+ "Chicken Enchiladas: 2\n"
	    		+ "Steak Fajitas: 1\n"
	    		+ "Steak Chimichanga: 1\n"
	    		+ "Dos Equis Ambar: 0\n"
	    		+ "Double Meat: 0\n"
	    		+ "Charro Beans: 0\n"
	    		+ "Traditional Flan: 0\n"
	    		+ "Bacon-Wrapped Stuffed Shrimp: 0\n"
	    		+ "Chile con Queso: 0\n"
	    		+ "Tres Leches Cake: 0\n"
	    		+ "El Jefe Margarita: 0\n"
	    		+ "Chile Rellonos: 0\n"
	    		+ "Corn Tortilla: 0\n"
	    		+ "Chicken Fajitas: 0\n"
	    		+ "No Cheese: 0\n"
	    		+ "Extra Guacamole: 0\n"
	    		+ "No Cilantro: 0\n"
	    		+ "No Onions: 0\n"
	    		+ "Green Chile Chicken Quesadilla: 0\n"
	    		+ "Flour Tortilla: 0\n"
	    		+ "Blackberry Mint Mojito: 0\n"
	    		+ "Fajita Shrimp Tacos: 0\n"
	    		+ "Mexican Rice: 0\n"
	    		+ "Tortilla Soup: 0\n"
	    		+ "Modelo Especial Lagar: 0\n"
	    		+ "Extra Salsa: 0\n"
	    		+ "Vegetable Fajitas: 0\n"
	    		+ "No Peppers: 0\n"
	    		+ "Add Tajin: 0\n"
	    		+ "No Tomatoes: 0\n"
	    		+ "Chicken Salad: 0\n"
	    		+ "Dulce de Leche Cheesecake: 0\n"
	    		+ "Spanish Sangria: 0\n",management.sortMostOrdered());
	}
	
	@Test
	void testSortByRevenue() {
		management.addBill();
		
		management.addServer("Ethan");
	    Server ethan = management.getServer("Ethan");
	    
	    Table table1 = Table.ONE;
	    ethan.addTable(table1, 2);
	    
	    ethan.addItemToBill(table1, 1, "Chicken Enchiladas");
	    ethan.addItemToBill(table1, 1, "Steak Fajitas");
	    ethan.addItemToBill(table1, 1, "Guacamole");
	    
	    
	    ethan.addItemToBill(table1, 2, "Chicken Enchiladas");
	    ethan.addItemToBill(table1, 2, "Guacamole");
	    ethan.addItemToBill(table1, 2, "Steak Chimichanga");
	    
	    ethan.closeCheck(table1, 1);
	    
	    ethan.closeCheck(table1, 2);
	    management.addBill();
	    
	    
	    //System.out.println(management.sortMostMoneyByItem());
	    
	    assertEquals("Items sorted by total revenue:\n"
	    		+ "Chicken Enchiladas: $32.98\n"
	    		+ "Steak Fajitas: $29.49\n"
	    		+ "Guacamole: $26.58\n"
	    		+ "Steak Chimichanga: $25.19\n"
	    		+ "Dos Equis Ambar: $0.00\n"
	    		+ "Double Meat: $0.00\n"
	    		+ "Charro Beans: $0.00\n"
	    		+ "Traditional Flan: $0.00\n"
	    		+ "Bacon-Wrapped Stuffed Shrimp: $0.00\n"
	    		+ "Chile con Queso: $0.00\n"
	    		+ "Tres Leches Cake: $0.00\n"
	    		+ "El Jefe Margarita: $0.00\n"
	    		+ "Chile Rellonos: $0.00\n"
	    		+ "Corn Tortilla: $0.00\n"
	    		+ "Chicken Fajitas: $0.00\n"
	    		+ "No Cheese: $0.00\n"
	    		+ "Extra Guacamole: $0.00\n"
	    		+ "No Cilantro: $0.00\n"
	    		+ "No Onions: $0.00\n"
	    		+ "Green Chile Chicken Quesadilla: $0.00\n"
	    		+ "Flour Tortilla: $0.00\n"
	    		+ "Blackberry Mint Mojito: $0.00\n"
	    		+ "Fajita Shrimp Tacos: $0.00\n"
	    		+ "Mexican Rice: $0.00\n"
	    		+ "Tortilla Soup: $0.00\n"
	    		+ "Modelo Especial Lagar: $0.00\n"
	    		+ "Extra Salsa: $0.00\n"
	    		+ "Vegetable Fajitas: $0.00\n"
	    		+ "No Peppers: $0.00\n"
	    		+ "Add Tajin: $0.00\n"
	    		+ "No Tomatoes: $0.00\n"
	    		+ "Chicken Salad: $0.00\n"
	    		+ "Dulce de Leche Cheesecake: $0.00\n"
	    		+ "Spanish Sangria: $0.00\n", management.sortMostMoneyByItem());
	}
	
	
	
	
	

}