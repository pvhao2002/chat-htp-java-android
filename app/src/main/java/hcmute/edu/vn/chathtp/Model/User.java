package hcmute.edu.vn.chathtp.Model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.HashMap;

@IgnoreExtraProperties
public class User implements Serializable {
    private String user_id;
    private String username;
    private String password;
    private String phone;
    private String email;
    private String avatar;
    private boolean isVerified;  //
    private String dob;
    private HashMap<String, User> friends;

    public HashMap<String, User> getFriends() {
        return friends;
    }

    public void setFriends(HashMap<String, User> friends) {
        this.friends = friends;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public User() {
        friends = new HashMap<>();
    }
    public User( String username, String password, String email, String avatar, String phone, boolean isVerified, String dob) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.avatar = avatar;
        this.isVerified = isVerified;
        this.dob = dob;
        this.phone = phone;
        friends = new HashMap<>();
    }
    public User(String userid, String username, String password, String email, String phone, String avatar, boolean isVerified, String dob) {
        this.user_id = userid;
        this.username = username;
        this.password = password;
        this.email = email;
        this.avatar = avatar;
        this.isVerified = isVerified;
        this.dob = dob;
        friends = new HashMap<>();
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", avatar='" + avatar + '\'' +
                ", isVerified=" + isVerified +
                ", dob='" + dob + '\'' +
                '}';
    }
}
