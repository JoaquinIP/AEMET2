import Controller.ScheduleTask;

import java.util.Timer;

public class Feeder {
    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.schedule(new ScheduleTask(), 0, 3600000);
    }
}