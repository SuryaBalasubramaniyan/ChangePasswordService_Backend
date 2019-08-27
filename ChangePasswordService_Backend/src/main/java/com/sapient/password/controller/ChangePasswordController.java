package com.sapient.password.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.sapient.password.model.CustomPasswordEncoder;
import com.sapient.password.model.User;

@RestController
@RequestMapping("/api/change")
@CrossOrigin(origins = "*")
public class ChangePasswordController {

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	CustomPasswordEncoder customPasswordEncoder;

	@PostMapping
	public String changePassword(@RequestBody User loginUser) {
		String url = "http://localhost:8017/api/data/user";
		User user = new User();

		if (loginUser.getUserID() != null) {
			user.setUserID(loginUser.getUserID());
		} else {
			user.setEmailID(loginUser.getEmailID());
		}

		HttpEntity<User> httpEntity = new HttpEntity<>(user);
		ResponseEntity<Optional<User>> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity,
				new ParameterizedTypeReference<Optional<User>>() {
				});
		Optional<User> optional = response.getBody();

		return changePasswordOfUser(optional, loginUser.getPasswordHistory().getPwd1(),
				loginUser.getPasswordHistory().getPwd2());
		
	}

	private String changePasswordOfUser(Optional<User> optional, String rawOldPassword, String rawNewPassword) {
		if (optional.isPresent()) {
			User user = optional.get();
			String storedHashedOldPassword1 = user.getPasswordHistory().getPwd1();
			String storedHashedOldPassword2 = user.getPasswordHistory().getPwd2();
			String storedHashedOldPassword3 = user.getPasswordHistory().getPwd3();
			
			String storedSalt1 = user.getPasswordHistory().getSalt1();
			String storedSalt2 = user.getPasswordHistory().getSalt2();
			String storedSalt3 = user.getPasswordHistory().getSalt3();
			String inputHashedOldPassword = customPasswordEncoder.encodeWithSalt(rawOldPassword, storedSalt1); //old pwd from frontend
			
			System.out.println(" stored hashed old pwd:"+storedHashedOldPassword1);
			System.out.println("input hashed old pwd:"+inputHashedOldPassword);
			
			
			String inputHashedNewPassword1,inputHashedNewPassword2,inputHashedNewPassword3;
			if (inputHashedOldPassword.equals(storedHashedOldPassword1)) {
				
				if(storedSalt1!=null && storedSalt2!=null && storedSalt3!=null) {
					System.out.println("salts are not null");
					 inputHashedNewPassword1 = customPasswordEncoder.encodeWithSalt(rawNewPassword, storedSalt1);
					 inputHashedNewPassword2 = customPasswordEncoder.encodeWithSalt(rawNewPassword, storedSalt2);
					 inputHashedNewPassword3 = customPasswordEncoder.encodeWithSalt(rawNewPassword, storedSalt3);
					 
						
					if((inputHashedNewPassword1.equals(storedHashedOldPassword1)) || (inputHashedNewPassword2.equals(storedHashedOldPassword2)) || (inputHashedNewPassword3.equals(storedHashedOldPassword3)))  {
					
						return "old and new password should not be same";
					}
				}
				
				else if(storedSalt2==null) {
					 inputHashedNewPassword1 = customPasswordEncoder.encodeWithSalt(rawNewPassword, storedSalt1);
					 System.out.println("within storedSalt2 is null");
					if((inputHashedNewPassword1.equals(storedHashedOldPassword1))  )  {
					
						return "old and new password should not be same";
					}
				}
				else {
					 inputHashedNewPassword1 = customPasswordEncoder.encodeWithSalt(rawNewPassword, storedSalt1);
					 inputHashedNewPassword2 = customPasswordEncoder.encodeWithSalt(rawNewPassword, storedSalt2);
					 System.out.println("storedsalt3 is null");
					if((inputHashedNewPassword1.equals(storedHashedOldPassword1)) || (inputHashedNewPassword2.equals(storedHashedOldPassword2)) )  {
					
						return "old and new password should not be same";
					}
				}
			
				
				String newSalt = BCrypt.gensalt(12);	
				String inputHashedNewPassword = customPasswordEncoder.encodeWithSalt(rawNewPassword, newSalt);
					
				if(user.getPasswordHistory().getPwd2()==null){
						System.out.println("within change");
						System.out.println("new password:"+inputHashedNewPassword);
						user.getPasswordHistory().setPwd2(user.getPasswordHistory().getPwd1());
						user.getPasswordHistory().setSalt2(user.getPasswordHistory().getSalt1());
						user.getPasswordHistory().setPwd1(inputHashedNewPassword);
						user.getPasswordHistory().setSalt1(newSalt);
				}
				else {
						user.getPasswordHistory().setPwd3(user.getPasswordHistory().getPwd2());
						user.getPasswordHistory().setSalt3(user.getPasswordHistory().getSalt2());
						user.getPasswordHistory().setPwd2(user.getPasswordHistory().getPwd1());
						user.getPasswordHistory().setSalt2(user.getPasswordHistory().getSalt1());
						user.getPasswordHistory().setPwd1(inputHashedNewPassword);
						user.getPasswordHistory().setSalt1(newSalt);
				}
				
				
				System.out.println("After changing");
				System.out.println("----------");
				System.out.println("storedsalt1:"+storedSalt1);
				System.out.println("storedsalt2:"+storedSalt2);
				System.out.println("storedsalt3:"+storedSalt3);
				System.out.println("after changing  password1:"+user.getPasswordHistory().getPwd1());
				System.out.println("password2:"+user.getPasswordHistory().getPwd2());
				
				
				String url = "http://localhost:8017/api/data/update";
				HttpEntity<User> httpEntity = new HttpEntity<>(user);
				ResponseEntity<Integer> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity,
						new ParameterizedTypeReference<Integer>() {
						});
				int status = response.getBody();
				if (status > 0) {
					return "Password changed successfully";
				} else {
					return "Some error";
				}
			
		}
			return "Please enter correct password";
		}
		return "User doesn't exist";
	}
	
	
}

