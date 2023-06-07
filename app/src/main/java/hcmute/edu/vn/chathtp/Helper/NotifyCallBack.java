package hcmute.edu.vn.chathtp.Helper;

import java.util.List;

import hcmute.edu.vn.chathtp.Model.Friend;

public interface NotifyCallBack {
    void onLoadListFriend(List<Friend> listFriend);
    void error(String message);
    void onConfirmRequest(String message);
    void onRemoveRequest(String message);
}
