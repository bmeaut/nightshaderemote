package hu.bme.aut.nightshaderemote.connectivity;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
        return "/" + prefix + "?file=" + filename;
    }

    @Override
    public boolean isPost() {
        return true;
    }

    @Override
    public void writePostData(OutputStream out) {
        final String APP_FOLDER = "NightshadeRemote";
        final String CUSTOM_BUTTONS_FOLDER = "custom_buttons";
        File dir = new File(Environment.getExternalStorageDirectory(), new File(APP_FOLDER, CUSTOM_BUTTONS_FOLDER).getPath());
        File f = new File(dir, filename);

        if (f.exists() && f.canRead()) {
            InputStream in = null;
            try {
                in = new FileInputStream(f);
                byte[] buffer = new byte[10*1024*1024]; // 10 KB buffer
                int len;
                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
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
        } else {
            Log.e(TAG, "File doesn't exists, or cannot read it! (" + filename + ")");
        }
    }
}
