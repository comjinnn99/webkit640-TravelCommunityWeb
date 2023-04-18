package com.example.travelko.controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.example.travelko.entity.Recruit;
import com.example.travelko.entity.Reply;
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
	
	@PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String replyModify(ReplyForm replyForm, @PathVariable("id") Integer id, Principal principal) {
        Reply reply = this.replyService.getReply(id);
        if (!reply.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        replyForm.setContent(reply.getContent());
        return "reply_form";
    }
	
	@PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String replyModify(@Valid ReplyForm replyForm, BindingResult bindingResult,
            @PathVariable("id") Integer id, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "reply_form";
        }
        Reply reply = this.replyService.getReply(id);
        if (!reply.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.replyService.modify(reply, replyForm.getContent());
        return String.format("redirect:/recruit/detail/%s", reply.getRecruit().getId());
    }
	
	@PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String replyDelete(Principal principal, @PathVariable("id") Integer id) {
		Reply reply = this.replyService.getReply(id);
        if (!reply.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.replyService.delete(reply);
        return String.format("redirect:/recruit/detail/%s", reply.getRecruit().getId());
    }
}
