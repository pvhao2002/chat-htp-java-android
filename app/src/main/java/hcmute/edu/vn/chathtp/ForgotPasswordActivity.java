package hcmute.edu.vn.chathtp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.core.Tag;

import hcmute.edu.vn.chathtp.Helper.ForgotPasswordCallBack;
import hcmute.edu.vn.chathtp.Service.ForgotPasswordService;

public class ForgotPasswordActivity extends AppCompatActivity implements ForgotPasswordCallBack {

    TextView txtGoBack, txtCheckEmail;
    EditText editTextEmail;
    Button btnSendCode;
    ForgotPasswordService forgotPasswordService;
    private static final long SPLASH_DELAY = 5000; // Delay in milliseconds (5 seconds)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        txtGoBack = findViewById(R.id.txtGoBack);
        txtCheckEmail = findViewById(R.id.txtCheckEmail);
        editTextEmail = findViewById(R.id.editTextEmail);
        btnSendCode = findViewById(R.id.btnSendCode);

        forgotPasswordService = new ForgotPasswordService();

        btnSendCode.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString().trim();
                if(email.isEmpty()){
                    editTextEmail.setError("Email is required!");
                    txtCheckEmail.setText("Email is required!");
                    txtCheckEmail.setTextColor(getResources().getColor(R.color.red));
                    editTextEmail.requestFocus();
                    return;
                }
                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    editTextEmail.setError("Please provide valid email!");
                    txtCheckEmail.setText("Please provide valid email!");
                    txtCheckEmail.setTextColor(getResources().getColor(R.color.red));
                    editTextEmail.requestFocus();
                    return;
                }
                forgotPasswordService.sendPasswordResetEmail(email, ForgotPasswordActivity.this);
            }
        });


        txtGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish(); // Finish the SplashScreenActivity to prevent going back to it
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onSendPasswordResetEmailSuccess(String message) {
        txtCheckEmail.setTextColor(getResources().getColor(R.color.white));
        txtCheckEmail.setText("Email was sent, Please check your email!");
        // Create a handler and post a delayed runnable
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the LoginActivity after the delay
                startLoginActivity();
            }
        }, SPLASH_DELAY);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onError(String message) {
        txtCheckEmail.setTextColor(getResources().getColor(R.color.red));
        txtCheckEmail.setText("Email is not exist!");
    }

    @Override
    public void onUpdatePasswordSuccess(String message) {

    }
}