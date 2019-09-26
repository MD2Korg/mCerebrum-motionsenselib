package org.md2k.motionsenselib;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import org.md2k.motionsenselib.device.Device;
import org.md2k.motionsenselib.device.MotionSenseManager;
import org.md2k.motionsenselib.plot.ActivityPlot;
import org.md2k.motionsenselib.settings.Settings;
import org.md2k.motionsenselib.summary.Summary;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * MotionsenselibPlugin
 */
public class MotionsenselibPlugin implements MethodCallHandler {
    private static final String SET_SETTING = "SET_SETTINGS";
    private static final String GET_SUMMARY = "GET_SUMMARY";
    private static final String BACKGROUND_SERVICE = "BACKGROUND_SERVICE";
    private static final String PLOT = "PLOT";
    private Activity activity;

    /**
     * Plugin registration.
     */

    public static void registerWith(Registrar registrar) {
        MotionSenseManager.init(registrar.context());
        MotionsenselibPlugin motionsensePlugin = new MotionsenselibPlugin();
        motionsensePlugin.activity = registrar.activity();
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "org.md2k.motionsenselib.channel");
        channel.setMethodCallHandler(motionsensePlugin);
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
