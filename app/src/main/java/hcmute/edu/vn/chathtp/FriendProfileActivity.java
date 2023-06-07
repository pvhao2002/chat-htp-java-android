package hcmute.edu.vn.chathtp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

import hcmute.edu.vn.chathtp.Helper.FriendProfileCallBack;
import hcmute.edu.vn.chathtp.Model.Friend;
import hcmute.edu.vn.chathtp.Model.User;
import hcmute.edu.vn.chathtp.Service.FriendProfileService;

public class FriendProfileActivity extends AppCompatActivity implements FriendProfileCallBack {
    ImageView imageViewAvatar;
    TextView textViewName, textViewEmail;
    EditText editTextUsername, editTextDateOfBirth, editTextPhonenumber;
    Button btnAddFriend, btnChat;
    FriendProfileService friendProfileService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
        imageViewAvatar = findViewById(R.id.imageViewAvatar);
        textViewName = findViewById(R.id.textViewName);
        textViewEmail = findViewById(R.id.textViewEmail);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextDateOfBirth = findViewById(R.id.editTextDateOfBirth);
        editTextPhonenumber = findViewById(R.id.editTextPhonenumber);
        btnAddFriend = findViewById(R.id.btnAddFriend);
        btnChat = findViewById(R.id.btnChat);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        User user = (User) bundle.getSerializable("user");
        textViewName.setText(user.getUsername());
        textViewEmail.setText(user.getEmail());
        editTextUsername.setText(user.getUsername());
        editTextDateOfBirth.setText(user.getDob());
        editTextPhonenumber.setText(user.getPhone());
        Glide.with(getApplicationContext())
                .load(user.getAvatar())
                .circleCrop()
                .into(imageViewAvatar);
        friendProfileService = new FriendProfileService();
        friendProfileService.checkIsFriend(this, user);

        btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnAddFriend.getText().toString().equals("Add Friend")){
                    sendRequestFriend(user);
                }else if(btnAddFriend.getText().toString().equals("Confirm")){
                    confirmRequest(user);
                }
            }
        });
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(FriendProfileActivity.this, MessageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void confirmRequest(User user) {
        friendProfileService.confirmFriend(this, user);
    }

    private void sendRequestFriend(User user) {
        friendProfileService.sendRequestFriend(this, user);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForColorStateLists"})
    @Override
    public void onIsFriendCallBack(String status) {
        if ("Accepted".equals(status)) {
            btnAddFriend.setText("Friend");
            btnAddFriend.setBackgroundTintList(getResources().getColorStateList(R.color.btnChat));
            btnChat.setBackgroundTintList(getResources().getColorStateList(R.color.btnAddFriend));
            btnAddFriend.setEnabled(false);
        } else if("Waiting".equals(status) || "Refused".equals(status)) {
            btnAddFriend.setText("Waiting");
            btnAddFriend.setBackgroundTintList(getResources().getColorStateList(R.color.btnAddFriend));
            btnChat.setBackgroundTintList(getResources().getColorStateList(R.color.btnChat));
            btnAddFriend.setEnabled(false);
        }else if("Confirm".equals(status)){
            btnAddFriend.setText("Confirm");
            btnAddFriend.setBackgroundTintList(getResources().getColorStateList(R.color.btnAddFriend));
            btnChat.setBackgroundTintList(getResources().getColorStateList(R.color.btnChat));
        }
    }
    @Override
    public void onErrorMessageCallBack(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    @SuppressLint({"SetTextI18n", "UseCompatLoadingForColorStateLists"})
    @Override
    public void onSendRequestFriendCallBack(String message) {
        btnAddFriend.setText("Waiting");
        btnAddFriend.setEnabled(false);
        btnAddFriend.setBackgroundTintList(getResources().getColorStateList(R.color.btnChat));
        btnChat.setBackgroundTintList(getResources().getColorStateList(R.color.btnAddFriend));
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onIsReceivedRequest(String message) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onConfirmRequest(String message) {
        btnAddFriend.setText("Friend");
        btnAddFriend.setEnabled(false);
        btnAddFriend.setBackgroundTintList(getResources().getColorStateList(R.color.btnChat));
        btnChat.setBackgroundTintList(getResources().getColorStateList(R.color.btnAddFriend));
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}