package org.example.myapplication;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class imageDownload extends AppCompatActivity {
    ImageView imageView;
    Button btnSave;
    URL imgUrl=null;

    String imgPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_download);
        imageView=findViewById(R.id.imageDown);
        Intent intent=getIntent();
        String path=intent.getStringExtra("path");
        imgPath=path;
        Toast.makeText(imageDownload.this,path,Toast.LENGTH_LONG).show();
        Glide.with(this).load(path).into(imageView);
        Thread1 thread1=new Thread1();
        thread1.start();
    }



    private void saveImageToGallery(Bitmap bitmap){

        OutputStream fos;

        try{
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
                ContentResolver resolver=getContentResolver();
                ContentValues contentValues=new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME,"Image_"+".jpg");
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE,"image/jpg");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES+ File.separator+"TestFolder");
                Uri imageUri=resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);

                fos=(FileOutputStream) resolver.openOutputStream(Objects.requireNonNull(imageUri));

                bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
                Objects.requireNonNull(fos);


                //Toast.makeText(this,"Image Saved",Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public class Thread1 extends Thread{
        Bitmap bitmap;
        @Override
        public void run() {
            super.run();
            try {
                bitmap = getBitmap(imgPath);
                saveImageToGallery(bitmap);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }


    private Bitmap getBitmap(String url) {
        URL imgUrl = null;
        HttpURLConnection connection = null;
        InputStream is = null;

        Bitmap retBitmap = null;

        try{
            imgUrl = new URL(url);
            connection = (HttpURLConnection) imgUrl.openConnection();
            connection.setDoInput(true); //url로 input받는 flag 허용
            connection.connect(); //연결
            is = connection.getInputStream(); // get inputstream
            retBitmap = BitmapFactory.decodeStream(is);
        }catch(Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            if(connection!=null) {
                connection.disconnect();
            }
            return retBitmap;
        }
    }


}