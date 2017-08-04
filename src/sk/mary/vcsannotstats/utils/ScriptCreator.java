package sk.mary.vcsannotstats.utils;

import sk.mary.vcsannotstats.PluginConstants;

import java.io.*;

public class ScriptCreator {

    private String os; //operating system

    public ScriptCreator(String os){
        this.os = convertOs(os);
    }

    public BufferedWriter getScriptWriter(String basePath) throws IOException {
        String filePath = basePath + File.separator + PluginConstants.SCRIPT_FILENAME;
        return PluginConstants.WINDOWS_OS.equals(os) ? new BufferedWriter(new FileWriter(filePath + PluginConstants.WINDOWS_EXTENSION)) :
            new BufferedWriter(new FileWriter(filePath + PluginConstants.LINUX_EXTENSION));
    }

    private String convertOs(String os){
        return os.toUpperCase().contains(PluginConstants.WINDOWS_OS) ? PluginConstants.WINDOWS_OS : PluginConstants.LINUX_OS;
    }

}
