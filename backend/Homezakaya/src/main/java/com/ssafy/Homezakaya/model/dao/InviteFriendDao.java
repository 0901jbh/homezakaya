package com.ssafy.Homezakaya.model.dao;

import com.ssafy.Homezakaya.model.dto.InviteFriendDto;
import com.ssafy.Homezakaya.model.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface InviteFriendDao {

    int inviteFriend(InviteFriendDto inviteFriendDto);

    // 초대한 유저 id만 (fromUser)
    List<String> inviteList(String toUserId);

    int deleteInvite(InviteFriendDto inviteFriendDto);

    List<UserDto> selectInviteValidFriends(String fromUserId);
}
