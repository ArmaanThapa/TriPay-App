package com.tripayapp.repositories;

import java.util.List;

import com.tripayapp.model.Gender;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.tripayapp.entity.UserDetail;

public interface UserDetailRepository extends CrudRepository<UserDetail, Long>, JpaSpecificationExecutor<UserDetail> {

	@Query("select u from UserDetail u where u.email=?1")
	List<UserDetail> checkMail(String mail);

	@Query("select COUNT(u) from UserDetail u where u.gender=?1")
	Long countUsersByGender(Gender gender);

	@Modifying
	@Transactional
	@Query("update UserDetail u set u.image=?1 where u.contactNo=?2")
	int updateUserImage(String url, String username);

	@Modifying
	@Transactional
	@Query("update UserDetail u set u.address=?1, u.firstName=?2, u.lastName=?3, u.email=?4, u.gender=?6 where u.contactNo=?5")
	int updateUserDetail(String address, String firstName, String lastName, String email, String username,Gender gender);

	@Modifying
	@Transactional
	@Query("update UserDetail u set u.address=?1, u.firstName=?2, u.lastName=?3 , u.gender=?5 where u.contactNo=?4")
	int updateUserDetailOnly(String address, String firstName, String lastName, String username,Gender gender);
	
	@Modifying
	@Transactional
	@Query("update UserDetail u set u.mpin=?1 where u.contactNo=?2")
	int updateUserMPIN(String mpin, String username);


	@Modifying
	@Transactional
	@Query("update UserDetail u set u.mpin=null where u.contactNo=?1")
    int deleteUserMPIN(String username);
	
	@Modifying
	@Transactional
	@Query("update UserDetail c set c.email=?1 where c.id =?2")
	int updateChangeEmail(String email, long id);
	
}
