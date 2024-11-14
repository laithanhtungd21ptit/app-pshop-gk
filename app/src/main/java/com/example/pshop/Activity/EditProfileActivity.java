package com.example.pshop.Activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.pshop.R;
import com.example.pshop.databinding.ActivityEditProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivity extends BaseActivity {
    private ActivityEditProfileBinding binding;
    private DatabaseReference mDatabase;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Lấy ID của người dùng hiện tại
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        // Tải thông tin người dùng hiện tại
        loadUserProfile();

        binding.buttonSave.setOnClickListener(view -> {
            String name = binding.editTextName.getText().toString();
            String email = binding.editTextEmail.getText().toString();
            String phone = binding.editTextPhone.getText().toString();
            String address = binding.editTextAddress.getText().toString();

            if (!name.isEmpty() && !email.isEmpty() && !phone.isEmpty() && !address.isEmpty()) {
                // Lưu thông tin đã chỉnh sửa vào Firebase
                mDatabase.child("name").setValue(name);
                mDatabase.child("email").setValue(email);
                mDatabase.child("phone").setValue(phone);
                mDatabase.child("address").setValue(address);

                Toast.makeText(EditProfileActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(EditProfileActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            }
        });
        ImageView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(view -> finish()); // Đóng activity và quay lại màn hình trước

    }

    private void loadUserProfile() {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String phone = snapshot.child("phone").getValue(String.class);
                    String address = snapshot.child("address").getValue(String.class);

                    // Điền thông tin cũ vào các trường EditText
                    binding.editTextName.setText(name);
                    binding.editTextEmail.setText(email);
                    binding.editTextPhone.setText(phone);
                    binding.editTextAddress.setText(address);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditProfileActivity.this, "Không thể tải thông tin người dùng", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
