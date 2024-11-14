package com.example.pshop.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pshop.R;

public class OrderSuccessActivity extends BaseActivity {

    // Khai báo các TextView để hiển thị thông tin đơn hàng
    private TextView orderIdTxt, productCountTxt, totalAmountTxt, accountNumberTxt, bankNameTxt, accountNameTxt, transferNoteTxt, statusTxt;
    private LinearLayout homeBtn, cartBtn, personalBtn;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success);

        // Ánh xạ các TextView
        orderIdTxt = findViewById(R.id.orderIdTxt);
        productCountTxt = findViewById(R.id.productCountTxt);
        totalAmountTxt = findViewById(R.id.totalAmountTxt);
        accountNumberTxt = findViewById(R.id.accountNumberTxt);
        bankNameTxt = findViewById(R.id.bankNameTxt);
        accountNameTxt = findViewById(R.id.accountNameTxt);
        transferNoteTxt = findViewById(R.id.transferNoteTxt);
        statusTxt = findViewById(R.id.statusTxt);

        // Ánh xạ các Button (nút)
        homeBtn = findViewById(R.id.homeBtn);
        cartBtn = findViewById(R.id.cart_btn);
        personalBtn = findViewById(R.id.perBtn);
        backBtn = findViewById(R.id.backBtn);

        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        String orderId = intent.getStringExtra("orderId");
        int productCount = intent.getIntExtra("productCount", 0);
        double totalAmount = intent.getDoubleExtra("totalAmount", 0.0);
        String accountNumber = intent.getStringExtra("accountNumber");
        String bankName = intent.getStringExtra("bankName");
        String accountName = intent.getStringExtra("accountName");
        String status = intent.getStringExtra("status");

        // Hiển thị thông tin đơn hàng
        orderIdTxt.setText("Mã đơn hàng: " + orderId);
        productCountTxt.setText("Số sản phẩm: " + productCount);
        totalAmountTxt.setText("Tổng tiền: " + totalAmount + "đ");
        accountNumberTxt.setText("Số tài khoản: " + accountNumber);
        bankNameTxt.setText("Tên ngân hàng: " + bankName);
        accountNameTxt.setText("Tên tài khoản: " + accountName);
        transferNoteTxt.setText("Nội dung chuyển tiền: " + orderId);
        statusTxt.setText("Trạng thái: " + status);

        // Xử lý sự kiện nhấn nút Home
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến trang chủ (MainActivity)
                Intent homeIntent = new Intent(OrderSuccessActivity.this, MainActivity.class);
                startActivity(homeIntent);
                finish(); // Đảm bảo không quay lại trang này khi nhấn back
            }
        });

        // Xử lý sự kiện nhấn nút Giỏ hàng
        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến trang giỏ hàng (CartActivity)
                Intent cartIntent = new Intent(OrderSuccessActivity.this, CartActivity.class);
                startActivity(cartIntent);
                finish();
            }
        });

        // Xử lý sự kiện nhấn nút Cá nhân
        personalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến trang cá nhân (PersonalActivity)
                Intent personalIntent = new Intent(OrderSuccessActivity.this, ProfileActivity.class);
                startActivity(personalIntent);
                finish();
            }
        });

        // Xử lý sự kiện nhấn nút Back
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Quay lại trang trước (Giỏ hàng)
                onBackPressed(); // Hoặc có thể tạo Intent về giỏ hàng trực tiếp như CartActivity nếu cần
            }
        });
    }
}
