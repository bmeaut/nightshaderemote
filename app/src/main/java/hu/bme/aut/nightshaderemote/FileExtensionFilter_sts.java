package hu.bme.aut.nightshaderemote;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Filefilter az ".sts" kiterjesztésű fájlok szűrsésre.
 *
 * Created by Marci on 2014.03.17..
 */
public class FileExtensionFilter_sts implements FilenameFilter {
    public boolean accept(File dir, String name) {
        return (name.endsWith(".sts") || name.endsWith(".STS"));
    }
}
