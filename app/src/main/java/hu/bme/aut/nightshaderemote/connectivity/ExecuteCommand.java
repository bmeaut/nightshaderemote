package hu.bme.aut.nightshaderemote.connectivity;

import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * A command to execute a script on the server.
 * The script can be single line, on multiline, separated by '\n'.
 *
 * @author Ákos Pap
 */
public class ExecuteCommand implements Command {

    /**
     * The prefix in the URL, that chooses the functionality.
     */
    public static final String prefix = "execute";

    /**
     * This string will be sent to the server for execution.
     */
    protected String content;

    /**
     * Constructs a command to execute a script on the server.
     * @param content The script to execute.
     */
    public ExecuteCommand(String content) {
        this.content = content;
    }

    /** {@inheritDoc} */
    @Override
    public String getPath() {
        return "/" + prefix;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isPost() {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public void writePostData(OutputStream out) {
        PrintWriter pw = new PrintWriter(out);
        pw.print(content);
        pw.flush();
    }
}
