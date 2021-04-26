package com.example.sleepsamplejava;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.location.SleepClassifyEvent;
import com.google.android.gms.location.SleepSegmentEvent;

import java.util.List;

public class SleepReceiver extends BroadcastReceiver {
    public static PendingIntent createSleepReceiverPendingIntent(Context context) {
        Intent sleepIntent = new Intent(context, SleepReceiver.class);
        return PendingIntent.getBroadcast(context, 0, sleepIntent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(SleepSegmentEvent.hasEvents(intent)){
            List<SleepSegmentEvent> sleepSegmentEvents = SleepSegmentEvent.extractEvents(intent);
            //
            addSleepSegmentEventsToArray(sleepSegmentEvents);
        }else if(SleepClassifyEvent.hasEvents(intent)){
            List<SleepClassifyEvent> sleepClassifyEvents = SleepClassifyEvent.extractEvents(intent);
            //
            addSleepClassifyEventsToArray(sleepClassifyEvents);
        }
    }

    private void addSleepClassifyEventsToArray(List<SleepClassifyEvent> sleepClassifyEvents) {
        if(!sleepClassifyEvents.isEmpty()){
            List<SleepClassifyEventEntity> slist = null;
            for(int i = 0 ; i < sleepClassifyEvents.size() ; i++){
                SleepClassifyEventEntity entity = new SleepClassifyEventEntity();
                entity.confidence = sleepClassifyEvents.get(i).getConfidence();
                entity.light = sleepClassifyEvents.get(i).getLight();
                entity.motion = sleepClassifyEvents.get(i).getMotion();
                entity.timestampSeconds = (int)sleepClassifyEvents.get(i).getTimestampMillis();
                slist.add(entity);
            }

        }
    }

    private void addSleepSegmentEventsToArray(List<SleepSegmentEvent> sleepSegmentEvents) {
        if(!sleepSegmentEvents.isEmpty()){
            List<SleepSegmentEventEntity> slist = null;
            for(int i = 0 ; i < sleepSegmentEvents.size() ; i++){
                SleepSegmentEventEntity entity = new SleepSegmentEventEntity();
                entity.startTimeMillis = sleepSegmentEvents.get(i).getStartTimeMillis();
                entity.endTimeMillis = sleepSegmentEvents.get(i).getEndTimeMillis();
                entity.status = sleepSegmentEvents.get(i).getStatus();
                slist.add(entity);
            }
        }
    }

}
