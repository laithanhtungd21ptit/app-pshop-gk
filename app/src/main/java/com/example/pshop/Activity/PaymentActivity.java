package com.example.pshop.Activity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import com.example.pshop.Helper.ManagmentCart;
import com.example.pshop.R;

import androidx.appcompat.app.AppCompatActivity;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;

import java.math.BigDecimal;

public class PaymentActivity extends BaseActivity {

    private static final String PAYPAL_CLIENT_ID = "AffzaAyBc8y0Xe5uzGLgNI8GpDPvPPfxdUxRXFre1EBi6zP8-AZnwruqvtI6Niz2zXmKN7lqIZrZY3Et"; // Sử dụng Client ID của bạn
    private static final int PAYPAL_REQUEST_CODE = 123;
    private Button buttonPayment;
    private TextView paymentInfo;
    private ManagmentCart cartManager;

    // Định nghĩa cấu hình PayPal
    private PayPalConfiguration payPalConfiguration = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX) // Sử dụng môi trường Sandbox
            .clientId(PAYPAL_CLIENT_ID)
            .merchantName("pshop")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.freeprivacypolicy.com/live/5696b213-56d4-4fff-98e4-257183383525"))
            .merchantUserAgreementUri(Uri.parse("https://www.freeprivacypolicy.com/live/5696b213-56d4-4fff-98e4-257183383525"));
           // Thay bằng URL thỏa thuận người dùng của bạn

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Khởi tạo các phần tử giao diện
        buttonPayment = findViewById(R.id.buttonPayment);
        paymentInfo = findViewById(R.id.paymentInfo);
        ImageView backBtn = findViewById(R.id.backBtn);

        // Khởi tạo CartManager
        cartManager = new ManagmentCart(this);

        // Lấy giá trị totalAmount từ Intent
        String totalAmount = getIntent().getStringExtra("totalAmount");

        // Kiểm tra nếu totalAmount không null
        if (totalAmount != null) {
            showPaymentDetails(totalAmount);  // Hiển thị thông tin thanh toán
        }

        // Quay lại CartActivity khi nhấn nút back
        backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(PaymentActivity.this, CartActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        // Cài đặt sự kiện cho nút thanh toán
        buttonPayment.setOnClickListener(view -> {
            // Tạo đối tượng thanh toán PayPal
            PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(totalAmount), "USD", "Product",
                    PayPalPayment.PAYMENT_INTENT_SALE);

            // Tạo Intent để gọi PayPalService
            Intent intent = new Intent(PaymentActivity.this, PayPalService.class);
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfiguration);
            startService(intent);

            // Chuyển sang PayPalFuturePaymentActivity để xử lý thanh toán
            Intent paymentIntent = new Intent(PaymentActivity.this, PayPalFuturePaymentActivity.class);
            paymentIntent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfiguration);
            paymentIntent.putExtra("PAYPAL_PAYMENT", payPalPayment); // Dùng đúng hằng số
            startActivityForResult(paymentIntent, PAYPAL_REQUEST_CODE);
        });
    }

    private void showPaymentDetails(String totalAmount) {
        // Hiển thị thông tin thanh toán
        paymentInfo.setText("Số tiền thanh toán: " + totalAmount + "đ");
        paymentInfo.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Nhận thông tin thanh toán từ Intent
                PaymentConfirmation confirmation = data.getParcelableExtra("PAYPAL_PAYMENT");
                if (confirmation != null) {
                    // In thông tin thanh toán ra log để kiểm tra
                    String paymentDetails = confirmation.toJSONObject().toString();
                    Log.d("PaymentConfirmation", paymentDetails);  // Log kết quả thanh toán
                    handleSuccessfulPayment(confirmation);
                } else {
                    Log.e("PaymentError", "Payment confirmation is null.");
                }
            } else if (resultCode == RESULT_CANCELED) {
                // Thanh toán bị hủy
                Log.e("PaymentError", "Thanh toán bị hủy.");
                Toast.makeText(this, "Thanh toán bị hủy.", Toast.LENGTH_SHORT).show();
            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
                // Thông báo lỗi không hợp lệ
                Log.e("PaymentError", "Có lỗi xảy ra trong quá trình thanh toán.");
                Toast.makeText(this, "Có lỗi xảy ra trong quá trình thanh toán.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void handleSuccessfulPayment(PaymentConfirmation confirmation) {
        String paymentDetails = confirmation.toJSONObject().toString();
        // Xóa giỏ hàng sau khi thanh toán thành công
        cartManager.clearCart();

        // Chuyển hướng về trang chủ
        Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // Kết thúc PaymentActivity
    }

    private void handleFailedPayment() {
        // Hiển thị thông báo khi thanh toán thất bại
        Toast.makeText(PaymentActivity.this, "Thanh toán không thành công. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        // Dừng PayPal service khi không sử dụng nữa
        stopService(new Intent(PaymentActivity.this, PayPalService.class));
        super.onDestroy();
    }
}
