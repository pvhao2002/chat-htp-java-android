package hcmute.edu.vn.chathtp.Service;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import hcmute.edu.vn.chathtp.Helper.ForgotPasswordCallBack;

public class ForgotPasswordService {
    FirebaseAuth mAuth;
    private static final long SPLASH_DELAY = 5000; // Delay in milliseconds (5 seconds)

    public ForgotPasswordService() {
        mAuth = FirebaseAuth.getInstance();
    }

    // send password reset email
    public synchronized void sendPasswordResetEmail(String email, ForgotPasswordCallBack callBack) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callBack.onSendPasswordResetEmailSuccess("Please check your email to reset password!");
                        // Sign in success, update UI with the signed-in user's information
                        Log.e("ForgotPasswordActivity", "Email sent.");
                    } else {
                        callBack.onError("Email is not exist!");
                        // If sign in fails, display a message to the user.
                        Log.e("ForgotPasswordActivity", "Email sent error.");
                    }
                });
    }
}
