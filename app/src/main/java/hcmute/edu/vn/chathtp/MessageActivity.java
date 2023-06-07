package hcmute.edu.vn.chathtp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;

import hcmute.edu.vn.chathtp.Adapter.MessageAdapter;
import hcmute.edu.vn.chathtp.Helper.MessageCallBack;
import hcmute.edu.vn.chathtp.Model.Message;
import hcmute.edu.vn.chathtp.Model.User;
import hcmute.edu.vn.chathtp.Remote.SharedPrefManager;
import hcmute.edu.vn.chathtp.Service.MessageService;

public class MessageActivity extends AppCompatActivity implements MessageCallBack {

    ImageView profile_image;
    TextView username;
    ImageButton btn_send;
    EditText text_send;

    MessageAdapter messageAdapter;
    List<Message> messages;
    User currentUser, user;

    RecyclerView recyclerView;

    Intent intent;
    MessageService messageService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        recyclerView = findViewById(R.id.rcv_chat);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.txt_send);
        messages = new ArrayList<>();
        messageService = new MessageService();
        intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        currentUser = SharedPrefManager.getUser();
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = text_send.getText().toString();
                if (!msg.equals("")) {
                    messageService.sendMessage(currentUser, user, msg);
                } else {
                    Toast.makeText(MessageActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });
        loadMessage(user);
        messageService.seenMessage(user, this);
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    private void loadMessage(User u) {
        messageService.loadMessage(u, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Perform actions when activity is paused
        messageService.outMessage();
    }
    @Override
    public void onLoadMessageSuccess(User u) {
        username.setText(u.getUsername());
        Glide.with(getApplicationContext()).
                load(u.getAvatar()).
                circleCrop().
                into(profile_image);
        messageService.readMessage(u, messages, this);
    }

    @Override
    public void onError(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onReadMessageSuccess(List<Message> messages, String avatar) {
        messageAdapter = new MessageAdapter(MessageActivity.this, messages, avatar);
        recyclerView.setAdapter(messageAdapter);
    }

    @Override
    public void onSeenMessageSuccess() {

    }
}