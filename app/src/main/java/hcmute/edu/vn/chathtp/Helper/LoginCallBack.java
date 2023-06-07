package hcmute.edu.vn.chathtp.Helper;

import hcmute.edu.vn.chathtp.Model.User;

public interface LoginCallBack {
    void onLoginSuccess(String message);
    void onError(String message);
    void onEnableAccount(User user);
    void isVerified(User user);
    void onEnableAccountSuccess(String message, User user);
}
