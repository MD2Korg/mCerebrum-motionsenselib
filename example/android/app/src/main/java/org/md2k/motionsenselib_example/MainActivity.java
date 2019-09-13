package org.md2k.motionsenselib_example;

import android.Manifest;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import org.md2k.motionsenselib.device.Data;
import org.md2k.motionsenselib.device.Device;
import org.md2k.motionsenselib.device.MotionSenseManager;
import org.md2k.motionsenselib.device.ReceiveCallback;
import org.md2k.motionsenselib.settings.Settings;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.flutter.app.FlutterActivity;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugins.GeneratedPluginRegistrant;

public class MainActivity extends FlutterActivity {
    HashMap<Device, ReceiveCallback> receiveCallbacks = new HashMap<>();
    HashMap<String, FileWriter> files = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MotionSenseManager.init(this);
        GeneratedPluginRegistrant.registerWith(this);

        new MethodChannel(this.getFlutterView(), "org.md2k.motionsenselib_example.channel").setMethodCallHandler(new MethodChannel.MethodCallHandler() {
            @Override
            public void onMethodCall(@NonNull MethodCall methodCall, @NonNull MethodChannel.Result result) {
                switch (methodCall.method) {
                    case "PERMISSION":
                        Dexter.withActivity(MainActivity.this).withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION).withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                if (report.areAllPermissionsGranted()) result.success(true);
                                else result.success(false);
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
                        break;
                    case "READ_SETTINGS":
                        StringBuilder myData = new StringBuilder();
                        try {
                            File myExternalFile = new File(getExternalFilesDir(null), "settings.json");
                            FileInputStream fis = new FileInputStream(myExternalFile);
                            DataInputStream in = new DataInputStream(fis);
                            BufferedReader br =
                                    new BufferedReader(new InputStreamReader(in));
                            String strLine;
                            while ((strLine = br.readLine()) != null) {
                                myData.append(strLine);
                            }
                            in.close();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        result.success(myData.toString());
                        break;
                    case "SAVE_SETTINGS":
                        String fileContents = methodCall.argument("settings");
                        String filename = getExternalFilesDir(null) + File.separator + "settings.json";

                        try {
                            FileOutputStream fstream = new FileOutputStream(filename);
                            fstream.write(fileContents.getBytes());
                            fstream.close();
                            result.success(true);
                        } catch (Exception e) {
                            e.printStackTrace();
                            result.success(false);
                        }
                        break;
                    case "SAVE_DATA_START":
                        startSave();
                        result.success(true);
                        break;
                    case "SAVE_DATA_STOP":
                        stopSave();
                        result.success(true);
                        break;
                    default:
                        result.notImplemented();
                        break;
                }
            }
        });
    }

    void stopSave() {
        for (Map.Entry<String, FileWriter> entry : files.entrySet()) {
            try {
                entry.getValue().flush();
                entry.getValue().close();
            } catch (Exception ignored) {
            }

        }
        files.clear();
        for (Map.Entry<Device, ReceiveCallback> entry : receiveCallbacks.entrySet()) {
            try {
                entry.getKey().removeListener(entry.getValue());
            } catch (Exception ignored) {
            }
        }
        receiveCallbacks.clear();

    }

    void startSave() {
        try {
            ArrayList<String> results = new ArrayList<>();
            for (int i = 0; i < MotionSenseManager.getDevices().size(); i++) {
                Device d = MotionSenseManager.getDevices().get(i);
                String privateDir = MainActivity.this.getExternalFilesDir(null).getAbsolutePath();
                String filename = d.getDeviceSettings().getDeviceId() + "_" + System.currentTimeMillis() + ".csv";

                ReceiveCallback r = data -> {
                    results.add(d.toString());
                    synchronized (results) {
                        if (results.size() > 100) {
                            try {
                                Log.d("abc", "writing");
                                if (files.get(d.getDeviceSettings().getDeviceId()) == null) {
                                    File newFile = new File(privateDir, filename);
                                    FileWriter fw = new FileWriter(newFile);
                                    files.put(d.getDeviceSettings().getDeviceId(), fw);
                                }
                                FileWriter fw = files.get(d.getDeviceSettings().getDeviceId());
                                for (int i1 = 0; i1 < results.size(); i1++) {
                                    fw.write(results.get(i1) + "\n");
                                }
                                results.clear();
                            } catch (IOException e) {
                                Log.e("abc", "error writing file exception=" + e.getMessage());
                            }
                        }
                    }

                    Log.d("abc", d.toString());
                };
                d.addListener(r);
                receiveCallbacks.put(d, r);
            }
        } catch (Exception e) {
            Log.e("abc", "error writing file exception:" + e.getMessage());
        }

    }
}
