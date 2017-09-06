package com.tripayapp.repositories;

import com.tripayapp.entity.InviteLog;
import com.tripayapp.entity.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface InviteLogRepository extends CrudRepository<InviteLog,Long>,JpaSpecificationExecutor<InviteLog> {

    @Query("SELECT i FROM InviteLog i WHERE i.user=?1")
    List<InviteLog> getLogByUser(User u);

    @Query("SELECT i FROM InviteLog i WHERE i.user=?1 AND i.contactNo = ?2")
    InviteLog getLogByUserAndMobile(User u,String contactNo);

    @Query("SELECT COUNT(i) FROM InviteLog i WHERE i.user=?1 AND DATE(i.created) BETWEEN ?2 AND ?3")
    long countInvitedFriends(User u,Date startDate,Date endDate);
}
