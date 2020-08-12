// Mythri Challa
// CS 4348.001, Project 3
// 11-25-19
/*
    This project simulates an operating system's scheduler scheduling a set of jobs. The program reads in a text file
    outlining a set of jobs and their start times and durations, and will allow the user to see a representation of
    how the jobs are executed via various scheduling algorithms.

 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Scheduler
{
    public static void main(String[] args) {
        // Getting the user's input choice
        String algChoice = args[1];

        // File input
        File input = new File(args[0]);

        // Run the corresponding method based on the second argument input
        try {
            Scanner inputFile = new Scanner(input);

            if (algChoice.equals("FCFS"))
                FCFS(inputFile);

            else if (algChoice.equals("SPN"))
                SPN(inputFile);

            else if (algChoice.equals("SRT"))
                SRT(inputFile);

            else if (algChoice.equals("HRRN"))
                HRRN(inputFile);

            else if (algChoice.equals("RR"))
                RR(inputFile);

            else if (algChoice.equals("FB"))
                FB(inputFile);

            else if (algChoice.equals("ALL")) {
                FCFS(inputFile);
                inputFile = new Scanner(input);         // must clear inputFile Scanner object before each print

                RR(inputFile);
                inputFile = new Scanner(input);

                SPN(inputFile);
                inputFile = new Scanner(input);

                SRT(inputFile);
                inputFile = new Scanner(input);

                HRRN(inputFile);
                inputFile = new Scanner(input);

                FB(inputFile);
                inputFile = new Scanner(input);
            }
            else
                System.out.println("This is not a valid scheduling method choice. Please try again.");  // print error statement
        }

        catch (FileNotFoundException e) {
            System.out.println(e.getMessage());     // Print exception if file not found
        }
    }

    /*
     < The FCFS method implements a basic approach to print all the output by First Come First Serve method
     *  @param Scanner inputFile
     *  @return void
     */

    public static void FCFS(Scanner inputFile) {
        System.out.println("\n--------------FCFS---------------");
        // Declare ArrayLists for the IDs, arrival times, wait times, and service times of each process
        ArrayList<Character> IDs = new ArrayList<>();
        ArrayList<Character> arrivals = new ArrayList<>();
        ArrayList<Character> services = new ArrayList<>();
        ArrayList<Integer> waits = new ArrayList<>();

        // Read all the IDs and times into their corresponding ArrayLists
        while (inputFile.hasNextLine())
        {
            String line = inputFile.nextLine();
            line.trim();                            // trim line to remove spaces
            IDs.add(line.charAt(0));
            arrivals.add(line.charAt(2));
            services.add(line.charAt(4));
        }

        // Creating an ArrayList of wait times for each process, first is manually set to 0
        waits.add(0);
        for (int i = 1; i < IDs.size(); i++)
            waits.add((Character.getNumericValue(services.get(i - 1))) + waits.get(i - 1));

        // Print each process based on the arrival time and service time
        for (int i = 0; i < IDs.size(); i++)
        {
            System.out.print(IDs.get(i) + " ");     // Printing the ID of the current process
            for (int k = 0; k < waits.get(i); k++)
                System.out.print(" ");              // Printing spaces for the correct amount of delays

            for (int j = 0; j < Character.getNumericValue(services.get(i)); j++)
                System.out.print("X");              // Print the number of Xs for each service time
            System.out.println();
        }
    }

    /*
     < The SPN method implements a basic approach to print all the output by Shortest Process Next method
     *  @param Scanner inputFile
     *  @return void
    */
    public static void SPN(Scanner inputFile) {
        System.out.println("\n--------------SPN---------------");

        // Declare ArrayLists for the IDs, arrival times, and service times of each process
        ArrayList<Character> IDs = new ArrayList<>();
        ArrayList<Character> arrivals = new ArrayList<>();
        ArrayList<Character> services = new ArrayList<>();
        ArrayList<Character> finished = new ArrayList<>();      // tracks IDs of the completed processes
        ArrayList<Integer> durations = new ArrayList<>();       // new ArrayList to convert the Char times to Ints

        // Read all the IDs and times into their corresponding ArrayLists
        while (inputFile.hasNextLine())
        {
            String line = inputFile.nextLine();
            line.trim();
            IDs.add(line.charAt(0));
            arrivals.add(line.charAt(2));
            services.add(line.charAt(4));
        }

        for (int i = 0; i < services.size(); i++)
            durations.add(Character.getNumericValue(services.get(i)));      // converting Chars to Ints for durations for easy use later

        int finishTime = 0, index = 0;                                      // initializing necessary time variables

        for (int i = 0; i < IDs.size(); i++)                                // Printing all the job names first
            System.out.print(IDs.get(i));
        System.out.println();

        // Entire loop runs while there are still processes to be completed
        while (finished.size() != IDs.size())
        {
            for (int i = 0; i < IDs.size(); i++)                            // Finding the next process to execute based on timing
            {
                // If the arrival time is before the current time and the process is incomplete, update index
                if (Character.getNumericValue(arrivals.get(i)) <= finishTime && !(finished.contains(IDs.get(i))))
                    index = i;
            }

            for (int i = 0; i < IDs.size(); i++)
            {
                // If the arrival time is before current time, the duration is shortest, and if the process is incomplete, update index
                if (Character.getNumericValue(arrivals.get(i)) <= finishTime && durations.get(i) < durations.get(index) && !(finished.contains(IDs.get(i))))
                    index = i;
            }

            int currentTime = finishTime;                                   // Update the current time
            finishTime += durations.get(index);                             // Update the finish time

            // Process the shortest process and move on
            int spaces = IDs.get(index) - 65;                               // # of spaces is calculated based on how much space to leave before each letter

            for (int i = currentTime; i < finishTime; i++)
            {
                int counter = 0;
                while (counter < spaces)
                {
                    System.out.print(" ");
                    counter++;
                }
                System.out.println("X");
            }

            finished.add(IDs.get(index));                                   // Add the newly finished process to the finished array
        }
    }

    /*
     < The SRT method implements a basic approach to print all the output by Shortest Remaining Time Next method
     *  @param Scanner inputFile
     *  @return void
    */
    public static void SRT(Scanner inputFile)
    {
        System.out.println("\n--------------SRT---------------");

        // Declare ArrayLists for the IDs, arrival times, and service times of each process
        ArrayList<Character> IDs = new ArrayList<>();
        ArrayList<Character> arrivals = new ArrayList<>();
        ArrayList<Character> services = new ArrayList<>();
        ArrayList<Character> finished = new ArrayList<>();      // tracks IDs of the completed processes
        ArrayList<Integer> durations = new ArrayList<>();       // new ArrayList to convert the Char times to Ints

        // Read all the IDs and times into their corresponding ArrayLists
        while (inputFile.hasNextLine())
        {
            String line = inputFile.nextLine();
            line.trim();
            IDs.add(line.charAt(0));
            arrivals.add(line.charAt(2));
            services.add(line.charAt(4));
        }

        // converting Chars to Ints for durations
        for (int i = 0; i < services.size(); i++)
            durations.add(Character.getNumericValue(services.get(i)));

        // Printing all the job names first
        for (int i = 0; i < IDs.size(); i++)
            System.out.print(IDs.get(i));
        System.out.println();

        int currentTime = 0, shortestRemaining = durations.get(0), index = 0;  // initializing necessary time variables

        while (finished.size() != IDs.size())           // Run while there are still incomplete processes
        {
            for (int i = 0; i < IDs.size(); i++)        // Finding the next process to execute based on timing
            {
                // If the arrival time is before the current time and the process is incomplete
                if (Character.getNumericValue(arrivals.get(i)) <= currentTime && !(finished.contains(IDs.get(i)))) {
                    shortestRemaining = durations.get(i);   // set the new shortest time
                    index = i;                              // update the index
                }
            }

            // Iterate through the ArrayList again
            for (int i = 0; i < IDs.size(); i++)
            {
                // If the arrival time is before the current time and the current process's time is shorter than the current shorter time
                if (durations.get(i) <= shortestRemaining && Character.getNumericValue(arrivals.get(i)) <= currentTime && !(finished.contains(IDs.get(i))))
                {
                    if (durations.get(i) == shortestRemaining)
                    {
                        if (IDs.get(i) < IDs.get(index))            // If the times are equal run the lowest name process first
                        {
                            shortestRemaining = durations.get(i);       // Update the time remaining
                            index = i;                                  // Update the index
                        }
                    }
                    else
                    {
                        shortestRemaining = durations.get(i);           // Update the time remaining and the index
                        index = i;
                    }
                }
            }

            // Process the shortest process and move on, printing the correct # of spaces
            int spaces = IDs.get(index) - 65;
            int counter = 0;

            while (counter < spaces)
            {
                System.out.print(" ");
                counter++;
            }
            System.out.println("X");

            durations.set(index, (durations.get(index) - 1));       // Update the new duration to the current duration - 1 for each iteration

            if (durations.get(index) == 0)                          // Once the duration remaining is 0, process is done so add it to finished list
                finished.add(IDs.get(index));

            currentTime++;                              // Increment current time by 1 each time a loop goes through
        }
    }

    /*
     < The HRRN method implements a basic approach to print all the output by Highest Response Ratio Next method
     *  @param Scanner inputFile
     *  @return void
    */
    public static void HRRN(Scanner inputFile)
    {
        System.out.println("\n--------------HRRN---------------");

        // Declare ArrayLists for the IDs, arrival times, and service times of each process
        ArrayList<Character> IDs = new ArrayList<>();
        ArrayList<Character> arrivals = new ArrayList<>();
        ArrayList<Character> services = new ArrayList<>();
        ArrayList<Character> finished = new ArrayList<>();     // tracks IDs of the completed processes
        ArrayList<Integer> durations = new ArrayList<>();       // new ArrayList to convert the Char times to Ints

        // Read all the IDs and times into their corresponding ArrayLists
        while (inputFile.hasNextLine()) {
            String line = inputFile.nextLine();
            line.trim();
            IDs.add(line.charAt(0));
            arrivals.add(line.charAt(2));
            services.add(line.charAt(4));
        }


        for (int i = 0; i < services.size(); i++)
            durations.add(Character.getNumericValue(services.get(i)));      // converting Chars to Ints for durations

        // initializing necessary time variables
        int finishTime = 0, index = 0;
        float HRR = -9999, tempHRR = 0;

        // Printing all the job names first
        for (int i = 0; i < IDs.size(); i++)                                // Printing all the job names first
            System.out.print(IDs.get(i));
        System.out.println();

        // Runs while there are still processes to be completed
        while (finished.size() != IDs.size())
        {
            for (int i = 0; i < IDs.size(); i++)        // Finding the next process to execute based on timing
            {
                // If the arrival time is before the current time and the process is incomplete
                if (Character.getNumericValue(arrivals.get(i)) <= finishTime && !(finished.contains(IDs.get(i))))
                {
                    // Calculate response ratio by grabbing the wait time from the current process and using the w+s/s formula
                    tempHRR = durations.get(i) + (finishTime - (Character.getNumericValue(arrivals.get(i)))) / (durations.get(i));

                    // If the current highest ratio is less than the temporary ratio that is being calculated, the temp ratio becomes the new highest ratio
                    if (HRR < tempHRR)
                    {
                        HRR = tempHRR;                  // Update the highest ratio
                        index = i;                      // update the index
                    }
                }
            }

            for (int i = 0; i < IDs.size(); i++)        // Iterate through loop again this time only checking the unfinished processes
            {
                if (Character.getNumericValue(arrivals.get(i)) <= finishTime && tempHRR < HRR && !(finished.contains(IDs.get(i))))
                    index = i;                          // Update the index if the current incomplete process's ratio is less than the highest ratio
            }

            int currentTime = finishTime;               // Update the current time
            finishTime += durations.get(index);         // Update the finish time once the process runs


            // Process the shortest process and move on
            int spaces = IDs.get(index) - 65;

            for (int i = currentTime; i < finishTime; i++)
            {
                int counter = 0;
                while (counter < spaces)
                {
                    System.out.print(" ");
                    counter++;
                }
                System.out.println("X");
            }

            finished.add(IDs.get(index));           // Add the finished process into the proper ArrayList
        }
    }

    /*
 < The FB method implements a basic approach to print all the output by Feedback method with a quantum of 1 and 3 queues
 *  @param Scanner inputFile
 *  @return void
*/
    public static void FB (Scanner inputFile)
    {
        System.out.println("\n--------------FB---------------");

        // Declare ArrayLists for the IDs, arrival times, and service times of each process
        ArrayList<Character> IDs = new ArrayList<>();
        ArrayList<Character> arrivals = new ArrayList<>();
        ArrayList<Character> services = new ArrayList<>();
        ArrayList<Character> finished = new ArrayList<>();      // tracks IDs of the completed processes
        ArrayList<Integer> durations = new ArrayList<>();       // new ArrayList to convert the Char times to Ints
        ArrayList<Integer> remaining = new ArrayList<>();       // new ArrayList to track the remaining times

        // ArrayLists for the 3 queues
        ArrayList<Integer> Queue1 = new ArrayList<>();
        ArrayList<Integer> Queue2 = new ArrayList<>();
        ArrayList<Integer> Queue3 = new ArrayList<>();

        // Read all the IDs and times into their corresponding ArrayLists
        while (inputFile.hasNextLine())
        {
            String line = inputFile.nextLine();
            line.trim();
            IDs.add(line.charAt(0));
            arrivals.add(line.charAt(2));
            services.add(line.charAt(4));
        }

        for (int i = 0; i < services.size(); i++)
        {
            durations.add(Character.getNumericValue(services.get(i)));      // converting Chars to Ints for durations
            remaining.add(Character.getNumericValue(services.get(i)));      // converting Chars to Ints for durations
            Queue1.add(Character.getNumericValue(services.get(i)));
            Queue2.add(Character.getNumericValue(services.get(i)));
            Queue3.add(Character.getNumericValue(services.get(i)));
        }

        int currentTime = 0, quantum = 1, index = 0, finishTime=0;          // Initializing the necessary timing variables

        for (int i = 0; i < IDs.size(); i++)            // Printing all the job names first
            System.out.print(IDs.get(i));
        System.out.println();

        while (finished.size() != IDs.size())           // runs while there are unfinished process
        {
            for (int i = 0; i < IDs.size(); i++)
            {
                if (remaining.get(i) < Queue1.get(i))       // check on Queue1
                {
                    currentTime += remaining.get(i);
                    Queue2.set(i, Queue1.get(i));           // move to another queue
                }
            }

            // Begin a process in queue 1 with high priority
            // Move down a queue for each process
            // Shorter processes will finish quicker

            // Keep the finishTime updated
            currentTime = finishTime;
            finishTime += durations.get(index);

            // Process the shortest process and move on
            int spaces = IDs.get(index) - 65;

            for (int i = currentTime; i < finishTime; i++)
            {
                int counter = 0;
                while (counter < spaces)
                {
                    System.out.print(" ");
                    counter++;
                }
                System.out.println("X");
            }

            finished.add(IDs.get(index));         // update the finished process according to the index
        }
    }

    /*
     < The RR method implements a basic approach to print all the output by Round Robin method with a quantum of 1
     *  @param Scanner inputFile
     *  @return void
    */
    public static void RR (Scanner inputFile)
    {
        System.out.println("\n--------------RR---------------");

        // Declare ArrayLists for the IDs, arrival times, and service times of each process
        ArrayList<Character> IDs = new ArrayList<>();
        ArrayList<Character> arrivals = new ArrayList<>();
        ArrayList<Character> services = new ArrayList<>();
        ArrayList<Character> finished = new ArrayList<>();      // tracks IDs of the completed processes
        ArrayList<Integer> durations = new ArrayList<>();       // new ArrayList to convert the Char times to Ints
        ArrayList<Integer> remaining = new ArrayList<>();       // new ArrayList to track the remaining times

        // Read all the IDs and times into their corresponding ArrayLists
        while (inputFile.hasNextLine())
        {
            String line = inputFile.nextLine();
            line.trim();
            IDs.add(line.charAt(0));
            arrivals.add(line.charAt(2));
            services.add(line.charAt(4));
        }

        for (int i = 0; i < services.size(); i++)
        {
            durations.add(Character.getNumericValue(services.get(i)));      // converting Chars to Ints for durations
            remaining.add(Character.getNumericValue(services.get(i)));      // converting Chars to Ints for durations
        }

        int currentTime = 0, quantum = 1, index = 0, finishTime=0;

        for (int i = 0; i < IDs.size(); i++)            // Printing all the job names first
            System.out.print(IDs.get(i));
        System.out.println();

        // Traverse all processes w/ RR method until all are complete
        while (true)
        {
            boolean done = true;                        // flag for checking if each process is done or not

            // Traverse all processes
            for (int i = 0; i < IDs.size(); i++)
            {
                if (remaining.get(i) > 0)            // shows that process isn't complete yet
                {
                    done = false;

                    if (remaining.get(i) > quantum)                         // If the process's remaining time is greater than the quantum of 1
                    {
                        currentTime += quantum;                             // increase the time by the quantum amount
                        remaining.set(i, remaining.get(i) - quantum);       // Decrease the time of the process by the quantum amount
                        index = i;                                          // update the index to accommodate for the current process
                    }

                    // If the process duration is less than or equal to the quantum, we're on the last cycle for the process
                    else
                    {
                        // Increase the currentTime and set the remaining time to 0 since process will finish, and update index accordingly
                        currentTime = currentTime + remaining.get(i);
                        remaining.set(i, 0);
                        index = i;
                    }
                }
            }

            if (done == true)                                               // continue this until process is finished
                break;

            // Keep the finishTime updated
            currentTime = finishTime;
            finishTime += durations.get(index);

            // Process the shortest process and move on
            int spaces = IDs.get(index) - 65;

            for (int i = currentTime; i < finishTime; i++)
            {
                int counter = 0;
                while (counter < spaces)
                {
                    System.out.print(" ");
                    counter++;
                }
                System.out.println("X");
            }

            finished.add(IDs.get(index));         // update the finished process according to the index
        }
    }
}
