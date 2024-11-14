package com.example.pshop.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pshop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends BaseActivity {

    private EditText nameEditText, phoneEditText, emailEditText, passwordEditText, addressEditText;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Khởi tạo FirebaseAuth và Firebase Database
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");

        // Khởi tạo các thành phần giao diện
        nameEditText = findViewById(R.id.nameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        addressEditText = findViewById(R.id.addressEditText);

        // Xử lý sự kiện đăng ký
        findViewById(R.id.registerBtn).setOnClickListener(view -> {
            // Lấy dữ liệu từ các EditText
            String name = nameEditText.getText().toString().trim();
            String phone = phoneEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String address = addressEditText.getText().toString().trim();

            // Kiểm tra các trường không được để trống
            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(email) ||
                    TextUtils.isEmpty(password) || TextUtils.isEmpty(address)) {
                Toast.makeText(RegisterActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            // Đăng ký với Firebase Authentication
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Đăng ký thành công, lưu thông tin người dùng vào Firebase Database
                            FirebaseUser user = mAuth.getCurrentUser();
                            User newUser = new User(name, phone, email, address);

                            // Lưu thông tin vào Firebase Realtime Database
                            mDatabase.child(user.getUid()).setValue(newUser)
                                    .addOnCompleteListener(this, databaseTask -> {
                                        if (databaseTask.isSuccessful()) {
                                            Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();

                                            // Sau khi đăng ký thành công và lưu thông tin người dùng vào Firebase, chuyển hướng về LoginActivity
                                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  // Đảm bảo xóa các Activity trước đó
                                            startActivity(intent);
                                            finish();  // Đóng RegisterActivity
                                        } else {
                                            Toast.makeText(RegisterActivity.this, "Lưu thông tin người dùng thất bại", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        } else {
                            // Nếu đăng ký thất bại
                            Toast.makeText(RegisterActivity.this, "Đăng ký thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
        findViewById(R.id.loginLink).setOnClickListener(view -> {
            // Chuyển hướng đến màn hình đăng ký
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class); // Tạo một Activity RegisterActivity
            startActivity(intent);
        });
    }

    // Lớp User để lưu thông tin người dùng
    public static class User {
        public String name, phone, email, address;

        public User(String name, String phone, String email, String address) {
            this.name = name;
            this.phone = phone;
            this.email = email;
            this.address = address;
        }
    }
}
