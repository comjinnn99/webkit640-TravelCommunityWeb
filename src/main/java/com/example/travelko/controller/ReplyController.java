package com.example.travelko.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.travelko.entity.Recruit;
import com.example.travelko.form.ReplyForm;
import com.example.travelko.service.RecruitService;
import com.example.travelko.service.ReplyService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/reply")
@RequiredArgsConstructor
@Controller
public class ReplyController {
	private final RecruitService recruitService;
	private final ReplyService replyService;

    @PostMapping("/create/{id}")
    public String createReply(
    		Model model,
    		@PathVariable("id") Integer id,
    		@Valid ReplyForm replyForm,
    		BindingResult bindingResult
    		) {
    	Recruit recruit = this.recruitService.getRecruit(id);
        if (bindingResult.hasErrors()) {
            model.addAttribute("recruit", recruit);
            return "recruit_detail";
        }
    	this.replyService.create(recruit, replyForm.getContent());
    	
        return String.format("redirect:/recruit/detail/%s", id);
    }
}
