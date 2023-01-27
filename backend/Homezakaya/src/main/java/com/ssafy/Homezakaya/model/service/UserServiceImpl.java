package com.ssafy.Homezakaya.model.service;

import com.ssafy.Homezakaya.model.dao.UserDao;
import com.ssafy.Homezakaya.model.dto.UserDto;
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
    public UserDto checkId(String userId) {
        return userDao.selectUserById(userId);
    }

    @Override
    public UserDto checkNickname(String nickname) {
        return userDao.selectUserByNickname(nickname);
    }

    @Override
    public boolean modifyUser(UserDto user) {
        UserDto originUser = userDao.selectUserById(user.getUserId());
        // 수정 가능 정보 :  비밀번호, 이메일, 알콜 도수, 닉네임
        originUser.setPassword(user.getPassword());
        originUser.setEmail(user.getEmail());
        originUser.setAlcoholPoint(user.getAlcoholPoint());
        originUser.setNickname(user.getNickname());

        return userDao.updateUser(originUser) == 1;
    }

    @Override
    public UserDto getUser(String userId) {
        return userDao.selectUserById(userId);
    }

    @Override
    public boolean removeUser(String userId) {
        return userDao.deleteUser(userId) == 1;
    }

    // 매너도수 누적
    @Override
    public boolean addMannerPoint(String userId, double mannerPointSum) {
        UserDto originUser = userDao.selectUserById(userId);

        originUser.setMannerPointSum(originUser.getMannerPointSum() + mannerPointSum);
        originUser.setEvaluatedCount(originUser.getEvaluatedCount() + 1);

        return userDao.addMannerPoint(originUser) == 1;
    }

    // 매너도수 평균
    @Override
    public double averageOfMannerPoint(String userId) {
        // 여기서 계산
        double mannerPoint = userDao.selectMannerPoint(userId);
        int evaluatePerson = userDao.selectEvaluateCount(userId);

        System.out.println(mannerPoint);
        return (double) (mannerPoint / evaluatePerson);
    }
}
