package com.ssafy.Homezakaya.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.Homezakaya.model.dto.TopicDto;
import org.springframework.stereotype.Repository;

@Mapper
public interface TopicDao {

	List<TopicDto> topicList();
}