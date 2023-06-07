package hcmute.edu.vn.chathtp.Remote;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import hcmute.edu.vn.chathtp.Model.User;

public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "login";
    private static final String KEY_ID = "key-id";
    private static final String KEY_NAME = "key-name";
    private static final String KEY_PHONE = "key-phone";
    private static final String KEY_EMAIL = "key-email";
    private static final String KEY_DOB = "key-dob";
    private static final String KEY_URL = "key-url";
    private static final String KEY_PASSWORD = "key-password";
    private static SharedPrefManager mInstance;
    private static Context ctx;

    private SharedPrefManager(Context context) {
        ctx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public void userLogin(User user) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ID, user.getUser_id());
        editor.putString(KEY_NAME, user.getUsername());
        editor.putString(KEY_PHONE, user.getPhone());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_PASSWORD, user.getPassword());
        editor.putString(KEY_URL, user.getAvatar());
        editor.putString(KEY_DOB, user.getDob());
        editor.apply();
    }

    public static boolean isLoggedIn() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_EMAIL, null) != null;
    }

    public static User getUser() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        User u = new User();
        u.setUser_id(sharedPreferences.getString(KEY_ID, null));
        u.setUsername(sharedPreferences.getString(KEY_NAME, null));
        u.setPhone(sharedPreferences.getString(KEY_PHONE, null));
        u.setEmail(sharedPreferences.getString(KEY_EMAIL, null));
        u.setPassword(sharedPreferences.getString(KEY_PASSWORD, null));
        u.setAvatar(sharedPreferences.getString(KEY_URL, null));
        u.setDob(sharedPreferences.getString(KEY_DOB, null));
        return u;
    }

    public static String getImageUser() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_URL, null);
    }

    public void logout() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

}
