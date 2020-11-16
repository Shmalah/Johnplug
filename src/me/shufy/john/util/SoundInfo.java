package me.shufy.john.util;

import java.util.concurrent.ThreadLocalRandom;

public class SoundInfo {
    public float volume, pitch;
    public SoundInfo(float volume, float pitch) {
        this.volume = volume;
        this.pitch = pitch;
    }
    public void randomizePitch() {
        this.pitch = ThreadLocalRandom.current().nextFloat();
    }
    public SoundInfo cloneInfo() {
        return new SoundInfo(this.volume, this.pitch);
    }
}
