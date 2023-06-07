package hcmute.edu.vn.chathtp.Service;

import android.view.View;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

import hcmute.edu.vn.chathtp.Helper.FriendProfileCallBack;
import hcmute.edu.vn.chathtp.Model.Friend;
import hcmute.edu.vn.chathtp.Model.User;
import hcmute.edu.vn.chathtp.Remote.SharedPrefManager;

public class FriendProfileService {
    private DatabaseReference reference; // DatabaseReference cho Firebase
    private FirebaseUser firebaseUser; // FirebaseUser hiện tại đang đăng nhập
    private StorageReference storageReference; // StorageReference cho Firebase
    private User currentUser; // User hiện tại

    public FriendProfileService() {
        // TODO implement here
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    // check if user is friend
    public synchronized void checkIsFriend(FriendProfileCallBack callBack, User user) {
        // TODO implement here
        currentUser = SharedPrefManager.getUser();
        reference = FirebaseDatabase.getInstance().getReference("Friends");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) { // Event listener khi data thay đổi
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Friend friend = dataSnapshot.getValue(Friend.class);

                    if (friend.getSender().getUser_id().equals(currentUser.getUser_id())
                            && friend.getReceiver().getUser_id().equals(user.getUser_id())) {
                        if (friend.getStatus().equals("Waiting") || friend.getStatus().equals("Refused")) {
                            callBack.onIsFriendCallBack("Waiting");
                        } else if (friend.getStatus().equals("Accepted")) {
                            callBack.onIsFriendCallBack("Accepted");
                        }
                    } else if (friend.getSender().getUser_id().equals(user.getUser_id())
                            && friend.getReceiver().getUser_id().equals(currentUser.getUser_id())) {
                        if (friend.getStatus().equals("Waiting") || friend.getStatus().equals("Refused")) {
                            callBack.onIsFriendCallBack("Confirm");
                        } else if (friend.getStatus().equals("Accepted")) {
                            callBack.onIsFriendCallBack("Accepted");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { // Event listener khi cancel
                callBack.onErrorMessageCallBack(error.getMessage());
            }
        });
    }

    // confirm friend
    public void confirmFriend(FriendProfileCallBack callBack, User user) {
        // TODO implement here
        reference = FirebaseDatabase.getInstance().getReference("Friends");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) { // Event listener khi data thay đổi
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Friend friend = dataSnapshot.getValue(Friend.class);
                    if (friend.getSender().getUser_id().equals(user.getUser_id())
                            && friend.getReceiver().getUser_id().equals(currentUser.getUser_id())) {
                        friend.setStatus("Accepted");
                        reference.child(friend.getFriendId()).setValue(friend)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        callBack.onConfirmRequest("Confirm friend successfully");
                                    } else {
                                        callBack.onErrorMessageCallBack("Confirm friend failed");
                                    }
                                });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { // Event listener khi cancel
                callBack.onErrorMessageCallBack(error.getMessage());
            }
        });
    }

    // send request friend
    public void sendRequestFriend(FriendProfileCallBack callBack, User user) {
        // TODO implement here
        String friendId = UUID.randomUUID().toString();
        reference = FirebaseDatabase.getInstance().getReference("Friends");
        Friend friend = new Friend();
        friend.setFriendId(friendId);
        friend.setSender(currentUser);
        friend.setReceiver(user);
        friend.setStatus("Waiting");
        reference.child(friendId).setValue(friend)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callBack.onSendRequestFriendCallBack("Send request friend successfully");
                    } else {
                        callBack.onErrorMessageCallBack("Send request friend failed");
                    }
                });
    }
}
