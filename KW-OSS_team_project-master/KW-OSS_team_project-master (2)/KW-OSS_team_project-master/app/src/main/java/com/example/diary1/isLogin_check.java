package com.example.diary1;

import android.app.Application;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class isLogin_check extends Application {
    char islogin='0';
    String id="비로그인";
    String name="익명";
    ArrayList<String> store_name=new ArrayList<String>();
    ArrayList<String> ac_store_name=new ArrayList<>();
    HashSet<String> final_name=new HashSet<>();





    public char getGlobalValue(){
        return islogin;
    }
    public void setGlobalValue(char mValue){
        this.islogin=mValue;
    }
    public String getId(){return id;}
    public void setId(String name){this.id=name;}
    public String getName(){return name;}
    public void setName(String name){this.name=name;}
    public void setStore_name(ArrayList<String> store_name){this.store_name=store_name;}
    public ArrayList<String> getStore_name(){
        return this.store_name;
    }
    public ArrayList<String> getAc_store_name(){return this.ac_store_name;}
    public void setAc_store_name(String y){
        this.ac_store_name.add(y);
    }
    public void setFinal_name(String name){this.final_name.add(name);}
    public HashSet<String> getFinal_name(){return this.final_name;}


}
