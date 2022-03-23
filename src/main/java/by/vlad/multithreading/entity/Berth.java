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
        logger.info("Начало загрузки корабля " + ship.getName());
        Port port = Port.getInstance();
        ship.setShipState(Ship.State.PROCESSING);

        while (!ship.isShipFull()){
            ship.addContainer();
            //delete container from port storage
            port.decrementContainer();

            System.out.println(ship.getName() + ": " + ship.getCurrentContainersValue() + " ------- " + port.getContainerNumber().get());
            try {
                TimeUnit.MILLISECONDS.sleep(new Random().nextInt(MAX_OPERATION_TIME));
            } catch (InterruptedException e) {
                logger.error("Ошибка при загрузке корабля", e);
            }
        }

        ship.setShipState(Ship.State.FINISHED);

        logger.info("Колличество контейнеров на складе порта составляет: " + port.getContainerNumber().get());
        logger.info("Окончание загрузки корабля " + ship.getName() + "колличество груза " + ship.getCurrentContainersValue() + "/" + ship.getMaxContainerValue());
    }

    public void unloadShip(Ship ship){
        logger.info("Начало разгрузки корабля " + ship.getName());
        Port port = Port.getInstance();
        ship.setShipState(Ship.State.PROCESSING);

        while(!ship.isShipEmpty()){
            ship.removeContainer();
            //add container to port storage
            System.out.println(ship.getName() + ": " + ship.getCurrentContainersValue() + " ------- " + port.getContainerNumber().get());
            port.incrementContainer();
            try {
                TimeUnit.MILLISECONDS.sleep(new Random().nextInt(MAX_OPERATION_TIME));
            } catch (InterruptedException e) {
                logger.error("Ошибка при разгрузке корабля", e);
            }
        }

        ship.setShipState(Ship.State.FINISHED);

        logger.info("Колличество контейнеров на складе порта составляет: " + port.getContainerNumber().get());
        logger.info("Окончание разгрузки корабля " + ship.getName() + "колличество груза " + ship.getCurrentContainersValue() + "/" + ship.getMaxContainerValue());
    }
}