package hcmute.edu.vn.chathtp.Helper;

public interface ForgotPasswordCallBack {
    void onSendPasswordResetEmailSuccess(String message);
    void onError(String message);
    void onUpdatePasswordSuccess(String message);
}
