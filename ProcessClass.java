public class ProcessClass {
    private static int uniquePid = 0;
    private int burstTime;
    private String[][] instructions;
    private int pid;
    private int ppid=- 1;
    private int completedCPUCycles;
    private int completedIOCycles;
    private int priority;
    public final int MEM_REQ = Math.floorDiv(burstTime, 4);
    private final String TYPE;
    private boolean isActiveProcess = false;
    public PCB processPCB = new PCB(0,pid);

    ProcessClass(String[][] instruct, String type) {
        //0=NEW, 1=READY, 2=RUN, 3=WAIT, 4=EXIT
        pid = uniquePid;
        processPCB.pidSet(pid);
        uniquePid++;
        processPCB.prioritySet(-1);
        priority = -1;
        instructions = instruct;
        TYPE = type;

        for (String[] instruction : instructions) {
            if (instruction[0].equals("C"))
                burstTime = burstTime + Integer.parseInt(instruction[1]);
        }
    }

    public int getUniquePid() {
        return uniquePid;
    }

    public static void setUniquePid(int uniquePid) {
        ProcessClass.uniquePid = uniquePid;
    }

    public int getPid() {
        return pid;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public int priorityGet() {
        //return processPCB.priorityGet();
        return this.priority;
    }

    public void prioritySet(int priority) {
        this.priority = priority;
    }

    public String getType() {
        return this.TYPE;
    }

    public String[][] getInstructions() {
        return instructions;
    }

    public boolean getActive() {
        return this.isActiveProcess;
    }

    public void setActive(boolean isAct) {
        this.isActiveProcess = isAct;
    }

}