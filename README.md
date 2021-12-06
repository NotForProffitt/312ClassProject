# 312ClassProject
This is my project submission for VCU CMSC-312 Introduction to Operating Systems.

# **Implementation**
A priority scheduler has also been implemented in this phase. After each process is created, it is randomly assigned a priority value based on a list of values generated from the amount of created processes. After processes have been scheduled, another check is run to ensure that each process has a valid priority.

I have implemented single level parent-child relationships via the FORK 0 command in the process templates. Each time this command is read, a new ProcessClass is created. The instructions passed to the child are identical to the parent’s instructions after the FORK command. This is done by providing the Instruction class a duplicate of the parent’s instruction set. The child is then inserted into the ready queue directly behind the parent and assigned memory if available. The process is then executed in its place in the queue like any other process. The child process is also assigned a PPID.

Memory is implemented as a resource capped at 1024MB. Each newly generated process is assigned a memory requirement equal to ¼ of the process’s total burst time. If the available memory is more than the process’s required memory, the process may enter the ready queue and is executed according to its scheduled order. If the process’s memory requirement exceeds the available system memory, the process remains in the NEW queue until sufficient memory may be allocated. After each process is executed, its required memory is deallocated back into system memory. After all READY processes have been executed, each process in the list of scheduled processes that have not been executed is assigned system memory and executed.

I/O events as caused by the process instructions are handled the same way as in phase 1. External event interrupts are simulated by implementing a random chance for each IO and CALC cycle to spawn an interrupt based on a randomly chosen number. If an event is spawned, the process state is set to WAIT for the duration of the interrupt.

Multithreading is present in this simulation. Currently, there are two threads generated per core. The list of scheduled processes is iterated over in sets of two, and each set of processes is assigned to the two core threads. These threads then run concurrently. The critical section resolving scheme implements a semaphore that controls the ability of a process to enter a critical section. If the semaphore is available, the critical section is entered and executed as normally. If the semaphore is not available, the thread waits until the concurrent thread has exited its critical section before entering its own. After both threads have ceased execution, they are set to null as a check to prevent newly created children processes from being assigned threads more than once, and the CPU iterates across two more processes until all scheduled processes are fully executed.

Some formatting is presented in the UI to more easily show information about processes,
including the execution order, burst time of each process, priority, and PPID if applicable. The UI presents each option available, when asking for type of process lowercase values are expected. Priority scheduling has now been implemented, so when prompted for choice of scheduler both option 1 will choose SJF scheduling and option 2 will choose priority scheduling. After process information has been displayed, 'y' will allow the user to continue simulating processes and 'n' will exit the program.




PHASE 1 DESCRIPTION:
I have chosen to implement a Shortest Job First algorithm for phase 1, and plan to implement a priority scheduler for phase 2. Shortest job first schedulers
use the predicted burst time (total calculation time spent on the CPU) to sort incoming processes. This is useful because it typically has the quickest response
time, but can cause process starvation if longer processes are constantly bumped back in the queue by shorter processes. It is also impractical in a real-world
setting, as it is impossible to know the true burst time of real processes and can only be simulated or estimated. Each process's state and PID are managed through
a PCB, implemented as an extended class of ProcessClass.

For process implementation, my program reads the input text file to get the correct steps and min/max boundaries for each step, then generates a process with randomized
cycle values based on those boundaries. These processes are added to an array list in order of creation and their state is set to NEW.

After all requested processes are created, the scheduler is called and sorts the processes into a new array list according to the shortest job first algorithm. Then,
each process has its state set to READY. From there, all processes in the newly sorted order are executed according to the generated instructions. During execution,
each process's state is set to RUN. if a process contains I/O cycle instructions, the state is set to WAIT for the duration of I/O execution. After each process is
executed, the state is set to EXIT and the user can choose to simulate more processes or exit.

To manage critical sections, a flag 'criticalSector' is present. Each process checks that this flag is set to false before executing, and toggles the flag whenever it
enters its own critical section. Because I have not implemented a round robin scheduler and multithreading is not present in this phase, no two processes will be
simulated to be executing in parallel and therefore no critical section collision will occur.

Some formatting is presented in the UI to more easily show information about processes, including the execution order and burst time of each process. The UI presents each option available, when asking for type of process lowercase values are expected. As priority scheduling has not yet been implemented, when prompted for choice of
scheduler both option 1 and 2 will choose SJF scheduling. After process information has been displayed, 'y' will allow the user to continue simulating processes and 'n' will exit the program.
