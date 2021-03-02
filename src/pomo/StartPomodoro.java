package pomo;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.ApplicationManager;

public class StartPomodoro extends AnAction{

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        System.out.println(anActionEvent);
        PomodoroComponent com = ApplicationManager.getApplication().getComponent(PomodoroComponent.class);
        com.chengeState();
    }
}
