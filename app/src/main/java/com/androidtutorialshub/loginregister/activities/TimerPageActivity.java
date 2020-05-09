package com.androidtutorialshub.loginregister.activities;

import android.content.Context;
import android.content.Intent;

import com.androidtutorialshub.loginregister.MainActivity;
import com.androidtutorialshub.loginregister.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hbisoft.hbrecorder.HBRecorder;
import com.hbisoft.hbrecorder.HBRecorderListener;

import androidx.annotation.IntegerRes;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimerPageActivity extends AppCompatActivity implements HBRecorderListener {
    HBRecorder hbRecorder;
    private static final int SCREEN_RECORD_REQUEST_CODE = 22;
    private static final int RESULT_OK = 1;

    TextView timerTextView;
    long startTime = 0;

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            timerTextView.setText(String.format("%d:%02d", minutes, seconds));

            timerHandler.postDelayed(this, 500);
        }
    };

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_countdown);

            ImageButton b = (ImageButton) findViewById(R.id.imageButton);
            b.setImageResource(R.drawable.play_button);
            b.setTag(R.drawable.play_button);
            Integer resource = (Integer) b.getTag();
            b.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    ImageButton bv = (ImageButton) v;
                    if (bv.getTag() == resource) {
                        startTime = System.currentTimeMillis();
                        timerHandler.postDelayed(timerRunnable, 0);
                        bv.setImageResource(R.drawable.pause_button);
                        bv.setTag(R.drawable.pause_button);

                        startRecordingScreen();
                        Toast.makeText(TimerPageActivity.this, "Recording started",
                                Toast.LENGTH_LONG).show();
                    } else {
                        timerHandler.removeCallbacks(timerRunnable);
                        bv.setImageResource(R.drawable.play_button);
                        bv.setTag(R.drawable.play_button);

                        hbRecorder.stopScreenRecording();
                        Toast.makeText(TimerPageActivity.this, "Recording Completed",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });

            //Init HBRecorder
            hbRecorder = new HBRecorder(this, this);

        }

        @Override
        public void onPause() {
            super.onPause();
            timerHandler.removeCallbacks(timerRunnable);
            ImageButton b = (ImageButton) findViewById(R.id.imageButton);
            b.setImageResource(R.drawable.play_button);
        }

    private void startRecordingScreen() {
        MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        Intent permissionIntent = mediaProjectionManager != null ? mediaProjectionManager.createScreenCaptureIntent() : null;
        startActivityForResult(permissionIntent, SCREEN_RECORD_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SCREEN_RECORD_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                //Start screen recording
                hbRecorder.startScreenRecording(data, resultCode, this);

            }
        }
    }

    @Override
    public void HBRecorderOnComplete() {
        Log.d("HBRecorderComplete", "Screen Recorded Successfully!");
    }

    @Override
    public void HBRecorderOnError(int errorCode, String reason) {
        Log.e("HBRecorderError", reason);
    }
}
