package org.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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

public class MainActivity extends AppCompatActivity {

    public static Context context_main;
    public char loginResult='0';
    public String name;


    CalendarView calendarView;
    TextView textcontent, title, location, weather;
    ScrollView scrollView;
    String date, fileName_content, fileName_location, fileName_title, fileName_image, fileName_weather;
    ImageView imageView;
    SwipeRefreshLayout swipeRefreshLayout;
    String permission_list[]={
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION

    };


    //permit_locaition

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //로그인 확인
        isLogin_check login=(isLogin_check) getApplication();
        loginResult=login.getGlobalValue();

        textcontent=(TextView) findViewById(R.id.contents);//일기 내용
        title=(TextView)findViewById(R.id.diary_title);
        location=(TextView)findViewById(R.id.location);
        weather=(TextView)findViewById(R.id.weather);
        imageView=(ImageView) findViewById(R.id.diary_image);
        scrollView=(ScrollView) findViewById(R.id.scrollView);
        swipeRefreshLayout=(SwipeRefreshLayout) findViewById(R.id.swipe);
        //context 지정
        context_main=this;


        //권한설정
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, permission_list, 0);
            for (int i = 0; i < permission_list.length; i++) {
                int permissioncheck = ContextCompat.checkSelfPermission(this, permission_list[i]);
                if (permissioncheck == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(getApplicationContext(), "위치를 불러올 수 없습니다. 권한 설정을 해주세요!", Toast.LENGTH_LONG).show();
                    break;
                }
            }
        }


        //새로고침
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //날짜 받아오기
                /*calendarView=(CalendarView) findViewById(R.id.calendarView);
                Date curDate=new Date(calendarView.getDate());
                date=(curDate.getYear()+1900)+"-"+(curDate.getMonth()+1)+"-"+(curDate.getDate());*/

                FileInputStream inputStream, locationStream, weatherStream, titleStream;

                fileName_content=date+"content";
                fileName_image=date+"image";
                fileName_location=date+"location";
                fileName_title=date+"title";
                fileName_weather=date+"weather";

                try {
                    //이미지 가져오기
                    String imagePath = getCacheDir() + "/" + fileName_image;
                    Bitmap bm = BitmapFactory.decodeFile(imagePath);
                    imageView.setImageBitmap(bm);
                    imageView.setVisibility(View.VISIBLE);
                }
                catch(Exception e){}

                try {
                    //내용가져오기
                    inputStream=openFileInput(fileName_content);
                    byte[] data=new byte[inputStream.available()];
                    while(inputStream.read(data)!=-1){}
                    textcontent.setText(new String(data));
                    textcontent.setVisibility(View.VISIBLE);

                    if(inputStream!=null){
                        inputStream.close();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "내용이 존재하지 않습니다.", Toast.LENGTH_LONG).show();
                }

                try {
                    //제목
                    titleStream=openFileInput(fileName_title);
                    byte[] data=new byte[titleStream.available()];
                    while(titleStream.read(data)!=-1){}
                    title.setText(new String(data));
                    title.setVisibility(View.VISIBLE);

                    if(titleStream!=null){
                        titleStream.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    //날씨
                    weatherStream=openFileInput(fileName_weather);
                    byte[] data=new byte[weatherStream.available()];
                    while(weatherStream.read(data)!=-1){};
                    weather.setText(new String(data));
                    weather.setVisibility(View.VISIBLE);

                    if(weatherStream!=null){
                        weatherStream.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    //위치
                    locationStream=openFileInput(fileName_location);
                    byte[] data=new byte[locationStream.available()];
                    while (locationStream.read(data)!=-1){}
                    location.setText(new String(data));
                    location.setVisibility(View.VISIBLE);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        calendarView=(CalendarView) findViewById(R.id.calendarView);
        Date curDate=new Date(calendarView.getDate());
        date=(curDate.getYear()+1900)+"/"+(curDate.getMonth()+1)+"/"+(curDate.getDate());
        date=date.replace('/', '-');
        find_data(date);

        //캘린더 뷰에서 날짜 선택 시
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {

                date = i + "-" + (i1 + 1) + "-" + i2;
                find_data(date);
            }
        });
        Intent intent=getIntent();
        //intent에서 값 받아오기
        loginResult=intent.getCharExtra("islogin", '0');
        BottomNavigationView bottom_menu=findViewById(R.id.talk_bottom);
        bottom_menu.setOnNavigationItemSelectedListener(new bottomMenuClick());
    }

    public void find_data(String date){

        title.setText(" ");
        weather.setText(" ");
        textcontent.setText(" ");
        location.setText(" ");
        imageView.setImageBitmap(null);

        FileInputStream inputStream, locationStream, weatherStream, titleStream;

        fileName_content=date+"content";
        fileName_image=date+"image";
        fileName_location=date+"location";
        fileName_title=date+"title";
        fileName_weather=date+"weather";

        try {
            //이미지 가져오기
            String imagePath = getCacheDir() + "/" + fileName_image;
            Bitmap bm = BitmapFactory.decodeFile(imagePath);
            imageView.setImageBitmap(bm);
            imageView.setVisibility(View.VISIBLE);
        }
        catch(Exception e){}

        try {
            //내용가져오기
            inputStream=openFileInput(fileName_content);
            byte[] data=new byte[inputStream.available()];
            while(inputStream.read(data)!=-1){}
            textcontent.setText(new String(data));
            textcontent.setVisibility(View.VISIBLE);

            if(inputStream!=null){
                inputStream.close();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "내용이 존재하지 않습니다.", Toast.LENGTH_LONG).show();
        }

        try {
            //제목
            titleStream=openFileInput(fileName_title);
            byte[] data=new byte[titleStream.available()];
            while(titleStream.read(data)!=-1){}
            title.setText(new String(data));
            title.setVisibility(View.VISIBLE);

            if(titleStream!=null){
                titleStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            //날씨
            weatherStream=openFileInput(fileName_weather);
            byte[] data=new byte[weatherStream.available()];
            while(weatherStream.read(data)!=-1){};
            weather.setText(new String(data));
            weather.setVisibility(View.VISIBLE);

            if(weatherStream!=null){
                weatherStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            //위치
            locationStream=openFileInput(fileName_location);
            byte[] data=new byte[locationStream.available()];
            while (locationStream.read(data)!=-1){}
            location.setText(new String(data));
            location.setVisibility(View.VISIBLE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //권한 처리 요청

    //bottom 메뉴 선택시
    class bottomMenuClick implements BottomNavigationView.OnNavigationItemSelectedListener{

        public boolean onNavigationItemSelected(MenuItem item){
            switch (item.getItemId()){
                case R.id.first_tab:{
                    //content의 activity 상태 확인, 실행 중인 액티비티 중 최상위 액티비티 불러오기
                    ActivityManager manager=(ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                    List<ActivityManager.RunningTaskInfo> info=manager.getRunningTasks(1);
                    ComponentName componentName=info.get(0).topActivity;
                    String topActivityName=componentName.getShortClassName().substring(1);

                    if (date.contains("/")) {
                        date=date.replace('/', '-');
                    }

                    if(topActivityName.equals("MainActivity")){
                        //writePage.class로 이동
                        Intent intent=new Intent(MainActivity.this, writePage.class);
                        intent.putExtra("Date", date);
                        startActivity(intent);
                        overridePendingTransition(0, 0);//애니메이션 설정

                    }
                    break;
                }
                case R.id.second_tab:{
                    //GoogleMAP.class로 이동
                    Intent intent=new Intent(MainActivity.this, GoogleMap.class);
                    startActivityForResult(intent, 111);//쌍방향
                    overridePendingTransition(0, 0);
                    break;
                }
                case R.id.third_tab:{
                    //Alert창 삽입
                    AlertDialog.Builder myAlertBuilder=new AlertDialog.Builder(MainActivity.this);
                    myAlertBuilder.setTitle("삭제");
                    myAlertBuilder.setMessage("정말로 삭제하시겠습니다. 삭제 버튼을 누르면 복구할 수 없습니다.");
                    //버튼 추가
                    myAlertBuilder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //삭제
                            boolean bDel = deleteFile(fileName_content);
                            boolean bDel2= deleteFile(fileName_location);
                            boolean bDel3=deleteFile(fileName_weather);
                            boolean bDel4=deleteFile(fileName_title);

                            File file=getCacheDir();
                            File[] flist=file.listFiles();
                            for(int j=0;j< flist.length;j++){
                                if(flist[j].getName().equals(fileName_image)){
                                    flist[j].delete();
                                }
                            }

                            if(bDel&bDel2&bDel3&bDel4){
                                Toast.makeText(getApplicationContext(), "삭제되었습니다", Toast.LENGTH_LONG).show();
                                title.setText("");
                                weather.setText("");
                                location.setText("");
                                textcontent.setText("");
                                imageView.setImageBitmap(null);
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "삭제 오류", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    myAlertBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //취소
                            Toast.makeText(getApplicationContext(), "취소되었습니다", Toast.LENGTH_LONG).show();
                        }
                    });
                    AlertDialog dialog=myAlertBuilder.create();
                    dialog.show();
                    break;
                }
                case R.id.fourth_tab:{
                    //TalkActivity로 이동
                    Intent intent=new Intent(MainActivity.this, TalkActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    break;
                }
                default: break;
            }
            return true;
        }
    }


   public boolean onPrepareOptionsMenu(Menu menu){
        isLogin_check login=(isLogin_check) getApplication();
        loginResult=login.getGlobalValue();
        name=login.getName();
        if(loginResult=='0'){
            menu.clear();
            MenuInflater inflater=getMenuInflater();//다른 화면 xml
            inflater.inflate(R.menu.main_menu, menu);
            menu.findItem(R.id.login).setTitle("로그인");
            menu.findItem(R.id.condition).setTitle("로그아웃 상태입니다");
        }
        else if(loginResult=='1'){
            menu.clear();
            MenuInflater inflater=getMenuInflater();
            inflater.inflate(R.menu.main_menu, menu);
            menu.findItem(R.id.login).setTitle("로그아웃");
            menu.findItem(R.id.condition).setTitle(name+"님");
        }
        return true;
   }

   public boolean onOptionsItemSelected(MenuItem item){
        isLogin_check login=(isLogin_check) getApplication();
        loginResult=login.getGlobalValue();

        int id=item.getItemId();
        switch(id){
            case R.id.login:{
                if(loginResult=='0'){//로그인 하기
                    Intent login_intent=new Intent(this, login_activity.class);
                    startActivityForResult(login_intent, 0);
                }
               else if(loginResult=='1'){//로그아웃하기
                   login.setName(null);
                   login.setId(null);
                   Toast.makeText(getApplicationContext(), "로그아웃 성공", Toast.LENGTH_LONG).show();
                   login.setGlobalValue('0');
                }
               break;
            }
           default: break;
        }
        return super.onOptionsItemSelected(item);
   }
   //activity를 호출하고 돌아올 떄 사용
   protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        isLogin_check login=(isLogin_check) getApplication();
        loginResult=login.getGlobalValue();
   }
}
