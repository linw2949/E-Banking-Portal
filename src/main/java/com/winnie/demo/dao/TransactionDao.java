package com.winnie.demo.dao;

import com.winnie.demo.model.DAOTransaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionDao extends JpaRepository<DAOTransaction, Integer> {
	DAOTransaction findById(String id);
	List<DAOTransaction> findAllByIban(String iban, Pageable pageable);
}