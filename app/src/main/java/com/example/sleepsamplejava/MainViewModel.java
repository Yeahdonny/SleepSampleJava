package com.example.sleepsamplejava;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends ViewModel {
    public MutableLiveData<Boolean> subscribedToSleepDataLiveData;
    public MutableLiveData<List<SleepSegmentEventEntity>> allSleepSegments;
    public MutableLiveData<List<SleepClassifyEventEntity>> allSleepClassifyEventEntities;
    ArrayList<SleepClassifyEventEntity> sleepClassifyEventEntityList;
    ArrayList<SleepSegmentEventEntity> sleepSegmentEventEntityList;

    public void updateSubscribedToSleepData(boolean subscribed) {
        //DB에 SUBSCRIPTION STATUS를 저장하는 곳임..
    }

    public void initViewModel() {
        subscribedToSleepDataLiveData = new MutableLiveData<>();
        allSleepSegments = new MutableLiveData<>();
        allSleepClassifyEventEntities = new MutableLiveData<>();
        sleepClassifyEventEntityList = new ArrayList<>();
        sleepSegmentEventEntityList = new ArrayList<>();
    }
    void addSleepClassify(SleepClassifyEventEntity sleepClassifyEventEntity){
        sleepClassifyEventEntityList.add(sleepClassifyEventEntity);
        allSleepClassifyEventEntities.setValue(sleepClassifyEventEntityList);
    }
    void addSleepSegment(SleepSegmentEventEntity sleepSegmentEventEntity){
        sleepSegmentEventEntityList.add(sleepSegmentEventEntity);
        allSleepSegments.setValue(sleepSegmentEventEntityList);
    }

}
