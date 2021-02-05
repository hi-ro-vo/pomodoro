package pomo;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;

import java.time.Instant;


public class Timer {
    PomodorWidget widget;

    Timer(PomodorWidget widget){
        this.widget = widget;
    }

    private boolean stop = false;

    class run implements Runnable{

        @Override
        public void run() {

            while (!stop){

                widget.onTime(Instant.now());

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void start(){
        ApplicationManager.getApplication().executeOnPooledThread( new run());
        ModalityState.any();
    }

}
