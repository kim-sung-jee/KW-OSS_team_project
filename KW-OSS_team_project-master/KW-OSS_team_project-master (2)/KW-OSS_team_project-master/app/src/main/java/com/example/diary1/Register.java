package com.example.diary1;

import static java.lang.Thread.sleep;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class Register extends AppCompatActivity {
    private static String IP_ADDRESS = "localhost";
    private static String TAG = "phptest";

    char result2;
    EditText idText;
    EditText passwordText;
    EditText emailText;
    TextView mTextViewResult;
    String name;
    String resultasd;
    Button validate;
    AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        idText = (EditText) findViewById(R.id.idText);
        passwordText = (EditText) findViewById(R.id.passwordText);
        emailText = (EditText) findViewById(R.id.emailText);
        validate=(Button)findViewById(R.id.validateButton1);
        mTextViewResult = (TextView) findViewById(R.id.textView2);
        mTextViewResult.setMovementMethod(new ScrollingMovementMethod());
        Button buttonInsert = (Button) findViewById(R.id.registerButton1);

        validate.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=idText.getText().toString();
                if(name.equals("")){
                    AlertDialog.Builder builder=new AlertDialog.Builder( Register.this );
                    dialog=builder.setMessage("아이디는 빈 칸일 수 없습니다")
                            .setPositiveButton("확인",null)
                            .create();
                    dialog.show();
                    return;
                }
                validate task=new validate();
                try {
                    String a = task.execute("http://lsvk9921.cafe24.com/validate.php", name).get();
                    if(a.contains("불가")){
                        idText.setText("");
                    }
                }catch (Exception e){

                }








            }
        }));










        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = idText.getText().toString();
                String password = passwordText.getText().toString();
                String phone = emailText.getText().toString();


                if(name.equals("")){
                    AlertDialog.Builder builder=new AlertDialog.Builder( Register.this );
                    dialog=builder.setMessage("아이디는 빈 칸일 수 없습니다")
                            .setPositiveButton("확인",null)
                            .create();
                    dialog.show();
                    return;
                }
                if(password.equals("")){
                    AlertDialog.Builder builder=new AlertDialog.Builder( Register.this );
                    dialog=builder.setMessage("비밀번호는 빈 칸일 수 없습니다")
                            .setPositiveButton("확인",null)
                            .create();
                    dialog.show();
                    return;
                }
                if(phone.equals("")){
                    AlertDialog.Builder builder=new AlertDialog.Builder( Register.this );
                    dialog=builder.setMessage("이메일은 빈 칸일 수 없습니다")
                            .setPositiveButton("확인",null)
                            .create();
                    dialog.show();
                    return;
                }

                InsertData task = new InsertData();
                task.execute("http://lsvk9921.cafe24.com/insert.php", name, password, phone);
                name = idText.getText().toString();


                idText.setText("");
                passwordText.setText("");
                emailText.setText("");

                Toast.makeText(getApplicationContext(), "id : " + name + " 님의 회원가입이 완료 되었습니다.", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(Register.this, login_activity.class);
                finish();
                startActivityForResult(intent,0);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        finish();
    }

    class validate extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("qq","\n\n"+result);
//            progressDialog.dismiss();
            result2 = result.toString().charAt(0);
            mTextViewResult.setText(result);
            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            Log.d("qq","\n\n"+result2);
//            if(result2=='1')
//            {
//                Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_LONG).show();
//                //Intent intent = new Intent(login_activity.this, MainActivity.class);
//                Intent intent=new Intent();
//                intent.putExtra("islogin",result2);
//                isLogin_check login=(isLogin_check) getApplication();
//                login.setGlobalValue(result2);
//                setResult(RESULT_OK,intent);
//                finish();
//
//            }
//            else
//            {
//                Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호가 다릅니다. ", Toast.LENGTH_LONG).show();
//            }
//            Log.d(TAG, "POST response  - " + result);
        }
        @Override
        protected String doInBackground(String... params) {
            String ID = (String)params[1];


            String serverURL = (String)params[0];
            serverURL = serverURL+"?" + "name=" + ID  ;
            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "GET response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }
                bufferedReader.close();
                return sb.toString();

            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error ", e);
                return new String("Error: " + e.getMessage());
            }
        }
    }

    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=ProgressDialog.show(Register.this,"Please wait",null,true,true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            mTextViewResult.setText(result);
            Log.d(TAG, "POST response  - " + result);

        }

        @Override
        protected String doInBackground(String... params) {

            String name = (String)params[1];
            String password = (String)params[2];
            String phone = (String)params[3];

            String serverURL = (String)params[0];
            String postParameters = ("name=" + name + "&password=" + password +"&phone="+phone);


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();


                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }

                //읽기 부분
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }
}