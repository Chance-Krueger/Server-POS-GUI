package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Server {

	private String name;
	private HashMap<Table, ArrayList<Bill>> tableMap;
	private Double totalTip;
	private ArrayList<Bill> closedTabs;

	public Server(String name) {
		this.name = name;
		this.tableMap = new HashMap<Table, ArrayList<Bill>>();
		this.totalTip = 0.0;
		this.closedTabs = new ArrayList<Bill>();
	}

	// DEEP COPY
	public void setPaidBills(ArrayList<Bill> billList) {
		this.closedTabs = new ArrayList<Bill>(billList);
	}

	// DEEP COPY?
	public void setTableMap(HashMap<Table, ArrayList<Bill>> tableMap) {
		this.tableMap = tableMap;
	}

	public Double getTotalTip() {
		return totalTip;
	}

	/*
	 * Add any additional Tips after each sale.
	 * 
	 * @arguments: Double - totalTip; represents a tip from customer.
	 */
	public void addTip(Double totalTip) {
		this.totalTip += totalTip;
	}

	public String getName() {
		return name;
	}

	public Map<Table, ArrayList<Bill>> getTableMap() {
		return Collections.unmodifiableMap(this.tableMap);
	}

	// get a copy of bills list
	public ArrayList<Bill> getTableBills(Table table) {

		ArrayList<Bill> bList = this.tableMap.get(table);

		ArrayList<Bill> copy = new ArrayList<Bill>();

		for (Bill b : bList) {
			copy.add(b.makeCopy());
		}

		return copy;
	}

	/*
	 * Checks if the server is currently managing the specified table.
	 * 
	 * @arguments: Table table -> The table to check for in the server's tableMap.
	 * 
	 * @returns: boolean -> True if the table exists in the server's map, false
	 * otherwise.
	 */
	public boolean hasTable(Table table) {
		return this.tableMap.containsKey(table);
	}

	/*
	 * Checks if one of the server's table has a specifc bill
	 * 
	 * @arguments: Table table -> The table to check for in the server's tableMap
	 * int billBum -> the bill number
	 * 
	 * @returns: boolean -> True if the table has a bill by the num otherwise.
	 */
	public boolean hasBill(Table table, int billNum) {
		ArrayList<Bill> bList = this.tableMap.get(table);

		for (Bill bill : bList) {
			// Bill is paid
			if (bill.getName().equals("bill" + billNum)) {
				return true;
			}
		}
		return false;
	}

	/*
	 * Adds a table to the server's HashMap, and initializes an ArrayList of all the
	 * Bill(s).
	 * 
	 * @arguments: Table table -> table to be added; amountPeople -> The amount of
	 * people to be sat at table
	 */
	public void addTable(Table table, int amountPeople) {
		ArrayList<Bill> billList = new ArrayList<Bill>();
		for (int num = 1; num <= amountPeople; num++) {
			Bill bill = new Bill(("bill" + num), getName());
			billList.add(bill);
		}
		this.tableMap.put(table, billList);
	}

	/*
	 * Gets all the bills that the server has closed. It then removes all the
	 * closedTabs to make sure not duplicate bills paid.
	 * 
	 * @returns: An ArrayList<Bill> of all the Bills paid in a deep copy of Bill.
	 * Then returns a Shallow copy of the instance variable for no escaping
	 * references.
	 */
	public ArrayList<Bill> getBillS() {
		ArrayList<Bill> closed = new ArrayList<Bill>(this.closedTabs);
		this.closedTabs.removeAll(this.closedTabs);
		return closed;
	}

	/*
	 * Closes a table and deletes from the HashMap.
	 * 
	 * @arguments: Table table -> Which table to remove from HashMap.
	 * 
	 * @returns: Boolean -> True if table was found in HashMap and deleted. False if
	 * table wasn't found.
	 */
	private void closeTable(Table table) {
		this.tableMap.remove(table);
	}

	// close all the checks at a table
	public void closeTableChecks(Table table) {

		if (!checkHashStandards(table)) {
			return;
		}

		ArrayList<Bill> tableBill = this.tableMap.get(table);

		for (Bill b : tableBill) {
			b.setPaid();
			this.closedTabs.add(b.makeCopy());
			this.addTip(b.getTip());
		}
		closeTable(table);
	}

	/*
	 * Manager/Model class should call this method until the check is closed.
	 * 
	 * @arguments: Table table -> The table associated with the bill to be closed.
	 * int billNum -> The specific bill number to be marked as paid.
	 * 
	 * @process: Validates that the table exists and is managed by the server. -
	 * Searches for the bill by name (e.g., "bill1") within the list of bills at the
	 * given table. If the bill is found and not already paid, it is marked as paid,
	 * copied into the closedTabs list, and the tip is added to the server's total.
	 * After checking all bills at the table, if all are paid, the table is removed
	 * from the server.
	 * 
	 * @returns: boolean -> True if the target bill was found (and processed if
	 * unpaid), false otherwise.
	 */
	public boolean closeCheck(Table table, int billNum) {

		if (!checkHashStandards(table)) {
			return false;
		}

		ArrayList<Bill> bList = this.tableMap.get(table);

		boolean allBillsPaid = true;
		boolean billFound = false;

		for (Bill bill : bList) {
			// Bill is paid
			if (bill.getName().equals("bill" + billNum)) {
				if (!bill.getPaid()) { // Bill was already closed.
					bill.setPaid();
					this.closedTabs.add(bill.makeCopy());
					this.addTip(bill.getTip());
				}
				billFound = true;
			}
			// One of the Bills at table aren't paid
			if (!bill.getPaid()) {
				allBillsPaid = false;
			}
		}
		if (allBillsPaid) {
			closeTable(table);
		}
		return billFound;
	}

	/*
	 * Adds an item to a customer's bill.
	 * 
	 * @arguments: Table table -> Which Table to order at. int billNum -> The bill
	 * to add the item onto. String item -> the food/drink item to be purchased and
	 * added to Bill.
	 * 
	 * @returns: Boolean -> True if Table, bill, and item was found and affected the
	 * bill. False if not.
	 * 
	 */
	public boolean addItemToBill(Table table, int billNum, String item) {

		if (!checkHashStandards(table)) {
			return false;
		}

		ArrayList<Bill> bList = this.tableMap.get(table);

		for (Bill bill : bList) {
			if (bill.getName().equals("bill" + billNum)) {
				double prevAmount = bill.getTotal();
				bill.addItem(item);
				// if Total didn't change -> return false: item wasn't on the menu.
				if (!(prevAmount < bill.getTotal())) {
					return false;
				}
				return true; // item was found.
			}
		}

		return false; // Couldn't find the bill at any table.
	}

	/*
	 * Adds a tip amount to a specific bill at a given table.
	 * 
	 * @arguments: Table table -> The table associated with the bill to tip. int
	 * billNum -> The specific bill number to apply the tip to. double tip -> The
	 * tip amount to be added. Must be greater than 0.
	 * 
	 * @returns: boolean -> True if the bill was found and the tip was successfully
	 * set. False if the table or bill was not found.
	 */
	public boolean addTipToBill(Table table, int billNum, double tip) {
		// No point in checking, shouldn't update if less than or equal to 0.
		if (tip <= 0.0) {
			return true;
		}

		if (!checkHashStandards(table)) {
			return false;
		}

		ArrayList<Bill> bList = this.tableMap.get(table);

		for (Bill bill : bList) {
			if (bill.getName().equals("bill" + billNum)) {
				bill.setTip(tip);
				return true;
			}
		}
		return false; // Bill couldn't be Found.
	}

	/*
	 * Validates that the server has tables and that the specified table exists in
	 * the map.
	 * 
	 * @arguments: Table table -> The table to check for in the server's tableMap.
	 * 
	 * @returns: boolean -> True if the server has tables and the table exists.
	 * False if no tables exist or the table is not assigned to the server.
	 */
	private boolean checkHashStandards(Table table) {
		// Server doesn't have any Tables.
		if (this.tableMap.size() == 0) {
			return false;

			// Table does not belong to Server.
		} else if (!this.tableMap.containsKey(table)) {
			return false;
		} else {
			return true;
		}
	}

	/*
	 * Overridden toString Method
	 * 
	 * @returns: name with amount of tables.
	 */
	@Override
	public String toString() {
		return this.name + " has " + this.tableMap.size() + " Tables Total.\n" + this.name + " has made "
				+ this.totalTip + " in Tips.";
	}

}