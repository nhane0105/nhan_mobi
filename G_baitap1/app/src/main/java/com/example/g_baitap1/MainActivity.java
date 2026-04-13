package com.example.g_baitap1;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButtonToggleGroup;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AnimatedView animatedView = findViewById(R.id.animated_view);
        MaterialButtonToggleGroup speedGroup = findViewById(R.id.speed_toggle_group);

        speedGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (!isChecked) {
                return;
            }
            float mult = 1f;
            if (checkedId == R.id.btn_speed_2x) {
                mult = 2f;
            } else if (checkedId == R.id.btn_speed_3x) {
                mult = 3f;
            }
            animatedView.setSpeedMultiplier(mult);
        });
    }
}
