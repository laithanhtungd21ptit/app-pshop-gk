package com.example.pshop.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.pshop.Activity.BaseActivity;
import com.example.pshop.Activity.MainActivity;
import com.example.pshop.Activity.ProfileActivity;
import com.example.pshop.Adapter.CartAdapter;
import com.example.pshop.Domain.ItemsDomain;
import com.example.pshop.Domain.Order;
import com.example.pshop.Helper.ManagmentCart;
import com.example.pshop.R;
import com.example.pshop.databinding.ActivityCartBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CartActivity extends BaseActivity {
    ActivityCartBinding binding;
    private double tax;
    private ManagmentCart managmentCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managmentCart = new ManagmentCart(this);
        calculatorCart();
        setVariable();
        initCartList();
    }

    private void initCartList() {
        if (managmentCart.getListCart().isEmpty()) {
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scrollViewCart.setVisibility(View.GONE);
        } else {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scrollViewCart.setVisibility(View.VISIBLE);
        }

        binding.cartView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.cartView.setAdapter(new CartAdapter(managmentCart.getListCart(), this, () -> calculatorCart()));
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(view -> finish());

        // Button chuyển đến trang cá nhân
        binding.perBtn.setOnClickListener(view -> {
            Intent intent = new Intent(CartActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        // Button chuyển đến trang chủ
        binding.homeBtn.setOnClickListener(view -> {
            Intent intent = new Intent(CartActivity.this, MainActivity.class);
            startActivity(intent);
        });

        // Button thanh toán
        binding.paymentBtn.setOnClickListener(view -> {
            // Lấy giá trị totalTxt từ binding và loại bỏ dấu phẩy
            String totalAmount = binding.totalTxt.getText().toString().replace("đ", "").trim();
            totalAmount = totalAmount.replace(",", "");  // Loại bỏ dấu phẩy

            // Lấy số tiền thanh toán
            double totalAmountDouble = Double.parseDouble(totalAmount);

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat minuteFormat = new SimpleDateFormat("mm");
            SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
            SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
            SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");

            String minute = minuteFormat.format(calendar.getTime());
            String day = dayFormat.format(calendar.getTime());
            String month = monthFormat.format(calendar.getTime());
            String year = yearFormat.format(calendar.getTime());

            String orderId = "DSA" + (int) totalAmountDouble + minute + day + month + year;

            // Lấy danh sách sản phẩm từ giỏ hàng
            List<ItemsDomain> itemsList = new ArrayList<>();
            for (ItemsDomain item : managmentCart.getListCart()) {
                itemsList.add(new ItemsDomain(item.getTitle(), item.getDescription(), item.getPicUrl(),
                        item.getPrice(), item.getOldPrice(), item.getReview(),
                        item.getRating()));
            }

            // Lấy userId từ Firebase Authentication
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            // Lưu đơn hàng vào Firebase
            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            String orderIdKey = "orderId_" + orderId;
            int productCount = managmentCart.getListCart().size();
            String accountNumber = "3228665114288";
            String bankName = "Ngân hàng ABC";
            String accountName = "Nguyễn Văn A";
            String status = "Chưa thanh toán";

            Order order = new Order(orderId, productCount, totalAmountDouble, accountNumber, bankName,
                    accountName, orderIdKey, status, itemsList);

            // Lưu đơn hàng vào Firebase trong nhánh của người dùng
            database.child("orders").child(userId).child(orderIdKey).setValue(order)
                    .addOnSuccessListener(aVoid -> {
                        // Chuyển sang màn hình "Đặt đơn hàng thành công"
                        Intent intentSuccess = new Intent(CartActivity.this, OrderSuccessActivity.class);
                        intentSuccess.putExtra("orderId", orderId);
                        intentSuccess.putExtra("productCount", productCount);
                        intentSuccess.putExtra("totalAmount", totalAmountDouble);
                        intentSuccess.putExtra("accountNumber", accountNumber);
                        intentSuccess.putExtra("bankName", bankName);
                        intentSuccess.putExtra("accountName", accountName);
                        intentSuccess.putExtra("status", status);
                        intentSuccess.putExtra("items", (Serializable) itemsList);
                        startActivity(intentSuccess);
                    })
                    .addOnFailureListener(e -> {
                        // Xử lý lỗi khi lưu vào Firebase
                        Toast.makeText(CartActivity.this, "Lỗi khi lưu đơn hàng vào Firebase!", Toast.LENGTH_SHORT).show();
                    });
        });
    }

    private void calculatorCart() {
        double percentTax = 0.0;  // Tỷ lệ thuế (2%)
        double delivery = 0.0;   // Phí giao hàng cố định

        // Tính thuế (tax) dựa trên tổng giá trị của giỏ hàng
        tax = Math.round((managmentCart.getTotalFee() * percentTax * 100.0)) / 100.0;

        // Tính tổng số tiền (total), bao gồm giá trị giỏ hàng, thuế và phí giao hàng
        double total = Math.round((managmentCart.getTotalFee() + tax + delivery) * 100) / 100;

        // Tính tổng giá trị của sản phẩm trong giỏ hàng (itemTotal)
        double itemTotal = Math.round(managmentCart.getTotalFee() * 100) / 100;

        // Hiển thị giá trị tính toán vào các TextViews
        binding.totalFeeTxt.setText(itemTotal + "đ");
        binding.taxTxt.setText(tax + "đ");
        binding.deliveryTxt.setText(delivery + "đ");
        binding.totalTxt.setText(total + "đ");
    }
}
