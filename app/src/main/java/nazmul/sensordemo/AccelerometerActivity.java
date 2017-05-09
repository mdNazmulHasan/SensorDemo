package nazmul.sensordemo;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class AccelerometerActivity extends AppCompatActivity {
    private static final String TAG = "sensor";
    SensorManager sensorManager;
    Sensor accelerometer;
    SensorEventListener accelerometerListener;

    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 600;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(accelerometer == null) {
            Log.e(TAG, "Accelerometer sensor not available.");
            finish(); // Close app
        }
        accelerometerListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                Sensor mySensor = sensorEvent.sensor;

                if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    float x = sensorEvent.values[0];
                    float y = sensorEvent.values[1];
                    float z = sensorEvent.values[2];
                    long curTime = System.currentTimeMillis();

                    if ((curTime - lastUpdate) > 100) {
                        long diffTime = (curTime - lastUpdate);
                        lastUpdate = curTime;

                        float speed = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;

                        if (speed > SHAKE_THRESHOLD) {
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "0172254344"));
                            startActivity(intent);
                        }

                        last_x = x;
                        last_y = y;
                        last_z = z;
                    }
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
        sensorManager.registerListener(accelerometerListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        sensorManager.unregisterListener(accelerometerListener);
        super.onPause();
    }

    public void moveToProximity(View view) {
        Intent intent=new Intent(AccelerometerActivity.this,ProximitySensorActivity.class);
        startActivity(intent);
    }
}
