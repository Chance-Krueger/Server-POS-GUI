import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import model.User;
import model.User.Question;
import model.UserDatabase;

class UserDatabaseTest {
	UserDatabase db = new UserDatabase();
	
	@Test
	void testAddUser() {
		User user = new User("username", "password", "answer", Question.ONE);
		db.addUser(user);
	}
	
	@Test
	void testGetUser() {
		User user = new User("username", "password", "answer", Question.ONE);
		assertEquals(db.getUser("username").getUsername(), "username");
	}
	
	@Test
	void testUpdateUser() {
		User user = new User("username", "password", "answer", Question.ONE);
		db.updateUser("username", user);
		assertEquals(user.getUsername(), "username");
		assertEquals(user.getLoginAttempts(), 0);
		assertEquals(user.getHashedPassword(), UserDatabase.hashPassword("password", user.getSalt()));
	}
	
	@Test
	void testVerifyUniqueUsername() {
		User user = new User("username", "password", "answer", Question.ONE);
		assertTrue(db.verifyUniqueUsername("username2"));
		assertFalse(db.verifyUniqueUsername("username"));
	}
	
	@Test
	void testVerifyStrongPassword() {
		assertTrue(db.verifyStrongPassword("Password!123"));
		assertFalse(db.verifyStrongPassword("password"));
	}
	
	@Test
	void testAuthenticate() {
		User user = new User("username", "password", "answer", Question.ONE);
		assertTrue(db.authenticate("username", "password"));
		assertFalse(db.authenticate("un", "password"));
		assertFalse(db.authenticate("username", "wrongPassword"));
	}
	
	@Test
	void testAuthenticateSecurityQuestion() {
		User user = new User("username", "password", "answer", Question.ONE);
		assertTrue(db.authenticateSecurityQuestion("username", "answer"));	
		assertFalse(db.authenticateSecurityQuestion("username", "wrongAnswer"));
	}

}
