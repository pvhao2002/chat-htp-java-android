package hcmute.edu.vn.chathtp.Helper;

public interface ProfileCallBack {
    void onUpdateProfileSuccess(String message);
    void onError(String message);
    void onUpdatePasswordSuccess(String message);
}
