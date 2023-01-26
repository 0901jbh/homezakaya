package com.ssafy.Homezakaya.model.service;

import com.ssafy.Homezakaya.model.dao.UserDao;
import com.ssafy.Homezakaya.model.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public void createUser(UserDto user) {
        userDao.insertUser(user);
    }

    @Override
    public boolean modifyUser(UserDto user) {
        UserDto originUser = userDao.selectUserById(user.getUserId());

        // 확인용
        System.out.println("originUser.getUserId() = " + originUser.getUserId());
        System.out.println("user.getUserId() = " + user.getUserId());

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

    @Override
    public double averageOfMannerPoint(String userId) {
        return userDao.averageOfMannerPoint(userId);
    }
}
