package com.example.pshop.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pshop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends BaseActivity {

    private EditText usernameEditText, passwordEditText;
    private Button loginButton;
    private FirebaseAuth mAuth; // Firebase Authentication

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Khởi tạo các thành phần giao diện
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginBtn);

        // Khởi tạo FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Cài đặt sự kiện cho nút đăng nhập
        loginButton.setOnClickListener(view -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            // Kiểm tra thông tin đăng nhập (đảm bảo người dùng không để trống các trường)
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Vui lòng nhập tên đăng nhập và mật khẩu", Toast.LENGTH_SHORT).show();
                return;
            }

            // Đăng nhập với Firebase Authentication
            mAuth.signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Đăng nhập thành công, lưu trạng thái và chuyển hướng đến MainActivity
                            FirebaseUser user = mAuth.getCurrentUser();
                            SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean("isLoggedIn", true);
                            editor.putString("userEmail", user.getEmail()); // Lưu email của người dùng
                            editor.apply();

                            // Kiểm tra xem người dùng là admin hay không
                            if ("admin@gmail.com".equals(user.getEmail())) {
                                // Nếu là admin, chuyển đến AdminActivity
                                Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                                startActivity(intent);
                            } else {
                                // Nếu không phải admin, chuyển đến MainActivity
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                            finish();  // Đóng LoginActivity
                        } else {
                            // Nếu đăng nhập thất bại, hiển thị thông báo lỗi
                            Toast.makeText(LoginActivity.this, "Đăng nhập thất bại. Vui lòng kiểm tra lại thông tin đăng nhập.", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Xử lý sự kiện Quên mật khẩu
        findViewById(R.id.forgotPasswordLink).setOnClickListener(view -> {
            // Hiển thị một dialog hoặc một activity để người dùng nhập email để reset mật khẩu
            String email = usernameEditText.getText().toString();
            if (!email.isEmpty()) {
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Đã gửi email để reset mật khẩu!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginActivity.this, "Lỗi khi gửi email reset mật khẩu.", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(LoginActivity.this, "Vui lòng nhập email của bạn", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý sự kiện Đăng ký tài khoản
        findViewById(R.id.registerLink).setOnClickListener(view -> {
            // Chuyển hướng đến màn hình đăng ký
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class); // Tạo một Activity RegisterActivity
            startActivity(intent);
        });
    }
}
