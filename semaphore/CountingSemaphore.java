public class CountingSemaphore {

    public CountingSemaphore(int maxpermits) {
        this.maxpermits = maxpermits; 
        this.permits = 0;
    }

    public synchronized void acquire() throws InterruptedException {
        while (maxpermits == permits) wait();
        permits++;
        notify();
    }

    public synchronized void release() throws InterruptedException {
        while (0 == permits) wait();
        permits--;
        notify();
    }

    private final int maxpermits;
    private int permits;
}
