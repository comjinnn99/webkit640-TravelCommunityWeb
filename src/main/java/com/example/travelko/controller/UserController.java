package com.example.travelko.controller;

import java.security.Principal;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.travelko.entity.SiteUser;
import com.example.travelko.form.RecruitForm;
import com.example.travelko.form.UserCreateForm;
import com.example.travelko.form.UserModifyForm;
import com.example.travelko.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm) {
        return "signup_form";
    }

    @PostMapping("/signup")
    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup_form";
        }

        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect", 
                    "2개의 패스워드가 일치하지 않습니다.");
            return "signup_form";
        }

        try {
            userService.create(
            		userCreateForm.getUsername(), 
                    userCreateForm.getEmail(),
                    userCreateForm.getPassword1(),
                    userCreateForm.getAge(),
                    userCreateForm.getGender(),
                    userCreateForm.getPhone()
                    );
        }catch(DataIntegrityViolationException e) {
        	// 중복 회원가입 처리
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "signup_form";
        }catch(Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "signup_form";
        }

        return "redirect:/";
    }
    
    @GetMapping("/login")
    public String login() {
        return "login_form";
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/withdraw")
    public String withdraw(Principal principal) {
    	System.out.println("usercon >>> "+principal.getName());
    	this.userService.delete(principal.getName());
    	return "redirect:/user/logout";
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify")
    public String modify(UserModifyForm userModifyForm ,Principal principal) {
    	SiteUser siteUser = this.userService.getUser(principal.getName());
    	
    	userModifyForm.setEmail(siteUser.getEmail());
    	userModifyForm.setAge(siteUser.getAge());
    	userModifyForm.setPhone(siteUser.getPhone());
        
    	return "user_modify_form";
    }
    
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify")
    public String modify(@Valid UserModifyForm userModifyForm, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "user_modify_form";
        }
        if (!userModifyForm.getPassword1().equals(userModifyForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect", 
                    "2개의 패스워드가 일치하지 않습니다.");
            return "user_modify_form";
        }
    	SiteUser siteUser = this.userService.getUser(principal.getName());
    	
    	this.userService.modify(
    			siteUser,
    			userModifyForm.getPassword1(),
    			userModifyForm.getEmail(),
    			userModifyForm.getAge(),
    			userModifyForm.getPhone()
    			);
    	
    	return "redirect:/user/logout";
    }
}
