package com.example.class9;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class summary extends AppCompatActivity {

    private TextView orderSummary;
    private String toSender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        orderSummary = findViewById(R.id.orderSummaryText_id);

        Intent intent = getIntent();
        String message = intent.getStringExtra("summaryMessage");
        toSender = intent.getStringExtra("toSender");
        orderSummary.setText(message);
    }

    // same function form the activity main
    public void ConfirmOrder(View view){

        String message = orderSummary.getText().toString();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {toSender});
        intent.putExtra(Intent.EXTRA_SUBJECT,"Your Pizza Order");
        intent.putExtra(Intent.EXTRA_TEXT, message);

        try {
            startActivity(Intent.createChooser(intent, "Sending mail"));
        }
        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(summary.this, "There are no email client installed", Toast.LENGTH_SHORT).show();
        }

    }
}