import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class OS {
    //used to store processes as they are created
    public static List<ProcessClass> processList = new ArrayList<>();
    //used to store processes after they have been sorted by scheduler call
    public static List<ProcessClass> processListScheduled = new ArrayList<>();
    //used to indicate that more processes should be simulated
    public static char cont ='y';
    //used to store generated priority values before they are assigned to processes
    public static ArrayList<Integer> priorityList = new ArrayList<>();
    //denotes maximum system available
    public static final int MAX_MEM = 1024;
    //indicates the current amount of memory available to processes
    public static int availableMem = MAX_MEM;
    //used for checking if all processes have been executed
    public static boolean hasREADY = false;
    //tallies total amount of processes simulated
    public static int execProcCount = 0;
    //checks whether to use SJF or priority scheduling
    public static int schedulerUse;
    //keeps a list of the currently executing processes
    public static List<ProcessClass> currentProcesses = new ArrayList<>();
    //CPU used for threading and execution of processes
    public static CPU cpu;
    static {
        try {
            cpu = new CPU();
        } catch (FileNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        while(cont=='y') {
            Scanner in = new Scanner(System.in);
            System.out.println("====================");
            System.out.println("Which Type of processes should be created?\nLong, Short, Mixed");
            System.out.println("====================");
            String processType = in.nextLine();

            System.out.println("====================");
            System.out.println("How many processes should be spawned?");
            System.out.println("====================");
            int processAmount = in.nextInt();

            System.out.println("====================");
            System.out.println("Which scheduler should be used?\n1 - SJF\n2 - Priority");
            System.out.println("====================");
            OS.schedulerUse = in.nextInt();

            //creates a list of newly generated processes based on user input
            for (int i = 0; i < processAmount; i++) {
                processList.add(Instruction.makeProcesses(processType, -1));
            }

            setPriority();

            //choose the scheduler to use
            switch (schedulerUse) {
                case 1 -> SchedulerSJF.schedule(processList);
                case 2 -> SchedulerPriority.schedule(processList);
            }

            updatePriority();

            //prints details about each process in scheduled order
            for (int i = 0; i < OS.processListScheduled.size(); i++) {
                if(OS.processListScheduled.get(i).MEM_REQ <= availableMem) {
                    OS.processListScheduled.get(i).processPCB.stateSet(1);
                    availableMem -= OS.processListScheduled.get(i).MEM_REQ;
                    OS.hasREADY = true;
                }
                System.out.println("Sorted Schedule Order: " + OS.processListScheduled.get(i).processPCB.pidGet() + ", with burst time of: " + OS.processListScheduled.get(i).getBurstTime() + " and priority of: " + OS.processListScheduled.get(i).processPCB.priorityGet() + " and state: " + OS.processListScheduled.get(i).processPCB.stateGet());
            }

            System.out.println("Executing processes...");

            cpu.processLoop();

            System.out.println("\nProcess execution complete.");
            System.out.println("Total number of simulated processes: " + execProcCount);
            System.out.println("\nSimulate more processes? Y/N");

            cont = in.next().charAt(0);

            //clears all process information to execute OS from beginning
            if(cont == 'y') {
                SchedulerSJF.tempArr.clear();
                ProcessClass.setUniquePid(0);
                processListScheduled.clear();
                priorityList.clear();
                processList.clear();
                execProcCount = 0;
            }
            //exits
            else if(cont == 'n') {
                System.out.println("Simulation Complete!");
            }
        }
    }

    //generates a list of unique priorities and assigns each created process a new priority from the list randomly
    public static void setPriority() {
        for (int i = 1; i < OS.processList.size()+1; i++) {
            priorityList.add(i);
        }
        Collections.shuffle(priorityList);
        for (int i = 0; i < OS.processList.size(); i++) {
            if (OS.processList.get(i).processPCB.priorityGet() == -1) {
                OS.processList.get(i).processPCB.prioritySet(priorityList.get(i));
                OS.processList.get(i).prioritySet(priorityList.get(i));
            }
        }
    }

    public static void updatePriority() {
        for (int i = 0; i < OS.processListScheduled.size(); i++) {
            if (OS.processListScheduled.get(i).processPCB.priorityGet() == -1) {
                OS.processListScheduled.get(i).processPCB.prioritySet(priorityList.get(i));
            }
        }
    }
}