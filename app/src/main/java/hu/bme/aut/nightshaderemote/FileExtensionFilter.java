package hu.bme.aut.nightshaderemote;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by Marci on 2014.03.17..
 */
public class FileExtensionFilter implements FilenameFilter {
    public boolean accept(File dir, String name) {
        return (name.endsWith(".sts") || name.endsWith(".STS"));
    }
}
