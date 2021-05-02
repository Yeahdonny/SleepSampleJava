package com.example.sleepsamplejava;

public class SleepClassifyEventEntity {
    int timestampSeconds;
    int confidence;
    int motion;
    int light;

    public int getTimestampSeconds() {
        return timestampSeconds;
    }

    public void setTimestampSeconds(int timestampSeconds) {
        this.timestampSeconds = timestampSeconds;
    }

    public int getConfidence() {
        return confidence;
    }

    public void setConfidence(int confidence) {
        this.confidence = confidence;
    }

    public int getMotion() {
        return motion;
    }

    public void setMotion(int motion) {
        this.motion = motion;
    }

    public int getLight() {
        return light;
    }

    public void setLight(int light) {
        this.light = light;
    }
}
