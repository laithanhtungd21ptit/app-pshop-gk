package com.example.pshop.Activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.pshop.R;
import com.example.pshop.databinding.ActivityChangePasswordBinding;

public class ChangePasswordActivity extends BaseActivity {
    private ActivityChangePasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonChangePassword.setOnClickListener(view -> {
            String oldPassword = binding.editTextOldPassword.getText().toString();
            String newPassword = binding.editTextNewPassword.getText().toString();

            if (!oldPassword.isEmpty() && !newPassword.isEmpty()) {
                // Thực hiện đổi mật khẩu ở đây (ví dụ: dùng Firebase Authentication)
                // Firebase auth logic goes here

                Toast.makeText(ChangePasswordActivity.this, "Mật khẩu đã được thay đổi", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(ChangePasswordActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            }
        });
        ImageView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(view -> finish()); // Đóng activity và quay lại màn hình trước

    }
}
