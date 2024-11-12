package com.example.pshop.Activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pshop.R;
import com.example.pshop.databinding.ActivityProfileBinding;

public class ProfileActivity extends BaseActivity {
    private ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Khởi tạo ViewBinding
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot()); // Thiết lập giao diện

        // Lấy thông tin người dùng từ Intent hoặc SharedPreferences
        String userName = "Nguyễn Văn A"; // Thông tin thực tế
        String email = "email@example.com"; // Thông tin thực tế
        String phone = "0123456789"; // Thông tin thực tế

        // Gán thông tin vào các TextView thông qua binding
        binding.profileName.setText(userName);
        binding.email.setText(email);
        binding.phone.setText(phone);

        // Xử lý sự kiện đăng xuất
        binding.buttonLogout.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class); // Chuyển đến trang chủ
            startActivity(intent);
        });

        // Xử lý sự kiện nhấn nút Trang chủ
        binding.homeBtn.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class); // Chuyển đến trang chủ
            startActivity(intent);
        });

        // Xử lý sự kiện nhấn nút Giỏ hàng
        binding.cartBtn.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, CartActivity.class); // Chuyển đến giỏ hàng
            startActivity(intent);
        });
    }
}
