package com.notes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.notes.models.User;

public interface UserRepository extends JpaRepository<User, String> {

	User findByusername(String userName);
}