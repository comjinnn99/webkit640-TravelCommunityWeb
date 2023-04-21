package com.example.travelko.service;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.travelko.DataNotFoundException;
import com.example.travelko.entity.SiteUser;
import com.example.travelko.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SiteUser create(
    		String username,
    		String email,
    		String password,
    		String age,
    		String gender,
    		String phone
    		) {
        SiteUser user = new SiteUser();
        user.setUsername(username);
        user.setEmail(email);
        // BCrypt 해싱 함수(BCrypt hashing function)를 사용해서 비밀번호를 암호화
        user.setPassword(passwordEncoder.encode(password));
        user.setAge(age);
        user.setGender(gender);
        user.setPhone(phone);
        this.userRepository.save(user);
        return user;
    }
    
    public SiteUser getUser(String username) {
        Optional<SiteUser> siteUser = this.userRepository.findByUsername(username);
        if (siteUser.isPresent()) {
            return siteUser.get();
        } else {
            throw new DataNotFoundException("siteuser not found");
        }
    }
    
    public void delete(String username) {
    	Optional<SiteUser> siteUser = this.userRepository.findByUsername(username);
        if (siteUser.isPresent()) {
        	SiteUser delSiteUser = siteUser.get();
        	this.userRepository.deleteById(delSiteUser.getId());
        } else {
            throw new DataNotFoundException("siteuser not found");
        }
    }
    
    public void modify(SiteUser siteUser, String password, String email, String age, String phone) {
    	siteUser.setPassword(passwordEncoder.encode(password));
    	siteUser.setEmail(email);
    	siteUser.setAge(age);
    	siteUser.setPhone(phone);
    	this.userRepository.save(siteUser);
    }
}
