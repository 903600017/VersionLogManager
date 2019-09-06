package com.zf.plugins.log

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project

class VersionLogPlugin implements Plugin<Project> {

    public static final String sPluginExtensionName = "vLogConfig";

    @Override
    void apply(Project project) {

        project.extensions.create(sPluginExtensionName, VersionLogConfig);

        project.tasks.create("versionLogManagerHelp", {
            description "帮助"
            group "Version Log"
            doLast {
                def content = Utils.getResourceContent("VersionLogManager_help.txt");
                project.logger.quiet(content)
            }
        })
        createVersionLogTask(project)
    }

    def createVersionLogTask(Project project) {
        project.afterEvaluate {

            project.tasks.create("recordVersionLog", VersionLogTask) {
                description "记录日志"
                group "Version Log"
                doFirst {
                    project.logger.println("开始生成日志")
                }

                doLast {
                    project.logger.println("生成日志完成")
                }
            }

            if (project[sPluginExtensionName] && project[sPluginExtensionName].vLogWorkDir) {
                def templeDir = new File(project[sPluginExtensionName].vLogWorkDir, Constant.TEMPLE_DIR_NAME)
                if (templeDir.exists() || templeDir.isDirectory()) {
                    def files = templeDir.listFiles();
                    if (files != null) {
                        files.each { File templeFile ->

                            def _templeFileNameWithSuffix = templeFile.getName()
                            def _templeFileName = Utils.getFileName(templeFile)

                            project.tasks.create("build${_templeFileName.capitalize()}", BuildLogTask) {
                                description "生成日志"
                                group "Version Log"
                                templeFileName _templeFileNameWithSuffix
                                doFirst {
                                    project.logger.println("开始生成日志")
                                }

                                doLast {
                                    project.logger.println("生成日志完成")
                                }
                            }
                        }
                    }
                }
            }
        }
    }


}

