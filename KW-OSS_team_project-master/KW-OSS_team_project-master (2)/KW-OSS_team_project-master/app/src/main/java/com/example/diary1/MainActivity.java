package com.example.diary1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.List;
import java.util.function.ToLongBiFunction;

public class MainActivity extends AppCompatActivity {
    Button button;
    Intent serviceIntent;



    public static Context context_main;
    FragmentManager fragmentManager = getSupportFragmentManager();
    public char loginResult='0';
    public String name;

    CalendarView calendarView;

    TextView textcontent;
    TextView title;
    TextView location;
    TextView weather;


    ScrollView scrollView;

    String date;
    String fileName_content;
    String fileName_weather;
    String fileName_location;
    String fileName_title;
    String fileName_image;
    ImageView imageView;


    SwipeRefreshLayout swipeRefreshLayout;


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

        textcontent=findViewById(R.id.textView34);
        title=findViewById(R.id.textView9);
        location=findViewById(R.id.textView8);
        weather=findViewById(R.id.textView7);
        context_main=this;
        imageView=findViewById(R.id.imageView2);

        scrollView=findViewById(R.id.scrollView);

        swipeRefreshLayout=findViewById(R.id.swipe);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                calendarView=(CalendarView) findViewById(R.id.calendarView);
                Date curDate=new Date(calendarView.getDate());
                date=(curDate.getYear()+1900)+"-"+(curDate.getMonth()+1)+"-"+(curDate.getDate());

                FileInputStream inputStream=null;
                FileInputStream locationStream=null;
                FileInputStream weatherStream=null;
                FileInputStream titleStream=null;

                fileName_content=date+"content";
                fileName_location=date+"location";
                fileName_weather=date+"weather";
                fileName_title=date+"title";
                fileName_image=date+"image";

                try{
                    String imagePath=getCacheDir()+"/"+fileName_image;
                    Bitmap bm= BitmapFactory.decodeFile(imagePath);
                    imageView.setImageBitmap(bm);


                }catch (Exception e){

                }
                try{

                    // 파일 유효성 검사
                    inputStream=openFileInput(fileName_content);
                    byte[] data = new byte[inputStream.available()];
                    while (inputStream.read(data) != -1) {}
                    textcontent.setText(new String(data));
//
                    try
                    {
                        //파일 읽기 성공 여부 상관없이 반드시 스트림 닫기
                        if(inputStream != null)
                            inputStream.close();
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }


                }catch (Exception e){
                    textcontent.setText("아직 내용이 없습니다");
                }


                try{
                    locationStream=openFileInput(fileName_location);
                    byte[]data2=new byte[locationStream.available()];
                    while(locationStream.read(data2)!=-1){}
                    location.setText(new String(data2));


                    try
                    {
                        //파일 읽기 성공 여부 상관없이 반드시 스트림 닫기
                        if(locationStream != null)
                            locationStream.close();
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }

                }catch (Exception e){
                    location.setText("아직 내용이 없습니다");
                }

                try{

                    // 파일 유효성 검사
                    titleStream=openFileInput(fileName_title);
                    byte[] data4 = new byte[titleStream.available()];
                    while (titleStream.read(data4) != -1) {}
                    title.setText(new String(data4));
//
                    try
                    {
                        //파일 읽기 성공 여부 상관없이 반드시 스트림 닫기
                        if(titleStream != null)
                            titleStream.close();
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }


                }catch (Exception e){
                    title.setText("아직 내용이 없습니다");
                }

                try{

                    // 파일 유효성 검사
                    weatherStream=openFileInput(fileName_weather);
                    byte[] data3 = new byte[weatherStream.available()];
                    while (weatherStream.read(data3) != -1) {}
                    weather.setText(new String(data3));
//
                    try
                    {
                        //파일 읽기 성공 여부 상관없이 반드시 스트림 닫기
                        if(weatherStream != null)
                            weatherStream.close();
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }


                }catch (Exception e){
                    weather.setText("아직 내용이 없습니다");
                }

