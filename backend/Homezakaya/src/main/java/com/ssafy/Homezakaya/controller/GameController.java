package com.ssafy.Homezakaya.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ssafy.Homezakaya.model.dto.GameKeywordDto;
import com.ssafy.Homezakaya.model.service.GameKeywordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

	@Autowired
	private GameKeywordService gameKeywordService;


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
	
	// 정확도 검사
	@PostMapping("/accuracy")
	private ResponseEntity<?> getAccuracy(@RequestBody SentenceDto sentenceDto) {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			int result = sentenceService.calculateAccuracy(sentenceDto.getContent(), sentenceDto.getStrPerson());
			resultMap.put("accuracy", result);
			return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
	}

	@GetMapping("/keyword")
	private ResponseEntity<?> getKeyword(){
		Map<String, Object> resultMap = new HashMap<>();
		try{
			List<GameKeywordDto> list = gameKeywordService.keywordList();
			if(list == null || list.size() == 0) {
				resultMap.put("message", "keyword 목록이 존재하지 않음");
				return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.NOT_FOUND);
			}else{
				int random = (int)(Math.random()* (list.size()-1));
				String result = list.get(random).getContent();

				resultMap.put("content", result);
				resultMap.put("message", SUCCESS);
				return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
			}
		}catch (Exception e){
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
	}
}