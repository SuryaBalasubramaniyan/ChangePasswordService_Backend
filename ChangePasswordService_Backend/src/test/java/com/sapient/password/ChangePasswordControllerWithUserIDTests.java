package com.sapient.password;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sapient.password.model.CustomPasswordEncoder;
import com.sapient.password.model.PasswordHistory;
import com.sapient.password.model.User;

@RunWith(SpringRunner.class)
@SpringBootTest // (it can disable authentication of S security)
//@WebMvcTest (it alone can't disable authentication of S security; it does autowiring of MockMvc; it can't be used along with @SpringBootTest; so now its not required to specify controller class explicitly)
public class ChangePasswordControllerWithUserIDTests {

	@Autowired
	WebApplicationContext webApplicationContext;

	@MockBean
	private RestTemplate restTemplate;
	@MockBean
	private CustomPasswordEncoder customPasswordEncoder;

//	@Autowired
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
	public void init() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	// Case1:
	// User exists
	// Input: Correct old password
	// Returns: Password changed successfully

	@Test
	public void testChangePasswordCase1() throws Exception {
		User userToChangePwdService = new User();
		userToChangePwdService.setUserID(1L);
		PasswordHistory password = new PasswordHistory();
		password.setPwd1("smallWorld");
		password.setPwd2("smallWorld@1");
		userToChangePwdService.setPasswordHistory(password);

		User userToDataService = new User();
		userToDataService.setUserID(1L);

		User userFromDataService = new User(1L, "abc@gmail.com", new PasswordHistory(2L,
				"$2a$12$2jDJzTrQ9UOP43LVEyrdweLBe10SA0csWa5EzsHlQm0suxaWv7UqS", "$2a$12$2jDJzTrQ9UOP43LVEyrdwe"));
		Optional<User> optional = Optional.of(userFromDataService);

//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<User> httpEntity = new HttpEntity<User>(userToDataService);
		when(restTemplate.exchange("http://localhost:8017/api/data/user", HttpMethod.POST, httpEntity,
				new ParameterizedTypeReference<Optional<User>>() {
				})).thenReturn(new ResponseEntity<>(optional, HttpStatus.OK));

		when(customPasswordEncoder.encodeWithSalt("smallWorld", "$2a$12$2jDJzTrQ9UOP43LVEyrdwe"))
				.thenReturn("$2a$12$2jDJzTrQ9UOP43LVEyrdweLBe10SA0csWa5EzsHlQm0suxaWv7UqS");

		when(customPasswordEncoder.encodeWithSalt(Mockito.eq("smallWorld@1"), Mockito.any(String.class)))
				.thenReturn("$2a$12$2jDJzTrQ9UOP43LVEyrdweLBe10SA0csWa5EzsHlQm0suxaWv7UqT");

		User userToDataServiceAgain = new User(1L, "abc@gmail.com", new PasswordHistory(2L,
				"$2a$12$2jDJzTrQ9UOP43LVEyrdweLBe10SA0csWa5EzsHlQm0suxaWv7UqT", "$2a$12$2jDJzTrQ9UOP43LVEyrdwf"));

		httpEntity = new HttpEntity<User>(userToDataServiceAgain);
		when(restTemplate.exchange("http://localhost:8017/api/data/update", HttpMethod.POST, httpEntity,
				new ParameterizedTypeReference<Integer>() {
				})).thenReturn(new ResponseEntity<>(1, HttpStatus.OK));

		MvcResult mvcResult = mvc.perform(post("/api/change").contentType(MediaType.APPLICATION_JSON)
				.content(mapToJson(userToChangePwdService)).accept(MediaType.ALL_VALUE)).andReturn();

		int apiCallStatus = mvcResult.getResponse().getStatus();
		String apiCallResponseFromChangePwdService = mvcResult.getResponse().getContentAsString();
		assertEquals(200, apiCallStatus);
		assertEquals("Password changed successfully", apiCallResponseFromChangePwdService);
	}

	// Case2:
	// User exists
	// Input: Incorrect old password
	// Returns: Incorrect password

	@Test
	public void testChangePasswordCase2() throws Exception {
		User userToChangePwdService = new User();
		userToChangePwdService.setUserID(1L);
		PasswordHistory password = new PasswordHistory();
		password.setPwd1("smallBall");
		password.setPwd2("smallWorld@1");
		userToChangePwdService.setPasswordHistory(password);

		User userToDataService = new User();
		userToDataService.setUserID(1L);

		User userFromDataService = new User(1L, "abc@gmail.com", new PasswordHistory(2L,
				"$2a$12$2jDJzTrQ9UOP43LVEyrdweLBe10SA0csWa5EzsHlQm0suxaWv7UqS", "$2a$12$2jDJzTrQ9UOP43LVEyrdwe"));
		Optional<User> optional = Optional.of(userFromDataService);

//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<User> httpEntity = new HttpEntity<User>(userToDataService);
		when(restTemplate.exchange("http://localhost:8017/api/data/user", HttpMethod.POST, httpEntity,
				new ParameterizedTypeReference<Optional<User>>() {
				})).thenReturn(new ResponseEntity<>(optional, HttpStatus.OK));

		when(customPasswordEncoder.encodeWithSalt("smallBall", "$2a$12$2jDJzTrQ9UOP43LVEyrdwe"))
				.thenReturn("$2a$12$2jDJzTrQ9UOP43LVEyrdweLBe10SA0csWa5EzsHlQm0suxaWv7UqT");

		MvcResult mvcResult = mvc.perform(post("/api/change").contentType(MediaType.APPLICATION_JSON)
				.content(mapToJson(userToChangePwdService)).accept(MediaType.ALL_VALUE)).andReturn();

		int apiCallStatus = mvcResult.getResponse().getStatus();
		String apiCallResponseFromChangePwdService = mvcResult.getResponse().getContentAsString();
		assertEquals(200, apiCallStatus);
		assertEquals("Please enter correct password", apiCallResponseFromChangePwdService);
	}

