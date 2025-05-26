package view;

import model.Management;
import model.LoadRestaurantData;
import model.Menu;
import model.ResturantModelServer;
import model.SaveResturantData; //
import model.User;
import model.UserDatabase;
import model.User.Question;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {
	private CardLayout cardLayout;
	private JPanel cards;
	private UserDatabase db;
	private User currentUser;
	private Menu menu;
	private Management mgmt;

	public LoginFrame() {
		super("Restaurant Login");
		db = new UserDatabase();
		menu = new Menu();
		mgmt = LoadRestaurantData.loadManagerInstance();
		// System.out.println("FUCK");
		// mgmt = new Management(menu);
		// rms = new ResturantModelServer();
		initUI();
	}

	private void initUI() {
		cardLayout = new CardLayout();
		cards = new JPanel(cardLayout);
		cards.add(new LoginPanel(), "Login");
		cards.add(new RegisterPanel(), "Register");
		cards.add(new ResetPasswordPanel(), "ResetPassword");
		getContentPane().add(cards);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(450, 300);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/** Panel for existing users to log in */
	private class LoginPanel extends JPanel {
		private JTextField userField;
		private JPasswordField passField;
		private JButton loginBtn, regBtn;

		LoginPanel() {
			setLayout(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(5, 5, 5, 5);
			gbc.fill = GridBagConstraints.HORIZONTAL;
			// Username
			gbc.gridx = 0;
			gbc.gridy = 0;
			add(new JLabel("Username:"), gbc);
			gbc.gridx = 1;
			userField = new JTextField(15);
			add(userField, gbc);
			// Password
			gbc.gridx = 0;
			gbc.gridy = 1;
			add(new JLabel("Password:"), gbc);
			gbc.gridx = 1;
			passField = new JPasswordField(15);
			add(passField, gbc);
			gbc.gridx = 0;
			gbc.gridy = 2;
			gbc.gridwidth = 2;
			loginBtn = new JButton("Login");
			add(loginBtn, gbc);
			gbc.gridy = 3;
			regBtn = new JButton("Register");
			add(regBtn, gbc);
			// Actions
			loginBtn.addActionListener(e -> attemptLogin());
			regBtn.addActionListener(e -> cardLayout.show(cards, "Register"));
		}

		private void attemptLogin() {
			String u = userField.getText().trim();
			String p = new String(passField.getPassword());

			if ("management".equals(u) && "Manager!123".equals(p)) {
				JOptionPane.showMessageDialog(this, "Welcome, Manager!", "Management Login",
						JOptionPane.INFORMATION_MESSAGE);
				// Launch the ManagerFrame, then close this login window

				mgmt = LoadRestaurantData.loadManagerInstance();

				SwingUtilities.invokeLater(() -> new ManagerFrame(mgmt));
				SwingUtilities.getWindowAncestor(this).dispose();
				return; // skip the rest of authentication
			}
			// 1. If username NOT in DB, show error and RETURN immediately
			if (db.verifyUniqueUsername(u)) {
				JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			// 2. Authenticate ONLY existing users
			if (db.authenticate(u, p)) {
				currentUser = db.getUser(u);
				db.updateUser(u, currentUser);
				db.saveUsers(); // persist change
				JOptionPane.showMessageDialog(this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);

				mgmt = LoadRestaurantData.loadManagerInstance();

				ResturantModelServer model = new ResturantModelServer(mgmt, u); // MODIFY IDK IF THIS HAS TO BE NEW OR
																				// EXISTING
				// Launch the Server Options Frame after successful login
				SwingUtilities.invokeLater(() -> {
					new serverOptionsFrame(u, model);
				});
				dispose(); // close the login window
			} else {
				// Failed auth: increment ONLY for real users
				if (db.verifyUniqueUsername(u) == true) {
					JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed",
							JOptionPane.ERROR_MESSAGE);
					return;
				} else {
					currentUser = db.getUser(u);
					JOptionPane.showMessageDialog(this,
							"Invalid credentials. Attempt " + currentUser.getLoginAttempts() + "/3", "Login Failed",
							JOptionPane.ERROR_MESSAGE);
					if (currentUser.getLoginAttempts() >= 3) {
						cardLayout.show(cards, "ResetPassword");
					}
					db.saveUsers();
				}

			}

			// Clear the form fields
			userField.setText("");
			passField.setText("");
		}
	}

	/** Panel for new user registration */
	private class RegisterPanel extends JPanel {
		private JTextField userField, ansField;
		private JPasswordField passField;
		private JComboBox<String> qBox;
		private JButton createBtn, backBtn;

		RegisterPanel() {
			setLayout(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(5, 5, 5, 5);
			gbc.fill = GridBagConstraints.HORIZONTAL;
			// Username
			gbc.gridx = 0;
			gbc.gridy = 0;
			add(new JLabel("Choose Username:"), gbc);
			gbc.gridx = 1;
			userField = new JTextField(15);
			add(userField, gbc);
			// Password
			gbc.gridx = 0;
			gbc.gridy = 1;
			add(new JLabel("Choose Password:"), gbc);
			gbc.gridx = 1;
			passField = new JPasswordField(15);
			add(passField, gbc);
			// Security Question
			gbc.gridx = 0;
			gbc.gridy = 2;
			add(new JLabel("Security Question:"), gbc);
			gbc.gridx = 1;
			qBox = new JComboBox<>(new String[] { "1. Street you grew up on", "2. Mother's maiden name",
					"3. First pet's name", "4. Childhood best friend" });
			add(qBox, gbc);
			// Answer
			gbc.gridx = 0;
			gbc.gridy = 3;
			add(new JLabel("Your Answer:"), gbc);
			gbc.gridx = 1;
			ansField = new JTextField(15);
			add(ansField, gbc);
			// Buttons
			gbc.gridx = 0;
			gbc.gridy = 4;
			createBtn = new JButton("Create Account");
			add(createBtn, gbc);
			gbc.gridx = 1;
			backBtn = new JButton("Back");
			add(backBtn, gbc);
			createBtn.addActionListener(e -> handleRegistration());
			backBtn.addActionListener(e -> cardLayout.show(cards, "Login"));
		}

		private void handleRegistration() {
			String u = userField.getText().trim();
			String p = new String(passField.getPassword());
			int qIndex = qBox.getSelectedIndex() + 1;
			String a = ansField.getText().trim();
			// Check username uniqueness
			if (!db.verifyUniqueUsername(u)) {
				JOptionPane.showMessageDialog(this, "Username already exists.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			// === Enforce strong password via loop ===
			while (!db.verifyStrongPassword(p)) {
				p = JOptionPane.showInputDialog(this,
						"Password must:\n" + "- Be at least 8 characters long\n"
								+ "- Contain at least one uppercase letter\n" + "- Contain at least one digit\n"
								+ "- Contain at least one special character",
						"Choose Password", JOptionPane.WARNING_MESSAGE);
				if (p == null) {
					// User canceled; return to Login card without registering
					return;
				}
			}
			// Proceed once a valid password is entered
			currentUser = new User(u, p, a, Question.fromInt(qIndex));

			db.addUser(currentUser);
			mgmt.addServer(u);
			SaveResturantData save = new SaveResturantData(mgmt);

			JOptionPane.showMessageDialog(this, "Account created! You may now log in.", "Success",
					JOptionPane.INFORMATION_MESSAGE);
			// Clear the form fields
			userField.setText(""); // clear username :contentReference[oaicite:2]{index=2}
			passField.setText(""); // clear password field :contentReference[oaicite:3]{index=3}
			qBox.setSelectedIndex(0); // reset security-question dropdown :contentReference[oaicite:4]{index=4}
			ansField.setText(""); // clear security-answer field :contentReference[oaicite:5]{index=5}
			cardLayout.show(cards, "Login");
		}
	}

	/** Panel for resetting a locked‐out password */
	private class ResetPasswordPanel extends JPanel {
		private JLabel prompt;
		private JTextField ansField;
		private JPasswordField newPassField;
		private JButton submitBtn, cancelBtn;

		ResetPasswordPanel() {
			setLayout(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(5, 5, 5, 5);
			gbc.fill = GridBagConstraints.HORIZONTAL;
			// Security Q prompt
			prompt = new JLabel();
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.gridwidth = 2;
			add(prompt, gbc);
			gbc.gridwidth = 1;
			// Answer
			gbc.gridx = 0;
			gbc.gridy = 1;
			add(new JLabel("Answer:"), gbc);
			gbc.gridx = 1;
			ansField = new JTextField(15);
			add(ansField, gbc);
			// New Password
			gbc.gridx = 0;
			gbc.gridy = 2;
			add(new JLabel("New Password:"), gbc);
			gbc.gridx = 1;
			newPassField = new JPasswordField(15);
			add(newPassField, gbc);
			// Buttons
			gbc.gridx = 0;
			gbc.gridy = 3;
			submitBtn = new JButton("Submit");
			add(submitBtn, gbc);
			gbc.gridx = 1;
			cancelBtn = new JButton("Cancel");
			add(cancelBtn, gbc);
			submitBtn.addActionListener(e -> attemptReset());
			cancelBtn.addActionListener(e -> {
				currentUser.setLoginAttempts(0);
				db.saveUsers();
				cardLayout.show(cards, "Login");
			});
		}

		@Override
		public void setVisible(boolean aFlag) {
			super.setVisible(aFlag);
			if (aFlag && currentUser != null) {
				// Update prompt with chosen security question
				String qv = currentUser.getQuestion().getValue();
				switch (qv) {
				case "1":
					prompt.setText("What street did you grow up on?");
					break;
				case "2":
					prompt.setText("What is your mother's maiden name?");
					break;
				case "3":
					prompt.setText("What was your first pet's name?");
					break;
				default:
					prompt.setText("Childhood best friend?");
					break;
				}
			}
		}

		private void attemptReset() {
			String ans = ansField.getText().trim();
			if (!db.authenticateSecurityQuestion(currentUser.getUsername(), ans)) {
				JOptionPane.showMessageDialog(this, "Incorrect answer.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			String np = new String(newPassField.getPassword());
			if (!db.verifyStrongPassword(np)) {
				JOptionPane.showMessageDialog(this,
						"Password must be ≥8 chars, include uppercase,\n" + "number & special char.", "Weak Password",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			currentUser.changePassword(np);
			db.updateUser(currentUser.getUsername(), currentUser);
			db.saveUsers();
			JOptionPane.showMessageDialog(this, "Password reset successful!", "Success",
					JOptionPane.INFORMATION_MESSAGE);
			cardLayout.show(cards, "Login");
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(LoginFrame::new);
	}
}
