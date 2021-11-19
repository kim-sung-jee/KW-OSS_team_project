package org.example.myapplication;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MainActivity2 extends AppCompatActivity {
    isLogin_check loginCheck;
    TextView textView;
    public ArrayList<String> name=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        textView=findViewById(R.id.texttext);
        textView.setText("1");

        isLogin_check acm=(isLogin_check) getApplication();

        Set<String> set=new HashSet<String>(acm.ac_store_name);
        for(String str:set){
            if(Collections.frequency(acm.ac_store_name,str)>=3){
                acm.setFinal_name(str);
                //System.out.println("saafa");
            }
        }
        Iterator<String> iterator=acm.getFinal_name().iterator();
        if(acm.getFinal_name().size()!=0) {
            textView.setText(iterator.next());
            while (iterator.hasNext()) {
                textView.append(iterator.next());
            }
        }
    }
}