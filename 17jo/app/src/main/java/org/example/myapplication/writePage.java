package org.example.myapplication;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

public class writePage extends AppCompatActivity {

    private String fileName_content, fileName_weather, fileName_location, fileName_title, fileName_image;
    Button btn_select;
    Button btn_select2;

    private long backPressTime = 0;
    ImageView imageView;
    Bitmap forImage;

    String T1H, PTY;

    double lat, lng;

    ValueHandler handler = new ValueHandler();
    String result;

    TextView date;
    EditText weather, location, title, content;

    String loading, m, l;

    LocationManager locationManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_page);

        //액티비티 간에 데이터 전달
        date = (TextView) findViewById(R.id.date);
        Intent intent = getIntent();
        m = intent.getStringExtra("Date");
        date.setText(m);
        imageView = findViewById(R.id.imageView);

        //날짜 설정
        StringTokenizer stringTokenizer = new StringTokenizer(m, "-");
        l += stringTokenizer.nextToken();
        l += stringTokenizer.nextToken();
        l += stringTokenizer.nextToken();

        if (l.length() == 11) {
            l = l.substring(4, 11);
        } else {
            l = l.substring(4, 12);
        }

        if (l.length() == 7) {
            char z = l.charAt(6);
            l = l.substring(0, 6) + '0' + z;
        }

        //위치 설정
        getMyLocation();

        btn_select = findViewById(R.id.location_button);
        btn_select2 = findViewById(R.id.weather_button);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        //
        btn_select.setOnClickListener(btnSelectListener);
        btn_select2.setOnClickListener(btnSelectListener2);

        //파일 설정
        fileName_image = m + "image";
        fileName_content = m + "content";
        fileName_location = m + "location";
        fileName_title = m + "title";
        fileName_weather = m + "weather";

        content = findViewById(R.id.content);
        weather = findViewById(R.id.make_weather);
        location = findViewById(R.id.make_location);
        title = findViewById(R.id.make_title);

        isLogin_check acm=(isLogin_check) getApplication();
        Set<String> set=new HashSet<String>(acm.ac_store_name);
        for(String str:set){
            if(Collections.frequency(acm.ac_store_name,str)>=5){
                acm.setFinal_name(str);
                //System.out.println("saafa");
            }
        }




    }

    class ValueHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            //날씨 설정
            Bundle bundle = msg.getData();
            String value = bundle.getString("value");
            StringTokenizer stringTokenizer = new StringTokenizer(value, "-");
            String a = stringTokenizer.nextToken();
            String b = stringTokenizer.nextToken();
            System.out.println("asdfa" + a);
            if (a.equals("0")) {
                weather.setText("맑음");
            } else if (a.equals("1")) {
                weather.setText("비");
            } else if (a.equals("2")) {
                weather.setText("비/눈");
            } else if (a.equals("3")) {
                weather.setText("눈");
            } else if (a.equals("4")) {
                weather.setText("소나기");
            }
            weather.append("/기온:" + b);
        }
    }

    //write_menu-->저장
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.write_menu, menu);
        return true;
    }

    public void getMyLocation() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        //권한 확인 작업
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                return;
            }
        }
        //이전 측정값 불려옴
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location location2 = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if (location2 != null) {
            lat = location2.getLatitude();
            lng = location2.getLongitude();
        }
        else if(location!=null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
        }
    }

    //자동 저장
    public boolean onOptionsItemSelected(MenuItem item) {

        FileOutputStream outputStream = null;
        FileOutputStream weatherStream = null;
        FileOutputStream locationStream = null;
        FileOutputStream titleStream = null;

        String s1 = content.getText().toString();
        String s2 = weather.getText().toString();
        String s3 = location.getText().toString();
        String s4 = title.getText().toString();

        if (s1.length() == 0 || s2.length() == 0 || s3.length() == 0 || s4.length() == 0) {
            Toast.makeText(getApplicationContext(), "빈칸은 허용하지 않습니다!", Toast.LENGTH_LONG).show();

        } else {
            saveBitmapToJpeg(forImage);

            try {
                outputStream = openFileOutput(fileName_content, MODE_PRIVATE);
                weatherStream = openFileOutput(fileName_weather, MODE_PRIVATE);
                locationStream = openFileOutput(fileName_location, MODE_PRIVATE);
                titleStream = openFileOutput(fileName_title, MODE_PRIVATE);

                //에디트 박스에 저장된 스트링 데이터를 스트림에 기록함
                outputStream.write(content.getText().toString().getBytes());
                outputStream.close();
                weatherStream.write(weather.getText().toString().getBytes());
                weatherStream.close();
                locationStream.write(location.getText().toString().getBytes());
                locationStream.close();
                titleStream.write(title.getText().toString().getBytes());
                titleStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            finish();//activity 끝
        }
        return super.onOptionsItemSelected(item);
    }

    //사진 선택시
    public void selectImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 101);
    }

    //사진 불러오기
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {//사진 선택 코드 번호
            if (resultCode == RESULT_OK) {
                Uri fileUri = data.getData();
                ContentResolver resolver = getContentResolver();
                InputStream inputStream = null;
                try {
                    inputStream = resolver.openInputStream(fileUri);
                    Bitmap imgBitmap = BitmapFactory.decodeStream(inputStream);
                    imageView.setImageBitmap(imgBitmap);
                    inputStream.close();
                    forImage = imgBitmap;
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    //사진 저장하기
    public void saveBitmapToJpeg(Bitmap bitmap) {
        File tempFile = new File(getCacheDir(), fileName_image);
        try {
            tempFile.createNewFile();
            FileOutputStream out = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();
        } catch (Exception e) {
        }
    }

    //날씨 버튼 선택 시
    View.OnClickListener btnSelectListener2 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Integer a = (int) lat;
            Integer b = (int) lng;
            String c = a.toString();
            String d = b.toString();

            long mNow=System.currentTimeMillis();
            Date date=new Date(mNow);
            SimpleDateFormat dateFormat=new SimpleDateFormat("hh");
            String getTime=dateFormat.format(date);


            //날씨 불러오기
            setWeather w = new setWeather();
            new Thread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void run() {
                    try {
                        loading = w.loading(c, d, l,getTime);
                        Message message = handler.obtainMessage();
                        Bundle bundle = new Bundle();
                        bundle.putString("value", loading);
                        message.setData(bundle);
                        handler.sendMessage(message);
                        Thread.sleep(3000);

                    }
                    catch (Exception e){};
                }
            }).start();
        }
    };
    //장소 버튼 눌렀을 떄
    View.OnClickListener btnSelectListener=new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            isLogin_check lct = (isLogin_check) getApplication();
            Iterator<String> iterator = lct.getFinal_name().iterator();

            //장소 팝업 띄우기
            PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);
            Menu menu = popupMenu.getMenu();

            if (lct.getFinal_name().size() != 0) {
                for (int i = 0; i < lct.getFinal_name().size(); i++) {
                    menu.add(0, i, 0, iterator.next());
                }
            }

            //팝업 선택하기
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    location.setText(menuItem.getTitle());
                    return true;
                }
            });
            popupMenu.show();
        }
    };


//파일 저장 버튼
//뒤로 두번 연속으로 누르면 종료 처리
    public void onBackPressed(){
        if(System.currentTimeMillis()-backPressTime>=2000){
            backPressTime = System.currentTimeMillis();
            Toast.makeText(getApplicationContext(), "백(Back) 버튼을 한 번 더 누르면 종료", Toast.LENGTH_LONG).show();
        }
        else if(System.currentTimeMillis() - backPressTime < 2000)
            finish();
    }
}

