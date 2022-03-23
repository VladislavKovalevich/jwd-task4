package by.vlad.multithreading.entity;

public class Ship extends Thread{
    private final String name;
    private final int maxContainerValue;
    private final ShipTypeEnum type;
    private int currentContainersValue;
    private State state;

    public enum State{
        NEW, PROCESSING, FINISHED
    }

    public Ship(String name, int maxContainerValue, int currentContainersValue, ShipTypeEnum type) {
        super("Thread - " + name);
        this.name = "Thread - " + name;
        this.maxContainerValue = maxContainerValue;
        this.currentContainersValue = currentContainersValue;
        this.state = State.NEW;
        this.type = type;
    }

    public boolean isShipFull(){
        return this.currentContainersValue == this.maxContainerValue;
    }

    public boolean isShipEmpty(){
        return this.currentContainersValue == 0;
    }

    public int getMaxContainerValue() {
        return maxContainerValue;
    }

    public int getCurrentContainersValue() {
        return currentContainersValue;
    }

    public void addContainer(){
        currentContainersValue++;
    }

    public void removeContainer(){
        currentContainersValue--;
    }

    public State getShipState() {
        return state;
    }

    public void setShipState(State state) {
        this.state = state;
    }

    @Override
    public void run() {
        Port port = Port.getInstance();
        Berth berth = port.getBerth();

        if (this.type == ShipTypeEnum.LOAD) {
            berth.loadShip(this);
        } else {
            berth.unloadShip(this);
        }

        port.returnBerth(berth);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ship ship = (Ship) o;

        if (maxContainerValue != ship.maxContainerValue) return false;
        if (currentContainersValue != ship.currentContainersValue) return false;
        if (!name.equals(ship.name)) return false;
        return type == ship.type;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + maxContainerValue;
        result = 31 * result + currentContainersValue;
        result = 31 * result + type.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return new StringBuilder("Ship{")
                .append("name='").append(name).append('\'')
                .append(", maxContainerValue=").append(maxContainerValue)
                .append(", currentContainersValue=").append(currentContainersValue)
                .append(", type=").append(type)
                .append('}').toString();
    }
}