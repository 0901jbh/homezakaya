package com.ssafy.Homezakaya.model.service;

import com.ssafy.Homezakaya.model.dao.FriendDao;
import com.ssafy.Homezakaya.model.dao.UserDao;
import com.ssafy.Homezakaya.model.dto.FriendDto;
import com.ssafy.Homezakaya.model.dto.UserDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendServiceImpl implements FriendService{

    @Autowired
    FriendDao fDao;

    @Autowired
    private SqlSession sqlSession;

    // 해당 유저의 친구 목록 조회
    @Override
    public List<UserDto> getFriendsById(String userId) {
        // 존재하는 유저인지 확인하기
        UserDto userDto = fDao.selectUserById(userId);
        if( userDto == null){
            return null;
        }
        return fDao.selectFriendsById(userId); // 친구 목록 리턴
    }

    // 친구 삭제 (친구 연결 끊기)
    @Override
    public int removeFriend(FriendDto friendDto) {
        // 친구 연결 1 (A-B 연결)
        int result1 = fDao.deleteFriend(friendDto);

        // 친구 연결 2 (B-A 연결)
        FriendDto friendDto2 = new FriendDto(friendDto.getUserBId(), friendDto.getUserAId(), friendDto.isConnected());
        int result2 = fDao.deleteFriend(friendDto2);
        if(result1 ==1 && result2==1) return 1;
        else return 0;
    }

    // 친구 요청(A->B)
    @Override
    public int createFriend(FriendDto friendDto) {
        // A->B 요청이 존재하는지 확인(이미 존재하는 요청인지 체크)
        if(fDao.selectFriendReq(friendDto) != null) return 0;

        FriendDto friendDto2 = new FriendDto(friendDto.getUserBId(), friendDto.getUserAId(), friendDto.isConnected());
        // B->A 요청이 존재하는지 확인
        if(fDao.selectFriendReq(friendDto2) != null) // 1. 존재한다면
        {
            // B->A 관계 true로 update
            fDao.updateFriend(new FriendDto(friendDto.getUserBId(), friendDto.getUserAId(), true));
            // A->B (true) insert
            fDao.insertFriend(new FriendDto(friendDto.getUserAId(), friendDto.getUserBId(), true));
        }
        // 2. 존재하지 않는다면
        else {
            // A->B (false) insert
            fDao.insertFriend(new FriendDto(friendDto.getUserAId(), friendDto.getUserBId(), false));
        }

        return 1;
    }

    // 친구 요청 리스트 조회
    @Override
    public List<UserDto> getFriendReqById(String userId) {
        UserDto userDto = fDao.selectUserById(userId);
        if( userDto == null){
            return null;
        }else return fDao.selectFriendReqById(userId);
    }

    // 친구 요청 수락
    @Override
    public int modifyFriend(FriendDto friendDto) {
        // B->A 관계 true로 update
        int result1 = fDao.updateFriend(new FriendDto(friendDto.getUserBId(), friendDto.getUserAId(), true));
        // A->B (true) insert
        int result2 = fDao.insertFriend(new FriendDto(friendDto.getUserAId(), friendDto.getUserBId(), true));

        if(result1 == 1 && result2 == 1) return 1;
        else return 0;
    }

    // 친구 요청 거절/취소
    @Override
    public int removeFriendReq(FriendDto friendDto) {
        // 존재하는 유저인지 확인하기
        UserDto userDto1 = fDao.selectUserById(friendDto.getUserAId());
        UserDto userDto2 = fDao.selectUserById(friendDto.getUserBId());
        if(userDto2 == null || userDto1 == null){
            return 0;
        }

        return fDao.deleteFriend(friendDto);
    }

    // 유저 조회(검색)
    @Override
    public List<UserDto> getUserByKeyword(String keyword) {
        return fDao.selectUserByKeyword(keyword);
    }


}
