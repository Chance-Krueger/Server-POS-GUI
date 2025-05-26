package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import model.Table;

public class LoadRestaurantData {

	public static Management loadManagerInstance() {

		Menu menu = new Menu();
		Management manager = new Management(menu);
		File file = new File("DataFile.txt");
		Scanner scanner;
		ArrayList<Bill> billsPaid = new ArrayList<Bill>();
		int check = 0;
		try {
			scanner = new Scanner(file);

			if (!scanner.hasNext()) {
				return manager;
			}

			boolean checkLine = false;
			scanner.nextLine();
			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				String temp = line;
				if (line.equals("BillsPaid:")) {
					check = 0;
				} else if (line.equals("FrequencyCount:")) {
					check = 1;
				} else if (line.equals("AllTables:")) {
					check = 2;
				} else if (line.equals("SERVER DATA:")) {
					check = 3;
				}
				if (check == 0) {
					if (line.equals("BillsPaid:")) {
						line = scanner.nextLine();
					}
					if (!line.equals("FrequencyCount:")) {
						String[] billArray = line.split(";");
						// POPULATE BILL
						Bill bill = new Bill(billArray[1], billArray[0]);
						bill.setTip(Double.parseDouble(billArray[2]));
						for (int i = 3; i < billArray.length; i += 1) {
							String[] split = billArray[i].split(",");
							for (int amount = 0; amount < Integer.parseInt(split[1]); amount++) {
								bill.addItem(split[0]);
							}
						}
						billsPaid.add(bill);
						// BillsPaid was an empty Line, don't want to get errors.
					} else {
						checkLine = true;
						check = 1;
					}
				} else if (check == 1 || checkLine) {
					if (!checkLine) {
						line = scanner.nextLine();
					}
					HashMap<String, Integer> freqCount = new HashMap<String, Integer>();
					String[] billArray = line.split(";");
					for (String cur : billArray) {
						String[] split = cur.split(",");
						freqCount.put(split[0], Integer.parseInt(split[1]));
					}
					manager.setFrequencyCount(freqCount);
					checkLine = false;
				} else if (check == 2) {
					HashMap<Table, Boolean> allTables = new HashMap<Table, Boolean>();
					String mapLine = scanner.nextLine();
					String[] mapArray = mapLine.split(";");
					for (String group : mapArray) {
						String[] split = group.split(",");
						allTables.put(Table.valueOf(split[0]), Boolean.valueOf(split[1]));
					}
					manager.setAllTables(allTables);
				} else if (check == 3) {
					// FILL SERVERS AND FILL INTO MANAGER
					// SHOULD BE FOR EACH SERVER
					if (line.equals("SERVER DATA:")) {
						line = scanner.nextLine();
					}
					if (line.equals("Server:")) {
						line = scanner.nextLine();
					}
					boolean flag = true;
					String[] strArray = line.split(";");
					String serverName = strArray[0];
					String servertip = strArray[1];
					manager.addServer(serverName);
					Server server = manager.getServer(serverName);
					server.addTip(Double.parseDouble(servertip));
					line = scanner.nextLine();
					while (flag) {
						if (line.equals("BILLS:")) {
							line = scanner.nextLine();
						}
						ArrayList<Bill> billList = new ArrayList<Bill>();

						// NEVER ENTERS INTO THE WHILE LOOP
						while (!line.equals("TABLE HASHMAP:") && !line.isBlank()) {
							String[] billArray = line.split(";");
							Bill bill = new Bill(billArray[1], billArray[0]);
							bill.setPaid();
							bill.setTip(Double.parseDouble(billArray[2]));
							for (int i = 4; i < billArray.length; i++) {
								String[] split = billArray[i].split(",");
								String item = split[0];
								Integer amount = Integer.parseInt(split[1]);
								for (int j = 0; j < amount; j++) {
									bill.addItem(item);
								}
							}
							billList.add(bill);
							line = scanner.nextLine();
						}
						HashMap<Table, ArrayList<Bill>> hm = new HashMap<Table, ArrayList<Bill>>();
						while (!line.equals("Server:") && !(line.isBlank() || line.isEmpty())) {
							if (line.equals("TABLE HASHMAP:")) {
								if (!scanner.hasNext()) {
									flag = false;
									check = 4;
									break;
								}
								line = scanner.nextLine();
								continue;
							}
							String[] hashArray = line.split(";");
							String[] tableAndServer = hashArray[0].split(",");
							Table table = Table.valueOf(tableAndServer[0]);
							ArrayList<Bill> bList = new ArrayList<Bill>();
							int i = 1;
							while (i < hashArray.length) {
								if (hashArray[i].equals(tableAndServer[1])) {
									i++;
								}
								Bill bill = new Bill(hashArray[i], hashArray[i - 1]);
								bill.setTip(Double.parseDouble(hashArray[i + 1]));
								if (Boolean.parseBoolean(hashArray[i + 2])) {
									bill.setPaid();
								}
								i += 3;
								while (i < hashArray.length && !hashArray[i].equals(tableAndServer[1])) {
									String[] split = hashArray[i].split(",");
									String item = split[0];
									Integer amount = Integer.parseInt(split[1]);
									for (int j = 0; j < amount; j++) {
										bill.addItem(item);
									}
									i++;
								}
								bList.add(bill);
							}
							hm.put(table, bList);

							if (scanner.hasNext()) {

								line = scanner.nextLine();
							} else {
								check = 4;
								flag = false;
								break;
							}
						}
						server.setTableMap(hm);
						if (line.equals("Server:") || line.isBlank() || line.isEmpty()) {
							if (line.isBlank() || line.isEmpty()) {
								check = 4; // Should end loop
							}
							flag = false;
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		manager.setBillsPaid(billsPaid);
		return manager;
	}
}
