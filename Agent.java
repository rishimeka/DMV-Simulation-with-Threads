public class Agent implements Runnable{
    public int agentID;
    public DepartmentOfMotorVehicles DMV;
    public Customer customer;
    public Thread agent;
    Agent(int agentID, DepartmentOfMotorVehicles DMV){
        this.agentID = agentID;
        this.DMV = DMV;
        System.out.println("Agent " + agentID + " created");
        agent = new Thread(this);
        agent.setDaemon(true);
        agent.start();
    }
    public void run() {
        try{
            while(true) {
                DMV.customerReadyForService.acquire(); // Waiting for a customer to be ready for service
                DMV.customersWaitingForAnAgentMutex.acquire(); // Locks the use of the customers waiting for an agent queue
                customer = DMV.customersWaitingForAnAgent.remove();
                System.out.println("Agent " + agentID + " is serving customer " + customer.customerID); // Printing that the agent is serving a customer
                DMV.customersWaitingForAnAgentMutex.release(); // Unlocks the use of the customers waiting for an agent queue
                // Printing out the service
                System.out.println("Agent " + agentID + " asks customer " + customer.customerID + " to take photo and eye exam");
                System.out.println("Customer " + customer.customerID + " completes photo and eye exam for agent " + agentID);
                System.out.println("Agent " + agentID + " gives license to customer " + customer.customerID);
                System.out.println("Customer " + customer.customerID + " gets license and departs");
                System.out.println("Customer " + customer.customerID + " was joined");
                customer.customer.join();
                DMV.customersJoined++;
            }
        }
        catch(Exception e) { e.printStackTrace(); }
    }
}
