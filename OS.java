import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OS {
    //used to store processes as they are created
    public static List<ProcessClass> processList = new ArrayList<>();
    //used to store processes after they have been sorted by scheduler call
    public static List<ProcessClass> processListScheduled = new ArrayList<>();
    public static List<PCB> pcbList = new ArrayList<>();
    public static boolean criticalSector = false;
    public static char cont ='y';


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
        System.out.println("Which scheduler should be used?\n1 - SJF\n2 - Priority (NOT IMPLEMENTED)");
        System.out.println("====================");
        int schedulerUse = in.nextInt();


        for (int i = 0; i < processAmount; i++) {
            processList.add(Instruction.makeProcesses(processType));
        }

        switch (schedulerUse) {
            case 1:
                SchedulerSJF.schedule(processList);
                break;
            case 2:
                SchedulerSJF.schedule(processList);
                System.out.println("Priority Scheduling not implemented. Using SJF...");
                break;
        }

        for (int i = 0; i < processListScheduled.size(); i++) {
            processListScheduled.get(i).createPCB();
            System.out.println("Sorted Schedule Order: " + processListScheduled.get(i).getPid() + ", with burst time of: " + processListScheduled.get(i).getBurstTime());
            pcbList.get(i).stateSet(1);
        }

        System.out.println("Executing processes...");
        for (int i = 0; i < processList.size(); i++) {
            System.out.println("    Executing process " + pcbList.get(i).pidGet() + "...");
            pcbList.get(i).stateSet(2);
            processList.get(i).execute(i);
            System.out.println("    Process ID " + pcbList.get(i).pidGet() + " execution complete.");
            pcbList.get(i).stateSet(4);
        }
        System.out.println("Process execution complete");
        System.out.println("Simulate more processes? Y/N");
        cont = in.next().charAt(0);

        if(cont == 'y') {
            processList.clear();
            processListScheduled.clear();
            pcbList.clear();
            SchedulerSJF.tempArr.clear();
            Instruction.processList.clear();
            ProcessClass.setUniquePid(0);
        }
        else if(cont == 'n') {
            System.out.println("Simulation Complete!");
        }
    }
        }

        //not used because no process should be interrupted in the middle of execute in phase 1 due to SJF scheduler
        public static boolean getCriticalSector () {
            return criticalSector;
        }

        public static void setCriticalSector (boolean sect){
            criticalSector = sect;
        }
}
