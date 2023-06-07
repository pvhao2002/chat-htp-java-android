package hcmute.edu.vn.chathtp.Service;

import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import hcmute.edu.vn.chathtp.Helper.ChatCallBack;
import hcmute.edu.vn.chathtp.Model.Message;
import hcmute.edu.vn.chathtp.Model.User;

public class ChatService {
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    public ChatService(){
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    // get all chat list
    public synchronized void getGetAllUser(List<User> userList, ChatCallBack callBack){
        reference = FirebaseDatabase.getInstance().getReference("Messages");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot snapshot_index : snapshot.getChildren()) {
                    Message message = snapshot_index.getValue(Message.class);
                    if (message.getSender().getUser_id().equals(firebaseUser.getUid())) {
                        userList.add(message.getReceiver());
                    }
                    if (message.getReceiver().getUser_id().equals(firebaseUser.getUid())) {
                        userList.add(message.getSender());
                    }
                }
                callBack.onGetAllUserSuccess(userList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callBack.onError(error.getMessage());
            }
        });
    }

    public synchronized void getLastMessage(User user, TextView tv, ChatCallBack callBack){
        String userId = user.getUser_id();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Messages");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String theLastMessage = "You can now chat with " + user.getUsername();
                boolean isBold = true;
                // get last message in list chat
                for (DataSnapshot snapshot_index : snapshot.getChildren()) {
                    Message message = snapshot_index.getValue(Message.class);
                    assert message != null;
                    assert firebaseUser != null;
                    if (message.getReceiver().getUser_id().equals(firebaseUser.getUid()) && message.getSender().getUser_id().equals(userId)) {
                        theLastMessage = message.getMessage();
                        isBold = !message.isIsseen();
                    } else {
                        if (message.getReceiver().getUser_id().equals(userId) && message.getSender().getUser_id().equals(firebaseUser.getUid())) {
                            theLastMessage = "Bạn: " + message.getMessage();
                        }
                        isBold = !message.isIsseen();
                    }
                }
                callBack.onGetLastMessage(theLastMessage, tv, isBold);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callBack.onError(error.getMessage());
            }
        });
    }
}
