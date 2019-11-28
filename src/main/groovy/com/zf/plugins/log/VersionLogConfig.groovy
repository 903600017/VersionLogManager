package com.zf.plugins.log

import org.gradle.api.Action
import org.gradle.api.Project

class VersionLogConfig {
    Integer versionCode
    File vLogWorkDir
    VersionLogDto logInfo


    void vLogWorkDir(File vLogWorkDir) {
        this.vLogWorkDir = vLogWorkDir
    }

    public static VersionLogConfig getConfig(Project project) {
        VersionLogConfig config =
                project.getExtensions().findByType(VersionLogConfig.class);
        if (config == null) {
            config = new VersionLogConfig();
        }
        return config;
    }

    void logInfo(Action<VersionLogDto> action) {
        if (logInfo == null) {
            logInfo = new VersionLogDto()
        }
        action.execute(logInfo)
    }

    void logInfo(Closure c) {
        if (logInfo == null) {
            logInfo = new VersionLogDto()
        }
        org.gradle.util.ConfigureUtil.configure(c, logInfo);
    }

}
