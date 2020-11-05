import java.util.concurrent.Semaphore;

public class UnisexBathroom {

    public UnisexBathroom() {
        inUseBy = NOT_IN_USE;
        numUsersInBathroom = 0;
    }

    public void maleUseBathroom(String name) throws InterruptedException {
        synchronized(this) {
            while (IN_USE_BY_FEMALE.equals(inUseBy)) wait();
            semaphore.acquire();
            numUsersInBathroom++;
            inUseBy = IN_USE_BY_MALE;
        }

        useBathroom(name);
        semaphore.release();

        synchronized(this) {
            numUsersInBathroom--;
            if (0 == numUsersInBathroom) inUseBy = NOT_IN_USE;
            notifyAll();
        }
    }

    public void femaleUseBathroom(String name) throws InterruptedException {
        synchronized(this) {
            while (IN_USE_BY_MALE.equals(inUseBy)) wait();
            semaphore.acquire();
            numUsersInBathroom++;
            inUseBy = IN_USE_BY_FEMALE;
        }

        useBathroom(name);
        semaphore.release();

        synchronized(this) {
            numUsersInBathroom--;
            if (0 == numUsersInBathroom) inUseBy = NOT_IN_USE;
            notifyAll();
        }
    }

    private void useBathroom(String name) throws InterruptedException {
        System.out.println("Bathroom in use by: " + name 
            + "; Number of users in bathroom: " + numUsersInBathroom);
        Thread.sleep(7000);
        System.out.println(name + " done using the bathroom");
    }

    private static final String IN_USE_BY_MALE   = "male";
    private static final String IN_USE_BY_FEMALE = "female";
    private static final String NOT_IN_USE       = "none";
    private static final int MAX_USERS_IN_BATHROOM = 3;
   
    private Semaphore semaphore = new Semaphore(MAX_USERS_IN_BATHROOM);
    private String inUseBy;
    private int numUsersInBathroom;
}
