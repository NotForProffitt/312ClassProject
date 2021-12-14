import java.io.FileNotFoundException;
import java.util.Random;
import java.util.concurrent.Semaphore;

import static java.lang.Thread.sleep;

public class ProcessThread implements Runnable {

    private int completedCPUCycles;
    private int completedIOCycles;
    public static String msg;
    ProcessClass p;
    Semaphore semaphore;

    public ProcessThread(ProcessClass processClass, Semaphore sem) {
        this.p = processClass;
        this.semaphore = sem;
    }

    public void processSetup() throws FileNotFoundException, InterruptedException {
        if(p.processPCB.stateGet() == 1) {
            System.out.println("    Executing process " + p.processPCB.pidGet() + "...");
            p.processPCB.stateSet(2);
            OS.registerMem += p.MEM_REQ;
            p.processPCB.setActive(true);
            OS.currentProcesses.add(p);
            processExecute();
            OS.currentProcesses.remove(p);
            OS.registerMem -= p.MEM_REQ;
            OS.execProcCount++;
            System.out.println("    Process ID " + p.processPCB.pidGet() + " execution complete.");
            p.processPCB.stateSet(4);
            p.processPCB.setActive(false);
            OS.availableMem += p.MEM_REQ;
        }
    }

    public void processExecute() throws InterruptedException, FileNotFoundException {
        for(int i = 0; i < p.getInstructions().length; i ++) {
            if (p.getInstructions()[i][0].equals("S") && p.getInstructions()[i][1].equals("0")) {
                semaphore.acquire();
                System.out.println("        Process " + p.processPCB.pidGet() + " entered critical state.");
            }

            if (p.getInstructions()[i][0].equals("S") && p.getInstructions()[i][1].equals("1")) {
                semaphore.release();
                System.out.println("        Process " + p.processPCB.pidGet() + " exited critical state.");
            }

            if (p.getInstructions()[i][0].equals("C")) {
                p.processPCB.stateSet(2);
                processCalc(Integer.parseInt(p.getInstructions()[i][1]));
                if (p.processPCB.ppidGet() != -1)
                    System.out.println("        Process " + p.processPCB.pidGet() + " With PPID: " + p.processPCB.ppidGet() + " State: " + p.processPCB.stateGet());
                else
                    System.out.println("        Process " + p.processPCB.pidGet() + " State: " + p.processPCB.stateGet());
            }

            if (p.getInstructions()[i][0].equals("I")) {
                p.processPCB.stateSet(3);
                processIO(Integer.parseInt(p.getInstructions()[i][1]));
                if (p.processPCB.ppidGet() != -1)
                    System.out.println("        Process " + p.processPCB.pidGet() + " With PPID: " + p.processPCB.ppidGet() + " State: " + p.processPCB.stateGet());
                else
                    System.out.println("        Process " + p.processPCB.pidGet() + " State: " + p.processPCB.stateGet());
            }

            if (p.getInstructions()[i][0].equals("F")) {
                p.processPCB.stateSet(2);
                processFork(p.getType(), i);
            }
        }
    }

    private void processCalc(int cycles) throws InterruptedException {
        for (int i = 0; i <= cycles; i++) {
            this.completedCPUCycles++;
            sleep(25);
            if(roulette() == 5) {
                interruptCall();
            }
        }
    }

    private void processIO(int cycles) throws InterruptedException {
        for (int i = 0; i <= cycles; i++) {
            this.completedIOCycles++;
            sleep(40);
        }
    }

    private void processFork(String type, int i) throws FileNotFoundException, InterruptedException {
        sleep(40);
        for(int j = 0; j < OS.processListScheduled.size(); j++) {
            if(OS.processListScheduled.get(j).processPCB.pidGet() == p.processPCB.pidGet()) {
                OS.processListScheduled.add(j+1, Instruction.makeProcesses(type, i+1));
                OS.processListScheduled.get(j+1).processPCB.ppidSet(p.processPCB.pidGet());
                OS.processListScheduled.get(j+1).processPCB.prioritySet(p.processPCB.priorityGet());
                if (OS.processListScheduled.get(j+1).MEM_REQ <= OS.availableMem) {
                    OS.availableMem -= OS.processListScheduled.get(j+1).MEM_REQ;
                    OS.processListScheduled.get(j+1).processPCB.stateSet(1);
                }
            }
        }
        OS.updatePriority();
        System.out.println("        Created Fork Process");
    }

    private int roulette() {
        Random r = new Random();
        int low = 1;
        int high = 100;
        return r.nextInt(high-low) + low;
    }

    private void interruptCall() throws InterruptedException {
        p.processPCB.stateSet(3);
        System.out.println("            Process Interrupt");
        sleep(75);
        p.processPCB.stateSet(2);
    }

    @Override
    public void run() {
        try {
            processSetup();
        } catch (FileNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
