package hcmute.edu.vn.chathtp.Service;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import hcmute.edu.vn.chathtp.Model.User;
import hcmute.edu.vn.chathtp.Remote.SharedPrefManager;

public class SearchService {
    private DatabaseReference reference; // DatabaseReference cho Firebase
    private FirebaseUser firebaseUser; // FirebaseUser hiện tại đang đăng nhập
    private StorageReference storageReference; // StorageReference cho Firebase

    public SearchService() {
        // Khởi tạo các biến Firebase
        reference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    // Search friend by phone
    public synchronized List<User> searchFriendByPhone(String phone, Context ctx) {
        List<User> users = new ArrayList<>();
        User currentUser = SharedPrefManager.getUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        // retrieve all users
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (!currentUser.getUser_id().equals(user.getUser_id()) && user.getPhone().equals(phone)) {
                        users.add(user);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ctx, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return users;
    }
}
