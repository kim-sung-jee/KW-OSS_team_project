package com.example.diary1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView out=(TextView)findViewById(R.id.text);

        weather w=new weather();
        try {
            final String loading = w.loading();
            out.setText(loading);
        } catch (IOException e) {
            out.setText("정보를 불러올 수 없습니다.");
        }
    }
}