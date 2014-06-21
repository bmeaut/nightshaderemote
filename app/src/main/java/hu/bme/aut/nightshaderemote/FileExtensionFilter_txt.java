package hu.bme.aut.nightshaderemote;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Filefilter a ".txt" kiterjesztésű fájlok szűrsésre.
 *
 * Created by Marci on 2014.05.07..
 */
public class FileExtensionFilter_txt implements FilenameFilter {
    public boolean accept(File dir, String name) {
        return (name.endsWith(".txt") || name.endsWith(".TXT"));
    }
}
