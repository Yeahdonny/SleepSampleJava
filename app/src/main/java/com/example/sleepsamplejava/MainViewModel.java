package com.example.sleepsamplejava;

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
    void addSleepClassify(ArrayList<SleepClassifyEventEntity> sleepClassifyEventEntity){
        for(int i = 0 ; i < sleepClassifyEventEntity.size() ;i++){
            sleepClassifyEventEntityList.add(sleepClassifyEventEntity.get(i));
        }
        allSleepClassifyEventEntities.postValue(sleepClassifyEventEntityList);
    }
    void addSleepSegment(ArrayList<SleepSegmentEventEntity> sleepSegmentEventEntity){
        for(int i = 0 ; i < sleepSegmentEventEntity.size() ; i++){
            sleepSegmentEventEntityList.add(sleepSegmentEventEntity.get(i));
        }
        allSleepSegments.postValue(sleepSegmentEventEntityList);
    }

}
