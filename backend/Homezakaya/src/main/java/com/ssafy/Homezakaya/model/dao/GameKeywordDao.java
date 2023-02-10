package com.ssafy.Homezakaya.model.dao;

import com.ssafy.Homezakaya.model.dto.GameKeywordDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface GameKeywordDao {
    List<GameKeywordDto> keywordList();
}
