package com.tripayapp.repositories;


import com.tripayapp.entity.BankTransfer;
import com.tripayapp.entity.Banks;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BanksRepository extends CrudRepository<Banks, Long>, PagingAndSortingRepository<Banks,Long>,JpaSpecificationExecutor<Banks> {

    @Query("select u from Banks u where u.code=?1")
    Banks findByCode(String code);

}
