import java.io.FileNotFoundException;
import java.util.concurrent.Semaphore;

public class CPU {
    private int completedCPUCycles;
    private int completedIOCycles;

    Thread proc = null;
    Thread proc2 = null;
    Semaphore sem = new Semaphore(1);

    public CPU() throws FileNotFoundException, InterruptedException {
    }

    //while there are processes in ready queue: execute all ready processes, realloc memory, and check for more ready processes
    public void processLoop() throws InterruptedException {
        while(readyCheck()) {
            executeProcesses();
            realloc();
        }
    }

    public void executeProcesses() throws InterruptedException {
        //edge case if only a single process is generated
        if(OS.processListScheduled.size() == 1) {
            proc = new Thread(new ProcessThread(OS.processListScheduled.get(0), sem));
            proc.start();
            proc.join();
            proc = null;
        }
        else {
            for (int i = 1; i < OS.processListScheduled.size(); i++) {
                if(OS.processListScheduled.get(i).processPCB.stateGet() == 1)
                    proc = new Thread(new ProcessThread(OS.processListScheduled.get(i), sem));

                if(OS.processListScheduled.get(i-1).processPCB.stateGet() == 1)
                    proc2 = new Thread(new ProcessThread(OS.processListScheduled.get(i-1), sem));

                if(proc != null)
                    proc.start();
                if(proc2 != null)
                    proc2.start();

                if(proc != null)
                    proc.join();
                if(proc2 != null)
                    proc2.join();
                //set threads to null so individual threads can be executed if <2 processes remain
                proc = null;
                proc2 = null;
            }
        }
    }

    //checks all processes for ready state
    public static boolean readyCheck() {
        OS.hasREADY = false;
        for (int i = 0; i < OS.processListScheduled.size(); i++) {
            if(OS.processListScheduled.get(i).processPCB.stateGet() == 1){
                OS.hasREADY = true;
                return true;
            }
        }
        return false;
    }

    //returns memory from executed processes to pool
    public static void realloc() {
        for (int i = 0; i < OS.processListScheduled.size(); i++) {
            if (OS.processListScheduled.get(i).MEM_REQ <= OS.availableMem && OS.processListScheduled.get(i).processPCB.stateGet() != 4) {
                OS.processListScheduled.get(i).processPCB.stateSet(1);
                OS.availableMem -= OS.processListScheduled.get(i).MEM_REQ;
                OS.hasREADY = true;
            }
        }
    }
}
