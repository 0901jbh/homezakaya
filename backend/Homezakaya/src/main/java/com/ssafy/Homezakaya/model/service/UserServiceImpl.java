package com.ssafy.Homezakaya.model.service;

import com.ssafy.Homezakaya.model.dao.UserDao;
import com.ssafy.Homezakaya.model.dto.UserDto;
import com.ssafy.Homezakaya.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public void createUser(UserDto user) {
        userDao.insertUser(user);
    }

    @Override
    public UserDto getUser(String userId) {
        return userDao.getUser(userId);
    }

    @Override
    public UserDto checkNickname(String nickname) {
        return userDao.checkNickname(nickname);
    }

    // 수정 가능 정보 :  비밀번호, 이메일, 알콜 도수, 닉네임
    @Override
    public boolean modifyUser(UserDto user) {
        return userDao.updateUser(user) == 1;
    }

    @Override
    public boolean removeUser(String userId) {
        return userDao.deleteUser(userId) == 1;
    }

    // 매너도수 평균
    @Override
    public boolean averageOfMannerPoint(String userId, double mannerPoint) {
        UserDto originUser = userDao.getUser(userId);

        double updateMannerPoint = (originUser.getMannerPoint() * originUser.getEvaluatedCount()) + mannerPoint; // 누적점수
        originUser.setEvaluatedCount(originUser.getEvaluatedCount() + 1);   // 평가수 + 1

        double avgMannerPoint = updateMannerPoint / (double) originUser.getEvaluatedCount();
        originUser.setMannerPoint(Math.round(avgMannerPoint * 10) / 10.0);   // 매너점수 update

        return userDao.updateMannerPoint(originUser) == 1;
    }

    @Override
    public void addTokenInfo(UserDto user) {
        userDao.addTokenInfo(user);
    }

    @Override
    public void removeTokenInfo(UserDto user) {
        userDao.deleteTokenInfo(user);
    }

}
