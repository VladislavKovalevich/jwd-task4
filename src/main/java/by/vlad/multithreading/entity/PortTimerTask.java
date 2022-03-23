package by.vlad.multithreading.entity;

import java.util.TimerTask;

public class PortTimerTask extends TimerTask {

    @Override
    public void run() {
        Port port = Port.getInstance();
        port.refreshPortStorage();
    }
}
