package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Map;

import model.*;
import model.Menu;

public class TakeOrderFrame extends JFrame {

    private ResturantModelServer serverModel;
    private String currentUser;
    private Table table;
    private int billNum;
    private Runnable returnCallback;
    private JTextArea billPreviewArea;
    private Menu menu;

    public TakeOrderFrame(String user, ResturantModelServer serverModel, Table table, int billNum, Runnable returnCallback) {
        this.currentUser = user;
        this.serverModel = serverModel;
        this.table = table;
        this.billNum = billNum;
        this.returnCallback = returnCallback;
        this.menu = new Menu();

        setTitle("Restaurant Management - Server: " + user);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Bill Preview
        billPreviewArea = new JTextArea(5, 30);
        billPreviewArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(billPreviewArea);
        JPanel billPreviewPanel = new JPanel(new BorderLayout());
        billPreviewPanel.add(scrollPane, BorderLayout.CENTER);

        // Menu Buttons
        JPanel menuPanel = new JPanel(new GridLayout(0, 1));
        for (String category : new String[]{"Drinks", "Appetizers", "Entrees", "Sides", "Desserts", "Modifications"}) {
            JButton categoryBtn = new JButton(category);
            categoryBtn.addActionListener(e -> displayItemsForCategory(category));
            menuPanel.add(categoryBtn);
        }

        // Done Button
        JButton doneButton = new JButton("Done Taking Order");
        doneButton.addActionListener(e -> {
            if (returnCallback != null) returnCallback.run();
            dispose();
        });

        mainPanel.add(menuPanel, BorderLayout.WEST);
        mainPanel.add(billPreviewPanel, BorderLayout.CENTER);
        mainPanel.add(doneButton, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private void displayItemsForCategory(String category) {
        JPanel itemsPanel = new JPanel(new GridLayout(0, 1));
        Map<String, Double> items = getCategoryItems(category);

        for (Map.Entry<String, Double> entry : items.entrySet()) {
            String item = entry.getKey();
            double cost = entry.getValue();
            JButton itemButton = new JButton(item + " - $" + cost);
            itemButton.addActionListener(e -> takeOrder(item));
            itemsPanel.add(itemButton);
        }

        JFrame itemsFrame = new JFrame(category + " Items");
        itemsFrame.setSize(300, 400);
        itemsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        itemsFrame.add(new JScrollPane(itemsPanel));
        itemsFrame.setLocationRelativeTo(this);
        itemsFrame.setVisible(true);
    }

    private Map<String, Double> getCategoryItems(String category) {
        switch (category) {
            case "Drinks": return menu.getDrinks();
            case "Appetizers": return menu.getAppetizers();
            case "Entrees": return menu.getEntrees();
            case "Sides": return menu.getSides();
            case "Desserts": return menu.getDesserts();
            case "Modifications": return menu.getModifications();
            default: return null;
        }
    }

    private void takeOrder(String item) {
        String message = serverModel.takeOrder(table, billNum, item);
        billPreviewArea.append(message + "\n");
    }
}

