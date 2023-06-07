package hcmute.edu.vn.chathtp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import hcmute.edu.vn.chathtp.Adapter.SearchAdapter;
import hcmute.edu.vn.chathtp.Model.User;
import hcmute.edu.vn.chathtp.Service.SearchService;

public class SearchActivity extends AppCompatActivity {
    SearchView search_view;
    RecyclerView recycler_view;
    SearchAdapter searchAdapter;
    SearchService searchService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        recycler_view = findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false);
        recycler_view.setLayoutManager(gridLayoutManager);
        searchAdapter = new SearchAdapter(new ArrayList<>(), this);
        recycler_view.setAdapter(searchAdapter);
        searchService = new SearchService();
        search_view = findViewById(R.id.search_view);
        search_view.requestFocus();
        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(s.equals("")) {
                    searchAdapter.setListner(new ArrayList<>());
                    return true;
                }
                filterList(s);
                return true;
            }
        });
    }

    private void filterList(String s) {
        List<User> listUser = searchService.searchFriendByPhone(s, this);
        searchAdapter.setListner(listUser);
        System.out.println(listUser.toString());
    }
}