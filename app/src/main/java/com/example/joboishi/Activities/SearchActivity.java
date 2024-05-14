package com.example.joboishi.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.joboishi.databinding.SearchLayoutBinding;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class SearchActivity extends AppCompatActivity {

    private SearchLayoutBinding binding;
    private ListView listRecentSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SearchLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        listRecentSearch = binding.listRecentSearches;
        ImageButton btnBack = binding.btnToolbarBack;
        AppCompatButton btnDelRecentSearches = binding.btnDelRecentSearches;
        btnBack.setOnClickListener(view -> Log.d("test", "onClick: "));

        EditText inputSearch = binding.inputMajor;
        inputSearch.setImeOptions(EditorInfo.IME_ACTION_DONE);
        inputSearch.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Xử lý tìm kiếm ở đây
                String key = inputSearch.getText().toString();
                // Gửi dữ liệu tìm kiếm sang Activity mới
                Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                intent.putExtra("key", key);
                startActivity(intent);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        saveSearchHistory(key);
                        loadSearchHistory(); // Tải lại lịch sử tìm kiếm sau khi thêm
                    }
                }, 1500);

                hideKeyboard();
                return true;
            }
            return false;
        });

        btnDelRecentSearches.setOnClickListener(view -> {
            clearSearchHistory();
            loadSearchHistory(); // Tải lại lịch sử tìm kiếm sau khi xóa
            Toast.makeText(SearchActivity.this, "Lịch sử tìm kiếm đã được xóa", Toast.LENGTH_SHORT).show();
        });

        // Thiết lập sự kiện cho các item trong ListView
        listRecentSearch.setOnItemClickListener((adapterView, view, position, id) -> {
            String selectedSearch = (String) adapterView.getItemAtPosition(position);
            Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
            intent.putExtra("key", selectedSearch);
            startActivity(intent);
        });

        loadSearchHistory(); // Tải lịch sử tìm kiếm khi khởi động Activity
    }

    // Hide Keyboard
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    // Save search history
    private void saveSearchHistory(String query) {
        SharedPreferences sharedPreferences = getSharedPreferences("SearchHistory", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Lấy danh sách lịch sử hiện tại
        Set<String> searchHistory = sharedPreferences.getStringSet("history", new LinkedHashSet<>());

        // Tạo một LinkedHashSet mới để giữ lại thứ tự của các mục
        Set<String> updatedHistory = new LinkedHashSet<>(searchHistory);

        // Thêm truy vấn mới vào đầu danh sách
        ArrayList<String> tempList = new ArrayList<>(updatedHistory);
        tempList.add(0, query);

        // Giữ lại 3 mục gần nhất
        if (tempList.size() > 3) {
            tempList = new ArrayList<>(tempList.subList(0, 3));
        }

        // Cập nhật LinkedHashSet
        updatedHistory.clear();
        updatedHistory.addAll(tempList);

        editor.putStringSet("history", updatedHistory);
        editor.apply();
    }

    // Load search history
    private void loadSearchHistory() {
        SharedPreferences sharedPreferences = getSharedPreferences("SearchHistory", MODE_PRIVATE);
        Set<String> searchHistory = sharedPreferences.getStringSet("history", new LinkedHashSet<>());

        ArrayList<String> historyList = new ArrayList<>(searchHistory);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, historyList);
        listRecentSearch.setAdapter(adapter);
    }

    // Clear search history
    private void clearSearchHistory() {
        SharedPreferences sharedPreferences = getSharedPreferences("SearchHistory", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
