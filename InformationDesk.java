public class InformationDesk implements Runnable{
    public int ID;
    public DepartmentOfMotorVehicles DMV;
    public Thread informationDesk;
    InformationDesk(int ID, DepartmentOfMotorVehicles DMV){
        this.ID = ID;
        this.DMV = DMV;
        System.out.println("Information desk created");
        informationDesk = new Thread(this);
        informationDesk.setDaemon(true);
        informationDesk.start();
    }

    public void run() {
        try{
            while(DMV.count <= DMV.capacity) {
                DMV.customersReadyForANumber.acquire();  // Waiting for a customer to be ready for a number
                DMV.customersInWaitingAreaMutex.acquire();  // Locks the use of the customers in waiting area queue
                DMV.customersWaitingForANumberMutex.acquire();  // Locks the use of the customer waiting for a num queue
                DMV.customersWaitingForANumber.peek().assignedNumber = DMV.count; // Assigning the number to the customer
                // Printing out that the customer has received a number
                System.out.println("Customer " + DMV.customersWaitingForANumber.peek().customerID +
                        " gets number " + DMV.customersWaitingForANumber.peek().assignedNumber + ", enters waiting room");
                DMV.count++; // Increase the number of customers
                DMV.customersInWaitingArea.add(DMV.customersWaitingForANumber.remove()); // Move the customer from the information desk line to the waiting area
                DMV.customersWaitingForANumberMutex.release(); // Unlocks the customer waiting for a number queue
                DMV.customersInWaitingAreaMutex.release(); // Unlocks the customer waiting in the waiting area queue
                DMV.customersReadyForANumber.release(); // Signaling that the customer got a number
            }
            informationDesk.join();
        }
        catch(Exception e) { e.printStackTrace(); }
    }
}
