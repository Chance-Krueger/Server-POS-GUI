import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import model.Management;
import model.Menu;
import model.ResturantModelServer;
import model.Table;

class ResturantModelServerTest {
	
	Menu menu = new Menu();
	Management mgmt = new Management(menu);
	ResturantModelServer m;
	
	public ResturantModelServerTest(){
		mgmt.addServer("Aisiri");
		m = new ResturantModelServer(mgmt, "Aisiri");
	}

	@Test
	void testSeatTable() {
		assertEquals(m.seatTable(Table.ONE, 0), "ERROR! Must seat at least 1 person\n");
		assertEquals(m.seatTable(Table.ONE, 2),"Table number 1 was seated successfully\n");
		assertEquals(m.seatTable(Table.ONE, 2),"ERROR! Table number 1 is already sat please pick a different table\n");
	}
	
	@Test
	void testFreeTable() {
		
		assertEquals(m.freeTable(Table.ONE), "ERROR! Table number 1 was never seated.\n");
		
		m.seatTable(Table.ONE, 1);
		
		assertEquals(m.freeTable(Table.ONE), "ERROR! Table number 1 still has open bills!");
		
		m.payBillIndividual(Table.ONE, 1);
		
		assertEquals(m.freeTable(Table.ONE), "Table number 1 was freed successfully\n");
			
	}
	
	@Test
	void testCheckTableAndBill() {
		
		assertEquals(m.takeOrder(Table.ONE, 1, "Extra Guacamole"), "ERROR! you do not have table number: 1\n");
		
		m.seatTable(Table.ONE, 1);
		
		assertEquals(m.takeOrder(Table.ONE, 2, "Extra Guacamole"), "ERROR! table number 1 does not have a bill by the name 2\n");
	}
	
	@Test
	void testTakeOrder() {
		
		m.seatTable(Table.ONE, 1);
		
		assertEquals(m.takeOrder(Table.ONE, 1, "Pizza"), "ERROR! Cannot place order, Pizza is not on our menu\n");
		
		assertEquals(m.takeOrder(Table.ONE, 1, "Extra Guacamole"), "Order placed!\nExtra Guacamole was added to bill 1 at table 1\n");
		
	}
	
	@Test
	void testPrintBill() {
		
		m.seatTable(Table.ONE, 2);
		
		m.takeOrder(Table.ONE, 1, "Extra Guacamole");
		m.takeOrder(Table.ONE, 2, "Mexican Rice");
		
		assertEquals(m.printBill(Table.ONE), "Person: 1\n"
				+ "Quantity  Item  Price\n"
				+ "1  Extra Guacamole   $3.79\n"
				+ "SUBTOTAL:   $3.79\n"
				+ "\n"
				+ "Person: 2\n"
				+ "Quantity  Item  Price\n"
				+ "1  Mexican Rice   $3.99\n"
				+ "SUBTOTAL:   $3.99\n\n");
	}
	
	@Test
	void testPrintBillWhenPaying() {
		
		m.seatTable(Table.ONE, 2);
		
		m.takeOrder(Table.ONE, 1, "Extra Guacamole");
		m.takeOrder(Table.ONE, 2, "Mexican Rice");
		
		assertEquals(m.printBillWhenPaying(Table.ONE, 0, 1), "Person: 1\n"
				+ "Quantity  Item  Price\n"
				+ "1  Extra Guacamole   $3.79\n"
				+ "SUBTOTAL:   $3.79\n"
				+ "Tip options provided for your conveniece:\n"
				+ "20%  -   $0.76\n"
				+ "18%  -   $0.68\n"
				+ "15%  -   $0.57\n\n");
		
		assertEquals(m.printBillWhenPaying(Table.ONE,1, 1), "Your table's bill split by 2 people is:\n"
				+ "Quantity  Item  Price\n"
				+ "1  Extra Guacamole   $3.79\n"
				+ "1  Mexican Rice   $3.99\n"
				+ "YOUR SUBTOTAL PER PERSON (2):   $3.89\n"
				+ "Tip options (per person) provided for your conveniece:\n"
				+ "20%  -   $0.78\n"
				+ "18%  -   $0.70\n"
				+ "15%  -   $0.58\n");
		
	}
	
	@Test
	void testAddTipIndividual() {
		assertEquals(m.addTipIndividual(Table.ONE, 1, 1.00), "ERROR! you do not have table number: 1\n");
		
		m.seatTable(Table.ONE, 1);
		
		assertEquals(m.addTipIndividual(Table.ONE, 2, 1.00), "ERROR! table number 1 does not have a bill by the name 2\n");
		
		assertEquals(m.addTipIndividual(Table.ONE, 1, 1.00), "Tip 1.0 was added to Bill Number 1\n");
	}
	
	@Test
	void testPayBillIndividual() {
		
        assertEquals(m.payBillIndividual(Table.ONE, 1), "ERROR! you do not have table number: 1\n");
		
		m.seatTable(Table.ONE, 1);
		
		assertEquals(m.payBillIndividual(Table.ONE, 2), "ERROR! table number 1 does not have a bill by the name 2\n");
	}
	
	@Test
	void testAddTipTable() {
		
		assertEquals(m.payBillIndividual(Table.ONE, 1), "ERROR! you do not have table number: 1\n");
			
		m.seatTable(Table.ONE, 2);
		
		assertEquals(m.addTipTable(Table.ONE, 2.00), "Tip 2.0 was added to the bill for table number 1\n");
		
	}
	
	@Test
	void testPayBillTable() {
		
		assertEquals(m.payBillTable(Table.ONE), "ERROR! you do not have table number: 1\n");
		
		m.seatTable(Table.ONE, 2);
		
		assertEquals(m.payBillTable(Table.ONE), "Table Number: 1 was successfully fully paid\n");
		
	}
	
	@Test
	void testServerTips() {
		
		m.seatTable(Table.ONE, 1);
		m.addTipIndividual(Table.ONE, 1, 2.59);
		m.payBillIndividual(Table.ONE, 1);
		
		assertEquals(m.serverTips(), "You have made $2.59 in tips!\n");
		
	}
	
	

}
