package pomo;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.openapi.wm.WindowManager;

public class ProjectManagerListener implements com.intellij.openapi.project.ProjectManagerListener {
    StatusBarWidget widget;
    ProjectManagerListener(StatusBarWidget wid){
        widget = wid;
    }


    class run implements Runnable{
        Project project;
        run(Project project){
            this.project = project;
        }
        @Override
        public void run() {
            StatusBar statusBar = WindowManager.getInstance().getStatusBar(project);
            statusBar.addWidget(widget, "before Position", project);
            System.out.println(statusBar);
        }
    }
    public void projectOpened(Project project) {

        ApplicationManager.getApplication().invokeLater(new run(project));
        StatusBar statusBar = WindowManager.getInstance().getStatusBar(project);

        System.out.println("project " + project.getName() + " opened");

        System.out.println(statusBar);
        //statusBar.addWidget(widget);
    }

    public void projectClosing(Project project) {}

    public void projectClosed(Project project) {}

    public void projectClosingBeforeSave(Project project) {}
}
