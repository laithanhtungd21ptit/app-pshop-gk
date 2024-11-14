package com.example.pshop.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pshop.Activity.OrderSuccessActivity;
import com.example.pshop.Domain.Order;
import com.example.pshop.R;

import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder> {

    private List<Order> orderList;
    private Context context;  // Lưu trữ context để sử dụng Intent

    public OrderHistoryAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_history, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.orderIdText.setText(order.getOrderId());
        holder.orderStatusText.setText(order.getStatus());
        holder.orderTotalText.setText(order.getTotalAmount() + "đ");
        holder.orderProductCountText.setText("Số lượng: " + order.getProductCount());

        // Xử lý sự kiện khi người dùng nhấn vào một đơn hàng
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrderSuccessActivity.class);
            intent.putExtra("orderId", order.getOrderId());
            intent.putExtra("productCount", order.getProductCount());
            intent.putExtra("totalAmount", order.getTotalAmount());
            intent.putExtra("accountNumber", order.getAccountNumber());
            intent.putExtra("bankName", order.getBankName());
            intent.putExtra("accountName", order.getAccountName());
            intent.putExtra("status", order.getStatus());
            context.startActivity(intent);  // Mở OrderSuccessActivity
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView orderIdText, orderStatusText, orderTotalText, orderProductCountText;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdText = itemView.findViewById(R.id.orderIdText);
            orderStatusText = itemView.findViewById(R.id.orderStatusText);
            orderTotalText = itemView.findViewById(R.id.orderTotalText);
            orderProductCountText = itemView.findViewById(R.id.orderProductCountText);
        }
    }
}
