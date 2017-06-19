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
                /**
                 * 当前进程和服务进程的pid
                 */
                int pid = mRemoteService.getPid();
                int currentPid = android.os.Process.myPid();
                Log.e("threadPid", "currentPid: " + currentPid + " remotePid: " + pid);
                /**
                 * 发送基本类型和引用类型数据到服务端
                 */
                mRemoteService.registerReceiveListener(messageReceiver);
                mRemoteService.sendText("test");
                mRemoteService.basicTypes(1,2,true, 1.2f, 2.4, "aidl");
                Person person = new Person("test", 1);
                mRemoteService.sendMessage(person);
            } catch (RemoteException e) {
                Log.e("error", e.toString());
            }
            Log.e("bind success! ", mRemoteService.toString());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("disconnected! ", mRemoteService.toString());
        }
    };

    private MessageReceiver messageReceiver = new MessageReceiver.Stub() {

        @Override
        public void onMessageReceived(Person person) throws RemoteException {
            Log.e("onMessageReceived", person.toString());
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            mRemoteService.unRegisterReceiveListener(messageReceiver);
        } catch (RemoteException e) {
            Log.e("RemoteException", e.toString());
        }
        unbindService(serviceConnection);
    }

    public void search(View view) {
        if(isConnected){
            String text = editText.getText().toString();
            if(!text.equals("")){
                try {
                    int pid = Integer.valueOf(text);
                    /**
                     * 接收服务端的基本类型引用类型数据
                     */
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
