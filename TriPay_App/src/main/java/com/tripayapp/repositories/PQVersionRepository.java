package com.tripayapp.repositories;

import com.tripayapp.entity.PQOperator;
import com.tripayapp.entity.PQVersion;
import com.tripayapp.model.Status;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface PQVersionRepository extends CrudRepository<PQVersion, Long>, JpaSpecificationExecutor<PQVersion> {

    @Query("select u from PQVersion u where u.versionCode=?1 AND u.subversionCode=?2")
    PQVersion findByVersionNo(int versionCode,int subVersionCode);

    @Query("select u from PQVersion u where u.status=?1")
    List<PQVersion> findAllVersionsByStatus(Status status);

    @Query("select u from PQVersion u where u.id = (select MIN(r.id) from PQVersion r where r.status='Active')")
    PQVersion findLatestVersion();

    @Query("select u from PQVersion u")
    List<PQVersion> getAllVersions();


    @Modifying
    @Transactional
    @Query("update PQVersion p set p.status = 'Inactive' where p.id <?1")
    int disablePreviousVersionsBeforeID(long id);

    @Modifying
    @Transactional
    @Query("update PQVersion u set u.status = 'Active' where u.id >= ?1 ")
    int activateAllVersionsAfterID(long id);


}
