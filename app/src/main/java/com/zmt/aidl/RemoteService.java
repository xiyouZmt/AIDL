package com.zmt.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zmt on 2017/3/31.
 */

public class RemoteService extends Service {

    private String [] name = {"吕布", "关羽", "赵子龙", "张飞"};
    private List<Person> personList = new ArrayList<>();

    @Override
    public IBinder onBind(Intent intent) {
        personList.add(new Person("吕布", 18));
        personList.add(new Person("关羽", 19));
        personList.add(new Person("赵子龙", 20));
        personList.add(new Person("张飞", 21));
//        return null;
        return mBinder;
    }

    private final IRemoteService.Stub mBinder = new IRemoteService.Stub(){

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {
            System.out.println("Thread:" + Thread.currentThread().getName());
            System.out.println("basicType int: " + anInt + " long: " + aLong + " boolean: " + aBoolean
                    + " float: " + aFloat + " double: " + aDouble + " string: " + aString);
        }

        @Override
        public int getPid() throws RemoteException {
            System.out.println("Thread:" + Thread.currentThread().getName());
            System.out.println("RemoteService getPid ");
            return android.os.Process.myPid();
        }

        @Override
        public void registerReceiveListener(MessageReceiver messageReceiver) throws RemoteException {

        }

        @Override
        public void unRegisterReceiveListener(MessageReceiver messageReceiver) throws RemoteException {

        }

        @Override
        public String getName(int id) throws RemoteException {
            return name[id];
        }

        @Override
        public Person getPerson(int id) throws RemoteException {
            return personList.get(id);
        }

        @Override
        public void sendMessage(Person person) throws RemoteException {

        }

        @Override
        public void sendText(String text) throws RemoteException {

        }
    };
}
