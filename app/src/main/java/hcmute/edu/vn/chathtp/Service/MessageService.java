package hcmute.edu.vn.chathtp.Service;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import hcmute.edu.vn.chathtp.Adapter.MessageAdapter;
import hcmute.edu.vn.chathtp.Helper.MessageCallBack;
import hcmute.edu.vn.chathtp.MessageActivity;
import hcmute.edu.vn.chathtp.Model.Message;
import hcmute.edu.vn.chathtp.Model.User;
import hcmute.edu.vn.chathtp.Remote.SharedPrefManager;

public class MessageService {
    DatabaseReference reference, reference1, referenceSend, referenceSeen;
    ValueEventListener seenListener;
    FirebaseUser firebaseUser;
    User currentUser, user;

    public MessageService() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUser = SharedPrefManager.getUser();
    }

    // load message when user click on a friend
    public synchronized void loadMessage(User user, MessageCallBack messageCallBack) {
        // ...
        reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUser_id());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageCallBack.onLoadMessageSuccess(user);
//                readMessages(firebaseUser.getUid(), user.getUser_id(), user.getAvatar());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                messageCallBack.onError(error.getMessage());
            }
        });
    }
    public void outMessage(){
        reference.removeEventListener(seenListener);
    }
    // read message from firebase
    public synchronized void readMessage(User u, List<Message> messages, MessageCallBack callBack) {
        reference = FirebaseDatabase.getInstance().getReference("Messages");
        Query messagesQuery = reference.orderByKey().limitToLast(100); //Giới hạn số lượng tin nhắn được truy vấn
        messagesQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                for (DataSnapshot snapshot_index : snapshot.getChildren()) {
                    Message message = snapshot_index.getValue(Message.class);
                    if (message.getReceiver().getUser_id().equals(currentUser.getUser_id()) && message.getSender().getUser_id().equals(u.getUser_id()) ||
                            message.getReceiver().getUser_id().equals(u.getUser_id()) && message.getSender().getUser_id().equals(currentUser.getUser_id())) {
                        messages.add(message);
                    }
                }
                callBack.onReadMessageSuccess(messages, u.getAvatar());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public synchronized void seenMessage(User sender, MessageCallBack callBack) {
        reference = FirebaseDatabase.getInstance().getReference("Messages");
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Message message1 = null;
                DataSnapshot snapshot1 = null;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message message = snapshot.getValue(Message.class);
                    if (message.getReceiver().getUser_id().equals(currentUser.getUser_id())
                            && message.getSender().getUser_id().equals(sender.getUser_id())) {
                        message1 = snapshot.getValue(Message.class);
                        snapshot1 = snapshot;
                    }
                }
                if (message1 != null) {
                    if (!message1.getSender().getUser_id().equals(currentUser.getUser_id())) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isseen", true);
                        snapshot1.getRef().updateChildren(hashMap);
                    }
                }
                callBack.onSeenMessageSuccess();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public synchronized void sendMessage(User sender, User receiver, String message) {
        String message_id = UUID.randomUUID().toString();
        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message_id", message_id);
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("isseen", false);
        reference2.child("Messages").push().setValue(hashMap);
    }
}
