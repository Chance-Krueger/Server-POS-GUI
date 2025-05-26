import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Map;

import org.junit.jupiter.api.Test;

import model.Bill;
import model.Server;
import model.Table;

public class ServerTest {

	@Test
	void testConstructor() {
		Server server = new Server("bob");
		assertTrue(server.getName().equals("bob"));
		assertTrue(server.getTotalTip() == 0.0);
	}

	@Test
	void testGetName() {
		Server server = new Server("bob");
		assertFalse(server.getName().equals("alexandra"));
		assertFalse(server.getName().equals("bobby"));
		assertTrue(server.getName().equals("bob"));
	}

	@Test
	void testAddTipAndGetTip() {
		Server server = new Server("bob");
		assertTrue(server.getTotalTip() == 0.0);

		server.addTable(Table.ONE, 1);
		server.addTable(Table.TWO, 1);
		server.addItemToBill(Table.ONE, 1, "El Jefe Margarita");
		server.addItemToBill(Table.ONE, 1, "El Jefe Margarita");
		server.addItemToBill(Table.ONE, 1, "El Jefe Margarita");

		server.addTipToBill(Table.ONE, 1, 1.50);
		assertTrue(server.getTotalTip() == 0.0);

		assertTrue(server.closeCheck(Table.ONE, 1));

		assertEquals(server.getTotalTip(), 1.5);

	}

	@Test
	void testGetTableMap() {
		Server server = new Server("bob");
		Map<Table, ArrayList<Bill>> hm = server.getTableMap();
		assertEquals(hm.size(), 0);

		server.addTable(Table.ONE, 5);
		assertEquals(hm.size(), 1);
		server.addTable(Table.TWO, 4);
		assertEquals(hm.size(), 2);
		server.addTable(Table.THREE, 3);
		assertEquals(hm.size(), 3);
		server.addTable(Table.FOUR, 2);
		assertEquals(hm.size(), 4);
		server.addTable(Table.FIVE, 1);
		assertEquals(hm.size(), 5);

		server.closeCheck(Table.FIVE, 1);
		assertEquals(hm.size(), 4);
	}

	@Test
	void testHasTable() {
		Server server = new Server("bob");
		server.addTable(Table.ONE, 1);
		server.addTable(Table.TWO, 2);
		server.addTable(Table.THREE, 3);
		server.addTable(Table.FOUR, 4);
		server.addTable(Table.FIVE, 5);

		assertTrue(server.hasTable(Table.TWO));
		assertFalse(server.hasTable(Table.EIGHT));
	}

	@Test
	void testAddTable() {
		Table t1 = Table.ONE;
		Table t2 = Table.TWO;
		Table t3 = Table.THREE;
		Table t4 = Table.FOUR;
		Table t5 = Table.FIVE;

		Server server = new Server("bob");
		Map<Table, ArrayList<Bill>> hm = server.getTableMap();
		assertEquals(hm.size(), 0);

		server.addTable(t1, 1);
		hm = server.getTableMap();
		assertEquals(hm.size(), 1);
		assertTrue(hm.containsKey(t1));
		ArrayList<Bill> bList = hm.get(t1);
		assertEquals(bList.size(), 1);

		server.addTable(t2, 2);
		hm = server.getTableMap();
		assertEquals(hm.size(), 2);
		assertTrue(hm.containsKey(t2));
		ArrayList<Bill> bList2 = hm.get(t2);
		assertEquals(bList2.size(), 2);

		server.addTable(t3, 3);
		hm = server.getTableMap();
		assertEquals(hm.size(), 3);
		assertTrue(hm.containsKey(t3));
		ArrayList<Bill> bList3 = hm.get(t3);
		assertEquals(bList3.size(), 3);

		server.addTable(t4, 4);
		hm = server.getTableMap();
		assertEquals(hm.size(), 4);
		assertTrue(hm.containsKey(t4));
		ArrayList<Bill> bList4 = hm.get(t4);
		assertEquals(bList4.size(), 4);

		server.addTable(t5, 5);
		hm = server.getTableMap();
		assertEquals(hm.size(), 5);
		assertTrue(hm.containsKey(t5));
		ArrayList<Bill> bList5 = hm.get(t5);
		assertEquals(bList5.size(), 5);

	}

