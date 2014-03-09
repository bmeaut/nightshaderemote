package hu.bme.aut.nightshaderemote.connectivity;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by akos on 3/6/14.
 */
public class SendCommand extends AsyncTask<Command, Void, String> {
    public static final String TAG = "SendCommand";

    protected String ip = "192.168.0.108";
    protected int port = 8888;

    final OnCommandSentListener callback;

    public SendCommand(String ip, int port, OnCommandSentListener callback) {
        this.ip = ip;
        this.port = port;
        this.callback = callback;
    }

    @Override
    protected String doInBackground(Command... params) {
        URL url = null;
        HttpURLConnection urlConnection = null;

        Command command = params[0];

        String result = "--- no result ---";
        try {
            url = new URL("http://" + ip + ":" + port + command.getPath());

            Log.d(TAG, "Sending command " + url.toString());

            urlConnection = (HttpURLConnection)url.openConnection();

            try {
                InputStream in = urlConnection.getInputStream();
                result = readStream(in);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) urlConnection.disconnect();
        }

        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        if (callback != null) {
            callback.onCommandSent(s);
        }
    }

    /**
     * Beolvassa a Stream tartalmát egy stringbe.
     * @param inputStream Ahonnan olvas.
     * @return Amit beolvasott. Üres, ha nem jött semmi.
     * @throws java.io.IOException Ha hiba történt a streamből olvasáskor.
     */
    public static String readStream(InputStream inputStream) throws IOException {
        // http://stackoverflow.com/questions/309424/read-convert-an-inputstream-to-a-string
        Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
        return (scanner.hasNext()) ? scanner.next() : "";
    }

    public interface OnCommandSentListener {
        void onCommandSent(String result);
    }
}
