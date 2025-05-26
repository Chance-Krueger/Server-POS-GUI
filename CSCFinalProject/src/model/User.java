package model;

import model.UserDatabase;

public class User {
	public enum Question {

		ONE(1), TWO(2), THREE(3), FOUR(4);
		
		private final int value;

	    // Constructor
	    Question(int value) {
	        this.value = value;
	    }

	    // getter to retrieve the integer value
	    public String getValue() {
	        return String.valueOf(value);
	    }
	    
	    // convert int to Enum
	    public static Question fromInt(int num) {
	        for (Question q : Question.values()) {
	            if (q.value == num) {
	                return q;
	            }
	        }
	        throw new IllegalArgumentException("Invalid Question number: " + num);
	    }
	    
	    // convert String to Enum
	    public static Question fromString(String num) {
	        for (Question q : Question.values()) {
	            if (q.getValue().equals(num)) {
	                return q;
	            }
	        }
	        throw new IllegalArgumentException("Invalid Question number: " + num);
	    }

	}
	
	// instance variables
	private String username;
	private String salt;
	private String hashedPassword;
	private int loginAttempts;
	private String hashedSecurityAnswer;
	private Question question;
	
	// User is a employee of the restaurant 
	public User(String username, String password, String securityAnswer, Question question) {
		this.username = username; 
		this.salt = UserDatabase.generateSalt();
		this.hashedPassword = UserDatabase.hashPassword(password, this.salt);
		this.loginAttempts = 0;
		this.hashedSecurityAnswer = UserDatabase.hashPassword(securityAnswer, this.salt);
		this.question = question;
	}
	
	public User(String username, String hashedPassword, String salt, String hashedSecurityAnswer, Question question, int loginAttempts) {
		this.username = username; 
		this.salt = salt;
		this.hashedPassword = hashedPassword;
		this.loginAttempts = loginAttempts;
		this.hashedSecurityAnswer = hashedSecurityAnswer;
		this.question = question;
	}

	public String getUsername() {
		return username;
	}
	
	public String getSalt() {
		return salt;
	}
	
	public String getHashedPassword() {
		return hashedPassword;
	}
	
	public int getLoginAttempts() {
		return loginAttempts;
	}
	
	public String getHashedSecurityAnswer() {
		return hashedSecurityAnswer;
	}
	
	public Question getQuestion() {
		return question;
	}
	
	public void addLoginAttempt() {
		this.loginAttempts += 1;
	}
	
	public void setLoginAttempts(int attempts) {
		this.loginAttempts = attempts;
	}
	
	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}
	
	public void changePassword(String plainTextPassword) {
		this.hashedPassword = UserDatabase.hashPassword(plainTextPassword, this.salt);
	}
}
