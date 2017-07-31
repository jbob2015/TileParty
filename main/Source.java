package main;

import org.lwjgl.openal.*;

public class Source {
    private int sourceId;

    public Source() {
        this.sourceId = AL10.alGenSources();
        AL10.alSourcef(this.sourceId, AL10.AL_GAIN, 1);
        AL10.alSourcef(this.sourceId, AL10.AL_PITCH, 1);
        AL10.alSource3f(this.sourceId, AL10.AL_POSITION, 0, 0, 0);
    }

    /**
     * Plays the sound with the specified id
     *
     * @param sound
     *            id of the sound to be played
     */
    public void play(int sound) {
        AL10.alSourcei(this.sourceId, AL10.AL_BUFFER, sound);
        AL10.alSourcePlay(this.sourceId);
    }

    /**
     * Deletes The Source (Must Be Done Before Exiting Program)
     */
    public void delete() {
        AL10.alDeleteSources(this.sourceId);
    }

    /**
     * Stops the specified sound from playing
     *
     * @param sound
     *            sound to be stopped
     */
    public void stop(int sound) {
        AL10.alSourcei(this.sourceId, AL10.AL_BUFFER, sound);
        AL10.alSourceStop(this.sourceId);
    }

    /**
     * Pauses the specified sound
     * 
     * @param sound
     *            sound to be paused
     */
    public void pause(int sound) {
        AL10.alSourcei(this.sourceId, AL10.AL_BUFFER, sound);
        AL10.alSourcePause(this.sourceId);
    }
}
