package by.vlad.multithreading.util;

public class IdGenerator {
    private int id;
    private static IdGenerator instance;

    private IdGenerator(){
        id = 0;
    }

    public static IdGenerator getInstance() {
        if (instance == null) {
            instance = new IdGenerator();
        }

        return instance;
    }

    public int generateId(){
        id = id + 1;
        return id;
    }
}
