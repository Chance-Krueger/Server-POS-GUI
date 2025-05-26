import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import model.Bill;

import org.junit.jupiter.api.Test;

class BillTest {

	@Test
	void testConstructor() {
		Bill b1 = new Bill("1", "Aisiri");
		assertEquals(b1.getName(), "1");
		assertEquals(b1.getServerName(), "Aisiri");
	}
	
	@Test 
	void testAddItem() {
		Bill b1 = new Bill("1","Aisiri");
		b1.addItem("Mexican Rice");
		assertEquals(b1.getTotal(),3.99);
		b1.addItem("Mexican Rice");
		assertEquals(b1.getTotal(),7.98);
	}
	
	@Test
	void testPaid() {
		Bill b1 = new Bill("1", "Aisiri");
		assertFalse(b1.getPaid());
		b1.setPaid();
		assertTrue(b1.getPaid());
	}
	
	@Test
	void testTip() {
		Bill b1 = new Bill("1","Aisiri");
		assertEquals(b1.getTip(),0.0);
		b1.setTip(2.0);
		assertEquals(b1.getTip(),2.0);
	}
	
	@Test
	void testToString(){
		Bill b1 = new Bill("1","Aisiri");
		b1.addItem("Mexican Rice");
		b1.addItem("Mexican Rice");
		assertEquals(b1.toString(),"Quantity  Item  Price\n"
				+ "2  Mexican Rice   $3.99\n"
				+ "SUBTOTAL:   $7.98\n"
				+ "Tip options provided for your conveniece:\n"
				+ "20%  -   $1.60\n"
				+ "18%  -   $1.44\n"
				+ "15%  -   $1.20\n"
				+ "");
	}
	
	@Test
	void testGetItems() {
		Bill b1 = new Bill("1","Aisiri");
		assertEquals(b1.getItems().size(), 0);
		b1.addItem("Mexican Rice");
		b1.addItem("Mexican Rice");
		assertEquals(b1.getItems().size(), 1);
		assertEquals(b1.getTotal(), 7.98);
		assertNull(b1.getItems().get("rice"));
	}
	
	@Test
	void testMakeCopy() {
		Bill b1 = new Bill("1","Aisiri");
		assertNotNull(b1.makeCopy());
		assertNull(b1.makeCopy().getItems().get("Charro Beans"));
		b1.addItem("Charro Beans");
		b1.addItem("Charro Beans");
		b1.setTip(2.99);
		Bill copy = b1.makeCopy();
		assertEquals(copy.getItems().get("Charro Beans"),2);
		assertEquals(copy.getTip(), 2.99);
		
		
	}
	
	@Test
	void testPrintItemsOnly() {
		Bill b1 = new Bill("1","Aisiri");
		b1.addItem("Mexican Rice");
		b1.addItem("Mexican Rice");
		assertEquals(b1.printItemsOnly(),"Quantity  Item  Price\n"
				+ "2  Mexican Rice   $3.99\n"
				+ "SUBTOTAL:   $7.98\n");
	}
	
	@Test
	void testSplitandSplitString() {
		Bill b1 = new Bill("1", "Aisiri");
		Bill b2 = new Bill("1", "Aisiri");
		Bill b3 = new Bill("1", "Aisiri");;
		
		b1.addItem("Mexican Rice"); // 3.99
		b2.addItem("Charro Beans"); // 3.99
		b3.addItem("Mexican Rice");
		b2.addItem("Spanish Sangria"); // 10.99
		
		
		ArrayList<Bill> bills = new ArrayList<Bill>();
		
		bills.add(b1);
		bills.add(b2);
		bills.add(b3);
		
		assertEquals(String.format("%.2f",Bill.split(bills,3)), "7.65");
		
		assertEquals(Bill.splitString(bills, 3),"Your table's bill split by 3 people is:\n"
				+ "Quantity  Item  Price\n"
				+ "1  Mexican Rice   $3.99\n"
				+ "1  Spanish Sangria   $10.99\n"
				+ "1  Charro Beans   $3.99\n"
				+ "1  Mexican Rice   $3.99\n"
				+ "YOUR SUBTOTAL PER PERSON (3):   $7.65\n"
				+ "Tip options (per person) provided for your conveniece:\n"
				+ "20%  -   $1.53\n"
				+ "18%  -   $1.38\n"
				+ "15%  -   $1.15\n");
		
		assertEquals(Bill.splitString(bills, 0), "ERROR 0 input, your bill must at the least be split by 1 person\n");
	}
	

}
