package com.example.sleepsamplejava;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewbinding.BuildConfig;

import com.example.sleepsamplejava.databinding.ActivityMainBinding;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.SleepSegmentRequest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MainViewModel mainViewModel;
    private boolean subscribedToSleepData = false;
    private PendingIntent sleepPendingIntent;
    private String sleepSegmentOutput = "";
    private String sleepClassifyOutput = "";

    public void setSubscribedToSleepData(boolean newSubscribedToSleepData){
        if(newSubscribedToSleepData){
            binding.button.setText(R.string.sleep_button_unsubscribe_text);
        }else{
            binding.button.setText(R.string.sleep_button_subscribe_text);
        }
        subscribedToSleepData = newSubscribedToSleepData;
        updateOutput();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(R.layout.activity_main);
        setContentView(binding.getRoot());
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.initViewModel();
        startObserver();
        sleepPendingIntent = SleepReceiver.createSleepReceiverPendingIntent(getApplicationContext(), mainViewModel);
    }

    private void startObserver() {
        mainViewModel.subscribedToSleepDataLiveData.observe(this, newSubscribedToSleepData ->{
            if(subscribedToSleepData != newSubscribedToSleepData){
                subscribedToSleepData = newSubscribedToSleepData;
            }
        });
        mainViewModel.allSleepSegments.observe(this, sleepSegmentEventEntities ->{
            if(sleepSegmentEventEntities!=null){
                Log.d("donny", "segment: "+String.valueOf(sleepSegmentEventEntities.size()));
                sleepSegmentOutput="";
                for(int i = 0 ; i < sleepSegmentEventEntities.size() ; i++){
                    Log.d("donny", String.valueOf(sleepSegmentEventEntities.get(i).startTimeMillis));
                    sleepSegmentOutput += "start: "+sleepSegmentEventEntities.get(i).getStartTimeMillis() +
                            " end: " +sleepSegmentEventEntities.get(i).getEndTimeMillis() +
                            " status: " +sleepSegmentEventEntities.get(i).getStatus()+"\n";
                }
            }
            updateOutput();
        });
        mainViewModel.allSleepClassifyEventEntities.observe(this, sleepClassifyEventEntities ->{
            if(sleepClassifyEventEntities!=null){
                sleepClassifyOutput="";
                Log.d("donny","classify: "+ String.valueOf(sleepClassifyEventEntities.size()));
                for(int i = 0 ; i < sleepClassifyEventEntities.size() ; i++){
                    Log.d("donny", String.valueOf(sleepClassifyEventEntities.get(i).timestampSeconds));
                    sleepClassifyOutput += "confidence: "+ sleepClassifyEventEntities.get(i).getConfidence() +
                            " light: "+sleepClassifyEventEntities.get(i).getLight()+
                            " motion: "+sleepClassifyEventEntities.get(i).getMotion()+
                            " timestamp: "+ sleepClassifyEventEntities.get(i).getTimestampSeconds()+"\n";
                }
            }
            updateOutput();
        });
    }

    private void updateOutput() {
        String header;
        if(subscribedToSleepData){
            String timestamp = String.valueOf(System.currentTimeMillis());
            header = getString(R.string.main_output_header1_subscribed_sleep_data, timestamp);
        }else{
            header= getString(R.string.main_output_header1_unsubscribed_sleep_data);
        }
        String sleepData = getString(R.string.main_output_header2_and_sleep_data, sleepSegmentOutput, sleepClassifyOutput);
        String newOutput = header + sleepData;
        Log.d("donny", "updateOutput: "+newOutput);
        binding.outputTextView.setText(newOutput);
    }

    ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted->{
        if(!isGranted){
            displayPermissionSettingsSnackBar();
        }else{
            binding.outputTextView.setText(R.string.permission_approved);
        }
    });

    private void displayPermissionSettingsSnackBar() {
        Snackbar.make(binding.mainActivity, R.string.permission_rational, Snackbar.LENGTH_LONG)
                .setAction(R.string.action_settings, v -> {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", BuildConfig.LIBRARY_PACKAGE_NAME, null);
                    intent.setData(uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }).show();
    }

    public void onClickRequestSleepData(View view) {
        if(activityRecognitionPermissionApproved()){
            if(subscribedToSleepData){
                setSubscribedToSleepData(false);
                unsubscribeToSleepSegmentUpdates(getApplicationContext(), sleepPendingIntent);
            }else{
                setSubscribedToSleepData(true);
                subscribeToSleepSegmentUpdates(getApplicationContext(), sleepPendingIntent);
            }
        }else{
            requestPermissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION);
        }
    }

    @SuppressLint("MissingPermission") //필요 이유는,
    private void subscribeToSleepSegmentUpdates(Context applicationContext, PendingIntent sleepPendingIntent) {
        Task <Void> task = ActivityRecognition.getClient(applicationContext).requestSleepSegmentUpdates(sleepPendingIntent,
                SleepSegmentRequest.getDefaultSleepSegmentRequest());
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mainViewModel.updateSubscribedToSleepData(true);
            }
        });
       // task.addOnFailureListener(e -> {Log.d("Exception when subscribing to sleep date: "+e)});
    }

    private void unsubscribeToSleepSegmentUpdates(Context applicationContext, PendingIntent sleepPendingIntent) {
        Task <Void> task = ActivityRecognition.getClient(applicationContext).removeSleepSegmentUpdates(sleepPendingIntent);
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mainViewModel.updateSubscribedToSleepData(false);
            }
        });

    }

    private boolean activityRecognitionPermissionApproved() {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION);
    }
}