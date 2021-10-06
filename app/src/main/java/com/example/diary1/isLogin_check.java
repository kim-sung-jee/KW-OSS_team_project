package com.example.diary1;

import android.app.Application;

public class isLogin_check extends Application {
    char islogin='0';
    String id="비로그인";

    public char getGlobalValue(){
        return islogin;
    }
    public void setGlobalValue(char mValue){
        this.islogin=mValue;
    }
    public String getId(){return id;}
    public void setId(String name){this.id=name;}
}
