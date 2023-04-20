package com.example.travelko.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class SiteUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @Column(unique = true)
    private String email;
    
    private String age;
    
    private String gender;
    
    private String phone;
    
    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
    private List<Recruit> recruitList;
    
    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
    private List<Reply> replyList;
    
    @OneToMany(mappedBy = "siteUser", cascade = CascadeType.REMOVE)
    private List<Travel> travelList;
}
