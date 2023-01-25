package com.ssafy.Homezakaya.model.service;

import com.ssafy.Homezakaya.model.dto.UserDto;

public class UserServiceImpl implements UserService{
    @Override
    public void createUser(UserDto user) {

    }

    @Override
    public boolean modifyUser(UserDto user) {
        return false;
    }

    @Override
    public UserDto getUser(String userId) {
        return null;
    }

    @Override
    public boolean removeUser(String userId) {
        return false;
    }

    @Override
    public double averageOfMannerPoint(String userId) {
        return 0;
    }
}
