package view;

import model.Bill;
import model.SaveResturantData;
import model.ResturantModelServer;
import model.Table;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public class serverOptionsFrame extends JFrame {

	private String username;
	private ResturantModelServer model;

	public serverOptionsFrame(String username, ResturantModelServer model) {
		super("Restaurant Simulator - Welcome, " + username);
		this.username = username;
		this.model = model;

		setSize(500, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new GridLayout(0, 1, 10, 10));
		setJMenuBar(createMenuBar());

		add(createButton("Seat Table", this::seatTable));
		// add(createButton("Free Table", this::freeTable));
		add(createButton("Take Order", this::takeOrder));
		add(createButton("Print Bill", this::printBill));
		// add(createButton("Add Tip", this::addTip));
		add(createButton("Pay Bill", this::payBill));
		add(createButton("Show Tips", this::showTips));

		setVisible(true);
	}

	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem logoutItem = new JMenuItem("Logout");

		logoutItem.addActionListener(e -> logout());
		fileMenu.add(logoutItem);
		menuBar.add(fileMenu);

		return menuBar;
	}

	private void logout() {
		SaveResturantData save = new SaveResturantData(model.getMangement());
		dispose(); // Close this frame

		// Show the login frame again
		SwingUtilities.invokeLater(() -> {
			// System.out.println("FUCK");
			new LoginFrame(); // 👈 Replace with your actual login frame class
		});
	}

	private JButton createButton(String text, Runnable action) {
		JButton button = new JButton(text);
		button.addActionListener(e -> action.run());
		return button;
	}

	private void seatTable() {
		try {
			int tableNum = Integer.parseInt(JOptionPane.showInputDialog("Enter Table Number:"));
			int people = Integer.parseInt(JOptionPane.showInputDialog("Enter Number of People:"));
			String message = model.seatTable(Table.getTable(tableNum), people);
			showMessage(message);
		} catch (Exception e) {
			showMessage("Invalid input: " + e.getMessage());
		}
	}

	private void freeTable(int tableNum) {
		try {
			// int tableNum = Integer.parseInt(JOptionPane.showInputDialog("Enter Table
			// Number to Free:"));
			String message = model.freeTable(Table.getTable(tableNum));
			// showMessage(message);
		} catch (Exception e) {
			showMessage("Invalid input.");
		}
	}

	private void takeOrder() {
		try {
			int tableNum = Integer.parseInt(JOptionPane.showInputDialog("Enter Table Number:"));
			int billNum = Integer.parseInt(JOptionPane.showInputDialog("Enter Bill Number:"));
			Table table = Table.getTable(tableNum);
			if(model.checkTable(table) != null){
				showMessage(model.checkTable(table));
				return;
			}
			if(model.checkBill(table,billNum) != null){
				showMessage(model.checkBill(table,billNum));
				return;
			}
			Map<Table, ArrayList<Bill>> bills = model.getMangement().getServer(username).getTableMap();
			int maxNumPeople = bills.get(table).size();
			if (model.checkTable(table) != null) {
				showMessage(model.checkTable(table));
				return;
			}
			if (model.checkBill(table, billNum) != null) {
				showMessage(model.checkBill(table, billNum));
				return;
			}
			if (billNum > maxNumPeople || billNum <= 0) {
				showMessage("Bill number must be between 1 and " + maxNumPeople);
				return;
			}
			this.setVisible(false);
			SwingUtilities.invokeLater(() -> {
				new TakeOrderFrame(username, model, table, billNum, () -> this.setVisible(true));
			});

		} catch (Exception e) {
			showMessage("Invalid input.");
		}
	}

	private void printBill() {
		try {
			int tableNum = Integer.parseInt(JOptionPane.showInputDialog("Enter Table Number:"));
			Table table = Table.getTable(tableNum);
			String message = model.printBill(table); // adjust if you support multiple bills
			showMessage(message);
			// If Cancel is selected, do nothing

		} catch (Exception e) {
			showMessage("Invalid input.");
		}
	}

	private void payBill() {
		try {
			int tableNum = Integer.parseInt(JOptionPane.showInputDialog("Enter Table Number:"));
			Table table = Table.getTable(tableNum);

			String[] options = { "View Split Bill", "View Individual Bill" };
			int choice = JOptionPane.showOptionDialog(this, "Choose how to split the bill:", "Split Bill Options",
				    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);

			if (choice == 1) {
				int billNum = Integer.parseInt(JOptionPane.showInputDialog("Enter Bill Number:"));
				String message = model.printBillWhenPaying(table, 0, billNum); // adjust if you support multiple bills
				showMessage(message);
				addTip(choice, tableNum, billNum);
				model.payBillIndividual(table, billNum);
				freeTable(tableNum);

			} else if (choice == 0) { // BILL IS SPLIT BY NUM PEOPLE
				String message = model.printBillWhenPaying(table, 1, 0);
				showMessage(message);
				addTip(choice, tableNum, 0);
				model.payBillTable(table);
				freeTable(tableNum);
			}
			// If Cancel is selected, do nothing

		} catch (Exception e) {
			showMessage("Invalid input.");
		}
	}

	private void addTip(int pay, int tableNum, int billNum) {
		try {
			double tip;

			String message;
			if (pay == 0) {
				tip = Double.parseDouble(JOptionPane.showInputDialog("Enter Tip Amount Per Person:"));
				// System.out.println(model.getMangement().getServer(username).getBillS().size());
				Map<Table, ArrayList<Bill>> bills = model.getMangement().getServer(username).getTableMap();
				int maxNumPeople = bills.get(Table.getTable(tableNum)).size();
				message = model.addTipTable(Table.getTable(tableNum), tip * maxNumPeople);
			} else {
				tip = Double.parseDouble(JOptionPane.showInputDialog("Enter Tip Amount:"));
				message = model.addTipIndividual(Table.getTable(tableNum), billNum, tip);
			}

			showMessage(message);
		} catch (Exception e) {
			showMessage("Invalid input.");
		}
	}

	private void showTips() {
		showMessage(model.serverTips());
	}

	private void showMessage(String msg) {
		JTextArea textArea = new JTextArea(msg);
		textArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setPreferredSize(new Dimension(400, 200));
		JOptionPane.showMessageDialog(this, scrollPane, "Output", JOptionPane.INFORMATION_MESSAGE);
	}
}
