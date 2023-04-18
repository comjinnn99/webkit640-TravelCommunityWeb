package com.example.travelko.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.travelko.DataNotFoundException;
import com.example.travelko.entity.Recruit;
import com.example.travelko.entity.Reply;
import com.example.travelko.entity.SiteUser;
import com.example.travelko.repository.ReplyRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ReplyService {
    private final ReplyRepository replyRepository;

    public void create(Recruit recruit, String content, SiteUser author) {
    	Reply reply = new Reply();
    	reply.setContent(content);
    	reply.setCreateDate(LocalDateTime.now());
    	reply.setRecruit(recruit);
    	reply.setAuthor(author);
        this.replyRepository.save(reply);
    }
    
    public Reply getReply(Integer id) {
        Optional<Reply> reply = this.replyRepository.findById(id);
        if (reply.isPresent()) {
            return reply.get();
        } else {
            throw new DataNotFoundException("reply not found");
        }
    }

    public void modify(Reply reply, String content) {
    	reply.setContent(content);
    	reply.setModifyDate(LocalDateTime.now());
        this.replyRepository.save(reply);
    }
    
    public void delete(Reply reply) {
        this.replyRepository.delete(reply);
    }
}
