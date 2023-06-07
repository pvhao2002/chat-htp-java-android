package hcmute.edu.vn.chathtp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import hcmute.edu.vn.chathtp.FriendProfileActivity;
import hcmute.edu.vn.chathtp.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import hcmute.edu.vn.chathtp.Model.User;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.UserViewHolder> {
    private List<User> mDatas;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public SearchAdapter(List<User> mDatas, Context context) {
        this.mDatas = mDatas;
        this.context = context;
    }


    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(parent.getContext());
        View itemView = li.inflate(R.layout.row_item_user, parent, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.UserViewHolder holder, int position) {
        User user = mDatas.get(position);
        Glide.with(holder.imageViewAvatar.getContext()).
                load(user.getAvatar()).
                circleCrop().
                into(holder.imageViewAvatar);
        holder.textViewName.setText(user.getUsername());
        holder.textViewPhone.setText(user.getPhone());
        holder.setUser(user);
    }
    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }
    public interface OnItemClickListener {
        void itemClick(User chat);
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setListner(List<User> uList) {
        this.mDatas = uList;
        notifyDataSetChanged();
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    // show profile of friend
    public void showProfile(User user) {
        Intent intent = new Intent(context, FriendProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private AdapterView.OnItemClickListener onItemClickListener;
        private User user;
        private ImageView imageViewAvatar;
        private TextView textViewName, textViewPhone;
        public UserViewHolder(final View itemView) {
            super(itemView);
            imageViewAvatar = itemView.findViewById(R.id.imageViewAvatar);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewPhone = itemView.findViewById(R.id.textViewPhone);
            itemView.setOnClickListener(this);
        }
        public void setUser(User user) {
            this.user = user;
        }
        @Override
        public void onClick(View view) {
            showToast("Click");
            showProfile(user);
        }
    }

}
