package action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataConstants;
import com.intellij.openapi.project.Project;

import java.io.IOException;


public class FirstAction extends AnAction {

    public FirstAction() {
        super("My First Action");
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        System.out.println("Action performed.");

        String basePath = getProjectBasePath(anActionEvent);
        System.out.println(basePath);

        executeGitCommand("");
    }


    private String getProjectBasePath(AnActionEvent anActionEvent) {
        Project project = (Project) anActionEvent.getDataContext().getData(DataConstants.PROJECT);
        return project.getBasePath();
    }

    private void executeGitCommand(String fileName) {
        try {
            Process p = Runtime.getRuntime().exec("cmd /c && dir > D:\\log.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

