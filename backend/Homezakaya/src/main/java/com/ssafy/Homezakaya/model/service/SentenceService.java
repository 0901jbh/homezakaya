package com.ssafy.Homezakaya.model.service;

import java.util.List;

import com.ssafy.Homezakaya.model.dto.SentenceDto;
public interface SentenceService {
	List<SentenceDto> sentenceList();

	int calculateAccuracy(String s1, String s2);
}