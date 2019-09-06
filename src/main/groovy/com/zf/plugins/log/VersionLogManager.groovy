package com.zf.plugins.log

import com.google.gson.reflect.TypeToken

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class VersionLogManager {

    private File vLogFile

    public VersionLogManager(File vLogFile) {
        this.vLogFile = vLogFile
    }

    public List<VersionLog> getAllLog() {
        if (vLogFile.exists()) {
            Type versionLogListType = new TypeToken<ArrayList<VersionLog>>() {}.getType();
            return GSonFactory.instance.gson.fromJson(vLogFile.newReader("utf-8"), versionLogListType);
        }else{
            return null
        }
    }

    public synchronized void add(VersionLog vLog) {
        def logs = getAllLog()

        if (logs == null) {
            logs = new ArrayList<VersionLog>();
        }
        def oldLog = logs.find { def log ->
            return log.versionCode == vLog.versionCode
        }

        if (oldLog) {
            vLog.addTime = oldLog.addTime
            logs.remove(oldLog)
        }

        logs.add(vLog)

        Collections.sort(logs)

        def jsonString = GSonFactory.instance.gson.toJson(logs);
        vLogFile.write(jsonString)
    }

}
