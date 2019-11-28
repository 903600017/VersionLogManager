package com.zf.plugins.log

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction

public class VersionLogTask extends DefaultTask {

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

    /**
     * 生成默认的模板 demo
     * @param versionLogConfig
     * @return
     */
    def createDefaultLogTempleFile(VersionLogConfig versionLogConfig) {
        File templeDir = new File(versionLogConfig.vLogWorkDir, Constant.TEMPLE_DIR_NAME)
        if (!templeDir.exists()) {
            templeDir.mkdirs();
        }
        File logDemoTempleFile = new File(templeDir, Constant.DEFAULT_LOG_TEMPLE_NAME);
        if (!logDemoTempleFile.exists()) {
            def defaultLogTemple = Utils.getResourceContent(Constant.DEFAULT_LOG_TEMPLE_NAME);
            logDemoTempleFile.write(defaultLogTemple)
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
        log.extraMap.putAll(versionLogDto.extraMap)
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
        createDefaultLogTempleFile(versionLogConfig);

        File vLogFile = new File(versionLogConfig.vLogWorkDir, Constant.LOG_SAVE_FILE_NAME)
        VersionLogManager versionLogManager = new VersionLogManager(vLogFile)
        VersionLog versionLog = getCurrentVersionLog(versionLogConfig.logInfo)
        versionLogManager.save(versionLog)

        logger.quiet(GSonFactory.instance.GSon.toJson(versionLog))
    }

}
