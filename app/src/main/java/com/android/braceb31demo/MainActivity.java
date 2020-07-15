package com.android.braceb31demo;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.brace.android.b31.activity.ScanActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn1,btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn1:
                startActivity(new Intent(MainActivity.this,OwnScanActivity.class));
                break;
            case R.id.btn2:
                startActivity(new Intent(MainActivity.this, ScanActivity.class));
                break;
        }
    }
}
