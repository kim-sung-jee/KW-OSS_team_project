package org.example.myapplication;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GoogleMap extends AppCompatActivity {
    Intent serviceIntent;
    String permission_list[]={
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION

    };
    Button startBtn;
    Button finishBtn;
    Button button;
    LocationManager locationManager;
    TextView text1,text2,text3,text4,text5;
    com.google.android.gms.maps.GoogleMap map;

    isLogin_check isLoginCheck=new isLogin_check();

    ArrayList<Double> lat_list=new ArrayList<Double>();
    ArrayList<Double>lng_list=new ArrayList<Double>();
    ArrayList<String>name_list=new ArrayList<String>();
    ArrayList<String>vicinity_list=new ArrayList<String>();

    ArrayList<Marker>marker_list=new ArrayList<Marker>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);

        text1=(TextView) findViewById(R.id.textView);
        text2=(TextView) findViewById(R.id.textView2);
        text3=(TextView) findViewById(R.id.textView3);
        text4=(TextView) findViewById(R.id.textView6);
        text5=(TextView) findViewById(R.id.textView10);
        button=findViewById(R.id.button7);



        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, permission_list, 0);
            for (int i = 0; i < permission_list.length; i++) {
                int permissioncheck = ContextCompat.checkSelfPermission(this, permission_list[i]);
                if (permissioncheck == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(getApplicationContext(), "????????? ????????? ??? ????????????. ?????? ????????? ????????????!", Toast.LENGTH_LONG).show();
                    break;
                }
            }
        }



        startBtn=findViewById(R.id.button5);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startService();
            }
        });
        finishBtn=findViewById(R.id.button6);
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stopService();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(GoogleMap.this,MainActivity2.class);
                startActivity(intent);
            }
        });

    }


    public void startService(){
        serviceIntent=new Intent(this,MyService.class);
        if(Build.VERSION.SDK_INT>=26) {
            ContextCompat.startForegroundService(this,serviceIntent);
//            ContextCompat.startF
        }else{
            startService(serviceIntent);
        }
    }
    public void stopService(){
        serviceIntent=new Intent(this,MyService.class);
        stopService(serviceIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for(int result:grantResults){
            if(result== PackageManager.PERMISSION_DENIED){
                return;

            }
        }
        init();
    }
    // ??? ??????????????? ????????? ?????????
    public void init(){
        FragmentManager fragmentManager=getSupportFragmentManager();
        SupportMapFragment mapFragment=(SupportMapFragment) fragmentManager.findFragmentById(R.id.map);

        MapReadyCallback callback1=new MapReadyCallback();
        mapFragment.getMapAsync(callback1);
    }

    //?????? ?????? ?????? ????????? ???????????? ???????????? ??????
    class MapReadyCallback implements OnMapReadyCallback {
        @Override
        public void onMapReady(@NonNull com.google.android.gms.maps.GoogleMap googleMap) {
            map=googleMap;
            getMyLocation();
        }
    }
    //?????? ????????? ???????????? ?????????
    public void getMyLocation(){
        locationManager=(LocationManager) getSystemService(LOCATION_SERVICE);

        //?????? ?????? ??????
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_DENIED){
                return;
            }
        }
        //????????? ???????????? ?????? ????????????.
        Location location1=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location location2=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if(location1!=null){
            setMyLocation(location1,1);
        }else{
            if(location2!=null){
                setMyLocation(location2,2);
            }
        }

        //????????? ????????????.
        GetMyLocationListener listener=new GetMyLocationListener();
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)==true){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,10f,listener);
        }
        if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)==true){
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000,10f,listener);
        }


    }

    public void setMyLocation(Location location,int i){
        Log.d("test123","??????:"+location.getLatitude());
        Log.d("test123","??????:"+location.getLongitude());
        if(i==1){
            text1.setText(location.getLatitude()+"?????????"+location.getLongitude());
        }else{
            text2.setText(location.getLatitude()+"?????????"+location.getLongitude());
        }
        //????????? ???????????? ???????????? ??????
        LatLng position=new LatLng(location.getLatitude(),location.getLongitude());
        CameraUpdate update1= CameraUpdateFactory.newLatLng(position);
        CameraUpdate update2=CameraUpdateFactory.zoomTo(18f);
        map.moveCamera(update1);
        map.animateCamera(update2);


        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_DENIED){
                return;
            }
        }
        //?????? ?????? ??????
        map.setMyLocationEnabled(true);
        NetworkdThread thread=new NetworkdThread(location.getLatitude(),location.getLongitude());
        thread.start();
    }

    //?????? ?????? ????????? ???????????? ???????????? ?????????
    class GetMyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            setMyLocation(location,2);
            locationManager.removeUpdates(this);
        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {

        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    }

    //?????? ???????????? ?????? ????????? ???????????? ?????? ?????????
    class NetworkdThread extends Thread{

        double lat,lng;
        NetworkdThread(double lat,double lng){
            this.lat=lat;
            this.lng=lng;
        }
        @Override
        public void run() {
            super.run();

            OkHttpClient client=new OkHttpClient();
            Request.Builder builder=new Request.Builder();
            String site="https://maps.googleapis.com/maps/api/place/nearbysearch/json"
                    +"?location="+lat+"%2C"+lng
                    +"&radius=100"
                    +"&type=restaurant"
                    +"&key=";
            Log.d("test",site);
            builder=builder.url(site);
            Request request=builder.build();
            Callback1 callback1=new Callback1();
            Call call= client.newCall(request);
            call.enqueue(callback1);
        }
    }

    class Callback1 implements Callback {
        @Override
        public void onFailure(Call call, IOException e) {

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            try{
                String result=response.body().string();
                Log.d("test123",result);
                JSONObject obj=new JSONObject(result);

                String status=obj.getString("status");
                if(status.equals("OK")){
                    JSONArray results=obj.getJSONArray("results");
                    text3.setText("?????????");
                    text4.setText("?????????");
                    text5.setText("?????????");
                    lat_list.clear();
                    lng_list.clear();
                    name_list.clear();
                    vicinity_list.clear();
                    for(int i=0;i<results.length();i++){
                        JSONObject obj2=results.getJSONObject(i);
                        JSONObject geometry=obj2.getJSONObject("geometry");
                        JSONObject location=geometry.getJSONObject("location");
                        double lat2=location.getDouble("lat");
                        double lng2=location.getDouble("lng");

                        String name=obj2.getString("name");
                        String vicinity=obj2.getString("vicinity");
//                        if(i%3==0){
//                            text3.append(name);
//                        }else if(i%3==1){
//                            text4.append(name);
//                        }else{
//                            text5.append(name);
//                        }
                        text3.append(isLoginCheck.getStore_name().get(i));
                        lat_list.add(lat2);
                        lng_list.add(lng2);
                        name_list.add(name);
                        vicinity_list.add(vicinity);
                    }


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //????????? ???????????? ?????? ????????? ????????????.
                            for(Marker marker:marker_list){
                                marker.remove();
                            }
                            marker_list.clear();
                            for(int i=0;i<lat_list.size();i++){
                                double lat3=lat_list.get(i);
                                double lng3=lng_list.get(i);
                                String name3=name_list.get(i);
                                String vicinity3=vicinity_list.get(i);

                                LatLng position=new LatLng(lat3,lng3);

                                MarkerOptions option= new MarkerOptions();
                                option.position(position);

                                option.title(name3);
                                option.snippet(vicinity3);

                                BitmapDescriptor bitmap= BitmapDescriptorFactory.fromResource(R.drawable.common_full_open_on_phone);
                                option.icon(bitmap);


                                Marker marker=map.addMarker(option);
                                marker_list.add(marker);
                            }
                        }
                    });
//                    for(int i=0;i< lat_list.size();i++){
//                        double a1= lat_list.get(i);
//                        double a2=lng_list.get(i);
//                        String a3=name_list.get(i);
//                        String a4=vicinity_list.get(i);
//                        //text3.append(a1+a2+a3+a4);
//                        Log.d("test123",a1+","+a2+","+a3+","+a4);
//                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
