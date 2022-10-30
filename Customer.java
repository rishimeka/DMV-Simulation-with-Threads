public class Customer implements Runnable{
    public int customerID;
    public int assignedNumber;
    public DepartmentOfMotorVehicles DMV;
    public Thread customer;
    Customer(int customerID, DepartmentOfMotorVehicles DMV){
        this.customerID = customerID;
        this.DMV = DMV;
        customer = new Thread(this);
        customer.start();
    }

    public void run(){
        try{
            DMV.customersWaitingForANumberMutex.acquire(); // Locks the use of the customer waiting for a num queue
            DMV.customersWaitingForANumber.add(this);      // Adds the customer to the queue
            DMV.customersWaitingForANumberMutex.release(); // Unlocks the customer waiting for a num queue
            DMV.informationDesk.acquire();  // Wait for the information desk to be free
            System.out.println("Customer " + customerID + " created, enters DMV."); // Print out that the customer has entered the DMV
            DMV.customersReadyForANumber.release(); // Signal that the customers ready for a number
            DMV.informationDesk.release(); // Signal that the information desk is free
            DMV.customerReadyForAnAgent.release(); // Signal that the customer is ready for an agent
        }
        catch(Exception e) { e.printStackTrace(); }
    }
}
