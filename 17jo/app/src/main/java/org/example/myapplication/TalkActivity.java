package org.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class TalkActivity extends AppCompatActivity {

    ListView listView;
    org.example.myapplication.TalkAdapter talkAdapter;
    Button delete_button;

    char login_result;
    int i;
    ArrayList<org.example.myapplication.TalkItem> talkItems= new ArrayList<>();
    final String TAG = getClass().getSimpleName();
    EditText editText;



    public static Context mContext;
    public String a;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk);
        SwipeRefreshLayout swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.refresh);
        delete_button=(Button)findViewById(R.id.button4);



        isLogin_check login=(isLogin_check) getApplication();

        a=login.getId();
        mContext=this;

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDB();
                swipeRefreshLayout.setRefreshing(false);

            }
        });

        //???????????? ???????????? ????????????
        loadDB();

        listView= findViewById(R.id.listView);
        talkAdapter= new org.example.myapplication.TalkAdapter(getLayoutInflater(),talkItems,getApplicationContext());
        listView.setAdapter(talkAdapter);

        login_result=login.getGlobalValue();

        //????????? ?????? ??????
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            ) {
                Log.d(TAG, "?????? ?????? ??????");
            } else {
                Log.d(TAG, "?????? ?????? ??????");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }



        // swipe ?????? ????????? ????????? ??????
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                    int topRowVerticalPosition=(absListView==null||absListView.getChildCount()==0?
                            0:listView.getChildAt(0).getTop());
                    swipeRefreshLayout.setEnabled((topRowVerticalPosition>=0));
            }
        });

        //?????? ???????????? ??????
        BottomNavigationView bottom_menu = findViewById(R.id.talk_bottom);

        bottom_menu.setOnNavigationItemSelectedListener(new bottomMenuClick());
    }

    public void search(String charText){

    }


    class bottomMenuClick implements BottomNavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){

                case R.id.callItem:

                    if(login_result=='0'){
                        Toast.makeText(getApplicationContext(),"????????? ????????????!",Toast.LENGTH_LONG).show();
                    }else {
                        Intent intent = new Intent(TalkActivity.this, gallery.class);
                        startActivity(intent);
                    }
                    return true;
                default:
                    return true;
            }

        }
    }





    public void loadDB(){
        //volley library??? ?????? ??????
        new Thread(){
            @Override
            public void run() {

                String serverUri="http://lsvk9921.cafe24.com/loadDB.php";

                try {
                    URL url= new URL(serverUri);

                    HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);

                    connection.setUseCaches(false);

                    InputStream is=connection.getInputStream();
                    InputStreamReader isr= new InputStreamReader(is);
                    BufferedReader reader= new BufferedReader(isr);

                    final StringBuffer buffer= new StringBuffer();
                    String line= reader.readLine();
                    while (line!=null){
                        buffer.append(line+"\n");
                        line= reader.readLine();
                    }

                    //????????? ??????????????? row(?????????)?????? ???????????? ????????? ????????????
                    String[] rows=buffer.toString().split(";");

                    //????????? ?????????
                    talkItems.clear();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            talkAdapter.notifyDataSetChanged();
                        }
                    });

                    for(String row : rows){
                        //?????? ??????????????? ??? ?????? ??????
                        String[] datas=row.split("&");
                        if(datas.length!=6) continue;

                        int no= Integer.parseInt(datas[0]); i=no;
                        String name=datas[1];
                        String msg=datas[2];
                        String imgPath= "https://lsvk9921.cafe24.com/"+datas[3];   //???????????? ?????????????????? ?????? ?????? ????????? ????????????.
                        String date=datas[4];
                        String nickname=datas[5];
                        //Log.d("test",imgPath);
                        //????????? ????????? ArrayList??? ??????
                        talkItems.add(0,new org.example.myapplication.TalkItem(no,name,msg,imgPath,date,nickname));

                        //???????????? ??????
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                talkAdapter.notifyDataSetChanged();
                            }
                        });
                    }

                    //???????????? ????????? ?????? ????????? ??????
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            new AlertDialog.Builder(TalkActivity.this).setMessage(buffer.toString()).create().show();
//
//
//                        }
//                    });

                } catch (MalformedURLException e) { e.printStackTrace(); } catch (IOException e) {e.printStackTrace();}
            }
        }.start();
    }//loadDB() ..
}