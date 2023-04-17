package com.example.travelko;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.travelko.entity.Recruit;
import com.example.travelko.entity.Reply;
import com.example.travelko.repository.RecruitRepository;
import com.example.travelko.repository.ReplyRepository;
import com.example.travelko.service.RecruitService;

import jakarta.transaction.Transactional;

@SpringBootTest
class TravelkoApplicationTests {

	@Autowired
    private RecruitService recruitService;
    
	@Test
	void testJpa() {
        for (int i = 1; i <= 100; i++) {
            String subject = String.format("테스트 데이터입니다:[%03d]", i);
            String content = "내용무";
            String region = "서울";
            String startDate = "2023/05/01";
            String endDate = "2023/05/05";
            
            this.recruitService.create(
            		subject,
            		content,
            		region,
            		startDate,
            		endDate
            		);
        }
	}

}
