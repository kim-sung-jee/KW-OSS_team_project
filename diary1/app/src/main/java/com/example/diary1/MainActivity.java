package com.example.diary1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
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
            e.printStackTrace();
            out.setText("정보를 불러올 수 없습니다.");
        }
    }
}