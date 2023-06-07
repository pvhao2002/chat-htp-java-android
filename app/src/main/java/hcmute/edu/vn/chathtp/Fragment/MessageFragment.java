package hcmute.edu.vn.chathtp.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hcmute.edu.vn.chathtp.Adapter.ChatAdapter;
import hcmute.edu.vn.chathtp.Helper.ChatCallBack;
import hcmute.edu.vn.chathtp.Model.User;
import hcmute.edu.vn.chathtp.R;
import hcmute.edu.vn.chathtp.SearchActivity;
import hcmute.edu.vn.chathtp.Service.ChatService;


public class MessageFragment extends Fragment implements ChatCallBack {

    View view;
    RecyclerView mRecyclerView;
    ImageView iconSearch;
    TextView textViewSearch;

    ChatAdapter userAdapter;
    List<User> mUser;
    ChatService chatService;

    List<User> userList;

    public MessageFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_message, container, false);
        iconSearch = view.findViewById(R.id.iconSearch);
        textViewSearch = view.findViewById(R.id.textViewSearch);
        chatService = new ChatService();
        mRecyclerView = view.findViewById(R.id.recyclerViewUser);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        userList = new ArrayList<>();
        mUser = new ArrayList<>();
        getUsers();
        TimKiem();
        return view;
    }

    private void getUsers() {
        chatService.getGetAllUser(userList, this);
    }

    private void TimKiem() {
        iconSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });
        textViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });
    }

    private void readMessages() {
        mUser.clear();
        for (User us : userList) {
            boolean isUserExist = false;
            if (mUser.size() != 0) {
                for (User user1 : mUser) {
                    if (us.getUser_id().equals(user1.getUser_id())) {
                        isUserExist = true;
                        break;
                    }
                }
            }
            if (!isUserExist) {
                mUser.add(us);
            }
        }
        // đảo ngc list
        Collections.reverse(mUser);
        userAdapter = new ChatAdapter(getContext(), mUser);
        mRecyclerView.setAdapter(userAdapter);
    }

    @Override
    public void onGetAllUserSuccess(List<User> userList) {
        this.userList = userList;
        readMessages();
    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void onGetLastMessage(String message, TextView tv, boolean b) {

    }
}