package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.Comparator;

public class Management {
	private ArrayList<Bill> billsPaid;
	private ArrayList<Server> allServers;
	private HashMap<String, Integer> frequencyCount;
	private HashMap<Table, Boolean> allTables;
	private Map<String, Double> allItemPrices;

	// initialize lists and hashmap
	public Management(Menu menu) {
		this.billsPaid = new ArrayList<>();
		this.allServers = new ArrayList<>();
		this.frequencyCount = new HashMap<>();
		this.allTables = new HashMap<>();
		this.allItemPrices = menu.getAllItems();

		// add all menu items to frequencyCount
		for (String item : menu.getAllItems().keySet()) {
			frequencyCount.put(item, 0);
		}

		// set all tables to be open
		for (Table table : Table.values()) {
			this.allTables.put(table, false);
		}
	}

	public Map<String, Integer> getFrequencyCount() {
		return Collections.unmodifiableMap(this.frequencyCount);
	}

	public Map<Table, Boolean> getAllTables() {
		return Collections.unmodifiableMap(allTables);
	}

	// DEEP COPY
	public void setBillsPaid(ArrayList<Bill> billsPaid) {
		this.billsPaid = billsPaid;
	}

	// DEEP COPY
	public void setFrequencyCount(HashMap<String, Integer> frequencyCount) {
		this.frequencyCount = frequencyCount;
	}

	// DEEP COPY
	public void setAllTables(HashMap<Table, Boolean> allTables) {
		this.allTables = allTables;
	}

	// returns copy of billsPaid list
	public ArrayList<Bill> getBillsPaid() {
		return new ArrayList<Bill>(billsPaid);
	}

	// returns next available table
	public Table nextTable() {
		for (Table t : allTables.keySet()) {
			if (allTables.get(t) == false) {
				return t;
			}
		}
		return null;
	}

	// sets the table value in the hashmap to true (taken)
	public boolean seatTable(Table table) {
		if (allTables.containsKey(table) && !allTables.get(table)) {
			allTables.put(table, true);
			return true; // table was seated successfully
		}
		return false; // table is already occupied
	}

	// sets the table value in the hashmap to false (open)
	public boolean freeTable(Table tableNumber) {
		if (allTables.containsKey(tableNumber) && allTables.get(tableNumber)) {
			allTables.put(tableNumber, false);
			return true; // table is now open
		}
		return false; // table was not found or is already open
	}

	// checks if a table is taken or free for seating
	public boolean isTaken(Table tableNumber) {
		if (allTables.containsKey(tableNumber)) {
			return allTables.get(tableNumber);
		}
		return false;
	}

	// adds a new server to the allServers list
	public void addServer(String name) {
		for (Server s : allServers) {
			if (s.getName().equals(name)) {
				return; // server already exists don't add server
			}
		}
		allServers.add(new Server(name));
	}

	// gets a server from the allServers list
	public Server getServer(String name) {
		for (Server server : allServers) {
			if (server.getName().equals(name)) {
				return server;
			}
		}
		return null;
	}

	/*
	 * gets all the bills for specified server
	 */
	public ArrayList<Bill> getBillsForServer(String name) {
		for (Server server : allServers) {
			if (server.getName().equals(name)) {
				return server.getBillS();
			}
		}
		return null;
	}

	public ArrayList<Bill> getPaidBillsForServer(String name) {
		ArrayList<Bill> copy = new ArrayList<Bill>();
		for (Bill b : billsPaid) {
			if (b.getServerName().equals(name)) {
				copy.add(b);
			}
		}
		return copy;
	}

	/*
	 * returns the server with the highest tip count as a string
	 */
	public String getHighestTipCount() {
		if (allServers.size() == 0) {
			return null;
		}

		Server topServer = allServers.get(0); // make max server first in the list
		double maxTips = topServer.getTotalTip();

		for (int i = 1; i < allServers.size(); i++) {
			if (allServers.get(i).getTotalTip() > maxTips) {
				topServer = allServers.get(i);
				maxTips = allServers.get(i).getTotalTip();
			}
		}

		return topServer.getName() + " has the highest tip count: " + maxTips;
	}

	/*
	 * adds the newly paid bill to billsPaid list and updates the count of the menu
	 * items
	 */
	public void addBill() {
		for (Server server : allServers) {
			ArrayList<Bill> billList = server.getBillS();
			for (Bill bill : billList) {
				HashMap<String, Integer> items = bill.getItems();
				for (String item : items.keySet()) {
					if (frequencyCount.containsKey(item)) {
						int updateCount = frequencyCount.get(item) + items.get(item);
						frequencyCount.put(item, updateCount);
					}
				}
				billsPaid.add(bill);
			}
		}
	}

	public void addBillPaid(ArrayList<Bill> bills) {
		for (Bill b : bills) {
			billsPaid.add(b);
		}
	}

	public String sortMostOrdered() {
		// store an array list of key and value pairs
		ArrayList<HashMap.Entry<String, Integer>> itemList = new ArrayList<>(frequencyCount.entrySet());

		Collections.sort(itemList, new Comparator<HashMap.Entry<String, Integer>>() {
			@Override
			public int compare(HashMap.Entry<String, Integer> e1, HashMap.Entry<String, Integer> e2) {
				return e2.getValue() - e1.getValue();
			}
		});

		String mostOrderedItems = "Most Frequently Ordered Items: \n";
		for (HashMap.Entry<String, Integer> item : itemList) {
			mostOrderedItems += item.getKey() + ": " + item.getValue() + "\n";
		}

		return mostOrderedItems;
	}

	private HashMap<String, Double> getAmountMadePerItem() {
		HashMap<String, Double> allItems = new HashMap<String, Double>();

		for (String item : frequencyCount.keySet()) {
			allItems.put(item, frequencyCount.get(item) * allItemPrices.get(item));
		}
		return allItems;
	}

	public String sortMostMoneyByItem() {
		HashMap<String, Double> allTotals = getAmountMadePerItem();

		ArrayList<HashMap.Entry<String, Double>> itemList = new ArrayList<>(allTotals.entrySet());

		Collections.sort(itemList, new Comparator<HashMap.Entry<String, Double>>() {
			@Override
			public int compare(HashMap.Entry<String, Double> e1, HashMap.Entry<String, Double> e2) {
				return Double.compare(e2.getValue(), e1.getValue());
			}
		});

		StringBuilder result = new StringBuilder("Items sorted by total revenue:\n");
		for (HashMap.Entry<String, Double> item : itemList) {
			result.append(item.getKey()).append(": $").append(String.format("%.2f", item.getValue())).append("\n");
		}

		return result.toString();
	}

	public ArrayList<Server> getAllServers() {
		ArrayList<Server> copy = new ArrayList<Server>();
		for (Server s : allServers) {
			copy.add(s);
		}
		return copy;
	}

}