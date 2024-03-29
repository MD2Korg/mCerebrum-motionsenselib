package org.md2k.motionsenselib.plot;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.components.YAxis;

import org.md2k.motionsenselib.device.Data;
import org.md2k.motionsenselib.device.Device;
import org.md2k.motionsenselib.device.MotionSenseManager;
import org.md2k.motionsenselib.device.ReceiveCallback;
import org.md2k.motionsenselib.device.SensorInfo;
import org.md2k.motionsenselib.device.SensorType;

import java.util.ArrayList;

public class ActivityPlot extends RealtimeLineChartActivity {
    Device device;
    SensorInfo sensorInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {

            String deviceId = getIntent().getStringExtra("deviceId");
            SensorType sensorType = SensorType.valueOf(getIntent().getStringExtra("sensorType"));
            device = MotionSenseManager.getDevice(deviceId);
            assert device != null;
            sensorInfo = device.getSensorInfo(sensorType);
//            YAxis leftAxis = mChart.getAxisLeft();
//            leftAxis.setAxisMinimum(sensorInfo.getMinValue());
//            leftAxis.setAxisMaximum(sensorInfo.getMaxValue());
        } catch (Exception e) {
            finish();
        }
    }

    void updatePlot(Data data) {
        getmChart().getDescription().setText(sensorInfo.getTitle());
        getmChart().getDescription().setPosition(1f, 1f);
        getmChart().getDescription().setEnabled(true);
        getmChart().getDescription().setTextColor(Color.WHITE);
        double[] samples = data.getSample();
        float[] sampleTemp = new float[samples.length];
        for (int i = 0; i < samples.length; i++) {
            sampleTemp[i] = (float) samples[i];
        }
        String[] fields=new String[sensorInfo.getFields().length];
        for(int i =0;i<sensorInfo.getFields().length;i++){
            fields[i]=sensorInfo.getFields()[i].getId();
        }
        addEntry(sampleTemp, fields, 400);
    }

    @Override
    public void onResume() {

        device.addListener(receiveCallback);
        super.onResume();
    }

    @Override
    public void onPause() {
        device.removeListener(receiveCallback);
        super.onPause();

    }

    private ReceiveCallback receiveCallback = new ReceiveCallback() {
        @Override
        public void onReceive(ArrayList<Data> d) {
            for(int i=0;i<d.size();i++) {
                if (d.get(i).getSensorType() == sensorInfo.getSensorType()) {
                    updatePlot(d.get(i));
                }
            }
        }
    };

}
