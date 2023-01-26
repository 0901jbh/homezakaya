package com.ssafy.Homezakaya.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.Homezakaya.model.dao.TopicDao;
import com.ssafy.Homezakaya.model.dto.TopicDto;

@Service
public class TopicServiceImpl implements TopicService {
	
	private TopicDao topicDao;
	
	@Autowired
	public TopicServiceImpl(TopicDao topicDao) {
		super();
		this.topicDao = topicDao;
	}
	@Override
	public List<TopicDto> topicList() {
		return topicDao.topicList();
	}
	
}
