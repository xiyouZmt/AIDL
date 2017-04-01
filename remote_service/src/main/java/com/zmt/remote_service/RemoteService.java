package com.zmt.remote_service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.zmt.aidl.IRemoteService;
import com.zmt.aidl.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zmt on 2017/3/31.
 */

public class RemoteService extends Service {

    private List<Person> personList = new ArrayList<>();

    @Override
    public IBinder onBind(Intent intent) {
        for (int i = 0; i < MainActivity.name.length; i++) {
            personList.add(new Person(MainActivity.name[i], MainActivity.age[i]));
        }
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
        public String getName(int id) throws RemoteException {
            return MainActivity.name[id];
        }

        @Override
        public Person getPerson(int id) throws RemoteException {
            return personList.get(id);
        }
    };
}
