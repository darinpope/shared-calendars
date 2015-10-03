package utils;

import org.apache.commons.lang3.StringUtils;

public class Helper {
    public static String getTempDir() {
        String tempDir = System.getProperty("java.io.tmpdir");
        if (!StringUtils.endsWith(tempDir, "/")) {
            tempDir = tempDir + "/";
        }
        return tempDir;
    }
}
