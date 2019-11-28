package com.zf.plugins.log

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction

public class BuildLogTask extends DefaultTask {

    String templeFileName

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
    }

    @TaskAction
    public void run() throws Exception {

        checkVersionLogConfig()

        VersionLogConfig vLogConfig = VersionLogConfig.getConfig(project);

        File templeFile = new File(vLogConfig.vLogWorkDir, Constant.TEMPLE_DIR_NAME);
        BuildLogFactory buildLogFactory = new BuildLogFactory(templeFile)

        File vLogFile = new File(vLogConfig.vLogWorkDir, Constant.LOG_SAVE_FILE_NAME)
        VersionLogManager versionLogManager = new VersionLogManager(vLogFile)

        def log = versionLogManager.getAllLog()
        if (log == null) {
            log = new ArrayList<VersionLog>()
        }
        def file = new File(vLogConfig.vLogWorkDir, templeFileName)
        if (file.exists()) {
            def delete = file.delete();
            if (!delete) {
                throw new GradleException("删除已存在生成的日志文件(${file.absolutePath})失败,请手动删除后再生成")
            }
        }
        buildLogFactory.buildLog(templeFileName, log, file)
        logger.quiet(file.absolutePath)
    }

}
