package com.example.txpc_bai2;


import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.txpc_bai2.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static final int AUDIO_PERMISSION_CODE = 200;

    private ActivityMainBinding binding;

    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;

    private String audioFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnStop.setEnabled(false);
        binding.btnPlay.setEnabled(false);

        binding.btnRecord.setOnClickListener(v -> checkAudioPermission());
        binding.btnStop.setOnClickListener(v -> stopRecording());
        binding.btnPlay.setOnClickListener(v -> playRecording());
    }

    // ================== XIN QUYỀN ==================
    private void checkAudioPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    AUDIO_PERMISSION_CODE);
        } else {
            startRecording();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == AUDIO_PERMISSION_CODE) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                startRecording();
            } else {
                Toast.makeText(this, "Bị từ chối quyền ghi âm", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // ================== GHI ÂM ==================
    private void startRecording() {
        try {
            File musicDir = getExternalFilesDir(Environment.DIRECTORY_MUSIC);
            if (musicDir == null) {
                Toast.makeText(this, "Không thể truy cập thư mục lưu trữ", Toast.LENGTH_SHORT).show();
                return;
            }
            audioFilePath = musicDir.getAbsolutePath() + "/recorded_audio.3gp";

            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setOutputFile(audioFilePath);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            mediaRecorder.prepare();
            mediaRecorder.start();

            binding.btnRecord.setEnabled(false);
            binding.btnStop.setEnabled(true);
            binding.btnPlay.setEnabled(false);

            Toast.makeText(this, "Đang ghi âm...", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi ghi âm", Toast.LENGTH_SHORT).show();
        }
    }

    // ================== DỪNG ==================
    private void stopRecording() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;

            binding.btnRecord.setEnabled(true);
            binding.btnStop.setEnabled(false);
            binding.btnPlay.setEnabled(true);

            Toast.makeText(this, "Đã lưu file", Toast.LENGTH_SHORT).show();
        }
    }

    // ================== PHÁT ==================
    private void playRecording() {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(audioFilePath);
            mediaPlayer.prepare();
            mediaPlayer.start();

            Toast.makeText(this, "Đang phát...", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi phát audio", Toast.LENGTH_SHORT).show();
        }
    }

    // ================== CLEAN ==================
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}