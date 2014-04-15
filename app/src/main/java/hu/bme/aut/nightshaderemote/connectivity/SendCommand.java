package hu.bme.aut.nightshaderemote.connectivity;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import hu.bme.aut.nightshaderemote.connectivity.commands.Command;

/**
 * Calls the server with the URL constructed from the given command.
 * <p>
 * If {@link hu.bme.aut.nightshaderemote.connectivity.commands.Command#isPost()} returns true, asks the command to write its data to the connection stream.
 *
 * @author Ákos Pap
 */
public class SendCommand extends AsyncTask<Command, Void, String> {
    public static final String TAG = "SendCommand";

    protected String ip = "192.168.0.108";
    protected String port = "8888";

    final OnCommandSentListener callback;

    /**
     * Constructs an instance which will connect to the server on the given address, listening on the given port.
     *
     * @param ip The address of the server. Can be a domain or an IP(v4) address.
     * @param port The port the server listens on.
     * @param callback If set the result of the call is returned to this object.
     */
    public SendCommand(String ip, String port, OnCommandSentListener callback) {
        this.ip = ip;
        this.port = port;
        this.callback = callback;
    }

    @Override
    protected String doInBackground(Command... params) {
        URL url = null;
        HttpURLConnection urlConnection = null;

        Command command = params[0];

        String result = "";
        try {
            url = new URL("http://" + ip + ":" + port + command.getPath());

            Log.d(TAG, "Sending command " + url.toString());

            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setConnectTimeout(2000);

            if (command.isPost()) {
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());

                command.writePostData(out);

                out.flush();
                out.close();
            }

            try {
                InputStream in = urlConnection.getInputStream();
                result = readStream(in);
            } catch (IOException e) {
                //e.printStackTrace();
                Log.w(TAG, "Got an IOException. The server is probably not available.");
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


    /**
     * Implement this to receive the response of a call to the server.
     */
    public interface OnCommandSentListener {

        /**
         * Called when the server returns a response.
         *
         * @param result The response of the server.
         */
        void onCommandSent(String result);
    }
}