                swipeRefreshLayout.setRefreshing(false);
            }
        });


        calendarView=(CalendarView) findViewById(R.id.calendarView);
        Date curDate=new Date(calendarView.getDate());
        date=(curDate.getYear()+1900)+"-"+(curDate.getMonth()+1)+"-"+(curDate.getDate());

        FileInputStream inputStream=null;
        FileInputStream locationStream=null;
        FileInputStream weatherStream=null;
        FileInputStream titleStream=null;

        fileName_content=date+"content";
        fileName_location=date+"location";
        fileName_weather=date+"weather";
        fileName_title=date+"title";
        fileName_image=date+"image";

        try{
            String imagePath=getCacheDir()+"/"+fileName_image;
            Bitmap bm= BitmapFactory.decodeFile(imagePath);
            imageView.setImageBitmap(bm);


        }catch (Exception e){

        }
        try{

            // 파일 유효성 검사
            inputStream=openFileInput(fileName_content);
            byte[] data = new byte[inputStream.available()];
            while (inputStream.read(data) != -1) {}
            textcontent.setText(new String(data));
//
            try
            {
                //파일 읽기 성공 여부 상관없이 반드시 스트림 닫기
                if(inputStream != null)
                    inputStream.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }


        }catch (Exception e){
            textcontent.setText("아직 내용이 없습니다");
        }


        try{
            locationStream=openFileInput(fileName_location);
            byte[]data2=new byte[locationStream.available()];
            while(locationStream.read(data2)!=-1){}
            location.setText(new String(data2));


            try
            {
                //파일 읽기 성공 여부 상관없이 반드시 스트림 닫기
                if(locationStream != null)
                    locationStream.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

        }catch (Exception e){
            location.setText("아직 내용이 없습니다");
        }

        try{

            // 파일 유효성 검사
            titleStream=openFileInput(fileName_title);
            byte[] data4 = new byte[titleStream.available()];
            while (titleStream.read(data4) != -1) {}
            title.setText(new String(data4));
//
            try
            {
                //파일 읽기 성공 여부 상관없이 반드시 스트림 닫기
                if(titleStream != null)
                    titleStream.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }


        }catch (Exception e){
            title.setText("아직 내용이 없습니다");
        }

        try{

            // 파일 유효성 검사
            weatherStream=openFileInput(fileName_weather);
            byte[] data3 = new byte[weatherStream.available()];
            while (weatherStream.read(data3) != -1) {}
            weather.setText(new String(data3));
//
            try
            {
                //파일 읽기 성공 여부 상관없이 반드시 스트림 닫기
                if(weatherStream != null)
                    weatherStream.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }


        }catch (Exception e){
            weather.setText("아직 내용이 없습니다");
        }






//        calendarView=(CalendarView) findViewById(R.id.calendarView);
//        Date curDate=new Date(calendarView.getDate());
//        date=(curDate.getYear()+1900)+"/"+(curDate.getMonth()+1)+"/"+(curDate.getDate());
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {

                date=i+"-"+(i1+1)+"-"+i2;
                FileInputStream inputStream=null;
                FileInputStream locationStream=null;
                FileInputStream weatherStream=null;
                FileInputStream titleStream=null;

                fileName_content=date+"content";
                fileName_location=date+"location";
                fileName_weather=date+"weather";
                fileName_title=date+"title";
                fileName_image=date+"image";

                try{
                    String imagePath=getCacheDir()+"/"+fileName_image;
                    Bitmap bm= BitmapFactory.decodeFile(imagePath);
                    imageView.setImageBitmap(bm);


                }catch (Exception e){

                }
                try{

                    // 파일 유효성 검사
                    inputStream=openFileInput(fileName_content);
                    byte[] data = new byte[inputStream.available()];
                    while (inputStream.read(data) != -1) {}
                    textcontent.setText(new String(data));
//
                    try
                    {
                        //파일 읽기 성공 여부 상관없이 반드시 스트림 닫기
                        if(inputStream != null)
                            inputStream.close();
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }


                }catch (Exception e){
                    textcontent.setText("아직 내용이 없습니다");
                }


                try{
                    locationStream=openFileInput(fileName_location);
                    byte[]data2=new byte[locationStream.available()];
                    while(locationStream.read(data2)!=-1){}
                    location.setText(new String(data2));


                    try
                    {
                        //파일 읽기 성공 여부 상관없이 반드시 스트림 닫기
                        if(locationStream != null)
                            locationStream.close();
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }

                }catch (Exception e){
                    location.setText("아직 내용이 없습니다");
                }

                try{

                    // 파일 유효성 검사
                    titleStream=openFileInput(fileName_title);
                    byte[] data4 = new byte[titleStream.available()];
                    while (titleStream.read(data4) != -1) {}
                    title.setText(new String(data4));
//
                    try
                    {
                        //파일 읽기 성공 여부 상관없이 반드시 스트림 닫기
                        if(titleStream != null)
                            titleStream.close();
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }


                }catch (Exception e){
                    title.setText("아직 내용이 없습니다");
                }

                try{

                    // 파일 유효성 검사
                    weatherStream=openFileInput(fileName_weather);
                    byte[] data3 = new byte[weatherStream.available()];
                    while (weatherStream.read(data3) != -1) {}
                    weather.setText(new String(data3));
//
                    try
                    {
                        //파일 읽기 성공 여부 상관없이 반드시 스트림 닫기
                        if(weatherStream != null)
                            weatherStream.close();
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }


                }catch (Exception e){
                    weather.setText("아직 내용이 없습니다");
                }

            }
        });





        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            requestPermissions(permission_list,0);
        }

        Intent intent=getIntent();
        loginResult=intent.getCharExtra("islogin",'0');

        BottomNavigationView bottom_menu = findViewById(R.id.talk_bottom);

        bottom_menu.setOnNavigationItemSelectedListener(new bottomMenuClick());


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

                    ActivityManager manager=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
                    List<ActivityManager.RunningTaskInfo> info=manager.getRunningTasks(1);
                    ComponentName componentName=info.get(0).topActivity;
                    String topActivityName=componentName.getShortClassName().substring(1);


                    if(date.contains("/")) {
                        date=date.replace('/', '-');
                    }



                    if(topActivityName.equals("MainActivity")){

                            Intent intent=new Intent(MainActivity.this,writePage.class);
                            intent.putExtra("Date",date);
                            startActivity(intent);
                            overridePendingTransition(0,0);
                            return true;
                    }else{
                        Intent intent1=new Intent(getApplicationContext(),MainActivity.class);
                        startActivityForResult(intent1,333);
                        overridePendingTransition(0,0);
                        return true;
                    }


                case R.id.second_tab:
                    Intent intent=new Intent(MainActivity.this,GoogleMap.class);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivityForResult(intent,111);
                    overridePendingTransition(0,0);
                    return true;
                case R.id.third_tab:
                    boolean bDel = deleteFile(fileName_content);
                    boolean bDel2= deleteFile(fileName_location);
                    boolean bDel3=deleteFile(fileName_weather);
                    boolean bDel4=deleteFile(fileName_title);


                    File file=getCacheDir();
                    File[] flist=file.listFiles();
                    for(int i=0;i<flist.length;i++){
                        if(flist[i].getName().equals(fileName_image)){
                            flist[i].delete();
                            Toast.makeText(getApplicationContext(),"asd",Toast.LENGTH_LONG).show();
                        }
                    }
                    if(bDel&&bDel2&&bDel3&&bDel4)
                        Toast.makeText(getApplicationContext(), "메모 삭제 완료", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getApplicationContext(), "메모 삭제 실패", Toast.LENGTH_LONG).show();



                    return true;
                case R.id.fourth_tab:
                    Intent intent22=new Intent(MainActivity.this,TalkActivity.class);
                    startActivity(intent22);

                    overridePendingTransition(0,0);
                default:
                    return true;
            }
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)  {
        isLogin_check login=(isLogin_check) getApplication();
        loginResult=login.getGlobalValue();
        name=login.getName();
        if(loginResult=='0'){
            menu.clear();
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.main_menu, menu);
            menu.findItem(R.id.item2).setTitle("로그인하기");
            menu.findItem(R.id.item3).setTitle("로그아웃상태입니다");
        }else if(loginResult=='1'){
            menu.clear();
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.main_menu, menu);
            menu.findItem(R.id.item2).setTitle("로그아웃");
            menu.findItem(R.id.item3).setTitle(name+"으로 로그인 됨");
        }
        return true;
    }

    // 옵션 메뉴의 항목을 터치하면 호출되는 메서드
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        isLogin_check login=(isLogin_check) getApplication();
        loginResult=login.getGlobalValue();

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
                    isLogin_check login_check=new isLogin_check();
                    login_check.setName(null);
                    Toast.makeText(getApplicationContext(), "로그아웃 성공", Toast.LENGTH_LONG).show();
                    login.setGlobalValue('0');

                }
                //startActivity(item2_intent);
                break;
            case R.id.item3:

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

        if(requestCode==0&&resultCode==RESULT_OK){

        }
        if(requestCode==111){

        }

    }
}

