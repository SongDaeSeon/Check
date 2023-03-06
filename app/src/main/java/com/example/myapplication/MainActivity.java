package com.example.myapplication;

import static java.lang.Double.valueOf;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextToSpeech tts;
    private MediaRecorder mRecorder;
    private AudioManager audioManager;
    private static double mEMA = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // AudioManager 객체 생성
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        // MediaRecorder 객체 생성
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mRecorder.setOutputFile("/dev/null");

        // MediaRecorder 시작
        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                String text = "안녕하십니까 지금 이것은 테스트 중입니다..안녕하십니까 지금 이것은 테스트 중입니다 안녕하십니까 지금 이것은 테스트 중입니다 안녕하십니까 지금 이것은 테스트 중입니다 안녕하십니까 지금 이것은 테스트 중입니다 안녕하십니까 지금 이것은 테스트 중입니다 안녕하십니까 지금 이것은 테스트 중입니다 안녕하십니까 지금 이것은 테스트 중입니다 안녕하십니까 지금 이것은 테스트 중입니다 안녕하십니까 지금 이것은 테스트 중입니다 안녕하십니까 지금 이것은 테스트 중입니다 안녕하십니까 지금 이것은 테스트 중입니다 안녕하십니까 지금 이것은 테스트 중입니다 안녕하십니까 지금 이것은 테스트 중입니다 안녕하십니까 지금 이것은 테스트 중입니다 안녕하십니까 지금 이것은 테스트 중입니다 안녕하십니까 지금 이것은 테스트 중입니다 안녕하십니까 지금 이것은 테스트 중입니다 안녕하십니까 지금 이것은 테스트 중입니다..안녕하십니까 지금 이것은 테스트 중입니까 안녕하십니까 지금 이것은 테스트 중입니다안녕하십니까 지금 이것은 테스트 중입니다안녕하십니까 지금 이것은 테스트 중입니다 안녕하십니까 지금 이것은 테스트 중입니다";
                Locale locale = Locale.getDefault();
                tts.setLanguage(locale);
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "id1");
        // 데시벨 측정 쓰레드 실행
        Runnable runnable = new Runnable() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    double amp = getAmplitudeEMA();
                    setVolume(amp);
                    Log.d("Decibel", String.valueOf(amp));

                    AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, ((int) amp /1500)+9, 0);

//                    if(valueOf(amp) < 10000) {
//                        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 5, 0);
//                    }
//                    else if(valueOf(amp) > 10000 && valueOf(amp) < 20000) {
//                        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 10, 0);
//                    }
//                    else{
//                        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 15, 0);
//                    }
                }
            }
        };
        new Thread(runnable).start();
    }
});
    }

    // 데시벨 값 계산 함수
    public double getAmplitude() {
        if (mRecorder != null) {
            return mRecorder.getMaxAmplitude();
        }
        else {
            return 0;
        }
    }

    // 평균 데시벨 값 계산 함수
    public double getAmplitudeEMA() {
        double amp = getAmplitude();
        mEMA = 0.6 * amp + (1.0 - 0.6) * mEMA;
        return mEMA;
    }

    // 음량 조절 함수
    private void setVolume(double db) {
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        double percentage = db / 100;
        int volume = (int) (percentage * maxVolume);

        if (volume > maxVolume) {
            volume = maxVolume;
        } else if (volume < 0) {
            volume = 0;
        }
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }
}
