package com.example.travelko.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.travelko.entity.Recruit;

public interface RecruitRepository extends JpaRepository<Recruit, Integer> {
	Recruit findBySubject(String subject);
    Recruit findBySubjectAndContent(String subject, String content);
    List<Recruit> findBySubjectLike(String subject);
	Page<Recruit> findAll(Pageable pageable);
	Page<Recruit> findAll(Specification<Recruit> spec, Pageable pageable);
	List<Recruit> findAllByAuthorId(Long authorId);
}
