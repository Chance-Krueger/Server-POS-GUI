import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import model.User;
import model.User.Question;
import model.UserDatabase;

class UserTest {
	private User user = new User("username", "hashedPassword", "salt", "hashedSecurityAnswer", Question.ONE, 2);
	@Test
	void testConstructors() {
		User user1 = new User("username", "password", "securityAnswer", Question.ONE);
		User user2 = new User("username", "hashedPassword", "salt", "hashedSecurityAnswer", Question.ONE, 2);
	}
	
	@Test 
	void testGetUsername() {
		assertEquals(user.getUsername(), "username");
	}
	
	@Test
	void testGetSalt() {
		assertEquals(user.getSalt(), "salt");
	}

	@Test
	void testGetHashedPassword() {
		assertEquals(user.getHashedPassword(), "hashedPassword");
	}
	
	@Test
	void testGetLoginAttempts() {
		assertEquals(user.getLoginAttempts(), 2);
	}
	
	@Test
	void testGetHashedSecurityAnswer() {
		assertEquals(user.getHashedSecurityAnswer(), "hashedSecurityAnswer");
	}
	
	@Test
	void testGetQuestion() {
		assertEquals(user.getQuestion(), Question.ONE);
	}
	
	@Test
	void testAddLoginAttempt() {
		user.addLoginAttempt();
		assertEquals(user.getLoginAttempts(), 3);
	}
	
	@Test
	void testSetLoginAttempts() {
		user.setLoginAttempts(0);
		assertEquals(user.getLoginAttempts(), 0);
	}
	
	@Test
	void testSetHashedPassword() {
		user.setHashedPassword("newHash");
		assertEquals(user.getHashedPassword(), "newHash");
	}
	
	@Test
	void testChangePassword() {
		user.changePassword("newPassword");
	}
	
	@Test
	void testGetValueQuestion() {
		Question question = Question.ONE;
		assertEquals(question.getValue(), "1");
	}
	
	@Test
	void testFromIntQuestion() {
		assertEquals(Question.fromInt(1), Question.ONE);
		assertThrows(IllegalArgumentException.class, () -> {
			Question.fromInt(6);
        });
	}
	
	@Test
	void testFromString() {
		assertEquals(Question.fromString("1"), Question.ONE);
		assertThrows(IllegalArgumentException.class, () -> {
			Question.fromString("6");
        });
	}
}