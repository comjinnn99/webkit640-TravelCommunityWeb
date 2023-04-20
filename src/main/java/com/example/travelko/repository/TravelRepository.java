package com.example.travelko.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.travelko.entity.Travel;

public interface TravelRepository extends JpaRepository<Travel, Integer> {
	List<Travel> findByRecruitId(Integer recruitId);
}
