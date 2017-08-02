package action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class FirstAction extends AnAction{

    public FirstAction(){
        super("My First Action");
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        System.out.println("My first action performed.");
    }
}
