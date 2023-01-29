package com.ssafy.Homezakaya.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.Homezakaya.model.dao.SentenceDao;
import com.ssafy.Homezakaya.model.dto.SentenceDto;

@Service
public class SentenceServiceImpl implements SentenceService {
	
	private SentenceDao sentenceDao;
	
	@Autowired
	public SentenceServiceImpl(SentenceDao sentenceDao) {
		super();
		this.sentenceDao = sentenceDao;
	}
	@Override
	public List<SentenceDto> sentenceList() {
		return sentenceDao.sentenceList();
	}
	
}
