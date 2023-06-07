package hcmute.edu.vn.chathtp.Service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import hcmute.edu.vn.chathtp.Helper.LoginCallBack;
import hcmute.edu.vn.chathtp.Model.User;

public class LoginService {
    FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    FirebaseUser mFirebaseUser;

    public LoginService() {
        mAuth = FirebaseAuth.getInstance();
    }

    // sign in user with email and password
    public synchronized void signInUser(String email, String password, LoginCallBack loginCallBack) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!mAuth.getCurrentUser().isEmailVerified()) {
                            loginCallBack.onError("Please check your email to enable account.");
                            return;
                        }
                        // Sign in success, update UI with the signed-in user's information
                        loginCallBack.onLoginSuccess("Login success");
                    } else {
                        // If sign in fails, display a message to the user.
                        loginCallBack.onError("Login error");
                    }
                });
    }

    // update user is enabled if user verify email
    public synchronized void updateIsEnabled(LoginCallBack loginCallBack) {
        mFirebaseUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        mDatabase.child(mFirebaseUser.getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                User user = task.getResult().getValue(User.class);
                if (user != null) {
                    if (user.isVerified()) {
                        loginCallBack.isVerified(user);
                    } else {
                        loginCallBack.onEnableAccount(user);
                    }
                } else {
                    loginCallBack.onError("You don't have account.");
                }
            } else {
                loginCallBack.onError("Please check your email to enable account.");
            }
        });
    }

    // enable account
    public synchronized void enableAccount(User user, LoginCallBack loginCallBack) {
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        mDatabase.child(user.getUser_id())
                .child("verified")
                .setValue(true)
                .addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                loginCallBack.onEnableAccountSuccess("Enable account success", user);
            } else {
                loginCallBack.onError("Login error");
            }
        });
    }

}
