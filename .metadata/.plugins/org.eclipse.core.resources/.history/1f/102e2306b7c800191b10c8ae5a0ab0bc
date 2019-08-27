package com.sapient.data;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.sapient.data.dao.DataDao;
import com.sapient.data.model.PasswordHistory;
import com.sapient.data.model.User;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
//@ContextConfiguration(classes = DataRetrievalServiceApplication.class)
public class DataDaoTests {
	@Autowired
	private DataDao dataDao;

	@Test
	public void testFindUserByUserID() {
		User expectedUserFromDataService = new User(1L, "abc@gmail.com", new PasswordHistory(2L, "$2a$12$2jDJzTrQ9UOP43LVEyrdweLBe10SA0csWa5EzsHlQm0suxaWv7UqS",
				"$2a$12$2jDJzTrQ9UOP43LVEyrdwe"));
		dataDao.save(expectedUserFromDataService);
		
		Optional<User> optional = dataDao.findUserByUserID(1L);
		
		assertNotNull(optional);
		assertEquals(true, optional.isPresent());
		
		User actualUserFromDataService = optional.get();

		// both pwd1 n salt r correct
		assertEquals(expectedUserFromDataService.getPasswordHistory().getPwd1(), actualUserFromDataService.getPasswordHistory().getPwd1());
		assertEquals(expectedUserFromDataService.getPasswordHistory().getSalt1(), actualUserFromDataService.getPasswordHistory().getSalt1());

		// expected pwd1 is different
		assertNotEquals("$2a$12$2jDJzTrQ9UOP43LVEyrdweLBe10SA0csWa5EzsHlQm0suxaWv7UqT", actualUserFromDataService.getPasswordHistory().getPwd1());

		// expected salt1 is different
		assertNotEquals("$2a$12$2jDJzTrQ9UOP43LVEyrdwf", actualUserFromDataService.getPasswordHistory().getSalt1());
	}

	@Test
	public void testFindUserByEmailID() {
		User expectedUserFromDataService = new User(1L, "abc@gmail.com", new PasswordHistory(2L, "$2a$12$2jDJzTrQ9UOP43LVEyrdweLBe10SA0csWa5EzsHlQm0suxaWv7UqS",
				"$2a$12$2jDJzTrQ9UOP43LVEyrdwe"));
		dataDao.save(expectedUserFromDataService);
		
		Optional<User> optional = dataDao.findUserByEmailID("abc@gmail.com");
		
		assertNotNull(optional);
		assertEquals(true, optional.isPresent());
		
		User actualUserFromDataService = optional.get();

		// both pwd1 n salt r correct
		assertEquals(expectedUserFromDataService.getPasswordHistory().getPwd1(), actualUserFromDataService.getPasswordHistory().getPwd1());
		assertEquals(expectedUserFromDataService.getPasswordHistory().getSalt1(), actualUserFromDataService.getPasswordHistory().getSalt1());

		// expected pwd1 is different
		assertNotEquals("$2a$12$2jDJzTrQ9UOP43LVEyrdweLBe10SA0csWa5EzsHlQm0suxaWv7UqT", actualUserFromDataService.getPasswordHistory().getPwd1());

		// expected salt1 is different
		assertNotEquals("$2a$12$2jDJzTrQ9UOP43LVEyrdwf", actualUserFromDataService.getPasswordHistory().getSalt1());
	}
	
	@Test
	public void testUpdatePassword() {
		User initialuser = new User(1L, "abc@gmail.com", new PasswordHistory(2L, "$2a$12$2jDJzTrQ9UOP43LVEyrdweLBe10SA0csWa5EzsHlQm0suxaWv7UqS",
				"$2a$12$2jDJzTrQ9UOP43LVEyrdwe"));
		dataDao.save(initialuser);
		PasswordHistory password = new PasswordHistory(2L, "$2a$12$2jDJzTrQ9UOP43LVEyrdweLBe10SA0csWa5EzsHlQm0suxaWv7UqT", "$2a$12$2jDJzTrQ9UOP43LVEyrdwf");
		int nRowsUpdated = dataDao.updatePassword(password.getPassId(), password.getPwd1(), password.getSalt1());
		assertNotEquals(0, nRowsUpdated);
		assertThat(nRowsUpdated > 0).isTrue();
		User finalUser = dataDao.findUserByUserID(1L).get();
		assertEquals(password.getPwd1(), finalUser.getPasswordHistory().getPwd1());
		assertEquals(password.getSalt1(), finalUser.getPasswordHistory().getSalt1());
	}

//	@Test
//	public void testGetEmailConfFlag() {
//		User user = new User(1L, "abc@gmail.com", new Password(2L, "$2a$12$2jDJzTrQ9UOP43LVEyrdweLBe10SA0csWa5EzsHlQm0suxaWv7UqS",
//				"$2a$12$2jDJzTrQ9UOP43LVEyrdwe"));
//		dataDao.save(user);
//		boolean flag = dataDao.getEmailConfFlag(user.getUserID());
//		assertEquals(true, flag);
//	}
}
