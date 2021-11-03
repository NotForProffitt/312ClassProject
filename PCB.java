public class PCB extends ProcessClass {
    //0=NEW, 1=READY, 2=RUN, 3=WAIT, 4=EXIT
    private int processState;
    private int pid;
    private int priority;
    private boolean isInCriticalSector;

    public PCB(int state, int pid) {
        this.processState = state;
        this.pid = pid;
        this.isInCriticalSector = false;
    }

    public void stateSet(int state) {
        this.processState = state;
    }

    public int stateGet() {
        return this.processState;
    }

    public void pidSet(int pid) {
        this.pid = pid;
    }
    public int pidGet() {
        return this.pid;
    }

    public void prioritySet(int priority) {
        this.priority = priority;
    }

    public int priorityGet() {
        return this.priority;
    }

    public void setInCriticalSector(boolean input) {
        this.isInCriticalSector = input;
    }

    public boolean getInCriticalSector() {
        return this.isInCriticalSector;
    }





}