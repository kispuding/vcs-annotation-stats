package sk.mary.vcsannotstats.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataConstants;
import com.intellij.openapi.project.Project;
import org.codehaus.plexus.util.FileUtils;
import sk.mary.vcsannotstats.utils.BlameProcessor;
import sk.mary.vcsannotstats.utils.ScriptCreator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.HashMap;


public class FirstAction extends AnAction {

    public FirstAction() {
        super("My First Action");
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        String basePath = getProjectBasePath(anActionEvent);
        BlameProcessor blameProcessor = new BlameProcessor();
        HashMap<String, List<String>> blameFilesWithRelevantLines = new HashMap<>();
        new File(basePath + File.separator + "_stats").mkdirs();

        try {
            Files.find(Paths.get(basePath), Integer.MAX_VALUE, (filePath, fileAttr) -> fileAttr.isRegularFile()).filter(path -> path.toString().contains("src"))
                    .forEach(path -> blameFilesWithRelevantLines.put(path.toString(), blameProcessor.deleteUnnecessaryLines(executeGitCommand(basePath, path.toString()))));
        } catch (IOException e) {
            e.printStackTrace();
        }

//        cleanup(basePath);
    }

    private String getProjectBasePath(AnActionEvent anActionEvent) {
        Project project = (Project) anActionEvent.getDataContext().getData(DataConstants.PROJECT);
        return project.getBasePath();
    }

    private String executeGitCommand(String basepath, String filepath) {
        String command = String.format("git blame -f %s", filepath);
        ScriptCreator scriptCreator = new ScriptCreator(System.getProperty("os.name"));
        return scriptCreator.createAndExecuteScript(basepath, filepath.substring(filepath.lastIndexOf(File.separator)+1, filepath.lastIndexOf(".")), command);
    }

    private void cleanup(String basePath){
        try {
            FileUtils.deleteDirectory(new File(basePath +File.separator + "_stats"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