	@Test
	void testGetBills() {
		Table t1 = Table.ONE;
		Table t2 = Table.TWO;
		Table t3 = Table.THREE;
		Table t4 = Table.FOUR;
		Table t5 = Table.FIVE;

		Server server = new Server("bob");
		server.addTable(t1, 1);
		server.addTable(t2, 1);
		server.addTable(t3, 1);
		server.addTable(t4, 1);
		server.addTable(t5, 2);

		Map<Table, ArrayList<Bill>> hm = server.getTableMap();
		assertEquals(hm.size(), 5);

		ArrayList<Bill> bList = server.getBillS();
		assertEquals(bList.size(), 0);

		server.addItemToBill(t5, 1, "El Jefe Margarita");
		server.addItemToBill(t4, 1, "El Jefe Margarita");
		server.addItemToBill(t3, 1, "El Jefe Margarita");
		server.addItemToBill(t2, 1, "El Jefe Margarita");
		server.addItemToBill(t1, 1, "El Jefe Margarita");

		server.closeCheck(t1, 1);
		server.closeCheck(t2, 1);
		server.closeCheck(t3, 1);
		server.closeCheck(t4, 1);
		server.closeCheck(t5, 1);

		bList = server.getBillS();
		assertEquals(bList.size(), 5);

		server.closeCheck(t5, 2);

		bList = server.getBillS();
		assertEquals(bList.size(), 1); // one left since it removes the other 5

	}

	@Test
	void testCloseTable() {
		Server server = new Server("bob");
		server.addTable(Table.ONE, 1);
		server.addTable(Table.TWO, 2);
		server.addTable(Table.THREE, 3);
		server.addTable(Table.FOUR, 4);
		server.addTable(Table.FIVE, 5);

		server.closeCheck(Table.FIVE, 1);
		assertEquals(server.getTableMap().size(), 5);

		server.closeCheck(Table.ONE, 1);
		assertEquals(server.getTableMap().size(), 4);
	}

	@Test
	void testCloseCheck() {

		Table t1 = Table.ONE;
		Table t2 = Table.TWO;
		Table t3 = Table.THREE;
		Table t4 = Table.FOUR;
		Table t5 = Table.FIVE;

		Server server = new Server("bob");
		server.addTable(t1, 1);
		server.addTable(t2, 2);
		server.addTable(t3, 3);
		server.addTable(t4, 4);
		server.addTable(t5, 5);

		Map<Table, ArrayList<Bill>> hm = server.getTableMap();
		assertEquals(hm.size(), 5);

		assertTrue(server.closeCheck(t5, 1));
		assertEquals(server.getBillS().size(), 1);

		assertTrue(server.closeCheck(t5, 1));
		assertEquals(server.getBillS().size(), 0); // was already added to closed tabs

		assertFalse(server.closeCheck(Table.EIGHT, 0));
		assertFalse(server.closeCheck(Table.FIVE, 6));

	}

	@Test
	void testAddItemToBill() {
		Server server = new Server("bob");
		server.addTable(Table.ONE, 6);

		assertTrue(server.addItemToBill(Table.ONE, 4, "El Jefe Margarita"));
		assertFalse(server.addItemToBill(Table.ONE, 7, "El Jefe Margarita"));
		assertFalse(server.addItemToBill(Table.ONE, 6, "I Need a Margarita After This Project"));
		assertFalse(server.addItemToBill(Table.TWO, 7, "El Jefe Margarita"));
	}

	@Test
	void testAddTipToBill() {
		Server server = new Server("bob");
		server.addTable(Table.ONE, 6);

		assertTrue(server.addTipToBill(Table.ONE, 1, 0.0));
		assertTrue(server.addTipToBill(Table.ONE, 1, -1.0));
		assertFalse(server.addTipToBill(Table.TWO, 1, 1.0));
		assertTrue(server.addTipToBill(Table.ONE, 2, 10.50));
		assertFalse(server.addTipToBill(Table.ONE, 7, 1.0));

	}

	@Test
	void testCheckHashStandards() {
		Server server = new Server("bob");
		assertFalse(server.closeCheck(Table.ONE, 1));
		server.addTable(Table.ONE, 3);
		assertFalse(server.closeCheck(Table.TWO, 4));

	}

	@Test
	void testToString() {
		Server server = new Server("bob");
		server.addTable(Table.ONE, 1);
		server.addTable(Table.TWO, 2);
		server.addTable(Table.THREE, 3);
		server.addTable(Table.FOUR, 4);
		server.addTable(Table.FIVE, 1);
		server.addTipToBill(Table.FIVE, 1, 420.99);
		server.closeCheck(Table.FIVE, 1);
		server.addTable(Table.FIVE, 5);
		assertTrue(server.toString().equals("bob has 5 Tables Total.\nbob has made 420.99 in Tips."));
	}
	
	@Test
	void testGetTableBills() {
		Server server = new Server("bob");
		
		server.addTable(Table.ONE, 3);
		
		assertEquals(server.getTableBills(Table.ONE).size(), 3);
		
	}
	
	@Test
	void testHasBill() {
		
		Server server = new Server("bob");
		
		server.addTable(Table.ONE, 1);
		
		assertFalse(server.hasBill(Table.ONE,2));
		assertTrue(server.hasBill(Table.ONE,1));
		
	}
	
	@Test
	void testCloseTableChecks() {
		
		Server server = new Server("bob");
		
		server.addTable(Table.ONE, 5);
		server.closeTableChecks(Table.ONE);
		
		assertEquals(server.getBillS().size(), 5);
		
	}

}
