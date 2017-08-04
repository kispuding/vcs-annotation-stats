package sk.mary.vcsannotstats.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataConstants;
import com.intellij.openapi.project.Project;

import java.io.*;


public class FirstAction extends AnAction {

    public FirstAction() {
        super("My First Action");
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        System.out.println("Action performed.");

        String basePath = getProjectBasePath(anActionEvent);
        System.out.println(System.getProperty("os.name"));
        System.out.println(basePath);
        System.out.println("user dir before set: " + System.getProperty("user.dir"));
        System.out.println("user dir set: " + System.setProperty("user.dir",basePath));
        System.out.println("user dir after set: " + System.getProperty("user.dir"));
//        System.setProperty("user.dir", basePath);
        executeGitCommand(basePath);
    }


    private String getProjectBasePath(AnActionEvent anActionEvent) {
        Project project = (Project) anActionEvent.getDataContext().getData(DataConstants.PROJECT);
        return project.getBasePath();
    }

    private void executeGitCommand(String basepath) {
        System.out.println("writing");
        StringBuilder sb = new StringBuilder();
        sb.append("git blame -f " + basepath + File.separator + "src\\sk\\mary\\vcsannotstats\\action\\FirstAction.java");

        try {
//            new File(basepath + File.separator + "test.bat");
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(basepath + File.separator + "test.bat"));
            bufferedWriter.write(sb.toString());
            bufferedWriter.close();
            Process p = new ProcessBuilder(basepath + File.separator + "test.bat").redirectOutput(new File("D:\\blameoutputtt.txt")).directory(new File(basepath)).start();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("writing done");
//        try {
//            Process cmd = new ProcessBuilder("cmd.exe").redirectErrorStream(true).redirectInput(new java.io.File("D:\\input.txt")).redirectOutput(new java.io.File("D:\\output.txt")).start();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }
}

