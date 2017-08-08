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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.HashMap;
import java.util.function.Consumer;


public class FirstAction extends AnAction {

    private HashMap<String, List<String>> blameFilesWithRelevantLines = new HashMap<>();

    public FirstAction() {
        super("My First Action");
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        String basePath = getProjectBasePath(anActionEvent);
        BlameProcessor blameProcessor = new BlameProcessor();

        new File(basePath + File.separator + "_stats").mkdirs();

        try {
            Files.find(Paths.get(basePath), Integer.MAX_VALUE, (filePath, fileAttr) -> fileAttr.isRegularFile()).filter(path -> path.toString().contains("src"))
                    .forEach(new Consumer<Path>() {
                        @Override
                        public void accept(Path path) {
                            String file = executeGitCommand(basePath, path.toString());
                            List<String> lines = blameProcessor.deleteUnnecessaryLines(file);


                            blameFilesWithRelevantLines.put(path.toString(), lines);
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Files: " + countFiles());
        System.out.println("Lines: " + countLinesOfCode());
        System.out.println("Contributors: " + countContributors(blameProcessor) + " " +blameProcessor.getContributors(blameFilesWithRelevantLines.values()));
        System.out.println("Commits: " + countCommits(blameProcessor) + " " + blameProcessor.getCommits(blameFilesWithRelevantLines.values()));
        System.out.println("Uncommited lines: " + blameProcessor.countUncommitted(blameFilesWithRelevantLines.values()));

//        cleanup(basePath);
    }

    private String getProjectBasePath(AnActionEvent anActionEvent) {
        Project project = (Project) anActionEvent.getDataContext().getData(DataConstants.PROJECT);
        return project.getBasePath();
    }

    private String executeGitCommand(String basepath, String filepath) {
        String command = String.format("git blame -f %s", filepath);
        ScriptCreator scriptCreator = new ScriptCreator(System.getProperty("os.name"));
        return scriptCreator.createAndExecuteScript(basepath, filepath.substring(filepath.lastIndexOf(File.separator) + 1, filepath.lastIndexOf(".")), command);
    }

    private void cleanup(String basePath) {
        File dir = new File(basePath + File.separator + "_stats");
        try {
            FileUtils.cleanDirectory(dir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String countFiles() {
        return String.valueOf(blameFilesWithRelevantLines.size());
    }

    private String countContributors(BlameProcessor blameProcessor) {
        return String.valueOf(blameProcessor.getContributors(blameFilesWithRelevantLines.values()).size());
    }

    private String countLinesOfCode() {
        return String.valueOf(blameFilesWithRelevantLines.values().stream().mapToInt(List::size).sum());
    }

    private String countCommits(BlameProcessor blameProcessor){
        return String.valueOf(blameProcessor.getCommits(blameFilesWithRelevantLines.values()).size());
    }

}

