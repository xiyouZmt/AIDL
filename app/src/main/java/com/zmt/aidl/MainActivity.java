package com.zmt.aidl;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends Activity {

    private IRemoteService mRemoteService;
    private EditText editText;
    private TextView textView;
    private boolean isConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.result);
        /**
         * 绑定远程服务
         */
        Intent intent = new Intent();
        intent.setAction("remote_service");
        intent.setPackage("com.zmt.remote_service");
        isConnected = bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mRemoteService = IRemoteService.Stub.asInterface(service);
            try {
                int pid = mRemoteService.getPid();
                int currentPid = android.os.Process.myPid();
                System.out.println("currentPid: " + currentPid + " remotePid: " + pid);
                mRemoteService.basicTypes(1,2,true, 1.2f, 2.4, "aidl");
            } catch (RemoteException e) {
                Log.e("error", e.toString());
            }
            System.out.println("bind success! " + mRemoteService.toString());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            System.out.println("disconnected " + mRemoteService.toString());
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }

    public void search(View view) {
        if(isConnected){
            String text = editText.getText().toString();
            if(!text.equals("")){
                try {
                    int pid = Integer.valueOf(text);
                    String name = mRemoteService.getName(pid);
                    Person person = mRemoteService.getPerson(pid);
                    if(person != null){
                        textView.setText("基础类型: " + name + "\n" + "对象类型: " + person.getName() + " " + person.getAge());
                    }
                } catch (Exception e) {
                    Log.e("error", e.toString());
                }
            }
        } else {
            Log.e("failed", "disconnected");
        }
    }
}
