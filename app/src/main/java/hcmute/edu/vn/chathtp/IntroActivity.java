package hcmute.edu.vn.chathtp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import hcmute.edu.vn.chathtp.Remote.SharedPrefManager;

public class IntroActivity extends AppCompatActivity {

    TextView btnLogin, btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        boolean isLogin = SharedPrefManager.getInstance(this).isLoggedIn();
        if (isLogin) {
            startActivity(new Intent(IntroActivity.this, MainActivity.class));
            finish();
        } else {
            btnLogin = findViewById(R.id.btnLogin);
            btnSignup = findViewById(R.id.btnSignup);

            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(IntroActivity.this, LoginActivity.class));
                }
            });

            btnSignup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(IntroActivity.this, SignupActivity.class));
                }
            });
        }

    }
}