package hu.bme.aut.nightshaderemote.connectivity.commands;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Ákos Pap
 */
public class RefreshCommand implements Command {

    public static final String prefix = "state";

    @Override
    public String getPath() {
        return "/" + prefix;
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
