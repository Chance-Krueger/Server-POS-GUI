import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import model.LoadRestaurantData;
import model.Management;
import model.Menu;
import model.SaveResturantData;
import model.Server;
import model.Table;

public class LoadRestaurantDataTest {

	@Test
	void testLoadRestaurantDataTest() {
		Menu menu = new Menu();
		Management manager = new Management(menu);
		manager.addServer("Lotz");
		Server s = manager.getServer("Lotz");
		manager.seatTable(Table.ONE);
		manager.seatTable(Table.TWO);
		s.addTable(Table.ONE, 2);
		s.addItemToBill(Table.ONE, 1, "Chicken Fajitas");
		s.addItemToBill(Table.ONE, 1, "Chicken Fajitas");
		s.addTipToBill(Table.ONE, 2, 100.00);
		s.closeCheck(Table.ONE, 1);
		s.closeCheck(Table.ONE, 2);
		s.addTable(Table.TWO, 3);
		manager.freeTable(Table.ONE);
		manager.addBill();
		s.addItemToBill(Table.TWO, 1, "Chicken Fajitas");
		s.closeCheck(Table.TWO, 1);
		s.addItemToBill(Table.TWO, 2, "Chicken Fajitas");
		s.addTable(Table.THREE, 5);
		s.addItemToBill(Table.THREE, 2, "Charro Beans");
		s.addItemToBill(Table.THREE, 4, "Vegetable Fajitas");
		s.addTable(Table.EIGHT, 1);
		s.addItemToBill(Table.EIGHT, 1, "Charro Beans");
		s.closeCheck(Table.EIGHT, 1);
		manager.addServer("Bob");
		SaveResturantData srd = new SaveResturantData(manager);
		Management manage = LoadRestaurantData.loadManagerInstance();
		SaveResturantData srd1 = new SaveResturantData(manage);

		assertEquals(manage.getAllServers().size(), 2);
	}

}
