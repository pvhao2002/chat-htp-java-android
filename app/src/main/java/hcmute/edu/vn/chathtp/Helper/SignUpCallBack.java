package hcmute.edu.vn.chathtp.Helper;

import hcmute.edu.vn.chathtp.Model.User;

public interface SignUpCallBack {
    void onSaveUserSuccess(String message, User user);
    void onSignUpSuccess(String message, User user);
    void onSendEmailSuccess(String message, User user);
    void onError(String message);
}
