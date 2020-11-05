import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DeferredCallbackExecutor {

    public static class Callback {
        long executeAt; 
        String message; 

        public Callback(long executeAfterSeconds, String message) {
            this.executeAt = System.currentTimeMillis() + executeAfterSeconds*1000;
            this.message = message;
        }
    }

    public void start() throws InterruptedException {
        long sleepDuration = 0L; 
        while (true) {
            lock.lock();
            while (0 == callbacks.size()) newCallbackRegistered.await();
            try {
                while (callbacks.size() != 0) {
                    sleepDuration = getSleepDuration();
                    if (sleepDuration <= 0) break;
                    newCallbackRegistered.await(sleepDuration, TimeUnit.SECONDS);
                }
                Callback cb = callbacks.poll();
                System.out.println("Executed at: " + System.currentTimeMillis()/1000 
                    + "; Expected at: " + cb.executeAt/1000 
                    + "; message: " + cb.message);
            } finally {
                lock.unlock();
            }
        }
    }

    /** 
     * Registers a callback that will be executed after the 
     * specified time. 
     */
    public void registerCallback(Callback cb) {
        lock.lock();
        try {
            callbacks.add(cb);
            newCallbackRegistered.signal();
        } finally {
            lock.unlock();
        }
    }

    private long getSleepDuration() {
        return callbacks.peek().executeAt - System.currentTimeMillis();
    }

    private static final Comparator<Callback> COMPARATOR = new Comparator<Callback>(){
        @Override
        public int compare(DeferredCallbackExecutor.Callback o1, DeferredCallbackExecutor.Callback o2) {
            return (int) (o1.executeAt - o2.executeAt);
        }
    };

    private PriorityQueue<Callback> callbacks = new PriorityQueue<>(COMPARATOR);
    private Lock lock = new ReentrantLock();
    private Condition newCallbackRegistered = lock.newCondition();
}
