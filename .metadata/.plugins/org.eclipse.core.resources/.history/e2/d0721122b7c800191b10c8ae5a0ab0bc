package com.sapient.data;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.io.IOException;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sapient.data.model.PasswordHistory;
import com.sapient.data.model.User;
import com.sapient.data.service.DataService;

@RunWith(SpringRunner.class)
//@WebMvcTest //this, not @SpringBootTest
@SpringBootTest // this wont work if i add spring security in pom
public class DataControllerTests {

	@Autowired
	WebApplicationContext webApplicationContext;

	@MockBean
	private DataService dataService;

	private MockMvc mvc;

	public String mapToJson(Object obj) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(obj);
	}

	public <T> T mapFromJson(String json, Class<T> clazz) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(json, clazz);
	}

	@Before
	public void setUp() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	// Case1:
	// User exists
	// Returns: User in optional

	@Test
	public void testGetUserCase1() throws Exception {
		User expectedUserFromDataService = new User(1L, "abc@gmail.com", new PasswordHistory(2L,
				"$2a$12$2jDJzTrQ9UOP43LVEyrdweLBe10SA0csWa5EzsHlQm0suxaWv7UqS", "$2a$12$2jDJzTrQ9UOP43LVEyrdwe"));

		User userFromOtherService = new User();
		userFromOtherService.setUserID(1L);

		Mockito.when(dataService.getUser(userFromOtherService))
				.thenReturn(Optional.ofNullable(expectedUserFromDataService));

		MvcResult mvcResult = mvc.perform(post("/api/data/user").contentType(MediaType.APPLICATION_JSON)
				.content(mapToJson(userFromOtherService)).accept(MediaType.APPLICATION_JSON)).andReturn();

		int apiCallStatus = mvcResult.getResponse().getStatus();
		assertEquals(200, apiCallStatus);
		String responseBody = mvcResult.getResponse().getContentAsString();
//		Optional<User> optional = mapFromJson(responseBody, Optional.class);
//		
//		assertNotNull(optional);

		User actualUserFromDataService = mapFromJson(responseBody, User.class);

		assertEquals(expectedUserFromDataService.getPasswordHistory().getPwd1(),
				actualUserFromDataService.getPasswordHistory().getPwd1());
		assertEquals(expectedUserFromDataService.getPasswordHistory().getSalt1(),
				actualUserFromDataService.getPasswordHistory().getSalt1());

		// later try using mockRest
	}

	// Case2:
	// User doesn't exist
	// Returns: null

	@Test
	public void testGetUserCase2() throws Exception {
		User userFromOtherService = new User();
		userFromOtherService.setUserID(10L);

		Mockito.when(dataService.getUser(userFromOtherService)).thenReturn(Optional.empty());

		MvcResult mvcResult = mvc.perform(post("/api/data/user").contentType(MediaType.APPLICATION_JSON)
				.content(mapToJson(userFromOtherService)).accept(MediaType.APPLICATION_JSON)).andReturn();

		int apiCallStatus = mvcResult.getResponse().getStatus();
		assertEquals(200, apiCallStatus);
		String responseBody = mvcResult.getResponse().getContentAsString();
		Optional<?> optional = mapFromJson(responseBody, Optional.class);

		// empty optional - not getting converted to json properly - the above string is
		// null
		assertEquals(null, optional);
		// later try using mockRest
	}

	// Case1:
	// new record is different from old record
	// Returns: 1 (#records updated)

	@Test
	public void testUpdateCase1() throws Exception {
		User initialUser = new User(1L, "abc@gmail.com", new PasswordHistory(2L,
				"$2a$12$2jDJzTrQ9UOP43LVEyrdweLBe10SA0csWa5EzsHlQm0suxaWv7UqT", "$2a$12$2jDJzTrQ9UOP43LVEyrdwf"));

		Mockito.when(dataService.changePassword(initialUser)).thenReturn(1);

		MvcResult mvcResult = mvc.perform(
				post("/api/data/update").contentType(MediaType.APPLICATION_JSON).content(mapToJson(initialUser)))
				.andReturn();

		int apiCallStatus = mvcResult.getResponse().getStatus();
		assertEquals(200, apiCallStatus);
		String apiCallResponseFromDataService = mvcResult.getResponse().getContentAsString();

		assertEquals(1, Integer.parseInt(apiCallResponseFromDataService));
	}

	// Case2:
	// new record is same as old record
	// Returns: 0 (#records updated)

	@Test
	public void testUpdateCase2() throws Exception {
		User initialUser = new User(1L, "abc@gmail.com", new PasswordHistory(2L,
				"$2a$12$2jDJzTrQ9UOP43LVEyrdweLBe10SA0csWa5EzsHlQm0suxaWv7UqS", "$2a$12$2jDJzTrQ9UOP43LVEyrdwe"));

		Mockito.when(dataService.changePassword(initialUser)).thenReturn(0);

		MvcResult mvcResult = mvc.perform(
				post("/api/data/update").contentType(MediaType.APPLICATION_JSON).content(mapToJson(initialUser)))
				.andReturn();

		int apiCallStatus = mvcResult.getResponse().getStatus();
		assertEquals(200, apiCallStatus);
		String apiCallResponseFromDataService = mvcResult.getResponse().getContentAsString();

		assertEquals(0, Integer.parseInt(apiCallResponseFromDataService));
	}
}
