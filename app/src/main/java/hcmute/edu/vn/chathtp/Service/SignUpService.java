package hcmute.edu.vn.chathtp.Service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import hcmute.edu.vn.chathtp.Helper.SignUpCallBack;
import hcmute.edu.vn.chathtp.LoginActivity;
import hcmute.edu.vn.chathtp.Model.User;
import hcmute.edu.vn.chathtp.SignupActivity;

public class SignUpService {
    FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    FirebaseUser mFirebaseUser;

    public SignUpService() {
        mAuth = FirebaseAuth.getInstance();
    }

    // signup User with email and password and save to firebase
    public synchronized void signUpUser(User user, SignUpCallBack signUpCallBack) {
        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        signUpCallBack.onSignUpSuccess("Sign up success", user);
                    } else {
                        // If sign in fails, display a message to the user.
                        signUpCallBack.onError("Your email is register");
                        Log.e("Error", Objects.requireNonNull(task.getException()).toString());
                    }
                });
    }

    // Send email to user to verify account
    public synchronized void sendEmailVerify(SignUpCallBack signUpCallBack, User user) {
        mFirebaseUser = mAuth.getCurrentUser();
        assert mFirebaseUser != null;
        mFirebaseUser.sendEmailVerification().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                signUpCallBack.onSendEmailSuccess("Please check your email to enable account.", user);
            } else {
                signUpCallBack.onError("Send email error");
                Log.e("Error", Objects.requireNonNull(task.getException()).toString());
            }
        });
    }


    // Save user into realtime database in firebase
    public synchronized void saveUser(User user, SignUpCallBack signUpCallBack) {
        mFirebaseUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        assert mFirebaseUser != null;
        String userId = mFirebaseUser.getUid();
        user.setUser_id(userId);
        mDatabase.child(userId).setValue(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                signUpCallBack.onSaveUserSuccess("Save user successfully.", user);
            } else {
                signUpCallBack.onError("Save user error");
                Log.e("Error", Objects.requireNonNull(task.getException()).toString());
            }
        });
    }
}
