package nazmul.sensordemo;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class ProximitySensorActivity extends AppCompatActivity {

    private static final String TAG = "sensor";
    SensorManager sensorManager;
    Sensor proximitySensor;
    SensorEventListener proximitySensorListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if(proximitySensor == null) {
            Log.e(TAG, "Proximity sensor not available.");
            finish(); // Close app
        }
        proximitySensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if(sensorEvent.values[0] < proximitySensor.getMaximumRange()) {
                    // Detected something nearby
                    getWindow().getDecorView().setBackgroundColor(Color.RED);
                } else {
                    // Nothing is nearby
                    getWindow().getDecorView().setBackgroundColor(Color.GREEN);
                }
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(proximitySensorListener, proximitySensor, 2 * 1000 * 1000);
    }

    @Override
    protected void onPause() {
        sensorManager.unregisterListener(proximitySensorListener);
        super.onPause();
    }

    public void moveToAccelerometer(View view) {
        Intent intent=new Intent(ProximitySensorActivity.this,AccelerometerActivity.class);
        startActivity(intent);
    }
}
