package com.zmt.remote_service;

import android.app.Activity;
import android.app.Service;
import android.os.Bundle;
import android.widget.TextView;

import com.zmt.aidl.IRemoteService;

import java.util.Arrays;

public class MainActivity extends Activity {

    public static String [] name = {"吕布", "关羽", "赵子龙", "张飞"};
    public static int [] age = {18, 19, 20, 21};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = (TextView)findViewById(R.id.text);
        textView.setText("基础类型: " + Arrays.toString(name) + "\n" + "对象类型: " + Arrays.toString(name) + Arrays.toString(age));
    }
}
