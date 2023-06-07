package hcmute.edu.vn.chathtp.Helper;

public interface FriendProfileCallBack {
    void onIsFriendCallBack(String status);
    void onErrorMessageCallBack(String message);
    void onSendRequestFriendCallBack(String message);
    void onIsReceivedRequest(String message);
    void onConfirmRequest(String message);
}
