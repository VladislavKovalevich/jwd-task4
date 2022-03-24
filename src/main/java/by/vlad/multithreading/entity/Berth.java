package by.vlad.multithreading.entity;

import by.vlad.multithreading.util.IdGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Berth {
    private static final Logger logger = LogManager.getLogger();
    public static final int MAX_OPERATION_TIME = 200;
    private final long id;

    public Berth(){
        id = IdGenerator.getInstance().generateId();
    }

    public long getId() {
        return id;
    }

    public void loadShip(Ship ship){
        logger.info("Ship loading start, ship №" + ship.getName());
        Port port = Port.getInstance();
        ship.setShipState(Ship.State.PROCESSING);

        while (!ship.isShipFull()){
            ship.addContainer();
            port.decrementContainer();

            try {
                TimeUnit.MILLISECONDS.sleep(new Random().nextInt(MAX_OPERATION_TIME));
            } catch (InterruptedException e) {
                logger.error(Thread.currentThread().getName() + " was interrupted.", e);
                Thread.currentThread().interrupt();
            }
        }

        ship.setShipState(Ship.State.FINISHED);

        logger.info("The number of containers in the port warehouse is: " + port.getContainerNumber().get());
        logger.info("Ship loading finished, ship №" + ship.getName() + ", number of containers " + ship.getCurrentContainersValue() + "/" + ship.getMaxContainerValue());
    }

    public void unloadShip(Ship ship){
        logger.info("Ship uploading start, ship №" + ship.getName());
        Port port = Port.getInstance();
        ship.setShipState(Ship.State.PROCESSING);

        while(!ship.isShipEmpty()){
            ship.removeContainer();
            port.incrementContainer();

            try {
                TimeUnit.MILLISECONDS.sleep(new Random().nextInt(MAX_OPERATION_TIME));
            } catch (InterruptedException e) {
                logger.error(Thread.currentThread().getName() + " was interrupted.", e);
                Thread.currentThread().interrupt();
            }
        }

        ship.setShipState(Ship.State.FINISHED);

        logger.info("The number of containers in the port warehouse is: " + port.getContainerNumber().get());
        logger.info("Ship uploading finished, ship №" + ship.getName() + ", number of containers " + ship.getCurrentContainersValue() + "/" + ship.getMaxContainerValue());
    }
}