package by.vlad.multithreading.main;

import by.vlad.multithreading.entity.Berth;
import by.vlad.multithreading.entity.PortTimerTask;
import by.vlad.multithreading.entity.Ship;
import by.vlad.multithreading.exception.CustomException;
import by.vlad.multithreading.parser.impl.ApplicationDataParserImpl;
import by.vlad.multithreading.reader.impl.CustomDataReaderImpl;

import java.util.List;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.schedule(new PortTimerTask(), Berth.MAX_OPERATION_TIME, Berth.MAX_OPERATION_TIME * 2);

        CustomDataReaderImpl reader = CustomDataReaderImpl.getInstance();
        ApplicationDataParserImpl parser = ApplicationDataParserImpl.getInstance();

        List<String> lines;
        List<Ship> ships;

        try {
            lines = reader.loadDataFromFile("ship_data.txt");
            ships = parser.parseShipData(lines);

            ExecutorService executor = Executors.newFixedThreadPool(ships.size());

            ships.forEach(executor::execute);

            TimeUnit.SECONDS.sleep(3);
            executor.shutdown();
            timer.cancel();
        } catch (CustomException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}