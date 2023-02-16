package com.ssafy.Homezakaya.model.service;

import com.ssafy.Homezakaya.model.dao.InviteFriendDao;
import com.ssafy.Homezakaya.model.dto.InviteFriendDto;
import com.ssafy.Homezakaya.model.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InviteFriendServiceImpl implements InviteFriendService {

    @Autowired
    InviteFriendDao inviteFriendDao;

    @Override
    public boolean createInvite(InviteFriendDto inviteFriendDto) {
        return inviteFriendDao.inviteFriend(inviteFriendDto) > 0;
    }

    @Override
    public List<String> getInvites(String toUserId) {
        return inviteFriendDao.inviteList(toUserId);
    }

    @Override
    public boolean removeInvite(InviteFriendDto inviteFriendDto) {
        return inviteFriendDao.deleteInvite(inviteFriendDto) > 0;
    }

    @Override
    public List<UserDto> getInviteValidFriends(String fromUserId) {
        return inviteFriendDao.selectInviteValidFriends(fromUserId);
    }
}
