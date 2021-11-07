package com.example.diary1;

import static java.lang.System.out;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.ThemedSpinnerAdapter;
import android.widget.Toast;

import com.android.volley.misc.AsyncTask;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class writePage extends AppCompatActivity {


    EditText editText_TextArea;
    private String fileName_content;
    private String fileName_weather;
    private String fileName_location;
    private String fileName_title;
    private String fileName_image;

    Button btn_select;
    Button btn_select2;

    private long backPressTime = 0;
    ImageView imageView;
    Bitmap forImage;

    String T1H;
    String PTY;

    double lat,lng;

    ValueHandler handler=new ValueHandler();

    String result;

    TextView date;
    EditText weather_;
    EditText location;
    EditText title;

    String loading;

    LocationManager locationManager;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_page);

        // 액티비티간 데이터 전달
        date=(TextView) findViewById(R.id.textView13);
        Intent secondIntent=getIntent();
        String m=secondIntent.getStringExtra("Date");
        date.setText(m);
        imageView=findViewById(R.id.fornoimage_png);


        getMyLocation();

        btn_select=findViewById(R.id.button10);
        btn_select2=findViewById(R.id.button11);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        btn_select.setOnClickListener(btnSelectListener);
        btn_select2.setOnClickListener(btnSelectListener2);

        //btn_Delete.setOnClickListener(btnDeleteListener);

        fileName_image=m+"image";
        fileName_content=m+"content";
        fileName_location=m+"location";
        fileName_weather=m+"weather";
        fileName_title=m+"title";

        editText_TextArea=findViewById(R.id.editText);
        weather_=findViewById(R.id.editTextTextPersonName3);
        location=findViewById(R.id.editTextTextPersonName2);
        title=findViewById(R.id.editTextTextPersonName);








    }


    class ValueHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle=msg.getData();
            String value=bundle.getString("value");
            StringTokenizer stringTokenizer=new StringTokenizer(value,"-");
            String a=stringTokenizer.nextToken();
            String b=stringTokenizer.nextToken();
            System.out.println("asdfa"+a);
            if(a.equals("0")){
                weather_.setText("맑음");
            }else if(a.equals("1")){
                weather_.setText("비");
            }else if(a.equals("2")){
                weather_.setText("비/눈");
            }else if(a.equals("3")){
                weather_.setText("눈");
            }else if(a.equals("4")){
                weather_.setText("소나기");
            }
            weather_.append("/기온:"+b);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.write_menu,menu);
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
        //이전에 측정했던 값을 가져온다.
        Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location location2 = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if(location2!=null){
            lat=location2.getLatitude();
            lng=location2.getLongitude();
        }else{
            lat=location1.getLatitude();
            lng=location1.getLongitude();
        }
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        FileOutputStream outputStream = null;
        FileOutputStream weatherStream = null;
        FileOutputStream locationStream = null;
        FileOutputStream titleStream = null;
        String s1 = editText_TextArea.getText().toString();
        String s2 = weather_.getText().toString();
        String s3 = location.getText().toString();
        String s4 = title.getText().toString();



        if (s1.length() == 0 || s2.length() == 0 || s3.length() == 0 || s4.length() == 0) {
            Toast.makeText(getApplicationContext(), "빈칸은 허용하지 않습니다!", Toast.LENGTH_LONG).show();

        } else {
            try {
                try {
                    saveBitmapToJpeg(forImage);
                } catch (Exception e) {

                }
                outputStream = openFileOutput(fileName_content, MODE_PRIVATE);
                weatherStream = openFileOutput(fileName_weather, MODE_PRIVATE);
                locationStream = openFileOutput(fileName_location, MODE_PRIVATE);
                titleStream = openFileOutput(fileName_title, MODE_PRIVATE);

                //에디트 박스에 저장된 스트링 데이터를 스트림에 기록함
                outputStream.write(editText_TextArea.getText().toString().getBytes());
                outputStream.close();
                weatherStream.write(weather_.getText().toString().getBytes());
                weatherStream.close();
                locationStream.write(location.getText().toString().getBytes());
                locationStream.close();
                titleStream.write(title.getText().toString().getBytes());
                titleStream.close();

                //Toast.makeText(getApplicationContext(), fileName_content+" "+fileName_location, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }

            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void selectImage(View view){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101){
            if(resultCode==RESULT_OK){
                Uri fileUri=data.getData();
                ContentResolver resolver=getContentResolver();
                try{
                    InputStream inputStream=resolver.openInputStream(fileUri);
                    Bitmap imgBitmap= BitmapFactory.decodeStream(inputStream);
                    imageView.setImageBitmap(imgBitmap);
                    inputStream.close();
                    forImage=imgBitmap;

                    //Toast.makeText(getApplicationContext(),"파일 불러오기 성공",Toast.LENGTH_LONG).show();

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"실패",Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void saveBitmapToJpeg(Bitmap bitmap){
        File tempFile=new File(getCacheDir(),fileName_image);
        try{
            tempFile.createNewFile();
            FileOutputStream out=new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,out);
            out.close();


        }catch (Exception e){

        }
    }



    View.OnClickListener btnSelectListener2=new View.OnClickListener(){
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onClick(View view) {
            Integer a=(int)lat;
            Integer b=(int)lng;
            String c=a.toString();
            String d=b.toString();
            weather w=new weather();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        loading=w.loading(c,d);
                        Message message=handler.obtainMessage();
                        Bundle bundle=new Bundle();
                        bundle.putString("value",loading);
                        message.setData(bundle);

                        handler.sendMessage(message);

                        Thread.sleep(3000);
                    }catch (IOException e){
                        e.printStackTrace();

                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }).start();



        }
    };






    View.OnClickListener btnSelectListener=new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            isLogin_check lct=(isLogin_check) getApplication();
            Iterator<String> iterator=lct.getFinal_name().iterator();

            PopupMenu popupMenu=new PopupMenu(getApplicationContext(),view);
            Menu menu=popupMenu.getMenu();
            //Toast.makeText(getApplicationContext(),"asd",Toast.LENGTH_LONG).show();
            if(lct.getFinal_name().size()!=0) {
            for (int i = 0; i < lct.getFinal_name().size(); i++) {
                menu.add(0, i, 0, iterator.next());
            }
            }
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    //int i=menuItem.getItemId();
                    location.setText(menuItem.getTitle());
                    return true;
                }
            });



            popupMenu.show();


        }
    };















    //파일 저장 버튼 클릭처리




    //백버튼 두번 연속 입력으로 종료 처리
    @Override
    public void onBackPressed()
    {
        if(System.currentTimeMillis() - backPressTime >= 2000)
        {
            backPressTime = System.currentTimeMillis();
            Toast.makeText(getApplicationContext(), "백(Back) 버튼을 한 번 더 누르면 종료", Toast.LENGTH_LONG).show();
        }
        else if(System.currentTimeMillis() - backPressTime < 2000)
            finish();
    }



















}