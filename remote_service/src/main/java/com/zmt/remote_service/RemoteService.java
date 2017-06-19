package com.zmt.remote_service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import com.zmt.aidl.IRemoteService;
import com.zmt.aidl.MessageReceiver;
import com.zmt.aidl.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zmt on 2017/3/31.
 */

public class RemoteService extends Service {

    /**
     * RemoteCallbackList专门用来管理多进程回调接口
     */
    private RemoteCallbackList<MessageReceiver> remoteCallbackList = new RemoteCallbackList<>();
    private List<Person> personList = new ArrayList<>();

    @Override
    public IBinder onBind(Intent intent) {
        for (int i = 0; i < MainActivity.name.length; i++) {
            personList.add(new Person(MainActivity.name[i], MainActivity.age[i]));
        }
        return mBinder;
    }

    private final IRemoteService.Stub mBinder = new IRemoteService.Stub(){

        /**
         * 接收的基本类型数据
         * @param anInt
         * @param aLong
         * @param aBoolean
         * @param aFloat
         * @param aDouble
         * @param aString
         * @throws RemoteException
         */
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {
            Log.e("basicType", " int: " + anInt + " long: " + aLong + " boolean: " + aBoolean
                    + " float: " + aFloat + " double: " + aDouble + " string: " + aString);
        }

        /**
         * 接收的基本类型数据
         * @param text
         * @throws RemoteException
         */
        @Override
        public void sendText(String text) throws RemoteException {
            Log.e("from client text", text);
        }

        /**
         * 接收的引用类型数据
         * @param person
         * @throws RemoteException
         */
        @Override
        public void sendMessage(Person person) throws RemoteException {
            Log.e("from client message", person.toString());
        }

        /**
         * 返回的基本类型数据
         * @return int
         * @throws RemoteException
         */
        @Override
        public int getPid() throws RemoteException {
            Log.e("ThreadName: ", Thread.currentThread().getName());
            Log.e("RemoteServicePid: ", android.os.Process.myPid() + "");
            return android.os.Process.myPid();
        }

        /**
         * 返回的基本类型数据
         * @param id
         * @return String
         * @throws RemoteException
         */
        @Override
        public String getName(int id) throws RemoteException {
            return MainActivity.name[id];
        }

        /**
         * 返回的引用类型数据
         * @param id
         * @return Person
         * @throws RemoteException
         */
        @Override
        public Person getPerson(int id) throws RemoteException {
            return personList.get(id);
        }

        @Override
        public void registerReceiveListener(MessageReceiver messageReceiver) throws RemoteException {
            remoteCallbackList.register(messageReceiver);
        }

        @Override
        public void unRegisterReceiveListener(MessageReceiver messageReceiver) throws RemoteException {
            remoteCallbackList.unregister(messageReceiver);
        }
    };
}
