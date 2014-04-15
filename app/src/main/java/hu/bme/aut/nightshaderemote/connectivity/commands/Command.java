package hu.bme.aut.nightshaderemote.connectivity.commands;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * Specifies the methods that every command has to implement.
 *
 * @author Ákos Pap
 */
public interface Command extends Serializable {

    /**
     * Composes the URL part after the server address and port.
     *
     * Must begin with a '/' (slash)
     * @return The path to append to the URL.
     */
    String getPath();

    /**
     * Whether this command needs to send data to the server.
     */
    boolean isPost();


    /**
     * If {@link #isPost} returns true, the executor will call this method.
     * Write post data content to the provided stream.
     *
     * <p><strong>Don't forget to flush the stream!</strong></p>
     * @param out The stream to write into.
     */
    void writePostData(OutputStream out) throws IOException;
}
