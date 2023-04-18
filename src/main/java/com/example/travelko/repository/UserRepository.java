package com.example.travelko.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.travelko.entity.SiteUser;

public interface UserRepository extends JpaRepository<SiteUser, Long> {
	Optional<SiteUser> findByUsername(String username);
}
