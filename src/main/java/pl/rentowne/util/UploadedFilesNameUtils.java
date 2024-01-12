package pl.rentowne.util;

import com.github.slugify.Slugify;
import org.apache.commons.io.FilenameUtils;

public class UploadedFilesNameUtils {

    public static String slugifyFileName(String filename) {
        String name = FilenameUtils.getBaseName(filename);
        Slugify slg = Slugify.builder()
                .customReplacement("_", "-")
                .build();
        String changedName = slg.slugify(name);
        return changedName + "." + FilenameUtils.getExtension(filename);
    }

}
