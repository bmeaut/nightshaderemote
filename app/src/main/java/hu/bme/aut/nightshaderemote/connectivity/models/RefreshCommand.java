package hu.bme.aut.nightshaderemote.connectivity.models;

import java.io.IOException;
import java.io.OutputStream;

import hu.bme.aut.nightshaderemote.connectivity.Command;

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
