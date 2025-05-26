package view;

import javax.swing.*;

import model.Bill;
import model.Management;
import model.Server;
import model.Menu;

import java.awt.*;
import java.util.ArrayList;

public class ManagerFrame extends JFrame {
	private JButton btnServers;
	private JButton btnBills;
	private JButton btnMostOrdered;
	private JButton btnMostRevenue;
	private Management management;

	public ManagerFrame(Management mgmt) {
		super("Management Console");
		management = mgmt;
		initUI();
	}

	private void initUI() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(500, 400);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout(10, 10));

		setJMenuBar(createMenuBar());

		// Title
		JLabel lbl = new JLabel("Manager Dashboard", JLabel.CENTER);
		lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 18f));
		add(lbl, BorderLayout.NORTH);

		// Buttons panel
		JPanel p = new JPanel(new GridLayout(2, 2, 20, 20));
		btnServers = new JButton("See Servers");
		btnBills = new JButton("See Bills");
		btnMostOrdered = new JButton("Most Ordered Items");
		btnMostRevenue = new JButton("Highest Revenue Items");
		p.add(btnServers);
		p.add(btnBills);
		p.add(btnMostOrdered);
		p.add(btnMostRevenue);
		add(p, BorderLayout.CENTER);

		// Hook up actions
		btnServers.addActionListener(e -> showServers());
		btnBills.addActionListener(e -> showBills());
		btnMostOrdered.addActionListener(e -> showMostOrdered());
		btnMostRevenue.addActionListener(e -> showHighestRevenue());

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
		dispose(); // Close the manager frame

		// Show the login frame again
		SwingUtilities.invokeLater(() -> {
			new LoginFrame(); // 👈 Replace with your actual login frame class if it's named differently
		});
	}

	private void showServers() {
		ArrayList<Server> servers = management.getAllServers(); // get your server list

		// Create a list model and populate it with server names
		DefaultListModel<String> listModel = new DefaultListModel<>();
		listModel.addElement(management.getHighestTipCount());
		for (Server s : servers) {
			listModel.addElement(s.getName() + " Tips: " + s.getTotalTip());
		}

		// Create the list component
		JList<String> serverList = new JList<>(listModel);
		serverList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// Put the list in a scroll pane
		JScrollPane scrollPane = new JScrollPane(serverList);

		// Show in a basic pop-up frame
		JFrame serverWindow = new JFrame("Servers");
		serverWindow.add(scrollPane);
		serverWindow.setSize(300, 200);
		serverWindow.setLocationRelativeTo(null); // center on screen
		serverWindow.setVisible(true);
	}

	private void showBills() {
		ArrayList<Server> servers = management.getAllServers(); // get your server list
		// System.out.println(management.getAllServers().size());
		// System.out.println(servers.get(0).getName());
		// Create a list model and populate it with server names
		DefaultListModel<String> listModel = new DefaultListModel<>();
		for (Server s : servers) {
			listModel.addElement(s.getName() + " Bills: ");
			for (Bill b : management.getPaidBillsForServer(s.getName())) {
				listModel.addElement(b.toString());
			}
		}

		// Create the list component
		JList<String> serverList = new JList<>(listModel);
		serverList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// Put the list in a scroll pane
		JScrollPane scrollPane = new JScrollPane(serverList);

		// Show in a basic pop-up frame
		JFrame serverWindow = new JFrame("Bills");
		serverWindow.add(scrollPane);
		serverWindow.setSize(300, 200);
		serverWindow.setLocationRelativeTo(null); // center on screen
		serverWindow.setVisible(true);
	}

	private void showMostOrdered() {
		String mostOrdered = management.sortMostOrdered();

		JOptionPane.showMessageDialog(this, mostOrdered, "Most Ordered Items", JOptionPane.INFORMATION_MESSAGE);
	}

	private void showHighestRevenue() {
		String revenue = management.sortMostMoneyByItem();

		JOptionPane.showMessageDialog(this, revenue, "Most Ordered Items", JOptionPane.INFORMATION_MESSAGE);
	}
}
