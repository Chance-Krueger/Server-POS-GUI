import static org.junit.jupiter.api.Assertions.*;

import model.Table;

import org.junit.jupiter.api.Test;

class TableTest {

	@Test
	void testGetTableNumber() {
		Table t = Table.getTable(1);
		assertEquals(t.getTableNumber(), 1);
	}
	
	@Test
	void testGetTable() {
		assertThrows(IllegalArgumentException.class, () -> Table.getTable(31));
	}

	

}
