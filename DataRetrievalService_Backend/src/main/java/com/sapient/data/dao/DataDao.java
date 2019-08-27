package com.sapient.data.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sapient.data.model.User;

@Repository
public interface DataDao extends CrudRepository<User, Long> {

	@Query(value = "SELECT * FROM REGISTER WHERE EMAILID = ?1", nativeQuery = true)
	Optional<User> findUserByEmailID(String emailID);

	@Query(value = "SELECT * FROM REGISTER WHERE USERID = ?1", nativeQuery = true)
	Optional<User> findUserByUserID(Long userID);

	@Modifying(clearAutomatically = true)
	@Query(value = "UPDATE PASSWORD_HISTORY SET PASSWORD3= :password3,PASSWORD2= :password1,PASSWORD1 = :hpwd,SALT3= :salt3,SALT2= :salt1, SALT1 = :salt WHERE PASS_ID = :pid ", nativeQuery = true)
	int updatePassword(@Param("pid") Long passID, @Param("hpwd") String password, @Param("salt") String salt,
			@Param("password1") String password1,@Param("salt1") String salt1,
			@Param("password3") String password3,@Param("salt3") String salt3);
	
	@Query(value = "SELECT EMAIL_CONFIRMATION_FLAG FROM REGISTER WHERE USERID = :uid", nativeQuery = true)
	boolean getEmailConfFlag(@Param("uid") Long userID);

}
