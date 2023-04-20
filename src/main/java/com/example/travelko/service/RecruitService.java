package com.example.travelko.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.travelko.DataNotFoundException;
import com.example.travelko.entity.Recruit;
import com.example.travelko.entity.Reply;
import com.example.travelko.entity.SiteUser;
import com.example.travelko.repository.RecruitRepository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RecruitService {
	private final RecruitRepository recruitRepository;

	public Page<Recruit> getList(int page, String kw) {
		// 객체 역순 조회
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("createDate"));
		// page는 조회할 페이지의 번호, 19은 한 페이지에 보여줄 게시물 갯수
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		Specification<Recruit> spec = search(kw);
		return this.recruitRepository.findAll(spec, pageable);
	}
	
	public List<Recruit> getMyRecruitList(Long authorId){
		return this.recruitRepository.findAllByAuthorId(authorId);
	}

	// id 값으로 Recruit 데이터 조회
	public Recruit getRecruit(Integer id) {
		Optional<Recruit> recruit = this.recruitRepository.findById(id);
		if (recruit.isPresent()) { // 해당 데이터가 있는지 검사하는 로직
			return recruit.get();
		} else {
			throw new DataNotFoundException("recruit not found");
		}
	}

	public Recruit create(String subject, String content, String region, String startDate, String endDate, SiteUser user) {
		Recruit q = new Recruit();
		q.setSubject(subject);
		q.setContent(content);
		q.setCreateDate(LocalDateTime.now());
		q.setRegion(region);
		q.setStartDate(startDate);
		q.setEndDate(endDate);
		q.setAuthor(user);
		return this.recruitRepository.save(q);
	}

	public void modify(Recruit recruit, String subject, String content, String region, String startDate,
			String endDate) {
		recruit.setSubject(subject);
		recruit.setContent(content);
		recruit.setRegion(region);
		recruit.setStartDate(startDate);
		recruit.setEndDate(endDate);
		recruit.setModifyDate(LocalDateTime.now());
		this.recruitRepository.save(recruit);
	}

	public void delete(Recruit question) {
		this.recruitRepository.delete(question);
	}

	private Specification<Recruit> search(String kw) {
		return new Specification<>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Recruit> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
				query.distinct(true); // 중복을 제거
				Join<Recruit, SiteUser> u1 = q.join("author", JoinType.LEFT);
				Join<Recruit, Reply> a = q.join("replyList", JoinType.LEFT);
				Join<Reply, SiteUser> u2 = a.join("author", JoinType.LEFT);
				return cb.or(cb.like(q.get("subject"), "%" + kw + "%"), // 제목
						cb.like(q.get("content"), "%" + kw + "%"), // 내용
						cb.like(q.get("region"), "%" + kw + "%")); // 내용
//						cb.like(u1.get("username"), "%" + kw + "%"), // 질문 작성자
//						cb.like(a.get("content"), "%" + kw + "%"), // 답변 내용
//						cb.like(u2.get("username"), "%" + kw + "%")); // 답변 작성자
			}
		};
	}

}
