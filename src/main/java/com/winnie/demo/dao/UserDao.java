package com.winnie.demo.dao;

import com.winnie.demo.model.DAOUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<DAOUser, String> {
	DAOUser findByUserId(String userId);
}