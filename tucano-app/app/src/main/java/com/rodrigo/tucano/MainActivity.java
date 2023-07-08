package com.rodrigo.tucano;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.assist.AssistStructure;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.rodrigo.tucano.databinding.ActivityMainBinding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class SystemProperties {
    public static final String GETPROP_EXECUTABLE_PATH = "/system/bin/getprop";
    public static final String SETPROP_EXECUTABLE_PATH = "/system/bin/setprop";
    public static String read(String propName) {
        Process process = null;
        BufferedReader bufferedReader = null;

        try {
            process = new ProcessBuilder().command(GETPROP_EXECUTABLE_PATH, propName).redirectErrorStream(true).start();
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = bufferedReader.readLine();
            if (line == null){
                line = ""; //prop not set
            }
            Log.i("SystemProperties","Reading prop:" + propName + " value:" + line);
            return line;

        } catch (Exception e) {
            Log.e("SystemProperties","Failed to read System Property " + propName,e);
            return "";
        } finally{
            if (bufferedReader != null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {}
            }
            if (process != null){
                process.destroy();
            }
        }
    }
    public static void write(String propName, String propValue) {
        Process process = null;
        BufferedReader bufferedReader = null;

        try {
            process = new ProcessBuilder().command(SETPROP_EXECUTABLE_PATH, propName, propValue).redirectErrorStream(true).start();
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = bufferedReader.readLine();
        } catch (Exception e) {
            Log.e("SystemProperties","Failed to write System Property " + propName,e);
        } finally{
            if (bufferedReader != null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {}
            }
            if (process != null){
                process.destroy();
            }
        }
    }
}

public class MainActivity extends AppCompatActivity {
    public Boolean cameraIsDisabled = true;

    ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SystemProperties.read("BLOCK_CAMERA");
        SystemProperties.write("BLOCK_CAMERA", Boolean.toString(cameraIsDisabled));
        SystemProperties.read("BLOCK_CAMERA");

        binding.handleCameraStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cameraIsDisabled) {
                    cameraIsDisabled = false;
                    view.setSelected(false);
//                    binding.handleCameraStatusBtn.setText("Camera enabled");
                } else {
                    cameraIsDisabled = true;
                    view.setSelected(true);
//                    binding.handleCameraStatusBtn.setText("Camera disabled");
                }
                Log.e("Btn Selected", (String.valueOf(view.isSelected())));

                SystemProperties.write("BLOCK_CAMERA", Boolean.toString(cameraIsDisabled));
                SystemProperties.read("BLOCK_CAMERA");
            }
        });
    }
}