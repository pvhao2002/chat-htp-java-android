package hcmute.edu.vn.chathtp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import hcmute.edu.vn.chathtp.Helper.ChatCallBack;
import hcmute.edu.vn.chathtp.MessageActivity;
import hcmute.edu.vn.chathtp.Model.User;
import hcmute.edu.vn.chathtp.R;
import hcmute.edu.vn.chathtp.Service.ChatService;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> implements ChatCallBack {
    private Context mContext;
    public static List<User> mUsers;
    ChatService chatService;

    public ChatAdapter(Context mContext, List<User> mUsers) {
        this.mContext = mContext;
        this.mUsers = mUsers;
        chatService = new ChatService();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.row_item_user_message, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = mUsers.get(position);
        holder.textViewName.setText(user.getUsername());
        Glide.with(holder.imageViewAvatar.getContext()).
                load(user.getAvatar()).
                circleCrop().
                into(holder.imageViewAvatar);
        chatService.getLastMessage(user, holder.last_message, this );

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    @Override
    public void onGetAllUserSuccess(List<User> userList) {

    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void onGetLastMessage(String message, TextView tv, boolean b) {
        tv.setText(message);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private User user;
        private ImageView imageViewAvatar;
        private TextView textViewName, last_message;

        public ViewHolder(View itemView) {
            super(itemView);
            imageViewAvatar = itemView.findViewById(R.id.imageViewAvatar);
            textViewName = itemView.findViewById(R.id.textViewName);
            last_message = itemView.findViewById(R.id.last_message);
        }
    }

    private void lastMessage(User user, TextView last_message) {

    }
    //Lấy ra tin nhắn cuối cùng

}