package com.example.deviceorientation_byankur;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensor1, sensor2;
    private float[] mAccelerometerData = new float[3];
    private float[] mMagnetometerData = new float[3];
    TextView azimuthTV, pitchTV, fallTV;
    private float[] rotationMatrix = new float[9];
    private float orientationValues[] = new float[3];
    private boolean rotationOK;

    float azimuth, pitch,roll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor1 = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensor2 = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        azimuthTV = (TextView) findViewById(R.id.azimuth);
        pitchTV = (TextView) findViewById(R.id.pitch);
        fallTV = (TextView) findViewById(R.id.fall);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensorType = sensorEvent.sensor.getType();
        Log.d("MyDataLog","Inside onSensorChanged function call");

        switch (sensorType) {
            case Sensor.TYPE_ACCELEROMETER:
                mAccelerometerData = sensorEvent.values.clone();
                Log.d("MyDataLog","Data from Accelerometer"+mAccelerometerData);
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                mMagnetometerData = sensorEvent.values.clone();
                Log.d("MyDataLog","Data from Accelerometer"+mAccelerometerData);
                break;
            default:
                return;
        }


        rotationOK = SensorManager.getRotationMatrix(rotationMatrix,null, mAccelerometerData, mMagnetometerData);
        Log.d("MyDataLog","Flag of rotationOK: "+rotationOK);
        Log.d("MyDataLog","Rotation Matrix data: "+rotationMatrix);


        if (rotationOK) {
            SensorManager.getOrientation(rotationMatrix, orientationValues);
        }

        //Azimuth : The direction (north/south/east/west) the device is pointing. 0 is magnetic north.
        //Pitch : The top-to-bottom tilt of the device. 0 is flat.
        //Roll : The left-to-right tilt of the device. 0 is flat.
         azimuth = orientationValues[0];
         pitch = orientationValues[1];
         roll = orientationValues[2];
        Log.d("MyDataLog","Azimuth Value: "+azimuth);
        Log.d("MyDataLog","Pitch Value: "+pitch);
        Log.d("MyDataLog","Fall Value: "+roll);

        azimuthTV.setText(String.valueOf(azimuth));
        pitchTV.setText(String.valueOf( pitch));
        fallTV.setText(String.valueOf(roll));

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    // register sensor type to listener
    @Override
    protected void onStart() {
        super.onStart();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),SensorManager.SENSOR_DELAY_UI);
    }

    // unregister sensor type to listener
    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
    }
}