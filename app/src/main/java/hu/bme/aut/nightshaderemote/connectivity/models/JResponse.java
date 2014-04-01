package hu.bme.aut.nightshaderemote.connectivity.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
* @author Ákos Pap
*/
public class JResponse implements Serializable {
    @JsonProperty(value = "response")
    private String response;

    @JsonProperty(value = "flagState")
    private JFlagState flagState;

    @JsonProperty(value = "scriptState")
    private JScriptState ScriptState;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public JFlagState getFlagState() {
        return flagState;
    }

    public void setFlagState(JFlagState flagState) {
        this.flagState = flagState;
    }

    public JScriptState getScriptState() {
        return ScriptState;
    }

    public void setScriptState(JScriptState scriptState) {
        ScriptState = scriptState;
    }
}
