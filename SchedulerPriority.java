import java.util.Comparator;
import java.util.List;

public class SchedulerPriority {
    public static void schedule(List<ProcessClass> processList) {
        processList.sort(Comparator.comparingInt(ProcessClass::priorityGet));
        OS.processListScheduled.addAll(processList);
    }
}