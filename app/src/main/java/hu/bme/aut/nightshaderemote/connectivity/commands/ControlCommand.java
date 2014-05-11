package hu.bme.aut.nightshaderemote.connectivity.commands;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Ákos Pap
 */
public class ControlCommand implements Command {

    public static enum CommandName {
        PLAY_PAUSE("scriptPlayPause"),
        STOP("scriptStop"),

        ZOOM_IN("zoom/in"),
        ZOOM_OUT("zoom/out"),

        SELECT("select"),
        DESELECT("deselect"),
        ;

        private String uriPart;

        CommandName(String uriPart) {
            this.uriPart = uriPart;
        }

        public String getPathPart() {
            return this.uriPart;
        }
    }

    public static final String prefix = "control";

    protected CommandName commandName;

    public ControlCommand(CommandName commandName) {
        this.commandName = commandName;
    }

    @Override
    public String getPath() {
        return "/" + prefix + "/" + commandName.getPathPart();
    }

    @Override
    public boolean isPost() {
        return true;
    }

    @Override
    public void writePostData(OutputStream out) throws IOException {
        // no data to post
        return;
    }
}
