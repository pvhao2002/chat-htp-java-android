package hcmute.edu.vn.chathtp.Helper;

import java.util.List;

import hcmute.edu.vn.chathtp.Model.User;

public interface FriendCallBack {
    void onGetAllFriendSuccess(List<User> listFriend);
}
