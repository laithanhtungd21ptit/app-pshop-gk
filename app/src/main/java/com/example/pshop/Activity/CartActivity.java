package com.example.pshop.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.pshop.Adapter.CartAdapter;
import com.example.pshop.Helper.ManagmentCart;
import com.example.pshop.R;
import com.example.pshop.databinding.ActivityCartBinding;

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
            // Lấy giá trị totalTxt từ binding
            String totalAmount = binding.totalTxt.getText().toString().replace("đ", "").trim();

            // Chuyển sang màn hình thanh toán và truyền giá trị totalAmount
            Intent intent = new Intent(CartActivity.this, PaymentActivity.class);
            intent.putExtra("totalAmount", totalAmount);  // Truyền giá trị vào Intent
            startActivity(intent);
        });
    }

    private void calculatorCart() {
        double percentTax = 0.02;
        double delivery = 20000;
        tax = Math.round((managmentCart.getTotalFee() * percentTax * 100.0)) / 100.0;

        double total = Math.round((managmentCart.getTotalFee() + tax + delivery) * 100) / 100;
        double itemTotal = Math.round(managmentCart.getTotalFee() * 100) / 100;

        binding.totalFeeTxt.setText(itemTotal + "đ");
        binding.taxTxt.setText(tax + "đ");
        binding.deliveryTxt.setText(delivery + "đ");
        binding.totalTxt.setText(total + "đ");
    }


}
