package com.ssafy.Homezakaya.model.service;

import com.ssafy.Homezakaya.model.dao.GameKeywordDao;
import com.ssafy.Homezakaya.model.dto.GameKeywordDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameKeywordServiceImpl implements GameKeywordService{
    @Autowired
    private GameKeywordDao gDao;
    @Override
    public List<GameKeywordDto> keywordList() {
        return gDao.keywordList();
    }
}
