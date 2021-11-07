package com.example.diary1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;

public class gallery extends AppCompatActivity {

    EditText etName,etMsg;
    ImageView iv;

    //업로드할 이미지의 절대경로(실제 경로)
    String imgPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        etName=findViewById(R.id.et_name);
        etMsg=findViewById(R.id.et_msg);
        iv=findViewById(R.id.iv);

        //업로드 하려면 외부저장소 권한 필요
        //동적 퍼미션 코드 필요..


        //동적퍼미션 작업
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            int permissionResult= checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(permissionResult== PackageManager.PERMISSION_DENIED){
                String[] permissions= new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions,10);
            }
        }else{
            //cv.setVisibility(View.VISIBLE);
        }


    }//onCreate() ..

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 10 :
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED) //사용자가 허가 했다면
                {
                    Toast.makeText(this, "외부 메모리 읽기/쓰기 사용 가능", Toast.LENGTH_SHORT).show();

                }else{//거부했다면
                    Toast.makeText(this, "외부 메모리 읽기/쓰기 제한", Toast.LENGTH_SHORT).show();

                }
                break;
        }
    }

    public void clickBtn(View view) {

        //갤러리 or 사진 앱 실행하여 사진을 선택하도록 하기 위한 코드
        Intent intent= new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,10);
    }
    //이미지 빈칸일때 null값으로 안들어옴
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // startActivityForResult(intent,10); 에서 반환값(data)은 이미지이다.
        switch (requestCode){
            case 10:
                if(resultCode==RESULT_OK){
                    //선택한 사진의 경로(Uri)객체 얻어오기
                    Uri uri= data.getData();
                    if(uri!=null){
                        iv.setImageURI(uri);

                        //갤러리앱에서 관리하는 DB정보가 있는데, 그것이 나온다 [실제 파일 경로가 아님!!]
                        //얻어온 Uri는 Gallery앱의 DB번호임. (content://-----/2854)
                        //업로드를 하려면 이미지의 절대경로(실제 경로: file:// -------/aaa.png 이런식)가 필요함
                        //Uri -->절대경로(String)로 변환
                        imgPath= getRealPathFromUri(uri);   //임의로 만든 메소드 (절대경로를 가져오는 메소드)

                        //이미지 경로 uri 확인해보기
                        new AlertDialog.Builder(this).setMessage(uri.toString()+"\n"+imgPath).create().show();
                    }

                }else
                {
                    Toast.makeText(this, "이미지 선택을 하지 않았습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }//onActivityResult() ..

    //Uri -- > 절대경로로 바꿔서 리턴시켜주는 메소드
    String getRealPathFromUri(Uri uri){
        String[] proj= {MediaStore.Images.Media.DATA};
        CursorLoader loader= new CursorLoader(this, uri, proj, null, null, null);
        Cursor cursor= loader.loadInBackground();
        int column_index= cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result= cursor.getString(column_index);
        cursor.close();
        return  result;
    }

    public void clickUpload(View view) {

        if(etName.getText().toString().replace(" ","").equals("")||
        etMsg.getText().toString().replace(" ","").equals("")){
            new AlertDialog.Builder(gallery.this).setMessage("빈칸은 허용하지 않습니다")
                    .create().show();
            return;
        }
        if(imgPath==null){
            new AlertDialog.Builder(gallery.this).setMessage("이미지를 선택해 주세요")
                    .create().show();
            return;
        }

        //서버로 보낼 데이터
        String name= etName.getText().toString();
        String msg= etMsg.getText().toString();
        //닉네임 체크
        isLogin_check login=(isLogin_check) getApplication();
        String nickname=login.getName();
        //안드로이드에서 보낼 데이터를 받을 php 서버 주소
        String serverUrl="http://lsvk9921.cafe24.com/insertDB.php";

        //Volley plus Library를 이용해서
        //파일 전송


        //파일 전송 요청 객체 생성[결과를 String으로 받음]
        SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //new AlertDialog.Builder(gallery.this).setMessage("업로드 성공!").create().show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(gallery.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        });

        //요청 객체에 보낼 데이터를 추가
        smpr.addStringParam("name", name);
        smpr.addStringParam("msg", msg);
        smpr.addStringParam("nickname",nickname);
        //이미지 파일 추가
        smpr.addFile("img", imgPath);
        //Toast.makeText(gallery.this,id,Toast.LENGTH_LONG).show();
        //요청객체를 서버로 보낼 우체통 같은 객체 생성
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(smpr);
        finish();
    }

    public void clickLoad(View view) {

        Intent intent=new Intent(this,TalkActivity.class);
        startActivity(intent);
    }
}

