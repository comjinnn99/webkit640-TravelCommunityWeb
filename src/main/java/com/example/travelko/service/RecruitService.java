package com.example.travelko.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.travelko.DataNotFoundException;
import com.example.travelko.entity.Recruit;
import com.example.travelko.repository.RecruitRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RecruitService {
    private final RecruitRepository recruitRepository;

    public List<Recruit> getList() {
        return this.recruitRepository.findAll();
    }
    
    public Recruit getRecruit(Integer id) {  
        Optional<Recruit> recruit = this.recruitRepository.findById(id);
        if (recruit.isPresent()) {
            return recruit.get();
        } else {
            throw new DataNotFoundException("recruit not found");
        }
    }
    
    public void create(
    		String subject,
    		String content,
    		String region,
    		String startDate,
    		String endDate
    		) {
    	Recruit q = new Recruit();
        q.setSubject(subject);
        q.setContent(content);
        q.setCreateDate(LocalDateTime.now());
        q.setRegion(region);
        q.setStartDate(startDate);
        q.setEndDate(endDate);
        this.recruitRepository.save(q);
    }
}
