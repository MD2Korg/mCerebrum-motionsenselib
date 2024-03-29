package org.md2k.motionsenselib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import org.md2k.motionsenselib.device.Data;
import org.md2k.motionsenselib.device.Device;
import org.md2k.motionsenselib.device.MotionSenseManager;
import org.md2k.motionsenselib.device.ReceiveCallback;
import org.md2k.motionsenselib.device.SensorInfo;
import org.md2k.motionsenselib.log.MyLog;
import org.md2k.motionsenselib.log.ReceiveLogCallback;
import org.md2k.motionsenselib.plot.ActivityPlot;
import org.md2k.motionsenselib.settings.Settings;
import org.md2k.motionsenselib.summary.Summary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * MotionSenseLibPlugin
 */
public class MotionSenseLibPlugin implements MethodCallHandler {
    private static final String SET_SETTING = "SET_SETTINGS";
    private static final String GET_SUMMARY = "GET_SUMMARY";
    private static final String BACKGROUND_SERVICE = "BACKGROUND_SERVICE";
    private static final String PLOT = "PLOT";
    private static final String SENSOR_INFO = "SENSOR_INFO";
    private Activity activity;

    private static final String EVENT_CHANNEL_SENSOR_DATA = "org.md2k.motionsenselib.eventchannel.sensordata";
    private static final String EVENT_CHANNEL_LOG = "org.md2k.motionsenselib.eventchannel.log";

    public static void registerWith(Registrar registrar) {
        MyLog.init();
        MotionSenseManager.init(registrar.context());
        MotionSenseLibPlugin motionSensePlugin = new MotionSenseLibPlugin();
        motionSensePlugin.activity = registrar.activity();
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "org.md2k.motionsenselib.channel");
        channel.setMethodCallHandler(motionSensePlugin);
        final EventChannel eventChannelSensorData =
                new EventChannel(registrar.messenger(), EVENT_CHANNEL_SENSOR_DATA);
        final EventChannel eventChannelLog =
                new EventChannel(registrar.messenger(), EVENT_CHANNEL_LOG);
        eventChannelLog.setStreamHandler(new EventChannel.StreamHandler() {

            @Override
            public void onListen(Object o, EventChannel.EventSink eventSink) {
                ReceiveLogCallback receiveLogCallback = new ReceiveLogCallback() {
                    @Override
                    public void onReceive(String logType, String className, String methodName, String message) {
                        eventSink.success(new String[]{logType, className, methodName, message});
                    }
                };
                MyLog.setCallback(receiveLogCallback);
            }

            @Override
            public void onCancel(Object o) {
                MyLog.setCallback(null);
            }
        });

        eventChannelSensorData.setStreamHandler(new EventChannel.StreamHandler() {
            private HashMap<String, ReceiveCallback> receiveCallbacks = new HashMap<>();

            @Override
            public void onListen(Object o, EventChannel.EventSink eventSink) {
//                Log.d("abc", "sensor data listen onListen..device=" + MotionSenseManager.getDevices().size());
                for (int i = 0; i < MotionSenseManager.getDevices().size(); i++) {
                    Device device = MotionSenseManager.getDevices().get(i);
                    ReceiveCallback receiveCallback = new ReceiveCallback() {
                        @Override
                        public void onReceive(ArrayList<Data> d) {
//                            Log.d("abc", "motionsenselib received size="+d.size());
                            ArrayList<HashMap<String, Object>> list = new ArrayList<>();
                            for(int i=0;i<d.size();i++){
                                HashMap<String, Object> r = d.get(i).toMap();
                                r.put("deviceId", device.getDeviceSettings().getDeviceId());
                                list.add(r);
                            }
                            eventSink.success(list);
                        }
                    };
                    device.addListener(receiveCallback);
                    receiveCallbacks.put(device.getDeviceSettings().getDeviceId(), receiveCallback);
                }

            }

            @Override
            public void onCancel(Object o) {

                for(Map.Entry<String, ReceiveCallback> r: receiveCallbacks.entrySet()){
                    Device d = MotionSenseManager.getDevice(r.getKey());
                    if(d!=null)
                        d.removeListener(r.getValue());
                }
                receiveCallbacks.clear();


            }
        });
        eventChannelLog.setStreamHandler(new EventChannel.StreamHandler() {
            private HashMap<String, ReceiveCallback> receiveCallbacks = new HashMap<>();

            @Override
            public void onListen(Object o, EventChannel.EventSink eventSink) {
//                Log.d("abc", "sensor data listen onListen..device=" + MotionSenseManager.getDevices().size());
/*
                for (int i = 0; i < MotionSenseManager.getDevices().size(); i++) {
                    Device device = MotionSenseManager.getDevices().get(i);
                    ReceiveCallback receiveCallback = new ReceiveCallback() {
                        @Override
                        public void onReceive(ArrayList<Data> d) {
                            Log.d("abc", "motionsenselib received size="+d.size());
                            ArrayList<HashMap<String, Object>> list = new ArrayList<>();
                            for(int i=0;i<d.size();i++){
                                HashMap<String, Object> r = d.get(i).toMap();
                                r.put("deviceId", device.getDeviceSettings().getDeviceId());
                                list.add(r);
                            }
                            eventSink.success(list);
                        }
                    };
                    device.addListener(receiveCallback);
                    receiveCallbacks.put(device.getDeviceSettings().getDeviceId(), receiveCallback);
                }
*/

            }

            @Override
            public void onCancel(Object o) {
/*
                for(Map.Entry<String, ReceiveCallback> r: receiveCallbacks.entrySet()){
                    MotionSenseManager.getDevice(r.getKey()).removeListener(r.getValue());
                }
                receiveCallbacks.clear();
*/

            }
        });
    }


    @Override
    public void onMethodCall(MethodCall call, @NonNull Result result) {
        Gson gson = new Gson();
        switch (call.method) {
            case SET_SETTING:
                String settingsStr = call.argument("settings");
                Settings settings = gson.fromJson(settingsStr, Settings.class);
                MotionSenseManager.removeDevices();
                for (int i = 0; i < settings.getMotionsense_devices().size(); i++) {
                    MotionSenseManager.addDevice(settings.getMotionsense_devices().get(i));
                }
                result.success(true);
                break;
            case BACKGROUND_SERVICE:
                boolean run = call.argument("run");
                if (run) MotionSenseManager.start();
                else MotionSenseManager.stop();
                result.success(true);
                break;
            case GET_SUMMARY:
                result.success(gson.toJson(Summary.create()));
                break;
            case SENSOR_INFO:
                String deviceId = call.argument("deviceId");
                Device deviceSensorInfo = MotionSenseManager.getDevice(deviceId);
                ArrayList<SensorInfo> x = deviceSensorInfo.getSensorInfo();
                result.success(gson.toJson(x));
                break;
            case PLOT:
                String platformType = call.argument("platformType");
                String platformId = call.argument("platformId");
                String sensorType = call.argument("sensorType");
                Device device = MotionSenseManager.getDevice(platformType, platformId);
                if (device != null) {
                    Intent intent = new Intent(activity, ActivityPlot.class);
                    intent.putExtra("deviceId", device.getDeviceSettings().getDeviceId());
                    intent.putExtra("sensorType", sensorType);
                    activity.startActivity(intent);
                }
                result.success(true);
                break;
            default:
                result.notImplemented();
        }
    }
}
