package com.ssafy.Homezakaya.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";
	@Autowired
	private TopicService topicService;
	@Autowired
	private SentenceService sentenceService;
	@GetMapping("/topic")
	private ResponseEntity<?> noticeList(){
		Map<String, Object> resultMap = new HashMap<>();
		List<TopicDto> list = topicService.topicList();
		try{
			if(list.size() > 0) {
				int ls = list.size();
				int random = (int)(Math.random()* (ls-1));
				String result = list.get(random).getContent();

				resultMap.put("content", result);
				resultMap.put("message", SUCCESS);
				return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
			} else {
				resultMap.put("message", FAIL);
				return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.NOT_FOUND);
			}
		}catch (Exception e){
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}

	}
	
	@GetMapping("/sentence")
	private ResponseEntity<?> sentenceList(){
		Map<String, Object> resultMap = new HashMap<>();
		try{
			List<SentenceDto> list = sentenceService.sentenceList();
			if(list.size() > 0) {
				int ls = list.size();
				int random = (int)(Math.random()* (ls-1));
				String result = list.get(random).getContent();

				resultMap.put("content", result);
				resultMap.put("message", SUCCESS);
				return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
			} else {
				resultMap.put("message", "문장이 존재하지 않음");
				return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.NOT_FOUND);
			}
		}catch (Exception e){
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
	}
	
	
	
	
}