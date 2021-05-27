package jp.ac.titech.itpro.sdl.die;

import android.annotation.SuppressLint;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener,SensorEventListener {
    private final static String TAG = MainActivity.class.getSimpleName();

    private GLSurfaceView glView;
    private SimpleRenderer renderer;

    private Cube cube;
    private Pyramid pyramid;
    private Slide slide;

    //gyroscope
    private SensorManager manager;
    private Sensor gyroscope;
    private float dir=0;
    private float timestamp=0;
    private static final float convert = 1.0f / 10000000.0f;
    float RotateX=0,RotateY=0,RotateZ=0;
    float r_progress=0;
    //gyro

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_main);


        glView = findViewById(R.id.gl_view);
        //gyroscope
        manager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (manager == null) {
            Toast.makeText(this, "1", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        gyroscope = manager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if (gyroscope == null) {
            Toast.makeText(this, "2", Toast.LENGTH_LONG).show();
        }
        //gyro
        SeekBar seekBarX = findViewById(R.id.seekbar_x);
        SeekBar seekBarY = findViewById(R.id.seekbar_y);
        SeekBar seekBarZ = findViewById(R.id.seekbar_z);
        seekBarX.setMax(360);
        seekBarY.setMax(360);
        seekBarZ.setMax(360);
        seekBarX.setOnSeekBarChangeListener(this);
        seekBarY.setOnSeekBarChangeListener(this);
        seekBarZ.setOnSeekBarChangeListener(this);

        renderer = new SimpleRenderer();
        cube = new Cube();
        pyramid = new Pyramid();
        slide=new Slide();
        renderer.setObj(cube);
        glView.setRenderer(renderer);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        manager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_FASTEST);
        glView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        manager.unregisterListener(this);
        glView.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
        public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected");
        switch (item.getItemId()) {
            case R.id.menu_cube:
                renderer.setObj(cube);
                break;
            case R.id.menu_pyramid:
                renderer.setObj(pyramid);
                break;
            case R.id.menu_slide:
                renderer.setObj(slide);
                break;
        }
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
        case R.id.seekbar_x:
            RotateX+=(progress-r_progress);
            r_progress=progress;
            renderer.rotateObjX(RotateX);
            break;
        case R.id.seekbar_y:
            RotateY+=(progress-r_progress);
            r_progress=progress;
            renderer.rotateObjY(RotateY);
            break;
        case R.id.seekbar_z:
            RotateZ+=(progress-r_progress);
            r_progress=progress;
            renderer.rotateObjZ(RotateZ);
            break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
    @Override
    public void onSensorChanged(SensorEvent event){
        float omegaX = event.values[0];
        float omegaY = event.values[1];
        float omegaZ = event.values[2];
        if(timestamp!=0) {
            final float time_s = (event.timestamp - timestamp) *convert;
            RotateX+=omegaX * time_s;
            RotateY+=omegaY * time_s;
            RotateZ+=omegaZ * time_s;
            timestamp = event.timestamp;
        }
        else {
            timestamp=event.timestamp;
        }
        renderer.rotateObjX(RotateX);
        renderer.rotateObjY(RotateY);
        renderer.rotateObjZ(RotateZ);

    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d(TAG, "onAccuracyChanged: accuracy=" + accuracy);
    }
}
