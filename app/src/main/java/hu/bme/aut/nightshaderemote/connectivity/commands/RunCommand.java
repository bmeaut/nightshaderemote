package hu.bme.aut.nightshaderemote.connectivity.commands;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import hu.bme.aut.nightshaderemote.U;

/**
 * @author Ákos Pap
 */
public class RunCommand implements Command {

    public static final String prefix = "run";
    public static final String TAG = "RunCommand";

    protected String filename;

    public RunCommand(String filename) {
        this.filename = filename;
    }

    @Override
    public String getPath() {
        try {
            return "/" + prefix + "?file=" + URLEncoder.encode(filename, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.wtf(TAG, "URLEncoder doesn't support UTF-8 encoding. WTF!?", e);
        }
        return "";
    }

    @Override
    public boolean isPost() {
        return true;
    }

    @Override
    public void writePostData(OutputStream out) {

        File dir = new File(Environment.getExternalStorageDirectory(), new File(U.C.APP_FOLDER, U.C.SCRIPTS_FOLDER).getPath());
        File f = new File(dir, filename);

            InputStream in = null;
            try {
                in = new FileInputStream(f);
                byte[] buffer = new byte[10*1024*1024]; // 10 KB buffer
                int len;
                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
            } catch (IOException e) {
                Log.e(TAG, "File doesn't exists, or cannot read it! (" + filename + ")", e);
            } finally {
                if (in != null) try {
                    in.close();
                } catch (IOException e) {
                    // fatal error...
                    e.printStackTrace();
                }
                try {
                    out.flush();
                } catch (IOException e) {
                    // fatal error
                    e.printStackTrace();
                }
            }


    }
}
