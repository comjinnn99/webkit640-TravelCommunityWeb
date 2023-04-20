package com.example.travelko.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Table(name = "SiteUser")
@Getter
@Setter
@Entity
public class SiteUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String age;
    
    @Column(nullable = false)
    private String gender;
    
    @Column(nullable = false)
    private String phone;
    
    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
    private List<Recruit> recruitList;
    
    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
    private List<Reply> replyList;
    
    @OneToMany(mappedBy = "siteUser", cascade = CascadeType.REMOVE)
    private List<Travel> travelList;
}
