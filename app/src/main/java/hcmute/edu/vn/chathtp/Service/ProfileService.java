package hcmute.edu.vn.chathtp.Service;

import android.util.Log;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import hcmute.edu.vn.chathtp.Helper.ProfileCallBack;
import hcmute.edu.vn.chathtp.Model.User;

public class ProfileService {
    private DatabaseReference reference; // DatabaseReference cho Firebase
    private FirebaseUser fireBaseUser; // FirebaseUser hiện tại đang đăng nhập
    private StorageReference storageReference; // StorageReference cho Firebase
    FirebaseAuth mAuth;

    public ProfileService() {
        mAuth = FirebaseAuth.getInstance();
        fireBaseUser = mAuth.getCurrentUser();
    }


    // update other information of fireBaseUser
    public void updateOtherInformation(String username, String dateOfBirth, String phoneNumber, ProfileCallBack callBack) {
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fireBaseUser.getUid());
        reference.child("username").setValue(username);
        reference.child("dob").setValue(dateOfBirth);
        reference.child("phone").setValue(phoneNumber);
        callBack.onUpdateProfileSuccess("Update other information success!");
    }

    // update password fireBaseUser
    public void updatePassword(User user, String password, ProfileCallBack callBack) {
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), user.getPassword());
        fireBaseUser.reauthenticate(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                fireBaseUser.updatePassword(password).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        callBack.onUpdatePasswordSuccess("Update password success!");
                    } else {
                        callBack.onError("Update password error!");
                        Log.e("Error1", task.getException().getMessage());
                    }
                });
            } else {
                callBack.onError("Update password error!");
                Log.e("Error2", task.getException().getMessage());
                Log.e("error3",user.getEmail() + ", " + user.getPassword());
            }
        });
    }
}
