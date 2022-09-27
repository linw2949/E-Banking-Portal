package com.winnie.demo.dao;

import com.winnie.demo.model.DAOAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountDao extends JpaRepository<DAOAccount, String> {
	DAOAccount findByIban(String iban);
	List<DAOAccount> findAllByUserId(String userId);
}