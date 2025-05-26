package model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map.Entry;

public class SaveResturantData {
	// This Class will save all the server data and manager data.
	// This should be called every time the user closes the application or needs to
	// switch users (server or management)
	private Management manager;

//	private String dataFile;
	public SaveResturantData(Management manager) {
		// NEED TO MAKE COPY CONTRUSTOR TO SAVE ENCAPSULATION
		this.manager = manager; // Has all the server info and management info
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("DataFile.txt"))) {
			saveManagementInfo(writer);
			saveAllServerInfo(writer);
		} catch (IOException e) {
			System.err.println("Error saving Management data: " + e.getMessage());
		}
	}

	private void saveManagementInfo(BufferedWriter writer) {
		try {
			writer.write("MANAGER DATA:\nBillsPaid:\n");
			for (Bill bill : this.manager.getBillsPaid()) {
				String sentence = "";
				sentence += (bill.getServerName() + ";" + bill.getName() + ";" + bill.getTip() + ";");
				for (Entry<String, Integer> entry : bill.getItems().entrySet()) {
					sentence += entry.getKey() + "," + entry.getValue() + ";";
				}
				writer.write(sentence + "\n");
			}
			String sentence = "";
			writer.write("FrequencyCount:\n");
			for (Entry<String, Integer> entry : this.manager.getFrequencyCount().entrySet()) {
				sentence += entry.getKey() + "," + entry.getValue() + ";";
			}
			sentence += "\n";
			writer.write(sentence);
			writer.write("AllTables:\n");
			sentence = "";
			for (Entry<Table, Boolean> entry : this.manager.getAllTables().entrySet()) {
				sentence += entry.getKey().toString() + "," + entry.getValue() + ";";
			}
			sentence += "\n";
			writer.write(sentence);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// WILL ITERATE THROUGH ALL OF SERVERS
	private void saveAllServerInfo(BufferedWriter writer) {
		try {
			writer.write("SERVER DATA:\n");
			for (Server s : this.manager.getAllServers()) {
				saveServerInfo(s, writer);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// HELPER FUNCTION
	private void saveServerInfo(Server server, BufferedWriter writer) {
		String sentence = "Server:\n" + server.getName() + ";" + server.getTotalTip() + ";\n" + "BILLS:\n";

		ArrayList<Bill> blist = server.getBillS();

		sentence += billHelperBuilder(blist, 1);
		sentence += "TABLE HASHMAP:\n";
		for (Entry<Table, ArrayList<Bill>> entry : server.getTableMap().entrySet()) {
			sentence += entry.getKey().toString() + "," + billHelperBuilder(entry.getValue()) + "\n";
		}
		try {
			writer.write(sentence);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String billHelperBuilder(ArrayList<Bill> billList) {
		String sentence = "";
		for (Bill bill : billList) {
			String serverName = bill.getServerName();
			if (serverName.contains(",")) {
				String[] split = serverName.split(",");
				serverName = split[1];
			}
			sentence += (serverName + ";" + bill.getName() + ";" + bill.getTip() + ";" + bill.getPaid()) + ";";
			for (Entry<String, Integer> entry : bill.getItems().entrySet()) {
				sentence += entry.getKey() + "," + entry.getValue() + ";";
			}
		}
		return sentence;
	}

	// Only difference is to add new Line.
	private String billHelperBuilder(ArrayList<Bill> billList, int check) {
		String sentence = "";
		for (Bill bill : billList) {
			String serverName = bill.getServerName();
			if (serverName.contains(",")) {
				String[] split = serverName.split(",");
				serverName = split[1];
			}
			sentence += (serverName + ";" + bill.getName() + ";" + bill.getTip() + ";" + bill.getPaid()) + ";";
			for (Entry<String, Integer> entry : bill.getItems().entrySet()) {
				sentence += entry.getKey() + "," + entry.getValue() + ";";
			}
			sentence += "\n";
		}
		return sentence;
	}
}
