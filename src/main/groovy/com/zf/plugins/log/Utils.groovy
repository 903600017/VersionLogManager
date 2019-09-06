package com.zf.plugins.log;

public class Utils {
    public static String getResourceContent(String filePath) {
        URL resource = Utils.class.getClassLoader().getResource(filePath);
        return resource.getText("utf-8")
    }

    public static String getFileName(File file) {
        String templeFileNameWithSuffix = file.getName();
        int index = templeFileNameWithSuffix.lastIndexOf(".");
        String _templeFileName = templeFileNameWithSuffix
        if (index >= 0) {
            _templeFileName = templeFileNameWithSuffix.substring(0, index)
        }

        return _templeFileName
    }
}
