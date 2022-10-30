public class Announcer implements Runnable{
    public int ID;
    public DepartmentOfMotorVehicles DMV;
    public Thread announcer;
    Announcer(int ID, DepartmentOfMotorVehicles DMV){
        this.ID = ID;
        this.DMV = DMV;
        System.out.println("Announcer created");
        announcer = new Thread(this);
        announcer.setDaemon(true);
        announcer.start();
    }
    public void run() {
        try {
            while(true) {
                DMV.agentLine.acquire(); // Waiting for the agent line to be free
                DMV.customerReadyForAnAgent.acquire(); // Waiting for a customer to be in the waiting room ready for an agent
                DMV.customersInWaitingAreaMutex.acquire(); // Locks the use of the customers in waiting area queue
                DMV.customersWaitingForAnAgentMutex.acquire(); // Locks the use of the customers waiting for an agent queue
                // Prints out that the announcer has announced a number, and that the customer has been moved to the agent line
                System.out.println("Announcer calls number " + DMV.customersInWaitingArea.peek().assignedNumber);
                System.out.println("Customer " + DMV.customersInWaitingArea.peek().customerID + " moves to agent line" );
                // Moving the customer from the waiting area into the agent line
                DMV.customersWaitingForAnAgent.add(DMV.customersInWaitingArea.remove());
                DMV.customersWaitingForAnAgentMutex.release(); // Unlocks the use of the customers waiting for an agent queue
                DMV.customersInWaitingAreaMutex.release(); // Unlocks the use of the customers in waiting area queue

                DMV.agent.acquire(); // Waiting for an agent to be ready to serve
                DMV.agentLine.release();
                DMV.customerReadyForService.release(); // Signal that the customer is ready for service
                DMV.agent.release(); // Signaling that the agent is ready to serve the next customer
            }

        } catch(Exception e) { e.printStackTrace(); }
    }
}
