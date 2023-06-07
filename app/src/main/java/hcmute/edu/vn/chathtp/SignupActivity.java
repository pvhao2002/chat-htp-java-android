package hcmute.edu.vn.chathtp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

import hcmute.edu.vn.chathtp.Helper.RandomAvatar;
import hcmute.edu.vn.chathtp.Helper.SignUpCallBack;
import hcmute.edu.vn.chathtp.Model.User;
import hcmute.edu.vn.chathtp.Service.SignUpService;

public class SignupActivity extends AppCompatActivity implements SignUpCallBack {
    TextView txtLogin;
    EditText editTextDateOfBirth, editTextName, editTextEmail, editTextPassword, editTextConfirmPassword, editTextPhone;
    DatePickerDialog picker;
    Button btnSignup;
    SignUpService signUpService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editTextDateOfBirth = findViewById(R.id.editTextDateOfBirth);
        txtLogin = findViewById(R.id.txtLogin);
        editTextName = findViewById(R.id.editTextUserName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        editTextPhone = findViewById(R.id.editTextPhoneNumber);
        editTextEmail = findViewById(R.id.editTextEmail);
        signUpService = new SignUpService();
        btnSignup = findViewById(R.id.btnSignup);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                String dateOfBirth = editTextDateOfBirth.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String confirmPassword = editTextConfirmPassword.getText().toString().trim();
                String phone = editTextPhone.getText().toString().trim();

                if (name.isEmpty()) {
                    editTextName.setError("Name is required!");
                    editTextName.requestFocus();
                    return;
                }

                if (dateOfBirth.isEmpty()) {
                    editTextDateOfBirth.setError("Date of birth is required!");
                    editTextDateOfBirth.requestFocus();
                    return;
                }

                if (email.isEmpty()) {
                    editTextEmail.setError("Email is required!");
                    editTextEmail.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    editTextPassword.setError("Password is required!");
                    editTextPassword.requestFocus();
                    return;
                }

                if (confirmPassword.isEmpty()) {
                    editTextConfirmPassword.setError("Confirm password is required!");
                    editTextConfirmPassword.requestFocus();
                    return;
                }

                if (phone.isEmpty()) {
                    editTextPhone.setError("Phone is required!");
                    editTextPhone.requestFocus();
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    editTextConfirmPassword.setError("Confirm password is not match!");
                    editTextConfirmPassword.requestFocus();
                    return;
                }

                if (password.length() < 6) {
                    editTextPassword.setError("Password must be at least 6 characters!");
                    editTextPassword.requestFocus();
                    return;
                }
                if (phone.length() != 10) {
                    editTextPhone.setError("Phone must be 10 characters!");
                    editTextPhone.requestFocus();
                    return;
                }
                // check email is valid using pattern check of android
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    editTextEmail.setError("Please enter a valid email!");
                    editTextEmail.requestFocus();
                    return;
                }

                String avatar = new RandomAvatar().getRandomAvatar();
                User user = new User(name, password, email, avatar, phone, false, dateOfBirth);
                signUp(user);
            }
        });

        editTextDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                picker = new DatePickerDialog(SignupActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        editTextDateOfBirth.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });
    }

    private void signUp(User user) {
        signUpService.signUpUser(user, this);
    }

    @Override
    public void onSaveUserSuccess(String message, User user) {
        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onSignUpSuccess(String message, User user) {
        signUpService.sendEmailVerify(this, user);
    }

    @Override
    public void onSendEmailSuccess(String message, User user) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        signUpService.saveUser(user, this);
    }

    @Override
    public void onError(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}