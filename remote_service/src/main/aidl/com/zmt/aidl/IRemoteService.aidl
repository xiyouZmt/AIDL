// IRemoteService.aidl
package com.zmt.aidl;

// Declare any non-default types here with import statements
import com.zmt.aidl.Person;
import com.zmt.aidl.MessageReceiver;

interface IRemoteService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     * 接收服务端的数据
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

    int getPid();

    String getName(int id);

    Person getPerson(int id);

    /**
     * 发送到服务端
     */
    void sendMessage(in Person person);

    void sendText(String text);

    /**
     * 注册与取消注册
     */
    void registerReceiveListener(MessageReceiver messageReceiver);

    void unRegisterReceiveListener(MessageReceiver messageReceiver);
}
