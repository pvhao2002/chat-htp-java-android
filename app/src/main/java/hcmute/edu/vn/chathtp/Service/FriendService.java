package hcmute.edu.vn.chathtp.Service;

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

import hcmute.edu.vn.chathtp.Adapter.FriendAdapter;
import hcmute.edu.vn.chathtp.Helper.FriendCallBack;
import hcmute.edu.vn.chathtp.Model.Friend;
import hcmute.edu.vn.chathtp.Model.User;
import hcmute.edu.vn.chathtp.Remote.SharedPrefManager;

public class FriendService {

    private DatabaseReference reference; //
    private FirebaseUser firebaseUser; //
    private StorageReference storageReference;
    private User currentUser;
    public FriendService() {
        // Required empty public constructor
        currentUser = SharedPrefManager.getUser();
    }

    // get all friend
    public synchronized void getAllFriend(FriendCallBack callBack) {
        List<User> mFriend = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Friends");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mFriend.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Friend friend = dataSnapshot.getValue(Friend.class);
                    if (friend != null && friend.getStatus().equals("Accepted")) {
                        if (friend.getReceiver().getUser_id().equals(currentUser.getUser_id())
                                && friend.getStatus().equals("Accepted")) {
                            User sender = friend.getSender();
                            mFriend.add(sender);
                        } else if (friend.getSender().getUser_id().equals(currentUser.getUser_id())
                                && friend.getStatus().equals("Accepted")) {
                            User receiver = friend.getReceiver();
                            mFriend.add(receiver);
                        }
                    }
                }
                callBack.onGetAllFriendSuccess(mFriend);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { // Event listener khi cancel
            }
        });
    }

}
