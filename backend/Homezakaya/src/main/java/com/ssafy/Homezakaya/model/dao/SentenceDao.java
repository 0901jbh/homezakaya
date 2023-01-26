package com.ssafy.Homezakaya.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.Homezakaya.model.dto.SentenceDto;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SentenceDao {

	List<SentenceDto> sentenceList();
}