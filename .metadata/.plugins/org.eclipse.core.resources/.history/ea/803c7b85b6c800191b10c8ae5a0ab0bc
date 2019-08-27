package com.sapient.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.sapient.data.dao.DataDao;
import com.sapient.data.model.PasswordHistory;
import com.sapient.data.model.User;
import com.sapient.data.service.DataService;

@RunWith(SpringRunner.class)
// @SpringBootTest // don't add this annot. as it is causing error of creating data service bean (duplicate beans...)
public class DataServiceTests {
	@TestConfiguration
	static class DataServiceTestContextConfiguration {

		@Bean
		public DataService dataService() {
			return new DataService();
		}
	}

	@Autowired
	private DataService daoService;

	@MockBean
	private DataDao dataDao;

	// Case1:
	// User exists
	// Returns: User in optional
	
	@Test
	public void testGetUserCase1() {
		User expectedUserFromDataService = new User(1L, "abc@gmail.com", new PasswordHistory(2L, "$2a$12$2jDJzTrQ9UOP43LVEyrdweLBe10SA0csWa5EzsHlQm0suxaWv7UqS",
				"$2a$12$2jDJzTrQ9UOP43LVEyrdwe"));
		
		Mockito.when(dataDao.findUserByUserID(expectedUserFromDataService.getUserID())).thenReturn(Optional.ofNullable(expectedUserFromDataService));

		User userFromOtherService = new User();
		userFromOtherService.setUserID(1L);

		Optional<User> optional = daoService.getUser(userFromOtherService);
		
		assertNotNull(optional);
		
		User actualUserFromDataService = optional.get();

		assertEquals(expectedUserFromDataService.getPasswordHistory().getPwd1(), actualUserFromDataService.getPasswordHistory().getPwd1());
		assertEquals(expectedUserFromDataService.getPasswordHistory().getSalt1(), actualUserFromDataService.getPasswordHistory().getSalt1());
	}

	// Case2:
	// User doesn't exist
	// Returns: empty optional

	@Test
	public void testGetUserCase2() {
		User userFromOtherService = new User();
		userFromOtherService.setUserID(10L);
		
		Mockito.when(dataDao.findUserByUserID(10L)).thenReturn(Optional.empty());

		Optional<User> optional = daoService.getUser(userFromOtherService);

		assertEquals(false, optional.isPresent());
	}

	// Case1:
	// new record is different from old record
	// Returns: 1 (#records updated)

	@Test
	public void testChangePasswordCase1() {
		PasswordHistory password = new PasswordHistory(2L, "$2a$12$2jDJzTrQ9UOP43LVEyrdweLBe10SA0csWa5EzsHlQm0suxaWv7UqS",
				"$2a$12$2jDJzTrQ9UOP43LVEyrdwe");
		
		User user = new User(1L, "abc@gmail.com", password);
		
		Mockito.when(dataDao.updatePassword(password.getPassId(), password.getPwd1(), password.getSalt1())).thenReturn(1);

		int nRowsUpdated = daoService.changePassword(user);
		assertEquals(1, nRowsUpdated);
	}

	// Case2:
	// new record is same as old record
	// Returns: 0 (#records updated)

	@Test
	public void testChangePasswordCase2() {
		PasswordHistory password = new PasswordHistory(2L, "$2a$12$2jDJzTrQ9UOP43LVEyrdweLBe10SA0csWa5EzsHlQm0suxaWv7UqS",
				"$2a$12$2jDJzTrQ9UOP43LVEyrdwe");
		
		User user = new User(1L, "abc@gmail.com", password);
		
		Mockito.when(dataDao.updatePassword(password.getPassId(), password.getPwd1(), password.getSalt1())).thenReturn(0);

		int nRowsUpdated = daoService.changePassword(user);
		assertEquals(0, nRowsUpdated);
	}
}
