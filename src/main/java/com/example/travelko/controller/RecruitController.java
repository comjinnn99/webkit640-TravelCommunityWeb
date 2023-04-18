package com.example.travelko.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.data.domain.Page;
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
import com.example.travelko.entity.SiteUser;
import com.example.travelko.form.RecruitForm;
import com.example.travelko.form.ReplyForm;
import com.example.travelko.service.RecruitService;
import com.example.travelko.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/recruit")
@RequiredArgsConstructor
@Controller
public class RecruitController {
	private final RecruitService recruitService;
	private final UserService userService;
	
    @GetMapping("/list")
    public String list(
    		Model model,
    		@RequestParam(value="page", defaultValue="0") int page
    		) {
    	Page<Recruit> paging = this.recruitService.getList(page);
        model.addAttribute("paging", paging);
        
        return "recruit_list";
    }
    
    @GetMapping(value = "/detail/{id}")
    public String detail(
    		Model model,
    		@PathVariable("id") Integer id,
    		ReplyForm replyForm
    		) {
    	Recruit recruit = this.recruitService.getRecruit(id);
        model.addAttribute("recruit", recruit);
        
    	return "recruit_detail";
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String recruitCreate(RecruitForm recruitForm) {
        return "recruit_form";
    }
    
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String recruitCreate(
    		@Valid RecruitForm recruitForm,
    		BindingResult bindingResult,
    		Principal principal
    		) {
        if (bindingResult.hasErrors()) {
            return "recruit_form";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
    	this.recruitService.create(
    			recruitForm.getSubject(),
    			recruitForm.getContent(),
    			recruitForm.getRegion(),
    			recruitForm.getStartDate(),
    			recruitForm.getEndDate(),
    			siteUser);
        return "redirect:/recruit/list"; // 질문 저장후 질문목록으로 이동
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String recruitModify(RecruitForm recruitForm, @PathVariable("id") Integer id, Principal principal) {
        Recruit recruit = this.recruitService.getRecruit(id);
        if(!recruit.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        recruitForm.setSubject(recruit.getSubject());
        recruitForm.setContent(recruit.getContent());
        recruitForm.setRegion(recruit.getRegion());
        recruitForm.setStartDate(recruit.getStartDate());
        recruitForm.setEndDate(recruit.getEndDate());
        return "recruit_form";
    }
    
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String questionModify(@Valid RecruitForm recruitForm, BindingResult bindingResult, 
            Principal principal, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        Recruit recruit = this.recruitService.getRecruit(id);
        if (!recruit.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.recruitService.modify(
        		recruit,
        		recruitForm.getSubject(),
        		recruitForm.getContent(),
        		recruitForm.getRegion(),
        		recruitForm.getStartDate(),
        		recruitForm.getEndDate()
        		);
        return String.format("redirect:/recruit/detail/%s", id);
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String questionDelete(Principal principal, @PathVariable("id") Integer id) {
    	Recruit recruit = this.recruitService.getRecruit(id);
        if (!recruit.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.recruitService.delete(recruit);
        return "redirect:/";
    }
}
