package com.example.travelko.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.travelko.DataNotFoundException;
import com.example.travelko.entity.Recruit;
import com.example.travelko.repository.RecruitRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RecruitService {
    private final RecruitRepository recruitRepository;

    public Page<Recruit> getList(int page) {
    	//객체 역순 조회
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
    	//page는 조회할 페이지의 번호, 19은 한 페이지에 보여줄 게시물 갯수
    	Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.recruitRepository.findAll(pageable);
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
