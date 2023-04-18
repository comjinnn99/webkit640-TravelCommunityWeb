package com.example.travelko.controller;

import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.travelko.entity.Recruit;
import com.example.travelko.entity.SiteUser;
import com.example.travelko.form.ReplyForm;
import com.example.travelko.service.RecruitService;
import com.example.travelko.service.ReplyService;
import com.example.travelko.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/reply")
@RequiredArgsConstructor
@Controller
public class ReplyController {
	private final RecruitService recruitService;
	private final ReplyService replyService;
	private final UserService userService;

	@PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String createReply(
    		Model model,
    		@PathVariable("id") Integer id,
    		@Valid ReplyForm replyForm,
    		BindingResult bindingResult,
    		Principal principal
    		) {
    	Recruit recruit = this.recruitService.getRecruit(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        if (bindingResult.hasErrors()) {
            model.addAttribute("recruit", recruit);
            return "recruit_detail";
        }
    	this.replyService.create(recruit, replyForm.getContent(), siteUser);
    	
        return String.format("redirect:/recruit/detail/%s", id);
    }
}
