package hu.bme.aut.nightshaderemote.connectivity.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
* @author Ákos Pap
*/
public class JScriptState implements Serializable {
    @JsonProperty(value = "isPlaying")
    private boolean isPlaying;

    @JsonProperty(value = "curentFile")
    private String currentFile;

    @JsonProperty(value = "isFaster")
    private boolean isFaster;

    @JsonProperty(value = "isPaused")
    private boolean isPaused;

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public String getCurrentFile() {
        return currentFile;
    }

    public void setCurrentFile(String currentFile) {
        this.currentFile = currentFile;
    }

    public boolean isFaster() {
        return isFaster;
    }

    public void setFaster(boolean isFaster) {
        this.isFaster = isFaster;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean isPaused) {
        this.isPaused = isPaused;
    }
}
