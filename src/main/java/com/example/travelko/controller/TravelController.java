package com.example.travelko.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.travelko.entity.Recruit;
import com.example.travelko.entity.SiteUser;
import com.example.travelko.entity.Travel;
import com.example.travelko.service.RecruitService;
import com.example.travelko.service.TravelService;
import com.example.travelko.service.UserService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/travel")
@RequiredArgsConstructor
@Controller
public class TravelController {
	private final TravelService travelService;
	private final RecruitService recruitService;
	private final UserService userService;

	// 여행 신청
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/request/{id}")
	public String request(Principal principal, @PathVariable("id") Integer id) {
		Recruit recruit = this.recruitService.getRecruit(id);
		SiteUser siteUser = this.userService.getUser(principal.getName());

		Travel travel = this.travelService.create(recruit, siteUser);

		return "redirect:/";
	}

	// 누가 신청했는지 보기
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/check")
	public String check(Model model, Principal principal) {
		SiteUser siteUser = this.userService.getUser(principal.getName());
		List<Recruit> myRecruitList = this.recruitService.getMyRecruitList(siteUser.getId());
		List<Travel> travelRequestList = new ArrayList<>();
		for (Recruit myRecruit : myRecruitList) {
			travelRequestList = this.travelService.getList(myRecruit.getId());
		}
		List<Travel> travelN = travelRequestList.stream().filter(t -> t.getAccept().equals("0")).collect(Collectors.toList());
		model.addAttribute("travelN", travelN);

		return "travel_request_form";
	}

	// 여행 신청 수락 (accept = 1)
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/check/accept/{id}")
	public String accept(@PathVariable("id") Integer id) {
		Travel travel = this.travelService.getTravel(id);
		travel.setAccept("1");
		this.travelService.modify(travel);

		return "redirect:/travel/check";
	}
	
	// 여행 신청 거절 (삭제)
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/check/reject/{id}")
	public String reject(@PathVariable("id") Integer id) {
		Travel travel = this.travelService.getTravel(id);
		this.travelService.delete(travel);
		
		return "redirect:/travel/check";
	}
	
	// 여행 이력 조회
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/history")
	public String history(Model model, Principal principal) {
		SiteUser siteUser = this.userService.getUser(principal.getName());
		List<Travel> myTravelList = this.travelService.getMyTravelList(siteUser.getId());
		List<Travel> myTravelListY = myTravelList.stream().filter(t -> t.getAccept().equals("1")).collect(Collectors.toList());
		List<Travel> myTravel = new ArrayList<>();
		for (Travel myTravelY : myTravelListY) {
			myTravel = this.travelService.getList(myTravelY.getRecruit().getId());
		}
		List<Travel> travel = myTravel.stream().filter(t -> t.getAccept().equals("1")).collect(Collectors.toList());
		model.addAttribute("travelY", travel);
		
		return "travel_history_form";
	}
}
