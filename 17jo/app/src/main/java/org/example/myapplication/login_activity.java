package org.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

//로그인 하기
public class login_activity extends AppCompatActivity {
    TextView registerButton;
    Button loginButton;
    EditText id, pw;

    char result_end;
    String temp;
    private static String TAG="phptest";

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        id=findViewById(R.id.idText);
        pw=findViewById(R.id.passwordText);

        //회원가입 화면으로 이동
        registerButton=(TextView)findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new listner());

        //로그인 하기
        loginButton=findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ID=id.getText().toString();
                String PW=pw.getText().toString();

                isLogin_check login=(isLogin_check) getApplication();
                login.setId(ID);

                InsertData task=new InsertData();
                task.execute("http://lsvk9921.cafe24.com/login.php",ID,PW);
                Log.d("qq", ID+PW);
            }
        });
    }

    //회원가입 화면
    class listner implements View.OnClickListener{
        public void onClick(View view){
            finish();//액티비티 종료
            Intent RegisterIntent=new Intent(login_activity.this, Register.class);
            login_activity.this.startActivity(RegisterIntent);//loginactivity 시작
        }
    }

    //로그인 하기
    class InsertData extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;//앱 작업 수행 시간 관리

        protected void onPreExecute(){//시작
            super.onPreExecute();
            progressDialog=ProgressDialog.show(login_activity.this, "Please Wait", null, true, true);
        }

        protected void onPostExecute(String result){//결과 확인
            super.onPostExecute(result);
            Log.d("qq", "\n\n"+result);
            progressDialog.dismiss();//작업 완료
            result_end=result.toString().charAt(0);

            Log.d("qq", "\n\n"+result_end);
            if(result_end=='1'){
                Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_LONG).show();

                Intent intent=new Intent();
                intent.putExtra("islogin", result_end);

                isLogin_check login=(isLogin_check) getApplication();//액티비티 시작
                login.setGlobalValue(result_end);
                login.setName(temp);

                setResult(RESULT_OK, intent);
                finish();//액티비티 종료
            }
            else{
                Toast.makeText(getApplicationContext(),"아이디 또는 비밀번호가 다릅니다.", Toast.LENGTH_SHORT).show();
            }
            Log.d(TAG, "POST response"+result);
        }

        protected String doInBackground(String... params){//작업 수행
            String ID=(String) params[1];
            String PW=(String) params[2];
            isLogin_check login=new isLogin_check();
            temp=ID;
            String serverURL=(String)params[0];
            serverURL=serverURL+"?name="+ID+"&password="+PW;

            try {
                //서버 연결, 게시판 불러오기
                URL url=new URL(serverURL);
                HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                int responseStatusCode=httpURLConnection.getResponseCode();
                Log.d(TAG, "GET response code - "+responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode==httpURLConnection.HTTP_OK){
                    inputStream=httpURLConnection.getInputStream();
                }
                else{
                    inputStream=httpURLConnection.getErrorStream();
                }
                InputStreamReader inputStreamReader=new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader=new BufferedReader(inputStreamReader);

                StringBuilder sb=new StringBuilder();
                String line;

                while((line=bufferedReader.readLine())!=null){
                    sb.append(line);
                }
                bufferedReader.close();
                return sb.toString();
            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error", e);
                return new String("Error:"+e.getMessage());
            }
        }
    }
}
