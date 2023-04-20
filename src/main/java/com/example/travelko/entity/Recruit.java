package com.example.travelko.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Table(name = "Recruit")
@Getter
@Setter
@Entity
public class Recruit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 200)
    private String subject;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    
    @Column(nullable = false)
    private LocalDateTime createDate;

    @Column(nullable = false, length = 100)
    private String region;
    
    @Column(nullable = false, length = 100)
    private String startDate;
    @Column(nullable = false, length = 100)
    private String endDate;
    
    @OneToMany(mappedBy = "recruit", cascade = CascadeType.REMOVE)
    private List<Reply> replyList;
    
    @ManyToOne
    private SiteUser author;
    
    private LocalDateTime modifyDate;
    
    @OneToMany(mappedBy = "recruit", cascade = CascadeType.REMOVE)
    private List<Travel> travelList;
}
