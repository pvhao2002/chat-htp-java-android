package hcmute.edu.vn.chathtp.Fragment;


import android.annotation.SuppressLint;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import hcmute.edu.vn.chathtp.Adapter.FriendAdapter;
import hcmute.edu.vn.chathtp.Helper.FriendCallBack;
import hcmute.edu.vn.chathtp.Model.Friend;
import hcmute.edu.vn.chathtp.Model.User;
import hcmute.edu.vn.chathtp.R;
import hcmute.edu.vn.chathtp.Remote.SharedPrefManager;
import hcmute.edu.vn.chathtp.Service.FriendService;


public class FriendFragment extends Fragment implements FriendCallBack {
    FriendAdapter friendAdapter;
    RecyclerView recycler_view;
    List<User> mFriend;
    ValueEventListener valueEventListener;
    View view;
    RecyclerView mRecyclerView;
    TextView tvFriend;
    FriendService friendService;
    public FriendFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_friend, container, false);
        recycler_view = view.findViewById(R.id.recyclerViewFriend);
        tvFriend = view.findViewById(R.id.tvFriend);
        mFriend = new ArrayList<>();
        friendService = new FriendService();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        recycler_view.setLayoutManager(gridLayoutManager);
        getFriend();
        return view;
    }
    private void getFriend() {
        friendService.getAllFriend(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onGetAllFriendSuccess(List<User> listFriend) {
        tvFriend.setText("Bạn bè (" + listFriend.size() + ")");
        friendAdapter = new FriendAdapter(getContext(), listFriend);
        recycler_view.setAdapter(friendAdapter);
    }
}