	// Case3:
	// User doesn't exist
	// Input: old password doesn't matter
	// Returns: User doesn't exist

	@Test
	public void testChangePasswordCase3() throws Exception {
		User userToChangePwdService = new User();
		userToChangePwdService.setUserID(10L);
		PasswordHistory password = new PasswordHistory();
		password.setPwd1("smallWorld");
		password.setPwd2("smallWorld@1");
		userToChangePwdService.setPasswordHistory(password);

		User userToDataService = new User();
		userToDataService.setUserID(10L);

		Optional<User> optional = Optional.empty();

//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<User> httpEntity = new HttpEntity<User>(userToDataService);
		when(restTemplate.exchange("http://localhost:8017/api/data/user", HttpMethod.POST, httpEntity,
				new ParameterizedTypeReference<Optional<User>>() {
				})).thenReturn(new ResponseEntity<>(optional, HttpStatus.OK));

		MvcResult mvcResult = mvc.perform(post("/api/change").contentType(MediaType.APPLICATION_JSON)
				.content(mapToJson(userToChangePwdService)).accept(MediaType.ALL_VALUE)).andReturn();

		int apiCallStatus = mvcResult.getResponse().getStatus();
		String apiCallResponseFromChangePwdService = mvcResult.getResponse().getContentAsString();
		assertEquals(200, apiCallStatus);
		assertEquals("User doesn't exist", apiCallResponseFromChangePwdService);
	}

	// Case4:
	// User exists
	// Input: Correct old password
	// Returns: Some error (due to db error or new n old passwords r same)

	@Test
	public void testChangePasswordCase4() throws Exception {
		User userToChangePwdService = new User();
		userToChangePwdService.setUserID(1L);
		PasswordHistory password = new PasswordHistory();
		password.setPwd1("smallWorld");
		password.setPwd2("smallWorld@1");
		userToChangePwdService.setPasswordHistory(password);

		User userToDataService = new User();
		userToDataService.setUserID(1L);

		User userFromDataService = new User(1L, "abc@gmail.com", new PasswordHistory(2L,
				"$2a$12$2jDJzTrQ9UOP43LVEyrdweLBe10SA0csWa5EzsHlQm0suxaWv7UqS", "$2a$12$2jDJzTrQ9UOP43LVEyrdwe"));
		Optional<User> optional = Optional.of(userFromDataService);

//			HttpHeaders headers = new HttpHeaders();
//			headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<User> httpEntity = new HttpEntity<User>(userToDataService);
		when(restTemplate.exchange("http://localhost:8017/api/data/user", HttpMethod.POST, httpEntity,
				new ParameterizedTypeReference<Optional<User>>() {
				})).thenReturn(new ResponseEntity<>(optional, HttpStatus.OK));

		when(customPasswordEncoder.encodeWithSalt("smallWorld", "$2a$12$2jDJzTrQ9UOP43LVEyrdwe"))
				.thenReturn("$2a$12$2jDJzTrQ9UOP43LVEyrdweLBe10SA0csWa5EzsHlQm0suxaWv7UqS");

		when(customPasswordEncoder.encodeWithSalt(Mockito.eq("smallWorld@1"), Mockito.any(String.class)))
				.thenReturn("$2a$12$2jDJzTrQ9UOP43LVEyrdweLBe10SA0csWa5EzsHlQm0suxaWv7UqT");

		User userToDataServiceAgain = new User(1L, "abc@gmail.com", new PasswordHistory(2L,
				"$2a$12$2jDJzTrQ9UOP43LVEyrdweLBe10SA0csWa5EzsHlQm0suxaWv7UqT", "$2a$12$2jDJzTrQ9UOP43LVEyrdwf"));

		httpEntity = new HttpEntity<User>(userToDataServiceAgain);
		when(restTemplate.exchange("http://localhost:8017/api/data/update", HttpMethod.POST, httpEntity,
				new ParameterizedTypeReference<Integer>() {
				})).thenReturn(new ResponseEntity<>(0, HttpStatus.OK));

		MvcResult mvcResult = mvc.perform(post("/api/change").contentType(MediaType.APPLICATION_JSON)
				.content(mapToJson(userToChangePwdService)).accept(MediaType.ALL_VALUE)).andReturn();

		int apiCallStatus = mvcResult.getResponse().getStatus();
		String apiCallResponseFromChangePwdService = mvcResult.getResponse().getContentAsString();
		assertEquals(200, apiCallStatus);
		assertEquals("Some error", apiCallResponseFromChangePwdService);
	}
}
