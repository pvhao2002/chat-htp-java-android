package hcmute.edu.vn.chathtp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import hcmute.edu.vn.chathtp.FriendProfileActivity;
import hcmute.edu.vn.chathtp.Helper.NotifyCallBack;
import hcmute.edu.vn.chathtp.Model.Friend;
import hcmute.edu.vn.chathtp.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import javax.annotation.Nonnull;

import hcmute.edu.vn.chathtp.Model.User;
import hcmute.edu.vn.chathtp.Remote.SharedPrefManager;
import hcmute.edu.vn.chathtp.Service.NotifyService;

public class ConfirmAdapter extends RecyclerView.Adapter<ConfirmAdapter.UserViewHolder> {

    private DatabaseReference reference; // DatabaseReference cho Firebase
    private FirebaseUser firebaseUser; // FirebaseUser hiện tại đang đăng nhập
    private StorageReference storageReference; // StorageReference cho Firebase
    private List<Friend> mListFriend;
    private Context context;


    @SuppressLint("NotifyDataSetChanged")
    public void setListner(List<Friend> uList) {
        this.mListFriend = uList;
        notifyDataSetChanged();
    }
    public ConfirmAdapter(List<Friend> friendList, Context ctx) {
        this.mListFriend = friendList;
        this.context = ctx;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(parent.getContext());
        View itemView = li.inflate(R.layout.row_item_userconfirm, parent, false);
        return new ConfirmAdapter.UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Friend friend = mListFriend.get(position);
        Glide.with(holder.profile_image.getContext()).
                load(friend.getSender().getAvatar()).
                circleCrop().
                into(holder.profile_image);
        holder.username.setText(friend.getSender().getUsername());
        holder.setUser(friend.getSender());
    }

    @SuppressLint("NotifyDataSetChanged")
    public void clear(){
        mListFriend.clear();
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return mListFriend == null ? 0 : mListFriend.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder implements NotifyCallBack {
        private AdapterView.OnItemClickListener onItemClickListener;
        private User requestUser;
        private ImageView profile_image;
        private TextView username;
        private NotifyService notifyService;
        private Button btnAccept, btnDelete;

        public UserViewHolder(final View itemView) {
            super(itemView);
            profile_image = itemView.findViewById(R.id.profile_image);
            username = itemView.findViewById(R.id.username);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            notifyService = new NotifyService();
            
            btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    confirmRequestFriend();
                }
            });
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeRequestFriend();
                }
            });
            
        }
        private void removeRequestFriend() {
            clear();
            notifyService.removeFriend(this, requestUser);
        }
        private void confirmRequestFriend() {
            clear();
            notifyService.confirmFriend(this, requestUser);
        }
        public User getUser(){return  requestUser;}
        public void setUser(User user) {
            this.requestUser = user;
        }

        @Override
        public void onLoadListFriend(List<Friend> listFriend) {

        }

        @Override
        public void error(String message) {

        }

        @Override
        public void onConfirmRequest(String message) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            notifyService.loadListRequestFriend(this);
        }

        @Override
        public void onRemoveRequest(String message) {
        }
    }

}
