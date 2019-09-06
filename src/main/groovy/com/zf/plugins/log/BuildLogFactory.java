package com.zf.plugins.log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class BuildLogFactory {

    Configuration cfg = null;

    public BuildLogFactory(File templeFile) throws IOException {
        cfg = new Configuration(Configuration.VERSION_2_3_22);
        cfg.setDirectoryForTemplateLoading(templeFile);
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    }

    public void buildLog(String templeFileName, ArrayList<String> logs, File logFile) throws IOException, TemplateException {

        Map<String, Object> root = new HashMap<>();
        root.put("logInfos", logs);
        Template temp = cfg.getTemplate(templeFileName);
        FileOutputStream fos = new FileOutputStream(logFile);
        Writer out = new OutputStreamWriter(fos);
        temp.process(root, out);
        out.close();

    }
}
