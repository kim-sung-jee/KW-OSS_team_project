package com.example.diary1;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyService extends Service {
    int i=0;
    int lenght;
    int value=0;
    Intent serviceIntent;




    ArrayList<Double> lat_list=new ArrayList<Double>();
    ArrayList<Double>lng_list=new ArrayList<Double>();
    ArrayList<String>name_list=new ArrayList<String>();
    ArrayList<String>vicinity_list=new ArrayList<String>();

    com.google.android.gms.maps.GoogleMap map;

    BackgroundTask task;



    LocationManager locationManager;
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        task=new BackgroundTask();
        task.execute();

        initializeNotification();
        return START_STICKY;
    }


    public void initializeNotification(){

        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,"1");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        NotificationCompat.BigTextStyle style=new NotificationCompat.BigTextStyle();
        style.bigText("click to see setting");
        style.setBigContentTitle(null);
        style.setSummaryText("service is on");
        builder.setContentText(null);
        builder.setContentTitle(null);
        builder.setOngoing(true);
        builder.setStyle(style);
        builder.setWhen(0);
        builder.setShowWhen(false);



        Intent notificationIntent=new Intent(this,GoogleMap.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,notificationIntent,0);
        builder.setContentIntent(pendingIntent);

        NotificationManager manager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            manager.createNotificationChannel(new NotificationChannel("1","foreground service",NotificationManager.IMPORTANCE_NONE));
        }
        Notification notification= builder.build();
        startForeground(1, notification);
    }

    class BackgroundTask1 extends Thread{
        boolean isRunning=true;
        BackgroundTask1(){this.isRunning=true;};

        @Override
        public void run() {
            super.run();
            while(isRunning==true){


                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                //권한 확인 작업
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                        stop();
                    }
                }
                //이전에 측정했던 값을 가져온다.
                Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Location location2 = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                Double lat,lng;
                lat=location2.getLatitude();
                lng=location2.getLongitude();
                OkHttpClient client=new OkHttpClient();
                Request.Builder builder=new Request.Builder();

                String site="http://lsvk9921.cafe24.com/getlocation.php";
                builder=builder.url(site);

                RequestBody formBody=new FormBody.Builder()
                        .add("lat",lat.toString())
                        .add("lng",lng.toString())
                        .build();

                Request request=new Request.Builder().url(site)
                        .post(formBody)
                        .build();




                try {
                    Callback1 callback1 = new Callback1();
                    Call call = client.newCall(request);
                    call.enqueue(callback1);
                    Thread.sleep(1000);
                }catch (InterruptedException E){}

//
//                for(int i=0;i<isLoginCheck.getStore_name().size();i++){
//                    System.out.println(isLoginCheck.getStore_name().get(i));
//                }

                isRunning=false;
            }
        }
    }
    class BackgroundTask extends AsyncTask<Integer,String,Integer> {

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            value=0;
        }

        @Override
        protected Integer doInBackground(Integer... integers) {


            while(isCancelled()==false) {
                try {


                    if(value%30==29){

                        BackgroundTask1 b1=new BackgroundTask1();
                        b1.start();
                        Thread.sleep(5000);





                        value++;
                    }else{

                        value++;
                        System.out.println(value);
                        Thread.sleep(1000);
                    }



                }catch(InterruptedException ex){}
            }

            return value;

        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

        }



        @Override
        protected void onCancelled() {
            value=0;
        }
    }

    @Override
    public void onDestroy() {

        Log.d("Myservice","onDestroy");

        task.cancel(true);
        //stopForeground(true);

//        task=new BackgroundTask();
//        task.execute();
//
//        initializeNotification();

//        try{
//            Thread.sleep(5000);
//            //restart();
//        }catch (InterruptedException e){}
//        restart();
        System.out.println("리스타트 호출");
    }





    class Callback1 implements Callback {
        @Override
        public void onFailure(Call call, IOException e) {

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            try{
                String result=response.body().string();

                JSONObject obj=new JSONObject(result);

                String status=obj.getString("status");
                if(status.equals("OK")){

                    JSONArray results=obj.getJSONArray("results");
                    lat_list.clear();
                    lng_list.clear();
                    vicinity_list.clear();


                    lenght=results.length();

                    for(int i=0;i<results.length();i++){
                        JSONObject obj2=results.getJSONObject(i);
                        JSONObject geometry=obj2.getJSONObject("geometry");
                        JSONObject location=geometry.getJSONObject("location");
                        double lat2=location.getDouble("lat");
                        double lng2=location.getDouble("lng");

                        String name=obj2.getString("name");
                        String vicinity=obj2.getString("vicinity");

                        lat_list.add(lat2);
                        lng_list.add(lng2);
                        name_list.add(name);
                        vicinity_list.add(vicinity);




                    }
                    ((isLogin_check) getApplication()).setStore_name(name_list);
                    isLogin_check acm=(isLogin_check)getApplication();
                    for(int i=0;i<acm.getStore_name().size();i++){
                        acm.setAc_store_name(acm.getStore_name().get(i));
                    }




                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


//    public void restart(){
//        final Calendar c=Calendar.getInstance();
//        c.setTimeInMillis(System.currentTimeMillis());
//        c.add(Calendar.SECOND,1);
//        Intent intent=new Intent(this,AlarmReceiver.class);
//        PendingIntent sender=PendingIntent.getBroadcast(this,0,intent,0);
//
//        AlarmManager manager=(AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        manager.set(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),sender);
//    }
//
//    static public class AlarmReceiver extends BroadcastReceiver{
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
//                Intent in=new Intent(context,MyService.class);
//                context.startForegroundService(in);
//            }else{
//                Intent in=new Intent(context,MyService.class);
//                context.startService(in);
//            }
//        }
//    }

}