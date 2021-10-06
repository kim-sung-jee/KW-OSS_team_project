package com.example.diary1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    fragment_pink pinkFragment = new fragment_pink();
    blue blueFragment=new blue();
    green greeFragment=new green();
    public static Context context_main;
    FragmentManager fragmentManager = getSupportFragmentManager();
    public char loginResult='0';
    TextView text1;

    String permission_list[]={
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isLogin_check login=(isLogin_check) getApplication();
        loginResult=login.getGlobalValue();

        context_main=this;


        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            requestPermissions(permission_list,0);
        }

        Intent intent=getIntent();
        loginResult=intent.getCharExtra("islogin",'0');

        BottomNavigationView bottom_menu = findViewById(R.id.bottom_menu);
        text1=(TextView)findViewById(R.id.textView3);
        bottom_menu.setOnNavigationItemSelectedListener(new bottomMenuClick());
        FragmentTransaction f1=getSupportFragmentManager().beginTransaction().replace(R.id.container, pinkFragment);
        f1.addToBackStack(null);
        f1.commit();

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for(int result:grantResults){
            if(result== PackageManager.PERMISSION_DENIED){
                return;
            }
        }

    }





    class bottomMenuClick implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.first_tab:
                    FragmentTransaction f1=getSupportFragmentManager().beginTransaction().replace(R.id.container, pinkFragment);
                    f1.addToBackStack(null);
                    f1.commit();
                    return true;

                case R.id.second_tab:
                    Intent intent=new Intent(MainActivity.this,GoogleMap.class);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivityForResult(intent,111);
                    overridePendingTransition(0,0);
                    return true;
                case R.id.third_tab:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,blueFragment).commit();
                    return true;
                case R.id.fourth_tab:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,greeFragment).commit();
                default:
                    return true;
            }
        }
    }
     //옵션 메뉴 구성을 위해 호출하는 메서드


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        isLogin_check login=(isLogin_check)getApplication();
//        loginResult=login.getGlobalValue();
//        if(loginResult=='0') {
//            MenuInflater inflater = getMenuInflater();
//            inflater.inflate(R.menu.main_menu, menu);
//        }else{
//            MenuInflater inflater = getMenuInflater();
//            inflater.inflate(R.menu.main_menu_logined, menu);
//        }
//
//
//        return super.onCreateOptionsMenu(menu);
//    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu)  {
        isLogin_check login=(isLogin_check) getApplication();
        loginResult=login.getGlobalValue();
        if(loginResult=='0'){
            menu.clear();
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.main_menu, menu);
            menu.findItem(R.id.item2).setTitle("로그인하기");
        }else if(loginResult=='1'){
            menu.clear();
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.main_menu, menu);
            menu.findItem(R.id.item2).setTitle("로그아웃");
        }
        return true;
    }

    // 옵션 메뉴의 항목을 터치하면 호출되는 메서드
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        isLogin_check login=(isLogin_check) getApplication();
        loginResult=login.getGlobalValue();
        //text1.setText("확인 :"+loginResult);
        //사용자가 선택한 메뉴 항목의 아이디를 추출한다.

        int id=item.getItemId();
        switch (id){
            case R.id.item1:
                break;
            case R.id.item2:
                if(loginResult=='0') {
                    //item.setTitle("로그인");

                    Intent item2_intent = new Intent(this, login_activity.class);
                    startActivityForResult(item2_intent, 0);
                }else if(loginResult=='1'){
                    //item.setTitle("로그아웃");

                    Toast.makeText(getApplicationContext(), "로그아웃 성공", Toast.LENGTH_LONG).show();
                    login.setGlobalValue('0');

                }
                //startActivity(item2_intent);
                break;
            case R.id.item3:
                text1.setText("확인 :"+login.getGlobalValue());
                break;
            case R.id.item4:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        isLogin_check login=(isLogin_check) getApplication();
        loginResult=login.getGlobalValue();
        text1=(TextView)findViewById(R.id.textView3);
        if(requestCode==0&&resultCode==RESULT_OK){
            text1.setText(login.getId());
        }
        if(requestCode==111){

        }
    }
}

