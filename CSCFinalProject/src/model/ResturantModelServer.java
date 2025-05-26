package model;

import java.util.ArrayList;

public class ResturantModelServer {
	/* INSTANCE VARIABLES */
	private Management management;
	private Server server; // current server logged in
	private Menu menu;

	public ResturantModelServer(Management mgmt, String username) {
		menu = new Menu();
		management = mgmt;
		this.server = management.getServer(username);
	}

	// seat a table
	public String seatTable(Table table, int numPeople) {
		// num people has to be more than 0>
		if (numPeople <= 0) {
			return("ERROR! Must seat at least 1 person\n");
		}
		// seat if table isn't taken
		if (!management.isTaken(table)) {
			management.seatTable(table); // seat the table
			server.addTable(table, numPeople); // add table to server tables
			return ("Table number " + table.getTableNumber() + " was seated successfully\n");
		}
		// table was already taken
		return ("ERROR! Table number " + table.getTableNumber() + " is already sat please pick a different table\n");
	}

	// free a table
	public String freeTable(Table table) {

		// make sure table is taken
		if (management.isTaken(table)) {

			// if server doesn't have table that means table is fully paid
			if (!server.hasTable(table)) {
				management.freeTable(table);
				return ("Table number " + table.getTableNumber() + " was freed successfully\n");
			}

			// if server still has table that means all the bills at the table are not paid
			if (server.hasTable(table)) {
				return ("ERROR! Table number " + table.getTableNumber() + " still has open bills!");
			}
		}

		// if table is not taken there is nothing to free
		return ("ERROR! Table number " + table.getTableNumber() + " was never seated.\n");
	}

	// take an order
	public String takeOrder(Table table, int billNum, String item) {
		// table was not one of server's tables
		if (checkTable(table) != null) {
			return checkTable(table);
		}
		// item does not exist on menu
		if (menu.getAllItems().get(item) == null) {
			return ("ERROR! Cannot place order, " + item + " is not on our menu\n");
		}
		// bill does not exist for table
		if (checkBill(table, billNum) != null) {
			return checkBill(table, billNum);
		}
		// table, item, and bill exist, now add item to bill
		server.addItemToBill(table, billNum, item);
		return ("Order placed!\n" + item + " was added to bill " + billNum + " at table " + table.getTableNumber()
				+ "\n");
	}

	// print bill string based on paying individual or split between table
	public String printBill(Table table) {
		String output = "";
		ArrayList<Bill> bills = server.getTableBills(table);
		int counter = 1;
		for (Bill b : bills) {
			output += "Person: " + counter + "\n" + b.printItemsOnly() + "\n";
			counter++;
		}
		return output;
	}

	public String printBillWhenPaying(Table table, int pay, int billNum) {
		String output = "";
		ArrayList<Bill> bills = server.getTableBills(table);
		if (pay == 0) {
			for (Bill b : bills) {
				if (b.getName().equals("bill" + billNum)) {
					output += "Person: " + billNum + "\n" + b.toString() + "\n";
					return output;
				}
			}
			return null;
		} else {
			return Bill.splitString(bills, bills.size());
		}

	}

	// add a tip to an individual bill
	public String addTipIndividual(Table table, int billNum, double tip) {
		// table was not one of server's tables
		if (checkTable(table) != null) {
			return checkTable(table);
		}
		// bill does not exist for table
		if (checkBill(table, billNum) != null) {
			return checkBill(table, billNum);
		}
		// bill and table found, add tip double to bill
		server.addTipToBill(table, billNum, tip);
		return ("Tip " + tip + " was added to Bill Number " + billNum + "\n");
	}

	// pay a bill individually
	public String payBillIndividual(Table table, int billNum) {
		// table was not one of server's tables
		if (checkTable(table) != null) {
			return checkTable(table);
		}
		// bill does not exist for table
		if (checkBill(table, billNum) != null) {
			return checkBill(table, billNum);
		}
		// bill table found, pay and close individual bill
		server.closeCheck(table, billNum);
		management.addBill();
		return ("Bill Number " + billNum + " was successfully paid\n");
	}

	// add a tip to table bill
	public String addTipTable(Table table, double tip) {
		// table was not one of server's tables
		if (checkTable(table) != null) {
			return checkTable(table);
		}
		// find how many people are at table
		int numPeople = server.getTableBills(table).size();
		// find tip per person
		double tipPerPerson = tip / numPeople;
		// add tip to all individual bills so when they are closed, tip gets added to
		// server tip total
		for (int i = 1; i <= numPeople; i++) {
			addTipIndividual(table, i, tipPerPerson);
		}
		return ("Tip " + tip + " was added to the bill for table number " + table.getTableNumber() + "\n");
	}

	// pay a table total bill
	public String payBillTable(Table table) {
		// table was not one of server's tables
		if (checkTable(table) != null) {
			return checkTable(table);
		}
		server.closeTableChecks(table);
		management.addBill();
		return ("Table Number: " + table.getTableNumber() + " was successfully fully paid\n");
	}

	public String serverTips() {
		return ("You have made $" + String.format("%.2f", server.getTotalTip()) + " in tips!\n");
	}

	public Management getMangement() {
		return management;
	}

	public String checkTable(Table table) {
		// table was not one of server's tables
		if (!server.hasTable(table)) {
			return ("ERROR! you do not have table number: " + table.getTableNumber() + "\n");
		}
		return null;
	}

	public String checkBill(Table table, int billNum) {
		// bill does not exist for table
		if (!server.hasBill(table, billNum)) {
			return ("ERROR! table number " + table.getTableNumber() + " does not have a bill by the name " + billNum
					+ "\n");
		}
		return null;
	}
}
