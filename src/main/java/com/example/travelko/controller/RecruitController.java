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
import com.example.travelko.entity.Travel;
import com.example.travelko.form.RecruitForm;
import com.example.travelko.form.ReplyForm;
import com.example.travelko.service.RecruitService;
import com.example.travelko.service.TravelService;
import com.example.travelko.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/recruit")
@RequiredArgsConstructor
@Controller
public class RecruitController {
	private final RecruitService recruitService;
	private final UserService userService;
	private final TravelService travelService;
	
	// 글 리스트
    @GetMapping("/list")
    public String list(
    		Model model,
    		@RequestParam(value="page", defaultValue="0") int page,
    		@RequestParam(value = "kw", defaultValue = "") String kw
    		) {
    	// Recruit 객체를 페이지에 전달
    	Page<Recruit> paging = this.recruitService.getList(page, kw);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        return "recruit_list";
    }
    
    // 글 상세조회
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
    
    // 작성 버튼 클릭 시 화면 생성
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String recruitCreate(RecruitForm recruitForm) {
        return "recruit_form";
    }
    
    // 작성 완료 시 데이터를 저장하고 리다이렉트
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
    	Recruit recruit = this.recruitService.create(
    			recruitForm.getSubject(),
    			recruitForm.getContent(),
    			recruitForm.getRegion(),
    			recruitForm.getStartDate(),
    			recruitForm.getEndDate(),
    			siteUser);
    	// Travel history
    	// 자기가 작성한 글은 여행참가되어야한다
    	Travel travel = this.travelService.create(recruit, siteUser);
    	Travel myTravel = this.travelService.getTravel(travel.getId());
    	myTravel.setAccept("1");
    	this.travelService.modify(myTravel);
    	// 생성 후 상세페이지로 리다이렉트
    	String id = Integer.toString(recruit.getId());
        return String.format("redirect:/recruit/detail/%s", id); // 질문 저장후 질문목록으로 이동
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
    public String recruitModify(@Valid RecruitForm recruitForm, BindingResult bindingResult, 
            Principal principal, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "recruit_form";
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
    public String recruitDelete(Principal principal, @PathVariable("id") Integer id) {
    	Recruit recruit = this.recruitService.getRecruit(id);
        if (!recruit.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.recruitService.delete(recruit);
        return "redirect:/";
    }
}
