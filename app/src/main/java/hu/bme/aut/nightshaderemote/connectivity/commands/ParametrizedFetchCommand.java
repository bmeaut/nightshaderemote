package hu.bme.aut.nightshaderemote.connectivity.commands;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ákos Pap
 */
public class ParametrizedFetchCommand extends FetchCommand {

    protected List<NameValuePair> parameters;

    public ParametrizedFetchCommand(FetchTarget target) {
        super(target);
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
