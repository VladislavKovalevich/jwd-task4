package by.vlad.multithreading.entity;

import by.vlad.multithreading.exception.CustomException;
import by.vlad.multithreading.parser.impl.DataParserImpl;
import by.vlad.multithreading.reader.impl.DataReaderImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Port {
    private static final Logger logger = LogManager.getLogger();

    private static final String CONTAINERS = "CONTAINERS";
    private static final String MAX_CONTAINERS = "MAX_CONTAINERS";
    private static final String COUNT_OF_BERTHS = "COUNT_OF_BERTHS";

    private static Port instance = null;
    private static ReentrantLock lock = new ReentrantLock();

    private int maxContainersNumber;
    private AtomicInteger containerNumber;

    private ArrayDeque<Berth> berthPool = new ArrayDeque<>();
    private ArrayDeque<Condition> waitingQueue = new ArrayDeque<>();

    private Port(){
        List<String> lines;
        try {
            lines = DataReaderImpl.getInstance().loadDataFromFile("port.txt");
            Map<String, Integer> map = DataParserImpl.getInstance().parsePortParams(lines);

            maxContainersNumber = map.get(MAX_CONTAINERS);
            containerNumber = new AtomicInteger(map.get(CONTAINERS));

            for (int i = 0; i < map.get(COUNT_OF_BERTHS); i++) {
                berthPool.add(new Berth());
            }
        } catch (CustomException e) {
            logger.error("data read error ", e);
        }
    }

    public static Port getInstance(){
        lock.lock();
        try {
            if (instance == null){
                instance = new Port();
            }
        } finally {
            lock.unlock();
        }

        return instance;
    }

    public Berth getBerth() {
        lock.lock();
        Berth berth = null;

        try {
            if (berthPool.isEmpty()) {
                Condition condition = lock.newCondition();

                waitingQueue.addLast(condition);
                condition.await();
            }

            berth = berthPool.removeFirst();
            logger.info("Berth id=" + berth.getId() + " was allocated for the ship " + Thread.currentThread().getName());
        } catch (InterruptedException e) {
            logger.error("Cannot getting berth for this ship", e);
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }

        return berth;
    }

    public void returnBerth(Berth berth){
        lock.lock();
        try {
            berthPool.push(berth);
            logger.info("Berth id=" + berth.getId() + " was released after loading/uploading the ship " + Thread.currentThread().getName());

            Condition condition = waitingQueue.pollFirst();

            if (condition != null){
                condition.signal();
            }
        }finally {
            lock.unlock();
        }
    }

    public int getMaxContainersNumber() {
        return maxContainersNumber;
    }

    public AtomicInteger getContainerNumber() {
        return containerNumber;
    }

    public void incrementContainer(){
        containerNumber.incrementAndGet();
    }

    public void decrementContainer(){
        containerNumber.decrementAndGet();
    }

    public void refreshPortStorage(){
        logger.info("changing the number of containers ");
        int current = containerNumber.get();

        if (current <= 0.3 * maxContainersNumber || current >= 0.8 * maxContainersNumber){
            containerNumber.set(maxContainersNumber / 2);
            logger.info("The current number of containers has changed: " + containerNumber);
        }
    }
}