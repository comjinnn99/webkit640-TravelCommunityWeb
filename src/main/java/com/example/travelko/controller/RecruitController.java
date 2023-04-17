package com.example.travelko.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.travelko.entity.Recruit;
import com.example.travelko.form.RecruitForm;
import com.example.travelko.form.ReplyForm;
import com.example.travelko.service.RecruitService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/recruit")
@RequiredArgsConstructor
@Controller
public class RecruitController {
	private final RecruitService recruitService;
	
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
    
    @GetMapping("/create")
    public String recruitCreate(RecruitForm recruitForm) {
        return "recruit_form";
    }
    
    @PostMapping("/create")
    public String recruitCreate(
    		@Valid RecruitForm recruitForm,
    		BindingResult bindingResult
    		) {
        if (bindingResult.hasErrors()) {
            return "recruit_form";
        }
    	this.recruitService.create(
    			recruitForm.getSubject(),
    			recruitForm.getContent(),
    			recruitForm.getRegion(),
    			recruitForm.getStartDate(),
    			recruitForm.getEndDate());
        return "redirect:/recruit/list"; // 질문 저장후 질문목록으로 이동
    }
}