import java.util.ArrayList;
import java.util.List;

public class SchedulerSJF {
    static ArrayList<int[]> tempArr = new ArrayList<>();
    static int burst = 0;

    public static void schedule(List<ProcessClass> processList) {
       ArrayList<int[]> tempArrTemp = new ArrayList<>();
       ArrayList<Integer> order = new ArrayList<>();
       for(int i = 0; i < processList.size(); i++) {
           int[] processArr = new int[6];
           tempArrTemp.add(processArr);
           int[] process = new int[6];
           tempArr.add(process);
           tempArr.get(i)[0] = Math.toIntExact(processList.get(i).processPCB.pidGet());
           tempArr.get(i)[1] = burst;
           tempArr.get(i)[2] = processList.get(i).getBurstTime();
       }

       arrangeArrival(tempArrTemp.size(), tempArr);
       completionTime(tempArrTemp.size(), tempArr);

        for (int[] ints : tempArr) {
            order.add(ints[0]);
        }

        for(int i = 0; i < order.size(); i++) {
            for (int j = 0; j < order.size(); j++) {
                if(OS.processList.get(j).processPCB.pidGet() == order.get(i)){
                    OS.processListScheduled.add(OS.processList.get(j));
                }
            }
        }
    }

    static void arrangeArrival(int num, ArrayList<int[]> tempArr) {
        for (int i = 0; i < num; i++) {
            for (int j = 0; j < num - i - 1; j++) {
                if (tempArr.get(j)[1] > tempArr.get(j + 1)[1]) {
                    for (int k = 0; k < 5; k++) {
                        int temp = tempArr.get(j)[k];
                        tempArr.get(j)[k] = tempArr.get(j + 1)[k];
                        tempArr.get(j + 1)[k] = temp;
                    }
                }
            }
        }
    }

    static void completionTime(int num, ArrayList<int[]> tempArr) {
        int temp, val = -1;
        tempArr.get(0)[3] = tempArr.get(0)[1] + tempArr.get(0)[2];
        tempArr.get(0)[5] = tempArr.get(0)[3] - tempArr.get(0)[1];
        tempArr.get(0)[4] = tempArr.get(0)[5] - tempArr.get(0)[2];

        for (int i = 1; i < num; i++) {
            temp = tempArr.get(i - 1)[3];
            int low = tempArr.get(i)[2];
            for (int j = i; j < num; j++) {
                if (temp >= tempArr.get(j)[1] && low >= tempArr.get(j)[2]) {
                    low = tempArr.get(j)[2];
                    val = j;
                }
            }
            tempArr.get(val)[3] = temp + tempArr.get(val)[2];
            tempArr.get(val)[5] = tempArr.get(val)[3] - tempArr.get(val)[1];
            tempArr.get(val)[4] = tempArr.get(val)[5] - tempArr.get(val)[2];
            for (int k = 0; k < 6; k++) {
                int tem = tempArr.get(val)[k];
                tempArr.get(val)[k] = tempArr.get(i)[k];
                tempArr.get(i)[k] = tem;
            }
        }
    }
}
