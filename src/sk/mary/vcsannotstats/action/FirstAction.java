package sk.mary.vcsannotstats.action;

import b.d.h.a.T;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataConstants;
import com.intellij.openapi.project.Project;
import org.codehaus.plexus.util.FileUtils;
import sk.mary.vcsannotstats.utils.ScriptCreator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Predicate;


public class FirstAction extends AnAction {

    public FirstAction() {
        super("My First Action");
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        String basePath = getProjectBasePath(anActionEvent);
        new File(basePath + File.separator + "_stats").mkdirs();

        try {
            Files.find(Paths.get(basePath), Integer.MAX_VALUE,(filePath, fileAttr) -> fileAttr.isRegularFile() ).filter(new Predicate<Path>() {
                @Override
                public boolean test(Path path) {
                    return path.toString().contains("src");
                }
            })
                    .forEach( path -> executeGitCommand(basePath, path.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        cleanup(basePath);
    }

    private String getProjectBasePath(AnActionEvent anActionEvent) {
        Project project = (Project) anActionEvent.getDataContext().getData(DataConstants.PROJECT);
        return project.getBasePath();
    }

    private void executeGitCommand(String basepath, String filepath) {
        String command = String.format("git blame -f %s", filepath);
        ScriptCreator scriptCreator = new ScriptCreator(System.getProperty("os.name"));
        scriptCreator.createAndExecuteScript(basepath, filepath.substring(filepath.lastIndexOf(File.separator)+1, filepath.lastIndexOf(".")), command);
    }

    private void cleanup(String basePath){
        try {
            FileUtils.deleteDirectory(new File(basePath +File.separator + "_stats"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

