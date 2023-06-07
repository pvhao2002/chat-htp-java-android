package hcmute.edu.vn.chathtp.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.TooltipCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;

import hcmute.edu.vn.chathtp.Helper.ProfileCallBack;
import hcmute.edu.vn.chathtp.IntroActivity;
import hcmute.edu.vn.chathtp.Model.User;
import hcmute.edu.vn.chathtp.R;
import hcmute.edu.vn.chathtp.Remote.SharedPrefManager;
import hcmute.edu.vn.chathtp.Service.ProfileService;
import hcmute.edu.vn.chathtp.SignupActivity;

public class ProfileFragment extends Fragment implements ProfileCallBack {

    View view;
    RecyclerView mRecyclerView;

    public ProfileFragment() {
        // Required empty public constructor
    }

    EditText editTextUsername, editTextDateOfBirth, editTextPhoneNumber, editTextEmail, editTextPassword;
    ProfileService profileService; // Service của Profile
    User currentUser; // User hiện tại
    ImageView imageViewAvatar; // ImageView avatar
    TextView textViewName, textViewEmail; // TextView tên và email
    Button btnLogout, btnEditProfile; // Button logout và update
    private String oldPassword; // Mật khẩu cũ

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        editTextUsername = view.findViewById(R.id.editTextUsername);
        editTextDateOfBirth = view.findViewById(R.id.editTextDateOfBirth);
        editTextPhoneNumber = view.findViewById(R.id.editTextPhonenumber);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        imageViewAvatar = view.findViewById(R.id.imageViewAvatar);
        textViewName = view.findViewById(R.id.textViewName);
        textViewEmail = view.findViewById(R.id.textViewEmail);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        profileService = new ProfileService();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLogoutConfirmationDialog();
            }
        });

        editTextDateOfBirth.setOnClickListener(e -> {
            showDatePicker();
        });

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if (btnEditProfile.getText().toString().equals("Save")) {
                    editProfile();
                } else {
                    btnLogout.setVisibility(View.GONE);
                    btnEditProfile.setText("Save");
                    editTextEmail.setTextColor(getResources().getColor(R.color.grey));
                    editTextPassword.setEnabled(true);
                    editTextDateOfBirth.setEnabled(true);
                    editTextUsername.setEnabled(true);
                    editTextPhoneNumber.setEnabled(true);
                    editTextUsername.requestFocus();
                }
            }
        });
        currentUser = SharedPrefManager.getUser();
        updateUI(currentUser);
        return view;
    }

    private void logout() {
        SharedPrefManager.getInstance(getContext()).logout();
    }

    @SuppressLint("SetTextI18n")
    private void editProfile() {
        // Update thông tin user trong Firebase
        profileService.updatePassword(currentUser, editTextPassword.getText().toString(), this);
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog picker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                editTextDateOfBirth.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
            }
        }, year, month, day);
        picker.show();
    }

    private void updateUI(User user) {
        setInfoAccount(user);
        Glide.with(getContext()).
                load(user.getAvatar())
                .circleCrop()
                .into(imageViewAvatar);
    }

    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Logout")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Perform logout operation
                        logout();
                        Intent intent = new Intent(getContext(), IntroActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void setInfoAccount(User currentUser) {
        editTextUsername.setText(currentUser.getUsername());
        editTextDateOfBirth.setText(currentUser.getDob());
        editTextPhoneNumber.setText(currentUser.getPhone());
        editTextEmail.setText(currentUser.getEmail());
        editTextPassword.setText(currentUser.getPassword());
        textViewName.setText(currentUser.getUsername());
        textViewEmail.setText(currentUser.getEmail());
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onUpdateProfileSuccess(String message) {
        // Update thông tin user trong SharedPrefManager
        User user = SharedPrefManager.getUser();
        user.setPhone(editTextPhoneNumber.getText().toString());
        user.setUsername(editTextUsername.getText().toString());
        user.setDob(editTextDateOfBirth.getText().toString());
        user.setPassword(editTextPassword.getText().toString());
        currentUser = user;
        SharedPrefManager.getInstance(getContext()).userLogin(user);
        editTextEmail.setTextColor(getResources().getColor(R.color.black));
        Toast.makeText(getContext(), "Update successfully", Toast.LENGTH_SHORT).show();
        btnLogout.setVisibility(View.VISIBLE);
        btnEditProfile.setText("Edit Profile");
        editTextUsername.setEnabled(false);
        editTextDateOfBirth.setEnabled(false);
        editTextPhoneNumber.setEnabled(false);
        editTextPassword.setEnabled(false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        updateUI(currentUser);
        editTextEmail.setTextColor(getResources().getColor(R.color.black));
        btnLogout.setVisibility(View.VISIBLE);
        btnEditProfile.setText("Edit Profile");
        editTextUsername.setEnabled(false);
        editTextDateOfBirth.setEnabled(false);
        editTextPhoneNumber.setEnabled(false);
        editTextPassword.setEnabled(false);
    }
    @Override
    public void onUpdatePasswordSuccess(String message) {
        profileService.updateOtherInformation(editTextUsername.getText().toString(),
                editTextDateOfBirth.getText().toString(),
                editTextPhoneNumber.getText().toString(), this);
    }
}