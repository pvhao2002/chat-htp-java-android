package hcmute.edu.vn.chathtp.Helper;

import java.util.List;

import hcmute.edu.vn.chathtp.Model.Message;
import hcmute.edu.vn.chathtp.Model.User;

public interface MessageCallBack {
    void onLoadMessageSuccess(User user);

    void onError(String message);

    void onReadMessageSuccess(List<Message> messages, String avatar);
    void onSeenMessageSuccess();
}
