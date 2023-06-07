package hcmute.edu.vn.chathtp.Service;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import hcmute.edu.vn.chathtp.Adapter.ConfirmAdapter;
import hcmute.edu.vn.chathtp.Helper.FriendProfileCallBack;
import hcmute.edu.vn.chathtp.Helper.NotifyCallBack;
import hcmute.edu.vn.chathtp.Model.Friend;
import hcmute.edu.vn.chathtp.Model.User;
import hcmute.edu.vn.chathtp.Remote.SharedPrefManager;

public class NotifyService {
    private DatabaseReference reference; // DatabaseReference cho Firebase
    private FirebaseUser firebaseUser; // FirebaseUser hiện tại đang đăng nhập
    private StorageReference storageReference; // StorageReference cho Firebase
    private User currentUser; // User hiện tại
    public NotifyService() {
        // TODO implement here
        currentUser = SharedPrefManager.getUser();
    }
    public synchronized void confirmFriend(NotifyCallBack callBack, User user) {
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
                                        callBack.error("Confirm friend failed");
                                    }
                                });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { // Event listener khi cancel
                callBack.error(error.getMessage());
            }
        });
    }

    // load list request friend
    public synchronized void loadListRequestFriend(NotifyCallBack callBack) {
        // TODO implement here
        List<Friend> listFriend = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Friends");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) { // Event listener khi data thay đổi

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Friend friend = dataSnapshot.getValue(Friend.class);
                    assert friend != null;
                    if(friend.getReceiver().getUser_id().equals(currentUser.getUser_id())
                            && friend.getStatus().equals("Waiting")){
                        listFriend.add(friend);
                        System.out.println("listFriend: " + listFriend.toString());
                    }
                }
                System.out.println("listFriend1: " + listFriend.toString());
                callBack.onLoadListFriend(listFriend);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { // Event listener khi cancel
                Log.e("Error", error.getMessage());
            }
        });
    }

    public synchronized void removeFriend(NotifyCallBack callBack, User requestUser) {
        reference = FirebaseDatabase.getInstance().getReference("Friends");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) { // Event listener khi data thay đổi
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Friend friend = dataSnapshot.getValue(Friend.class);
                    if (friend.getSender().getUser_id().equals(requestUser.getUser_id())
                            && friend.getReceiver().getUser_id().equals(currentUser.getUser_id())) {
                        friend.setStatus("Refused");
                        reference.child(friend.getFriendId()).setValue(friend)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        callBack.onConfirmRequest("You have refused friend request");
                                    } else {
                                        callBack.error("Refused friend failed");
                                    }
                                });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { // Event listener khi cancel
                callBack.error(error.getMessage());
            }
        });
    }
}
