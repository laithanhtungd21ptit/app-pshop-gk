package com.example.pshop.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pshop.R;
import com.google.firebase.auth.FirebaseAuth;

public class AdminActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);  // Đây là layout mà bạn đã chỉnh sửa

        // Quản lý đơn hàng
        findViewById(R.id.manageOrders).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Chuyển đến màn hình quản lý đơn hàng
                Intent intent = new Intent(AdminActivity.this, ManageOrdersActivity.class);
                startActivity(intent);
            }
        });

        // Quản lý sản phẩm
        findViewById(R.id.manageProducts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Chuyển đến màn hình quản lý sản phẩm
                Intent intent = new Intent(AdminActivity.this, ManageProductsActivity.class);
                startActivity(intent);
            }
        });

        // Quản lý tài khoản
        findViewById(R.id.manageAccounts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Chuyển đến màn hình quản lý tài khoản
                Intent intent = new Intent(AdminActivity.this, ManageAccountsActivity.class);
                startActivity(intent);
            }
        });

        // Đăng xuất
        findViewById(R.id.buttonLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Đăng xuất khỏi Firebase
                FirebaseAuth.getInstance().signOut();

                // Chuyển về màn hình đăng nhập
                Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();  // Đóng màn hình AdminActivity
            }
        });
    }
}
