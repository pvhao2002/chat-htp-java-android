package hcmute.edu.vn.chathtp.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import hcmute.edu.vn.chathtp.Adapter.ConfirmAdapter;
import hcmute.edu.vn.chathtp.Adapter.SearchAdapter;
import hcmute.edu.vn.chathtp.Helper.NotifyCallBack;
import hcmute.edu.vn.chathtp.Model.Friend;
import hcmute.edu.vn.chathtp.Model.User;
import hcmute.edu.vn.chathtp.R;
import hcmute.edu.vn.chathtp.Service.NotifyService;


public class NotifyFragment extends Fragment implements NotifyCallBack {
    View view;
    NotifyService notifyService;

    public NotifyFragment() {
        // Required empty public constructor
    }

    RecyclerView recycler_view;
    ConfirmAdapter confirmAdapter;
    TextView textViewName;
    private List<Friend> friendList;

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notify, container, false);
        recycler_view = view.findViewById(R.id.recycler_view);
        textViewName = view.findViewById(R.id.txtQuantity);
        recycler_view.setHasFixedSize(true);
        friendList = new ArrayList<>();
        notifyService = new NotifyService();
        confirmAdapter = new ConfirmAdapter(friendList, getContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        recycler_view.setLayoutManager(gridLayoutManager);
        if (friendList != null) {
            textViewName.setText(friendList.size() + " lời mời kết bạn");
            loadFriend();
        } else {
            Log.d("friendList", "null");
            textViewName.setText("0 lời mời kết bạn");

        }
        return view;
    }

    private void loadFriend() {
        notifyService.loadListRequestFriend(this);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onLoadListFriend(List<Friend> listFriend) {
        if (listFriend != null) {
            confirmAdapter.setListner(listFriend);
            recycler_view.setAdapter(confirmAdapter);
            textViewName.setText(listFriend.size() + " lời mời kết bạn");
        } else {
            textViewName.setText("0 lời mời kết bạn");
        }
    }

    @Override
    public void error(String message) {

    }

    @Override
    public void onConfirmRequest(String message) {
    }

    @Override
    public void onRemoveRequest(String message) {

    }
}

