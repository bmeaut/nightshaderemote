package hu.bme.aut.nightshaderemote.connectivity.commands;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ákos Pap
 */
public class ParametrizedControlCommand extends ControlCommand {

    public static final String PARAM_OBJECT_TYPE = "type";
    public static final String PARAM_IDENTIFIER = "id";

    protected List<NameValuePair> parameters;

    public ParametrizedControlCommand(CommandName commandName) {
        super(commandName);
        parameters = new ArrayList<>();
    }

    @Override
    public String getPath() {
        return super.getPath() + "?" + buildParams();
    }

    public void addParameter(String key, String value) {
        parameters.add(new BasicNameValuePair(key, value));
    }

    protected String buildParams() {
        String ret = URLEncodedUtils.format(parameters, "UTF-8");
        return ret;
    }
}
