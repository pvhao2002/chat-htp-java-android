package hcmute.edu.vn.chathtp.Helper;

import android.widget.TextView;

import java.util.List;

import hcmute.edu.vn.chathtp.Model.User;

public interface ChatCallBack {
    void onGetAllUserSuccess(List<User> userList);

    void onError(String message);

    void onGetLastMessage(String message, TextView tv, boolean b);
}
