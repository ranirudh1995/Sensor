package com.example.anirudh.sensor;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

/**
 * Created by anirudh on 27/4/15.
 */
public class SensorActivity extends Activity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;
    private double azimuth =0.0;
    private double pitch =0.0;
    private double roll =0.0;
    String t = "";
    private float[] mAccelerometer = null;
    private float[] mGeomagnetic = null;


    public void onAccuracyChanged(Sensor sensor, int accuracy){
    }

    @Override

    public void onSensorChanged(SensorEvent event) {
        // onSensorChanged gets called for each sensor so we have to remember the values
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            mAccelerometer = event.values;
        }

        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            mGeomagnetic = event.values;
        }

        if (mAccelerometer != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mAccelerometer, mGeomagnetic);

            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                // orientation contains the azimuth(direction), pitch and roll values.
                azimuth = 180 * orientation[0] / Math.PI;
                pitch = 180 * orientation[1] / Math.PI;
                roll = 180 * orientation[2] / Math.PI;
                //t = "Azimuth: " + azimuth + "\nPitch: " + pitch + "\nRoll: " + roll;
                //display(t);
                // TODO: udpate view based on these directions
            }
        }
        ImageView iview = (ImageView) findViewById(R.id.imageView2);

        if(roll < -160 || roll > 160){
            iview.setImageResource(R.mipmap.ic_launcher);
        }
        else if(roll < 20  && roll >-20){
            iview.setImageResource(R.mipmap.and1_image5);
        }
        else{
            if((azimuth >= 0  && azimuth < 45) || (azimuth < 0 && azimuth >= -45)){
                iview.setImageResource(R.mipmap.and1_image1);
            }
            else if((azimuth >= 45  && azimuth < 135)){
                iview.setImageResource(R.mipmap.and1_image2);
            }

            else if((azimuth >= 135) || (azimuth < -135 )){
                iview.setImageResource(R.mipmap.and1_image3);
            }
            else if((azimuth < -45  && azimuth >= -135)){
                iview.setImageResource(R.mipmap.and1_image4);
            }
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_activity);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);


    }

    @Override
    protected void onResume() {
        super.onResume();

        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_GAME);
    }
    /*public void display(String d){
        TextView t = (TextView) findViewById(R.id.text1);
        t.setText(d);
    }*/

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this, accelerometer);
        mSensorManager.unregisterListener(this, magnetometer);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sensor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
