package com.example.pshop.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pshop.R;
import com.example.pshop.databinding.ActivityProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends BaseActivity {
    private ActivityProfileBinding binding;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Khởi tạo ViewBinding
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot()); // Thiết lập giao diện

        // Khởi tạo FirebaseAuth và Firebase Database
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // Nếu người dùng chưa đăng nhập, chuyển về màn hình đăng nhập
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Đóng activity hiện tại
            return;
        }

        mDatabase = FirebaseDatabase.getInstance().getReference("Users");

        // Lấy thông tin người dùng từ Firebase
        mDatabase.child(currentUser.getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                // Lấy dữ liệu người dùng từ Firebase
                User user = task.getResult().getValue(User.class);
                if (user != null) {
                    binding.profileName.setText(user.getName());
                    binding.email.setText(user.getEmail());
                    binding.phone.setText(user.getPhone());
                    binding.address.setText(user.getAddress());
                } else {
                    Toast.makeText(ProfileActivity.this, "Không thể lấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ProfileActivity.this, "Không thể kết nối với cơ sở dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý sự kiện đăng xuất
        binding.buttonLogout.setOnClickListener(view -> {
            mAuth.signOut(); // Đăng xuất người dùng
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class); // Chuyển đến trang đăng nhập
            startActivity(intent);
            finish(); // Đóng activity hiện tại
        });

        // Chỉnh sửa thông tin cá nhân
        binding.editPer.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class); // Chuyển đến trang chỉnh sửa thông tin
            startActivity(intent);
        });

        // Đổi mật khẩu
        binding.support.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, ChangePasswordActivity.class); // Chuyển đến trang đổi mật khẩu
            startActivity(intent);
        });

        // Lịch sử đơn hàng
        binding.orderHistory.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, OrderHistoryActivity.class); // Chuyển đến trang lịch sử đơn hàng
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

    // Lớp User để lấy thông tin người dùng từ Firebase
    public static class User {
        private String name, email, phone, address;

        public User() {
            // Firebase cần một constructor không tham số
        }

        public User(String name, String email, String phone, String address) {
            this.name = name;
            this.email = email;
            this.phone = phone;
            this.address = address;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getPhone() {
            return phone;
        }

        public String getAddress() {
            return address;
        }
    }
}
