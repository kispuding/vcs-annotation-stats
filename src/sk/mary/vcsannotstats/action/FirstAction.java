package sk.mary.vcsannotstats.action;

import com.intellij.codeInsight.template.postfix.templates.SoutPostfixTemplate;
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
import java.util.function.BiConsumer;
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
                    .forEach(path -> blameFilesWithRelevantLines.put(path.toString(), blameProcessor.deleteUnnecessaryLines(executeGitCommand(basePath, path.toString()))));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(countFiles());
        System.out.println(countLinesOfCode());
//        blameFilesWithRelevantLines.forEach(new BiConsumer<String, List<String>>() {
//            @Override
//            public void accept(String s, List<String> strings) {
//
//                strings.forEach(line ->
//                    System.out.println(blameProcessor.parseNameFromLine(line) + " " +  blameProcessor.parseCommitFromLine(line) + " " + blameProcessor.parseDateFromLine(line)));
//            }
//        });
//        String str1 = "084bd86a (jkr  2017-07-17 08:07:29 +0200   1) package sk.eea.jira.pyro;";
//        String str2 = "47809dab src/sk/mary/vcsannotstats/utils/ScriptCreator.java (mary 2017-08-04 09:24:41 +0200  1) package sk.mary.vcsannotstats.utils;";
//        System.out.println(blameProcessor.parseNameFromLine(str1));
//        System.out.println(blameProcessor.parseNameFromLine(str2));
//        System.out.println(blameProcessor.parseCommitFromLine(str1));
//        System.out.println(blameProcessor.parseCommitFromLine(str2));
//        System.out.println(blameProcessor.parseDateFromLine(str1));
//        System.out.println(blameProcessor.parseDateFromLine(str2));

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

    private String countFiles(){
        return String.valueOf(blameFilesWithRelevantLines.size());
    }

    private String countContributors(){
        return null;
    }

    private String countLinesOfCode(){
        return String.valueOf(blameFilesWithRelevantLines.values().stream().mapToInt(List::size).sum());
    }
}

