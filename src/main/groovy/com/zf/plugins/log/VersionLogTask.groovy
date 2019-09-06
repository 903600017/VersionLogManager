package com.zf.plugins.log

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction

public class VersionLogTask extends DefaultTask {

    private final static String LOGS_FILE_NAME = "VersionLog.json"


    def checkVersionLogConfig() {

        VersionLogConfig vLogConfig = VersionLogConfig.getConfig(project);

        if (vLogConfig.vLogWorkDir == null) {
            throw new GradleException("必须配置vLogWorkDir参数")
        }

        if (!vLogConfig.vLogWorkDir.exists()) {
            throw new GradleException("vLogWorkDir 文件夹不存在")
        }

        if (!vLogConfig.vLogWorkDir.isDirectory()) {
            throw new GradleException("vLogWorkDir 文件夹不存在")
        }

        if (vLogConfig.logInfo == null) {
            throw new GradleException("必须配置logInfo参数")
        }

        if (vLogConfig.logInfo.versionCode == null) {
            throw new GradleException("必须配置versionCode参数")
        }

        if (vLogConfig.logInfo.logFile == null) {
            throw new GradleException("必须配置 logFile 参数")
        }

        if (!vLogConfig.logInfo.logFile.exists()) {
            throw new GradleException("logFile 文件不存在")
        }

        if (!vLogConfig.logInfo.logFile.isFile()) {
            throw new GradleException("logFile 文件不存在")
        }
    }

    def getCurrentVersionLog(VersionLogDto versionLogDto) {
        def time = System.currentTimeMillis()
        def currentLogs = getUpdateContent(versionLogDto.logFile)
        def log = new VersionLog();
        log.versionName = versionLogDto.versionName
        log.versionCode = versionLogDto.versionCode
        log.addTime = time
        log.updateTime = time
        log.logs.addAll(currentLogs)
        return log

    }

    def static List<String> getUpdateContent(File logFile) {
        ArrayList<String> logs = new ArrayList<>()
        logFile.each { line ->
            if (line.trim().length() != 0) {
                logs.add(line)
            }
        }
        return logs
    }


    @TaskAction
    public void run() throws Exception {
        checkVersionLogConfig()
        VersionLogConfig versionLogConfig = VersionLogConfig.getConfig(project)
        File vLogFile = new File(versionLogConfig.vLogWorkDir, LOGS_FILE_NAME)
        VersionLogManager versionLogManager = new VersionLogManager(vLogFile)
        VersionLog versionLog = getCurrentVersionLog(versionLogConfig.logInfo)
        versionLogManager.add(versionLog)
    }

}
