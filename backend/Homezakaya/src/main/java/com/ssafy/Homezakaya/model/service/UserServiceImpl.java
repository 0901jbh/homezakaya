package com.ssafy.Homezakaya.model.service;

import com.ssafy.Homezakaya.model.dao.UserDao;
import com.ssafy.Homezakaya.model.dto.UserDto;
import com.ssafy.Homezakaya.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
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

        double mp = originUser.getMannerPoint();
        int ep = originUser.getEvaluatedCount();

        double sum = mp * (double) ep + mannerPoint;

        int nep = ep+1;
        double nmp = sum / (double) nep;
        originUser.setMannerPoint(nmp);
        originUser.setEvaluatedCount(nep);

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

    // 유저 상태 수정
    @Override
    public int modifyUserState(String userId, String state) {
        return userDao.updateUserState(userId, state);
    }

    @Override
    public UserDto findByEmail(String email) {
        return userDao.findUserIdByEmail(email);
    }

}
