package com.example.travelko.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.travelko.entity.Recruit;
import com.example.travelko.entity.Reply;
import com.example.travelko.repository.ReplyRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ReplyService {
    private final ReplyRepository replyRepository;

    public void create(Recruit recruit, String content) {
    	Reply reply = new Reply();
    	reply.setContent(content);
    	reply.setCreateDate(LocalDateTime.now());
    	reply.setRecruit(recruit);
        this.replyRepository.save(reply);
    }
}
