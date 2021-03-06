package pomo;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.project.ProjectManager;

import java.time.Duration;
import java.time.Instant;

public class PomodoroComponent implements ApplicationComponent {//TODO: переименовать
    private Timer timer;
    PomodorWidget widget;
    @Override
    public void initComponent() {
        System.out.println(Duration.ofMillis(Instant.now().toEpochMilli()));
        widget = new PomodorWidget();
        ApplicationManager.getApplication().getMessageBus().connect().subscribe(ProjectManager.TOPIC, new pomo.ProjectManagerListener(widget));

        timer = new Timer(widget);
        timer.start();
        //statusBar.addWidget(widget, "before Position");

    }

    public void chengeState(){
        widget.chengeMod();
    }


}
