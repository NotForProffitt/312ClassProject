# 312ClassProject
This is my project submission for VCU CMSC-312 Introduction to Operating Systems.

# **Implementation**
I have chosen to implement a Shortest Job First algorithm for phase 1, and plan to implement a priority scheduler for phase 2. Shortest job first schedulers
use the predicted burst time (total calculation time spent on the CPU) to sort incoming processes. This is useful because it typically has the quickest response
time, but can cause process starvation if longer processes are consistantly bumped back in the queue by shorter processes. It is also impractical in a rea-world
setting, as it is impossible to know the true burst time of real processes and can only be simulated or estimated. Each process's state and PID are managed through
a PCB, implemented as a extended class from ProcessClass.

For process implementation, my program reads the input text file to get the correct steps and min/max boundaries for each step, then generates a process with randomized
cycle values based on those boundaries. These processes are added to an array list in order of creation and their state is set to NEW.

After all requested processes are created, the scheduler is called and sorts the processes into a new array list according the the shortest job first algorithm. Then,
each process has its state set to READY. From there, all processes in the newly sorted order are executed according to the generated instructions. During execution,
each process's state is set to RUN. if a process contains I/O cycle instructions, the state is set to WAIT for the duration of I/O execution. After each process is
executed, the state is set to EXIT and the user can choose to simulate more processes or exit.

To manage critical sections, a flag 'criticalSector' is present. Each process checks that this flag is set to false before executing, and toggles the flag whenever it
enters its own critical section. Because I have not implemented a round robin scheduler and multithreading is not present in this phase, not two processes will be
simulated to be executing in parallel and therefore no critical section collision will occur.

Some formatting is presented in the UI to more easily show information about processes, including the execution order and burst time of each process. The UI presents each option available, when asking for type of process lowercase values are expected. As priority scheduling has not yet been implemented, when prompted for choice of
scheduler both option 1 and 2 will choose SJF scheduling. After process has been displayed, 'y' will allow the user to continue simulating processes and 'n' will exit the program.
