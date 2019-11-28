package com.zf.plugins.log;

public class VersionLogDto {
    Integer versionCode
    String versionName;
    File logFile;
    Map<String, Object> extraMap = new HashMap<>();

    public void putExtra(String key, Object value) {
        extraMap.put(key, value)
    }

}

