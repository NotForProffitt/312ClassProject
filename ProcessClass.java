public class ProcessClass extends Thread {
    private static int uniquePid = 0;
    private int createdTime = 0;
    private int currentIndex = 0;
    private int burstTime;
    private String[][] instructions;
    private int pid;
    private int completedCPUCycles;
    private int completedIOCycles;

    ProcessClass(int state, String[][] instruct) {
        //0=NEW, 1=READY, 2=RUN, 3=WAIT, 4=EXIT
        pid = uniquePid;
        uniquePid++;
        //this.state = state;
        this.instructions = instruct;



        for (String[] instruction : instructions) {
            if (instruction[0].equals("C"))
                burstTime = burstTime + Integer.parseInt(instruction[1]);
        }
    }

    //creating associated PCB
    public void createPCB() {
        PCB pcb = new PCB(0, pid);
        OS.pcbList.add(pcb);
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

    public String[][] getInstructions() {
        return instructions;
    }

    public void execute(int PCBIndex) throws InterruptedException {
        //PCB set state to running
        for (int i = 0; i < instructions.length; i++) {
            if ((OS.criticalSector) && OS.pcbList.get(PCBIndex).getInCriticalSector()) {
                if (instructions[i][0].equals("S")) {
                    OS.setCriticalSector(!OS.getCriticalSector());
                    OS.pcbList.get(PCBIndex).setInCriticalSector(!OS.getCriticalSector());
                }
                if (instructions[i][0].equals("C")) {
                    cycle(Integer.parseInt(instructions[i][1]));
                    OS.pcbList.get(PCBIndex).stateSet(2);
                    System.out.println("    Process "+getPid()+" State: "+OS.pcbList.get(PCBIndex).stateGet());

                }

                if (instructions[i][0].equals("I")) {
                    OS.pcbList.get(PCBIndex).stateSet(3);
                    processIO(Integer.parseInt(instructions[i][1]));
                    System.out.println("    Process "+getPid()+" State: "+OS.pcbList.get(PCBIndex).stateGet());
                }
                if (instructions[i][0].equals("F")) processFork();

            }
        }
    }

    private void cycle(int cycles) throws InterruptedException {
        for (int i = 0; i <= cycles; i++) {
            this.completedCPUCycles++;
            sleep(25);
        }
    }

    private void processIO(int cycles) throws InterruptedException {
        for (int i = 0; i <= cycles; i++) {
            this.completedIOCycles++;
            sleep(40);
        }
    }

    //TODO stub
    private void processFork() {

    }

    ProcessClass() {
    }
}