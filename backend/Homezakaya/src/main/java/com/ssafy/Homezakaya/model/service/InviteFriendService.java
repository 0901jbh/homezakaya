package com.ssafy.Homezakaya.model.service;

import com.ssafy.Homezakaya.model.dto.InviteFriendDto;

import java.util.List;

public interface InviteFriendService {
    boolean createInvite(InviteFriendDto inviteFriendDto);
    List<String> getInvites(String toUserId);
    boolean removeInvite(InviteFriendDto inviteFriendDto);
}
