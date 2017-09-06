package com.tripayapp.repositories;

import java.util.List;

import com.tripayapp.model.UserType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.tripayapp.entity.PQAccountDetail;
import com.tripayapp.entity.User;
import com.tripayapp.entity.UserDetail;
import com.tripayapp.model.Status;

public interface UserRepository
		extends CrudRepository<User, Long>, PagingAndSortingRepository<User, Long>, JpaSpecificationExecutor<User> {

	@Query("select u from User u where u.username=?1")
	User checkForLogin(String username);

	@Query("select u from User u where u.username=?1")
	User findByUsername(String username);

	@Query("select u from User u where u.username=?1 and u.mobileStatus=?2")
	User findByUsernameAndStatus(String username, Status status);

	@Query("select u from User u where u.username=?1 and u.mobileStatus=?2 and u.emailStatus=?3")
	User findByUsernameAndMobileStatusAndEmailStatus(String username, Status mobileStatus, Status emailStatus);

	@Query("select u from User u where u.userType='1' Order by u.username ASC")
	List<User> findAllUser();

	@Query("select u from User u where u.authority='ROLE_USER,ROLE_BLOCKED'")
	List<User> findAllBlockedUsers();

	@Query("select u from User u where u.emailToken=?1")
	User findByEmailToken(String key);
	
	@Query("select u from User u where u.mobileToken=?1")
	User checkExistingMobileToken(String token);

	@Query("select u from User u where u.emailToken=?1 and u.emailStatus=?2")
	User findByEmailTokenAndStatus(String key, Status active);

	@Query("select u from User u where u.emailToken=?1 and u.emailStatus=?2 and u.mobileStatus=?3")
	User findByEmailTokenAndEmailStatusAndMobileStatus(String key, Status emailStatus, Status mobileStatus);

	@Query("select u from User u where u.mobileToken=?1 and u.mobileStatus=?2")
	User findByOTPAndStatus(String key, Status active);

	@Query("select u from User u where u.mobileToken=?1 and u.username=?2 and u.mobileStatus=?3")
	User findByMobileTokenAndStatus(String key, String mobile, Status active);

	@Query("select u from User u where u.emailToken=?1 and u.username=?2")
	User findByEmailTokenAndUsername(String key, String username);

	@Query("select u from User u where u.mobileToken=?1 and u.username=?2")
	User findByMobileTokenAndUsername(String key, String username);

	@Query("select u from User u where u.userDetail=?1")
	User findByUserDetails(UserDetail userDetail);

	@Query("select u from User u where u.accountDetail=?1")
	User findByAccountDetails(PQAccountDetail accountDetail);

	@Query("select p from User p")
	public List<User> findWithPageable(Pageable pageable);

	@Query("select p from User p where p.userType=2")
	public List<User> findAllMerchants();

	@Query("select p from User p")
	public List<User> findWithVerifiedUsersPageable(Pageable pageable);

	@Query("select COUNT(p) from User p where p.userType= ?1")
	Long getTotalUsersCount(UserType userType);

	@Query("select count(u) from User u")
	Long getUserCount();

	@Modifying
	@Transactional
	@Query("update User c set c.authority=?1 where c.id =?2")
	int updateUserAuthority(String authority, long id);
	
	@Query("select u from User u where u.authority='ROLE_USER,ROLE_AUTHENTICATED'")
	Page<User> findAll(Pageable page);
	
//	select * from payqwikdb.user where authority='ROLE_USER,ROLE_AUTHENTICATED' AND emailStatus='Active' AND mobileStatus='Active';
	@Query("select u from User u where u.authority='ROLE_USER,ROLE_AUTHENTICATED' AND u.emailStatus='Active' AND u.mobileStatus='Active'")
	Page<User> getActiveUsers(Pageable pageable);
	
//	@Query("select u from User u where u.mobileStatus='Inactive' ")
	@Query("select u from User u where u.authority='ROLE_USER,ROLE_AUTHENTICATED' AND u.emailStatus='Inactive' OR u.mobileStatus='Inactive'")
	Page<User> getInactiveUsers(Pageable pageable);
	
	@Query("select u from User u where u.authority='ROLE_USER,ROLE_BLOCKED'")
	Page<User> getBlockedUsers(Pageable pageable);

	@Query("select u from User u where u.authority='ROLE_USER,ROLE_LOCKED'")
	Page<User> getLockedUsers(Pageable pageable);


	@Query("select u from User u where u.id=?1")
	User findUserByUserId(long id);

	@Query("select u from User u where u.userType=?1")
	List<User> getTotalUsers(UserType userType);

	@Modifying
	@Transactional
	@Query("update User u set u.gcmId=?1 where u.username=?2")
	int updateGCMID(String gcmId, String username);

}



