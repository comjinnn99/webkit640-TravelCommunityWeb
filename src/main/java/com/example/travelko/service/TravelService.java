package com.example.travelko.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.travelko.DataNotFoundException;
import com.example.travelko.entity.Recruit;
import com.example.travelko.entity.SiteUser;
import com.example.travelko.entity.Travel;
import com.example.travelko.repository.TravelRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TravelService {
	private final TravelRepository travelRepository;
	
	public Travel create(Recruit recruit, SiteUser user) {
		Travel t = new Travel();
		t.setCreateDate(LocalDateTime.now());
		t.setAccept("0");
		t.setRecruit(recruit);
		t.setSiteUser(user);
		
		return this.travelRepository.save(t);
	}
	
	public List<Travel> getList(Integer recruitId){
		return this.travelRepository.findByRecruitId(recruitId);
	}
	
	public Travel getTravel(Integer id) {
		Optional<Travel> travel = this.travelRepository.findById(id);
		if (travel.isPresent()) { // 해당 데이터가 있는지 검사하는 로직
			return travel.get();
		} else {
			throw new DataNotFoundException("travel not found");
		}
	}

	public void modify(Travel travel) {
		this.travelRepository.save(travel);
	}

	public void delete(Travel travel) {
		this.travelRepository.delete(travel);
	}
}
