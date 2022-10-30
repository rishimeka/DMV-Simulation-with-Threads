//Author: @Rishi Meka

import java.lang.*;
import java.util.*;
import java.util.concurrent.Semaphore;

public class DepartmentOfMotorVehicles implements Runnable{
    public static Queue<Customer> customersWaitingForANumber = new LinkedList<Customer>(); // Customers waiting in line at Information Desk to get a number.
    public static Queue<Customer> customersInWaitingArea = new LinkedList<Customer>(); // Customers in the waiting area until number is called.
    public static Queue<Customer> customersWaitingForAnAgent = new LinkedList<Customer>(); // Customers in line for an agent.
    public static Semaphore customersWaitingForANumberMutex = new Semaphore(1, true); // Controls access to the customersWaitingForANumber queue.
    public static Semaphore customersInWaitingAreaMutex = new Semaphore(1, true); // Controls access to the customersInWaitingArea queue.
    public static Semaphore customersWaitingForAnAgentMutex = new Semaphore(1, true); // Controls access to the customersWaitingForAnAgent queue.
    public static Semaphore informationDesk = new Semaphore(1, true); // Shows if the information desk is available for the customer to approach
    public static Semaphore customersReadyForANumber =  new Semaphore(0, true); // Shows if a customer is in line ready for a number
    public static Semaphore customerReadyForAnAgent = new Semaphore(0, true); // Shows if a customer is in the waiting room ready for an agent
    public static Semaphore agent = new Semaphore(2, true); // Shows how many agents are available for the customer to approach
    public static Semaphore agentLine = new Semaphore(4, true); // Shows how many spots are open in the agent line
    public static Semaphore customerReadyForService = new Semaphore(0, true); // Show how many customers are in the agent line
    public static int customersJoined = 0; // Keeps track of the number of customers that joined
    public static int count = 1; // Keeps track of the number of customers in the DMV with a number
    public static int capacity = 20; // The max number of customers that can enter the DMV


    public DepartmentOfMotorVehicles(){}

    public static void main(String[] args) {
        DepartmentOfMotorVehicles DMV = new DepartmentOfMotorVehicles();
        new InformationDesk(1, DMV);
        new Announcer(1,DMV);
        for (int i = 0; i < 2; i++)
            new Agent(i, DMV);
        for (int i = 0; i < capacity; i++)
            new Customer(i, DMV);
        while(customersJoined < capacity)
            System.out.print("");
        System.out.println("Done");
        System.exit(0);
    }

    public void run() {}
}


