package com.example.pshop.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pshop.Adapter.OrderHistoryAdapter;
import com.example.pshop.Domain.Order;
import com.example.pshop.R;
import com.example.pshop.databinding.ActivityOrderHistoryBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryActivity extends BaseActivity {

    private ActivityOrderHistoryBinding binding;
    private OrderHistoryAdapter adapter;
    private List<Order> orderList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        orderList = new ArrayList<>();
        setupRecyclerView();
        loadOrderHistory();
        ImageView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(view -> finish()); // Đóng activity và quay lại màn hình trước

    }

    private void setupRecyclerView() {
        binding.recyclerViewOrders.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OrderHistoryAdapter(OrderHistoryActivity.this, orderList);

        binding.recyclerViewOrders.setAdapter(adapter);
    }

    private void loadOrderHistory() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("orders").child(userId);

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                        Order order = orderSnapshot.getValue(Order.class);
                        if (order != null) {
                            orderList.add(order);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    binding.emptyHistoryText.setVisibility(View.GONE);
                    binding.recyclerViewOrders.setVisibility(View.VISIBLE);
                } else {
                    binding.emptyHistoryText.setVisibility(View.VISIBLE);
                    binding.recyclerViewOrders.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OrderHistoryActivity.this, "Lỗi khi tải lịch sử đơn hàng", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
