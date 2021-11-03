import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Instruction {
    public static ArrayList processList = new ArrayList();

    //called by main to place the template values into an array and create a process with those instructions
    public static ProcessClass makeProcesses(String type) throws FileNotFoundException {
        Scanner file;
        ArrayList<ArrayList<String>> input = new ArrayList<>();
        switch (type) {
            case "long" -> file = new Scanner(new File("src/" + "process3.txt"));
            case "short" -> file = new Scanner(new File("src/" + "process1.txt"));
            case "mixed" -> file = new Scanner(new File("src/" + "process2.txt"));
            default -> throw new IllegalStateException("Unexpected value: " + type);
        }
        while(file.hasNextLine()) {
            final String nextLine = file.nextLine();
            final String[] items = nextLine.split(" ");
            ArrayList<String> line = new ArrayList<String>(Arrays.asList(items));
            input.add(line);
            Arrays.fill(items, null);
        }
        //converting input to a 2d array for ease of use
        String[][] processArray = input.stream().map(u -> u.toArray(new String[0])).toArray(String[][]::new);
        String[][] instructions = new String[processArray.length][2];

        for(int i = 0; i < processArray.length; i++) {
            Random random = new Random();
            int rand = 0;
            if(processArray[i][0].equals("CALCULATE") || processArray[i][0].equals("I/O") || processArray[i][0].equals("FORK")) {
                switch (processArray[i][0]) {
                    case "CALCULATE" -> instructions[i][0] = "C";
                    case "I/O" -> instructions[i][0] = "I";
                    case "FORK" -> instructions[i][0] = "F";
                }
                rand = random.nextInt(Integer.parseInt(processArray[i][2]) - Integer.parseInt(processArray[i][1])) + Integer.parseInt(processArray[i][1]);
                instructions[i][1] =  Integer.toString(rand);
            }
            else if(processArray[i][0].equals("SECTION")) {
                instructions[i][0] = "S";
                instructions[i][1] = "0";
            }
        }
        ProcessClass newClass = new ProcessClass(0, instructions);
        processList.add(newClass);
        return newClass;
    }
}
