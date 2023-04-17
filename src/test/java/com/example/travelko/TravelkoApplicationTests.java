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

import jakarta.transaction.Transactional;

@SpringBootTest
class TravelkoApplicationTests {

	@Autowired
	private RecruitRepository recruitRepository;
	
    @Autowired
    private ReplyRepository replyRepository;
    
    @Transactional
	@Test
	void testJpa() {
        Optional<Recruit> oq = this.recruitRepository.findById(4);
        assertTrue(oq.isPresent());
        Recruit q = oq.get();

        Reply a = new Reply();
        a.setContent("네 자동으로 생성됩니다.");
        a.setRecruit(q);  // 어떤 질문의 답변인지 알기위해서 Question 객체가 필요하다.
        a.setCreateDate(LocalDateTime.now());
        this.replyRepository.save(a);
	}

}
