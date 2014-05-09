package hu.bme.aut.nightshaderemote.connectivity.commands;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Ákos Pap
 */
public class FetchCommand implements Command {

    public static enum FetchTarget {
        OBJECT_IMAGE("objectImage");

        private String uriPart;

        FetchTarget(String uriPart) {
            this.uriPart = uriPart;
        }

        public String getPathPart() {
            return this.uriPart;
        }
    }

    public static final String prefix = "fetch";

    protected FetchTarget target;

    public FetchCommand(FetchTarget target) {
        this.target = target;
    }

    @Override
    public String getPath() {
        return "/" + prefix + "/" + target.getPathPart();
    }

    @Override
    public boolean isPost() {
        return false;
    }

    @Override
    public void writePostData(OutputStream out) throws IOException {
        throw new UnsupportedOperationException("This Command doesn't do any output!");
    }
}
