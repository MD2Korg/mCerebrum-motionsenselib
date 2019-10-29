package org.md2k.motionsenselib.log;

import android.util.Log;

public class MyLog {
    private static MyLog instance;
    private ReceiveLogCallback callback;
    public static void init(){
        instance = new MyLog();
    }
    public static void setCallback(ReceiveLogCallback callback){
        instance.callback = callback;
    }
    private MyLog(){

    }
    private void log(String logLevel, String className, String methodName, String message){
        switch(logLevel){
            case "ERROR":Log.e("MyLog", "["+className+","+methodName+"] "+message);break;
            case "INFO":Log.i("MyLog", "["+className+","+methodName+"] "+message);break;
            case "WARN":Log.w("MyLog", "["+className+","+methodName+"] "+message);break;
            default:
            Log.d("MyLog", "["+className+","+methodName+"] "+message);break;
        }
        if(callback!=null){
            callback.onReceive(logLevel, className, methodName, message);
        }
    }

    public static void debug(String className, String methodName, String message){
        instance.log("DEBUG", className, methodName, message);
    }
    public static void error(String className, String methodName, String message){
        instance.log("ERROR", className, methodName, message);
    }
    public static void info(String className, String methodName, String message){
        instance.log("INFO", className, methodName, message);
    }
    public static void warning(String className, String methodName, String message){
        instance.log("WARN", className, methodName, message);
    }

}
