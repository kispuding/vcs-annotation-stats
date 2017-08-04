package sk.mary.vcsannotstats.utils;

import sk.mary.vcsannotstats.PluginConstants;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ScriptCreator {

    private String os; //operating system
    private String fileNameWithExtension;

    public ScriptCreator(String os){
        this.os = convertOs(os);
        this.fileNameWithExtension = getFileNameWithExtension(this.os);
    }

    public void createAndExecuteScript(String basepath, String filename , String command){
        try {
            BufferedWriter bufferedWriter = getScriptWriter(basepath);
            bufferedWriter.write(command);
            bufferedWriter.close();
            Path outputpath;
            try {
                outputpath = Files.createFile(Paths.get(basepath + File.separator + "_stats" + File.separator + filename + ".blame"));
            } catch (FileAlreadyExistsException faee) {
                outputpath = Paths.get(basepath + File.separator + "_stats" + File.separator + filename);
            }
            Process p = new ProcessBuilder(basepath + File.separator + fileNameWithExtension).redirectOutput(new File(outputpath.toString())).directory(new File(basepath)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedWriter getScriptWriter(String basePath) throws IOException {
        String filePath = basePath + File.separator + PluginConstants.SCRIPT_FILENAME;
        return PluginConstants.WINDOWS_OS.equals(os) ? new BufferedWriter(new FileWriter(filePath + PluginConstants.WINDOWS_EXTENSION)) :
            new BufferedWriter(new FileWriter(filePath + PluginConstants.LINUX_EXTENSION));
    }

    private String convertOs(String os){
        return os.toUpperCase().contains(PluginConstants.WINDOWS_OS) ? PluginConstants.WINDOWS_OS : PluginConstants.LINUX_OS;
    }

    private String getFileNameWithExtension(String os){
        return os.toUpperCase().contains(PluginConstants.WINDOWS_OS) ? PluginConstants.SCRIPT_FILENAME + PluginConstants.WINDOWS_EXTENSION : PluginConstants.SCRIPT_FILENAME + "." + PluginConstants.LINUX_EXTENSION;
    }

}
