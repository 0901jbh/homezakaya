package com.ssafy.Homezakaya.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.Homezakaya.model.dto.SentenceDto;
import com.ssafy.Homezakaya.model.dto.TopicDto;
import com.ssafy.Homezakaya.model.service.SentenceService;
import com.ssafy.Homezakaya.model.service.TopicService;

@RequestMapping("/api/games")
@RestController
public class GameController{
	
	@Autowired
	private TopicService topicService;
	@Autowired
	private SentenceService sentenceService;
	@GetMapping("/topic")
	private ResponseEntity<String> noticeList(){
		List<TopicDto> list = topicService.topicList();
		if(list != null) {
			int ls = list.size();
			int random = (int)(Math.random()* (ls-1));
			String result = list.get(random).getContent();
			
			return ResponseEntity.ok(result);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/sentence")
	private ResponseEntity<String> sentenceList(){
		List<SentenceDto> list = sentenceService.sentenceList();
		if(list != null) {
			int ls = list.size();
			int random = (int)(Math.random()* (ls-1));
			String result = list.get(random).getContent();

			return ResponseEntity.ok(result);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	
	
	
}