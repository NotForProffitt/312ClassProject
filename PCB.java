public class PCB {
    //0=NEW, 1=READY, 2=RUN, 3=WAIT, 4=EXIT
    private int processState;
    private int pid;
    private int priority;
    private boolean isInCriticalSector;
    private int ppid;
    private boolean isActiveProcess = false;
    private String boardMsg;

    public PCB(int state, int pid) {
        this.processState = state;
        this.pid = pid;
        this.ppid = -1;
        this.isInCriticalSector = false;
        this.priority = -1;
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
    public void ppidSet(int ppid) {
        this.ppid = ppid;
    }
    public int ppidGet() {
        return this.ppid;
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

    public boolean getActive() {
        return this.isActiveProcess;
    }

    public void setActive(boolean isAct) {
        this.isActiveProcess = isAct;
    }

    //sets the shared memory message
    public void setSharedMessage(String sharedMessage) {
        CPU.sharedMessage = sharedMessage+"::PID"+this.pid;
    }

    public String getSharedMessage() {
        return CPU.sharedMessage;
    }

    //appends pid to message as a signature
    public void sendBoardMessage(String msg) {
        CPU.messageBoard.add(msg+"::PID"+this.pid);
    }

    public void getMessageBoard(int i) {
        this.boardMsg = CPU.messageBoard.get(i);
    }
}