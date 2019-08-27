package com.sapient.data.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sapient.data.dao.DataDao;
import com.sapient.data.model.User;

@Service
public class DataService {

	@Autowired
	DataDao dataDao;

	@Transactional
	public Optional<User> getUser(User loginUser) {

		if (loginUser.getUserID() != null) {
			return dataDao.findUserByUserID(loginUser.getUserID());
		} else {
			return dataDao.findUserByEmailID(loginUser.getEmailID());
		}
	}

	@Transactional
	public int changePassword(User user) {
		return dataDao.updatePassword(user.getPasswordHistory().getPassId(), user.getPasswordHistory().getPwd1(),
				user.getPasswordHistory().getSalt1());
	}

	@Transactional
	public boolean checkEmailConfFlag(User user) {
		return dataDao.getEmailConfFlag(user.getUserID());
	}

	@Transactional
	public User createUser(User user) {
		return dataDao.save(user);
	}
}
