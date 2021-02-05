package pomo;

import com.intellij.openapi.wm.CustomStatusBarWidget;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.util.Consumer;
import java.awt.event.MouseAdapter;
import org.jetbrains.annotations.Nullable;


import javax.swing.*;
import java.awt.event.MouseEvent;
import java.time.*;

class  PomodorWidget implements CustomStatusBarWidget {
    Instant startTime;
    Instant stopTime;
    StatusBar statusBar;
    int pomodorosAmaunt = 0;
    Duration runDuration = Duration.ofMinutes(25);
    Duration breakDuration = Duration.ofMinutes(5);
    Duration longBreakDuration = Duration.ofMinutes(15);
    int pomodorosToLongBreack = 2;

    boolean isBreak = false;
    boolean isFinish = true;
    private JLabel myLabel = new JLabel("00:00");
    private Mode mode = Mode.Stop;

    PomodorWidget(){
        System.out.println("widget start");
        startTime = Instant.now();
        myLabel.addMouseListener(new Mouse(this));
    }

    @Override
    public JComponent getComponent() {
        return myLabel;
    }

    @Override
    public String ID() {
        return "pomodoroWidget";
    }


    public class presentation implements WidgetPresentation{

        @Nullable
        @Override
        public String getTooltipText() {
            return "TooltipText";
        }

        @Nullable
        @Override
        public Consumer<MouseEvent> getClickConsumer() {
            return null;
        }
    }
    @Override
    public WidgetPresentation getPresentation(PlatformType platformType) {
        return new presentation();
    }

    @Override
    public void install(StatusBar statusBar) {
        this.statusBar = statusBar;
    }

    @Override
    public void dispose() {

    }


    class Mouse extends MouseAdapter{
        PomodorWidget widget;

        Mouse (PomodorWidget widget){
            this.widget = widget;
        }
        @Override
        public void mouseClicked(MouseEvent e) {
            //System.out.println("click");
            widget.chengeMod();
        }
        //void mouseEntered(e: MouseEvent?) { statusBar.info = statusBarTooltip(model) }
        //override fun mouseExited(e: MouseEvent?) { statusBar.info = "" }
    }

    public void onTime(Instant time) {
        switch (mode){
            case Run:{
                //System.out.println(Period.parse(startTime.toString()).toString());
                //myLabel.setText(time.toString());
                Duration duration = Duration.ofMillis(time.minusMillis(startTime.toEpochMilli()).toEpochMilli());

                myLabel.setText("Pomodoro "+ (pomodorosAmaunt + 1) +" "+
                        (runDuration.toMinutes() - duration.toMinutes()<11?"0":"") +
                        String.valueOf(runDuration.toMinutes() - duration.toMinutes() - (((runDuration.toSeconds() - duration.toSeconds()) % 60) > 0 ? 1:0)) +
                        ":" + (((runDuration.toSeconds() - duration.toSeconds()) % 60) < 10?"0":"") + String.valueOf((runDuration.toSeconds() - duration.toSeconds()) % 60));
                if (runDuration.toMinutes() - duration.toMinutes() <= 0) {
                    //Messages.showInfoMessage("Time to breack","Pomodoro");
                    mode = Mode.Break;
                    pomodorosAmaunt++;
                    startTime = Instant.now();
                }
                break;
            }
            case Stop:{

                break;
            }
            case Break:{
                Duration duration = Duration.ofMillis(time.minusMillis(startTime.toEpochMilli()).toEpochMilli());
                if (pomodorosAmaunt % pomodorosToLongBreack !=0) {
                    myLabel.setText("Time to break " +
                            (breakDuration.toMinutes() - duration.toMinutes() < 11 ? "0" : "") +
                            String.valueOf(breakDuration.toMinutes() - duration.toMinutes() - (((breakDuration.toSeconds() - duration.toSeconds()) % 60) > 0 ? 1 : 0)) +
                            ":" + (((breakDuration.toSeconds() - duration.toSeconds()) % 60) < 10 ? "0" : "") + String.valueOf((breakDuration.toSeconds() - duration.toSeconds()) % 60));
                    if (breakDuration.toMinutes() - duration.toMinutes() <= 0) {
                        mode = Mode.Run;
                        startTime = Instant.now();
                    }
                }else {
                    myLabel.setText("Time to long break " +
                            (longBreakDuration.toMinutes() - duration.toMinutes() < 11 ? "0" : "") +
                            String.valueOf(longBreakDuration.toMinutes() - duration.toMinutes() - (((longBreakDuration.toSeconds() - duration.toSeconds()) % 60) > 0 ? 1 : 0)) +
                            ":" + (((longBreakDuration.toSeconds() - duration.toSeconds()) % 60) < 10 ? "0" : "") + String.valueOf((longBreakDuration.toSeconds() - duration.toSeconds()) % 60));
                    if (longBreakDuration.toMinutes() - duration.toMinutes() <= 0) {
                        mode = Mode.Run;
                        startTime = Instant.now();
                    }
                }
                break;
            }
        }
    }

    private void chengeMod(){
        switch (mode){
            case Run:{
                mode = Mode.Stop;
                stopTime = Instant.now();
                isBreak = false;
                break;
            }
            case Stop:{
                if (isFinish){
                    startTime = Instant.now();
                    isFinish = false;
                } else {
                    startTime = startTime.plusMillis(Instant.now().minusMillis(stopTime.toEpochMilli()).toEpochMilli());
                }
                if (!isBreak) {
                    System.out.println("run");
                    mode = Mode.Run;
                }else {
                    System.out.println("break");
                    mode = Mode.Break;
                }
                 break;
            }
            case Break:{
                stopTime = Instant.now();
                mode = Mode.Stop;
                isBreak = true;
                break;
            }
        }
    }
    public enum Mode {

        /** Pomodoro in progress. */
        Run,
        /** Pomodoro during break. Can only happen after completing a pomodoro. */
        Break,
        /** Pomodoro timer was not started or was stopped during pomodoro or break. */
        Stop
    }
}
